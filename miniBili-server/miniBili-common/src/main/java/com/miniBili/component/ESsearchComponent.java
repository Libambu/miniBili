package com.miniBili.component;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniBili.entity.config.AppConfig;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.VideoInfoEsDto;
import com.miniBili.entity.enums.SearchOrderTypeEnum;
import com.miniBili.entity.po.UserInfo;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.po.VideoInfoPost;
import com.miniBili.entity.query.SimplePage;
import com.miniBili.entity.query.UserInfoQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.exception.BusinessException;
import com.miniBili.mappers.UserInfoMapper;
import com.miniBili.utils.CopyTools;
import com.miniBili.utils.JsonUtils;
import com.miniBili.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.document.SearchDocumentResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component("ESsearchComponent")
public class ESsearchComponent {
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 检查索引库是否存在
     * @return
     * @throws IOException
     */
    private Boolean isExistIndex() throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(appConfig.getEsIndexName());
        return client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    //创建索引库,服务启动的时候创建
    public void createIndex(){
        try{
            if(isExistIndex()){
                return;
            }
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(appConfig.getEsIndexName());
            //设置分词先按照,分格
            createIndexRequest.settings(EsCommand.COMMA, XContentType.JSON);
            //创建索引库
            createIndexRequest.mapping(EsCommand.CREATE_INDEX,XContentType.JSON);

            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);

            boolean ack = createIndexResponse.isAcknowledged();
            if(!ack){
                throw  new BusinessException("初始化es失败");
            }
        }catch (Exception e){
            log.error("初始化es失败" , e);
            throw  new BusinessException("初始化es失败");
        }
    }


    /**
     * 保存文档
     * @param videoInfo
     */
    public  void saveDoc(VideoInfo videoInfo){
        try {
            if(docExist(videoInfo.getVideoId())){
                updateDoc(videoInfo);
            }else{
                VideoInfoEsDto videoInfoEsDto = CopyTools.copy(videoInfo,VideoInfoEsDto.class);
                videoInfoEsDto.setCollectCount(0);
                videoInfoEsDto.setDanmuCount(0);
                videoInfoEsDto.setPlayCount(0);
                IndexRequest request = new IndexRequest(appConfig.getEsIndexName()).id(videoInfo.getVideoId());
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(videoInfoEsDto);   // createTime 会是 yyyy-MM-dd HH:mm:ss
                request.source(json,XContentType.JSON);
                client.index(request,RequestOptions.DEFAULT);
            }
        }catch (Exception e){
            log.error("保存到es失败",e);
            throw new BusinessException("保存到es失败");
        }
    }


    /**
     * 检查文档是否存在
     * @param id
     * @return
     * @throws IOException
     */
    private Boolean docExist(String id) throws IOException {
        GetRequest getRequest = new GetRequest(appConfig.getEsIndexName(),id);
        GetResponse documentFields = client.get(getRequest, RequestOptions.DEFAULT);
        return documentFields.isExists();
    }


    /**
     * 难点就是通过反射来获取非空的字段，防止将es的值刷成null
     * @param videoInfo
     */
    private void updateDoc(VideoInfo videoInfo)  {
        try {
            videoInfo.setLastUpdateTime(null);
            videoInfo.setCreateTime(null);
            Map<String,Object> dataMap = new HashMap<>();
            Field[] fields = videoInfo.getClass().getFields();
            for(Field field : fields){
                String methodName = "get" + StringTools.upperCaseFirstLetter(field.getName());
                Method method = videoInfo.getClass().getMethod(methodName);
                Object o = method.invoke(videoInfo);
                if((o!=null&&o instanceof String && !StringTools.isEmpty(o.toString()))||(o!=null&&!(o instanceof String))){
                    dataMap.put(field.getName(),o);
                }
            }
            if(dataMap.isEmpty()){
                return;
            }
            UpdateRequest updateRequest = new UpdateRequest(appConfig.getEsIndexName(),videoInfo.getVideoId());
            updateRequest.doc(dataMap);
            client.update(updateRequest,RequestOptions.DEFAULT);
        }catch (Exception e){
            log.error("更新es视频失败",e);
            throw new BusinessException("更新es视频失败");
        }
    }

    /**
     * 对数量进行更新
     * @param videoId
     * @param fieldName
     * @param count
     */
    public void updateDocCount(String videoId,String fieldName,Integer count){
        try{
            UpdateRequest updateRequest = new UpdateRequest(appConfig.getEsIndexName(),videoId);
            //解决并发冲突
            Script script = new Script(
                    ScriptType.INLINE,        // 脚本类型：内联脚本（直接写在代码里）
                    "painless",               // 脚本语言：Elasticsearch 的内置语言，叫 Painless（安全、快速）
                    "ctx._source." + fieldName + " += params.count", // 脚本内容
                    Collections.singletonMap("count", count) // 脚本参数
            );
            updateRequest.script(script);
            client.update(updateRequest,RequestOptions.DEFAULT);
        }catch (Exception e){
            log.error("更新数量到es失败",e);
            throw new BusinessException("更新数量到es失败");
        }
    }

    /**
     * 删除es中的视频信息
     * @param videoId
     */
    public void deleteDoc(String videoId){
        try{
            DeleteRequest deleteRequest = new DeleteRequest(appConfig.getEsIndexName(),videoId);
            client.delete(deleteRequest,RequestOptions.DEFAULT);
        }catch (Exception e){
            log.error("删除es视频信息失败",e);
            throw new BusinessException("删除es视频信息失败");
        }
    }


    /**
     * 搜索es信息
     * @param highlight
     * @param keyword
     * @param orderType
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PaginationResultVO<VideoInfo> search(Boolean highlight,
                                                String keyword,
                                                Integer orderType,
                                                Integer pageNo,
                                                Integer pageSize){
        try{
            SearchOrderTypeEnum searchOrderTypeEnum  = SearchOrderTypeEnum.getByType(orderType);

            SearchRequest searchRequest = new SearchRequest(appConfig.getEsIndexName());
            //设置查询
            searchRequest.source().query(QueryBuilders.multiMatchQuery(keyword,"videoName","tage"));
            //设置高亮
            if(highlight){
                HighlightBuilder highlightBuilder = new HighlightBuilder();
                highlightBuilder.field("videoName");
                highlightBuilder.preTags("<span class='highlight'>");
                highlightBuilder.postTags("</span>");
                searchRequest.source().highlighter(highlightBuilder);
            }
            //设置排序
            searchRequest.source().sort("_score", SortOrder.ASC);
            if(orderType!=null){
                searchRequest.source().sort(searchOrderTypeEnum.getField(),SortOrder.DESC);
            }
            //设置分页
            pageNo = pageNo==null?1:pageNo;
            pageSize = pageSize==null?20:pageSize;
            searchRequest.source().from((pageNo-1)*pageSize);
            searchRequest.source().size(pageSize);
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            //开始解析结果
            SearchHits hits = search.getHits();
            //解析出查出的视频的总条出
            Integer total = (int) hits.getTotalHits().value;
            List<VideoInfo> videoInfoList = new ArrayList<>();
            //存放用户id用来关联查询，因为es存的是userid，但是页面用的是userName
            List<String> userIdList = new ArrayList<>();
            for(SearchHit hit : hits.getHits()){
                VideoInfo videoInfo = JsonUtils.converJson2obj(hit.getSourceAsString(),VideoInfo.class);
                //这条文档的 videoName 里出现了关键词，那么这里就能拿到一个非 null 的 HighlightField
                if(hit.getHighlightFields().get("videoName")!=null){
                    videoInfo.setVideoName(hit.getHighlightFields().get("videoName").fragments()[0].string());
                }
                videoInfoList.add(videoInfo);
                userIdList.add(videoInfo.getUserId());
            }
            UserInfoQuery userInfoQuery = new UserInfoQuery();
            userInfoQuery.setUserIdList(userIdList);
            List<UserInfo> userInfoList = userInfoMapper.selectList(userInfoQuery);
            //转成map
            Map<String,UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(item->item.getUserId(), Function.identity(),(data1,data2)->data2));
            //然后写入userName
            for(VideoInfo v: videoInfoList){
                v.setNickName(userInfoMap.get(v.getUserId()).getNickName());
            }
            //分页操作
            SimplePage page = new SimplePage(pageNo,total,pageSize);
            PaginationResultVO<VideoInfo> resultVO = new PaginationResultVO<>(page.getCountTotal(),page.getPageSize(),page.getPageNo(),videoInfoList);
            return resultVO;
        }catch (Exception e){
            log.error("查询失败",e);
            throw new BusinessException("es查询失败");
        }
    }
}

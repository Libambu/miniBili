package com.miniBili.service.impl;

import java.io.File;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.util.*;

import javax.annotation.Resource;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.config.AppConfig;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.SysSettingDto;
import com.miniBili.entity.dto.UploadingFileDto;
import com.miniBili.entity.enums.*;
import com.miniBili.entity.po.VideoInfo;
import com.miniBili.entity.po.VideoInfoFile;
import com.miniBili.entity.po.VideoInfoFilePost;
import com.miniBili.entity.query.*;
import com.miniBili.exception.BusinessException;
import com.miniBili.mappers.VideoInfoFileMapper;
import com.miniBili.mappers.VideoInfoFilePostMapper;
import com.miniBili.mappers.VideoInfoMapper;
import com.miniBili.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniBili.entity.po.VideoInfoPost;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.mappers.VideoInfoPostMapper;
import com.miniBili.service.VideoInfoPostService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;


/**
 * 审核中的信息表 业务接口实现
 */
@Slf4j
@Service("videoInfoPostService")
public class VideoInfoPostServiceImpl implements VideoInfoPostService {

	@Resource
	private VideoInfoPostMapper<VideoInfoPost, VideoInfoPostQuery> videoInfoPostMapper;
    @Autowired
    private RedisComponent redisComponent;
	@Autowired
	private VideoInfoFilePostMapper videoInfoFilePostMapper;
	@Autowired
	private AppConfig appConfig;
	@Autowired
	private FFmpegUtils fFmpegUtils;
	@Autowired
	private VideoInfoMapper<VideoInfo, VideoInfoQuery> videoInfoMapper;
	@Autowired
	private VideoInfoFileMapper<VideoInfoFile,VideoInfoFileQuery> videoInfoFileMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<VideoInfoPost> findListByParam(VideoInfoPostQuery param) {
		return this.videoInfoPostMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(VideoInfoPostQuery param) {
		return this.videoInfoPostMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<VideoInfoPost> findListByPage(VideoInfoPostQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<VideoInfoPost> list = this.findListByParam(param);
		PaginationResultVO<VideoInfoPost> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(VideoInfoPost bean) {
		return this.videoInfoPostMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<VideoInfoPost> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoPostMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<VideoInfoPost> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.videoInfoPostMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(VideoInfoPost bean, VideoInfoPostQuery param) {
		StringTools.checkParam(param);
		return this.videoInfoPostMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(VideoInfoPostQuery param) {
		StringTools.checkParam(param);
		return this.videoInfoPostMapper.deleteByParam(param);
	}

	/**
	 * 根据VideoId获取对象
	 */
	@Override
	public VideoInfoPost getVideoInfoPostByVideoId(String videoId) {
		return this.videoInfoPostMapper.selectByVideoId(videoId);
	}

	/**
	 * 根据VideoId修改
	 */
	@Override
	public Integer updateVideoInfoPostByVideoId(VideoInfoPost bean, String videoId) {
		return this.videoInfoPostMapper.updateByVideoId(bean, videoId);
	}

	/**
	 * 根据VideoId删除
	 */
	@Override
	public Integer deleteVideoInfoPostByVideoId(String videoId) {
		return this.videoInfoPostMapper.deleteByVideoId(videoId);
	}

	@Override
	@Transactional
	public void saveVideoInfo(VideoInfoPost videoInfoPost, List<VideoInfoFilePost> filePostList) {
		if(filePostList.size()>redisComponent.getSystemSetting().getVideoPCount()){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		if(!StringTools.isEmpty(videoInfoPost.getVideoId())){
			//视频修改操作
			VideoInfoPost videoInfoPostDB = videoInfoPostMapper.selectByVideoId(videoInfoPost.getVideoId());
			if(videoInfoPostDB==null){
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
			if(ArrayUtils.contains(new Integer[]{VideoStatusEnum.STATUS0.getStatus(),VideoStatusEnum.STATUS2.getStatus()},videoInfoPostDB.getStatus())){
				//审核中 转码中的视频不允许修改
				throw new BusinessException(ResponseCodeEnum.CODE_600);
			}
		}
		Date curdate = new Date();
		String videoId = videoInfoPost.getVideoId();
		List<VideoInfoFilePost> delFileList = new ArrayList<>();
		List<VideoInfoFilePost> addFileList = new ArrayList<>();

		if(StringTools.isEmpty(videoId)){
			//新增操作
			videoId = StringTools.getRandomString(Constants.LENGTH_10);
			videoInfoPost.setCreateTime(curdate);
			videoInfoPost.setLastUpdateTime(curdate);
			videoInfoPost.setVideoId(videoId);
			videoInfoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
			videoInfoPostMapper.insert(videoInfoPost);

			for(VideoInfoFilePost file : filePostList){
				addFileList.add(file);
			}
		}else{
			//修改操作
			//修改关键就是要找出数据库哪些文件是要被删掉的，上传的列表里哪些是要写进数据库中的
			//1根据videoid和userId查出当前视频数据库中有哪些文件
			VideoInfoFilePostQuery fileQuery = new VideoInfoFilePostQuery();
			fileQuery.setVideoId(videoId);
			fileQuery.setUserId(videoInfoPost.getUserId());
			List<VideoInfoFilePost> dbInfoFileList = videoInfoFilePostMapper.selectList(fileQuery);
			//将上传的文件转为map
			Map<String,VideoInfoFilePost> uploadFileMap = new HashMap<>();
			for(VideoInfoFilePost v : filePostList){
				uploadFileMap.put(v.getUploadId(),v);
			}
			//视频是否修改了名称
			Boolean updateFileName = false;
			for(VideoInfoFilePost dbFilePost : dbInfoFileList){
				VideoInfoFilePost updateFileInfo = uploadFileMap.get(dbFilePost.getUploadId());
				if(updateFileInfo==null){
					delFileList.add(dbFilePost);
				}else if(!dbFilePost.getFileName().equals(updateFileInfo.getFileName())){
					updateFileName = true;
				}
			}
			//只要是fileId为NULL的就是新增的文件数据
			for(VideoInfoFilePost v : filePostList){
				if(v.getFileId()==null){
					addFileList.add(v);
				}
			}
			videoInfoPost.setLastUpdateTime(new Date());
			//标记视频是否做了修改
			Boolean changeVideoInfo = changeVideoInfo(videoInfoPost);
			if(addFileList.isEmpty()){
				//只涉及删除视频，就不用再审核了
				videoInfoPost.setStatus(VideoStatusEnum.STATUS0.getStatus());
			}else if(changeVideoInfo || updateFileName){
				videoInfoPost.setStatus(VideoStatusEnum.STATUS2.getStatus());
			}else {
				videoInfoPost.setStatus(VideoStatusEnum.STATUS2.getStatus());
			}
			videoInfoPostMapper.updateByVideoId(videoInfoPost,videoInfoPost.getVideoId());
		}
		//一直到这里才解决了视频审核表，视频具体的文件信息还没有开始操作
		if(!delFileList.isEmpty()){
			//删除数据库中的文件
			List<String> ids = new ArrayList<>();
			List<String> filePath =  new ArrayList<>();
			//TODO 到目前为止是没有管file的路径的，可能是在合并的时候会设置总路径，这里有关path的东西都是null
			for(VideoInfoFilePost file : delFileList){
				ids.add(file.getFileId());
				filePath.add(file.getFilePath());
			}
			videoInfoFilePostMapper.deleteBatchByFileIds(ids,videoInfoPost.getUserId());
			//使用redis做轻量级的消息队列
			redisComponent.addFile2DelList(videoId ,filePath);
		}
		//将视频对应的文件批量插入到数据库
		Integer index = 1;
		for(VideoInfoFilePost videoInfoFilePost : filePostList){
			videoInfoFilePost.setFileIndex(index++);
			videoInfoFilePost.setVideoId(videoId);
			videoInfoFilePost.setUserId(videoInfoPost.getUserId());
			if(videoInfoFilePost.getFileId()==null){
				videoInfoFilePost.setFileId(StringTools.getRandomString(20));
				videoInfoFilePost.setUpdateType(VideoFileUpdateTypeEnum.UPDATE.getStatus());
				videoInfoFilePost.setTransferResult(VideoFileTransferResultEnum.TRANSFER.getStatus());
			}
		}
		videoInfoFilePostMapper.insertOrUpdateBatch(filePostList);
		//将新增的视频文件放入消息队列中进行处理
		if(!addFileList.isEmpty()){
			for(VideoInfoFilePost videoInfoFilePost : addFileList){
				videoInfoFilePost.setUserId(videoInfoPost.getUserId());
				videoInfoFilePost.setVideoId(videoId);
			}
			redisComponent.addFile2TransFerQueue(videoId,addFileList);
		}

	}

	/**
	 * 进行文件切片合并
	 * @param videoInfoFilePost
	 */
	@Override
	public void transferVideoFile(VideoInfoFilePost videoInfoFilePost) {
		VideoInfoFilePost updateFilePost = new VideoInfoFilePost();
		try{
			UploadingFileDto fileDto = redisComponent.getUploadVideoFile(videoInfoFilePost.getUserId(), videoInfoFilePost.getUploadId());
			String tempPath = appConfig.getProjectFolder() + Constants.FILE_FOLDER +Constants.FILE_TEMP + fileDto.getFilePath();
			String targetPath = appConfig.getProjectFolder() + Constants.FILE_FOLDER +Constants.FILE_VIDEO + fileDto.getFilePath();
			File tempFile = new File(tempPath);
			File targetFile = new File(targetPath);
			//将临时目录里的分片文件拷贝到目标目录
			FileUtils.copyFolder(tempFile,targetFile);
			//删除临时目录
			FileUtils.deleteFile(tempFile);
			//删除redis记录
			redisComponent.delVideoFileInfo(videoInfoFilePost.getUserId(), videoInfoFilePost.getUploadId());
			//copy到video再合并2为mp4 最后这个mp4还要删除，转为ts m3u8 completeMp4为npm4文件
			String completeMp4 = targetPath + Constants.FILE_MP4;
			union(targetPath,completeMp4,true);
			//获取视频持续时长
			Integer duration = fFmpegUtils.getVideoInfoTime(completeMp4);
			updateFilePost.setDuration(duration);
			updateFilePost.setFileSize(new File(completeMp4).length());
			updateFilePost.setTransferResult(VideoFileTransferResultEnum.SUCCESS.getStatus());
			//todo 视频路径
			updateFilePost.setFilePath(Constants.FILE_VIDEO+fileDto.getFilePath());
			converMP42TS(completeMp4);
		} catch (Exception e) {
			updateFilePost.setTransferResult(VideoFileTransferResultEnum.FAIL.getStatus());
			log.error("文件转码失败");
			throw new RuntimeException(e);
		} finally {
				videoInfoFilePostMapper.updateByFileId(updateFilePost,videoInfoFilePost.getFileId());
				VideoInfoFilePostQuery query = new VideoInfoFilePostQuery();
				query.setVideoId(updateFilePost.getVideoId());
				query.setTransferResult(VideoFileTransferResultEnum.FAIL.getStatus());
				Integer failCount = videoInfoFilePostMapper.selectCount(query);
				if(failCount>0){
					VideoInfoPost videoUpdate = new VideoInfoPost();
					videoUpdate.setStatus(VideoStatusEnum.STATUS1.getStatus());
					videoInfoPostMapper.updateByVideoId(videoUpdate,videoInfoFilePost.getVideoId());
					return;
				}
				query.setTransferResult(VideoFileTransferResultEnum.TRANSFER.getStatus());
				Integer transferCount = videoInfoFilePostMapper.selectCount(query);
				if(transferCount==0){
					Integer duration = videoInfoFilePostMapper.sumDuration(videoInfoFilePost.getVideoId());
					if(duration==null){
						duration=0;
					}
					//没问题就进入审核状态
					VideoInfoPost updatePost = new VideoInfoPost();
					updatePost.setStatus(VideoStatusEnum.STATUS2.getStatus());
					updatePost.setDuration(duration);
					videoInfoPostMapper.updateByVideoId(updatePost,videoInfoFilePost.getVideoId());
				}
		}
	}


	private Boolean changeVideoInfo(VideoInfoPost videoInfoPost){
		VideoInfoPost dbFileInfo = videoInfoPostMapper.selectByVideoId(videoInfoPost.getVideoId());
		if(!videoInfoPost.getVideoName().equals(dbFileInfo.getVideoName())
				|| !videoInfoPost.getVideoCover().equals(dbFileInfo.getVideoCover())
			    || !videoInfoPost.getTags().equals(dbFileInfo.getTags())
				|| !videoInfoPost.getIntroduction().equals(dbFileInfo.getIntroduction())){
			return true;
		}else {
			return false;
		}

	}

	private void union(String firPath,String toFilePath,Boolean delSource) throws Exception {
		File dir = new File(firPath);
		if(!dir.exists()){
			throw new BusinessException("目录不存在");
		}
		File[] fileList = dir.listFiles();
		File targetFile = new File(toFilePath);
		RandomAccessFile writer = null;

		try{
			writer = new RandomAccessFile(targetFile,"rw");
			byte[] b = new byte[1024*10];
			for(int i=0;i<fileList.length;i++){
				int len = -1;
				File chunkFile = new File(firPath + "//" + i);
				RandomAccessFile read = new RandomAccessFile(chunkFile,"r");
				try{
					while((len=read.read(b))!=-1){
						writer.write(b,0,len);
					}
				} catch (Exception e) {
					log.error("合并分片失败");
					throw new BusinessException("合并分片失败");
				}finally {
					read.close();
				}
			}
		} catch (Exception e) {
			log.error("合并分片失败");
			throw new BusinessException("合并分片失败");
		}finally {
			writer.close();
			if(delSource&& dir.exists()){
				for(int i=0;i<fileList.length;i++){
					fileList[i].delete();
				}
			}
		}
	}

	private void converMP42TS(String completePath){
		File videoFile = new File(completePath);
		File tsFolder = videoFile.getParentFile();
		String codec = fFmpegUtils.getVideoCodec(completePath);
		if(Constants.VIDEOS_CODE_HEVC.equals(codec)){
			String newFilename = completePath+Constants.VIDEO_CODE_TEMP_SUFFIX;
			new File(completePath).renameTo(new File(newFilename));
			fFmpegUtils.convertHevc2Mp4(newFilename,completePath);
			new File(newFilename).delete();
		}
		fFmpegUtils.cutFile4Video(tsFolder.getPath(),completePath);
		videoFile.delete();
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void aduitVideo(String videoId, Integer status, String reason) {
		VideoStatusEnum videoStatusEnum = VideoStatusEnum.getByStatus(status);
		if(videoStatusEnum==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		//乐观锁
		VideoInfoPost videoInfoPost = new VideoInfoPost();
		videoInfoPost.setStatus(status);

		VideoInfoPostQuery query = new VideoInfoPostQuery();
		query.setStatus(VideoStatusEnum.STATUS2.getStatus());
		query.setVideoId(videoId);
		Integer count =  videoInfoPostMapper.updateByParam(videoInfoPost,query);
		if(count==0){
			throw new BusinessException("审核失败，稍后重试");
		}
		//更新视频文件的状态
		VideoInfoFilePost videoInfoFilePost = new VideoInfoFilePost();
		videoInfoFilePost.setUpdateType(VideoFileUpdateTypeEnum.NO_UPDATE.getStatus());

		VideoInfoFilePostQuery filePostQuery = new VideoInfoFilePostQuery();
		filePostQuery.setVideoId(videoId);
		videoInfoFilePostMapper.updateByParam(videoInfoFilePost,filePostQuery);
		//审核不通过return
		if(VideoStatusEnum.STATUS4 == videoStatusEnum){
			return;
		}

		VideoInfoPost infopost = videoInfoPostMapper.selectByVideoId(videoId);

		VideoInfo dbvideoInfo = videoInfoMapper.selectByVideoId(videoId);

		if(dbvideoInfo==null){
			//第一次投稿，给用户加积分
			SysSettingDto sysSettingDto = redisComponent.getSystemSetting();
			//TODO 给用户加硬币
		}
		//将审核表拷贝到正式表
		VideoInfo videoInfo = CopyTools.copy(infopost,VideoInfo.class);
		videoInfoMapper.insertOrUpdate(videoInfo);
		//更新视频文件到正式表，先都删除再添加
		VideoInfoFileQuery  videoInfoFileQuery = new VideoInfoFileQuery();
		videoInfoFileQuery.setVideoId(videoId);
		videoInfoFileMapper.deleteByParam(videoInfoFileQuery);
		VideoInfoFilePostQuery videoInfoFilePostQuery = new VideoInfoFilePostQuery();
		videoInfoFilePostQuery.setVideoId(videoId);
		List<VideoInfoFilePost> videoInfoFilePostsList = videoInfoFilePostMapper.selectList(videoInfoFilePostQuery);
		List<VideoInfoFile> videoInfoFileList = CopyTools.copyList(videoInfoFilePostsList,VideoInfoFile.class);
		videoInfoFileMapper.insertBatch(videoInfoFileList);

		/**
		 * 删除文件，从消息队列中取出删除,因为到目前为止还没有删除本地服务器的视频文件
		 */
		List<String> filePathList = redisComponent.getDelFileList(videoId);
		if(filePathList!=null){
			for(String path : filePathList){
				File file = new File(appConfig.getProjectFolder()+Constants.FILE_FOLDER + path);
				if(file.exists()){
					FileUtils.deleteFile(file);
				}
			}
		}
		redisComponent.cleanDelFileList(videoId);

		//TODO 保存信息到es中
	}

}
package com.miniBili.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.constants.Constants;
import com.miniBili.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniBili.entity.enums.PageSize;
import com.miniBili.entity.query.CategoryInfoQuery;
import com.miniBili.entity.po.CategoryInfo;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.query.SimplePage;
import com.miniBili.mappers.CategoryInfoMapper;
import com.miniBili.service.CategoryInfoService;
import com.miniBili.utils.StringTools;


/**
 * 分类信息表 业务接口实现
 */
@Service("categoryInfoService")
public class CategoryInfoServiceImpl implements CategoryInfoService {

	@Resource
	private CategoryInfoMapper<CategoryInfo, CategoryInfoQuery> categoryInfoMapper;
    @Autowired
    private RedisComponent redisComponent;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<CategoryInfo> findListByParam(CategoryInfoQuery param) {
		//线性的
		List<CategoryInfo> convertLine2Tree = this.categoryInfoMapper.selectList(param);
		//递归将线性的转为树形的
		List<CategoryInfo> TreeCategoryList = convertLine2Tree(convertLine2Tree,0);
		return TreeCategoryList;
	}

	private List<CategoryInfo> convertLine2Tree(List<CategoryInfo> list,Integer pid){
		List<CategoryInfo> childen = new ArrayList<>();
		for(CategoryInfo m :list){
			if(m.getCategoryId()!=null&&m.getpCategoryId()!=null&&m.getpCategoryId()==pid){
				m.setChildren(convertLine2Tree(list,m.getCategoryId()));
				childen.add(m);
			}
		}
		return childen;
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(CategoryInfoQuery param) {
		return this.categoryInfoMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<CategoryInfo> findListByPage(CategoryInfoQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<CategoryInfo> list = this.findListByParam(param);
		PaginationResultVO<CategoryInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(CategoryInfo bean) {
		return this.categoryInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<CategoryInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.categoryInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<CategoryInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.categoryInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(CategoryInfo bean, CategoryInfoQuery param) {
		StringTools.checkParam(param);
		return this.categoryInfoMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(CategoryInfoQuery param) {
		StringTools.checkParam(param);
		return this.categoryInfoMapper.deleteByParam(param);
	}

	/**
	 * 根据CategoryId获取对象
	 */
	@Override
	public CategoryInfo getCategoryInfoByCategoryId(Integer categoryId) {
		return this.categoryInfoMapper.selectByCategoryId(categoryId);
	}

	/**
	 * 根据CategoryId修改
	 */
	@Override
	public Integer updateCategoryInfoByCategoryId(CategoryInfo bean, Integer categoryId) {
		return this.categoryInfoMapper.updateByCategoryId(bean, categoryId);
	}

	/**
	 * 根据CategoryId删除
	 */
	@Override
	public Integer deleteCategoryInfoByCategoryId(Integer categoryId) {
		return this.categoryInfoMapper.deleteByCategoryId(categoryId);
	}

	/**
	 * 根据CategoryCode获取对象
	 */
	@Override
	public CategoryInfo getCategoryInfoByCategoryCode(String categoryCode) {
		return this.categoryInfoMapper.selectByCategoryCode(categoryCode);
	}

	/**
	 * 根据CategoryCode修改
	 */
	@Override
	public Integer updateCategoryInfoByCategoryCode(CategoryInfo bean, String categoryCode) {
		return this.categoryInfoMapper.updateByCategoryCode(bean, categoryCode);
	}

	/**
	 * 根据CategoryCode删除
	 */
	@Override
	public Integer deleteCategoryInfoByCategoryCode(String categoryCode) {
		return this.categoryInfoMapper.deleteByCategoryCode(categoryCode);
	}

	@Override
	public void saveCategory(CategoryInfo categoryInfo) {
		CategoryInfo dbcatagory = categoryInfoMapper.selectByCategoryCode(categoryInfo.getCategoryCode());
		if (dbcatagory!=null&&categoryInfo.getCategoryId()==null || categoryInfo.getCategoryId()!=null && dbcatagory!=null&&!categoryInfo.getCategoryId().equals(dbcatagory.getCategoryId())){
			 throw new BusinessException("分类编号已存在");
		}
		if(categoryInfo.getCategoryId()==null){
			//新增操作
			Integer maxSort = categoryInfoMapper.getMaxSort(categoryInfo.getpCategoryId());
			if(maxSort==null){
				maxSort = 0;
			}
			categoryInfo.setSort(maxSort+1);
			categoryInfoMapper.insert(categoryInfo);
		}else {
			//更新
			categoryInfoMapper.updateByCategoryId(categoryInfo,categoryInfo.getCategoryId());
		}
		save2redis();
	}

	@Override
	public void delCategory(Integer categoryId) {
		//TODO查询分类下是否有视频，有就不能删喔

		//要删该分类下层的分类,一条SQL完成
		CategoryInfoQuery query = new CategoryInfoQuery();
		query.setCategoryIdOrPcategoryId(categoryId);
		categoryInfoMapper.deleteByParam(query);
//		我想用递归来删除试着自己写一下喔
//		List<Integer> delIds = new ArrayList<>();
//		findAllIds(delIds,categoryId);
//		for(Integer id : delIds){
//			categoryInfoMapper.deleteByCategoryId(id);
//		}
		save2redis();
	}

	@Override
	public void changeSort(Integer pCategoryId, String categoryIds) {
		String[] categoryIdArray = categoryIds.split(",");
		List<CategoryInfo> list = new ArrayList<>();
		Integer sort = 1;
		for(String id : categoryIdArray){
			CategoryInfo categoryInfo = new CategoryInfo();
			categoryInfo.setpCategoryId(pCategoryId);
			categoryInfo.setCategoryId(Integer.parseInt(id));
			categoryInfo.setSort(sort++);
			list.add(categoryInfo);
		}
		categoryInfoMapper.updateSortBatch(list);
		save2redis();
	}

	@Override
	public List<CategoryInfo> getAllCategoryList() {
		List<CategoryInfo> categoryInfoList = redisComponent.getCategoryList();
		if(categoryInfoList==null){
			save2redis();
		}
		return redisComponent.getCategoryList();
	}

	private void findAllIds(List<Integer>allIds, Integer categoryId){
		allIds.add(categoryId);
		List<CategoryInfo> categoryList = categoryInfoMapper.selectByCategoryPId(categoryId);
		if(categoryList==null) return;
		for(CategoryInfo c : categoryList){
			findAllIds(allIds,c.getCategoryId());
		}
	}

	private void save2redis(){
		CategoryInfoQuery query = new CategoryInfoQuery();
		query.setConvert2Tree(true);
		query.setOrderBy("sort asc");
		List<CategoryInfo> listByParam = findListByParam(query);
		redisComponent.saveCategoryList(listByParam);
	}

}
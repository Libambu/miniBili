package com.miniBili.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.entity.po.UserInfo;
import com.miniBili.entity.query.UserInfoQuery;
import com.miniBili.exception.BusinessException;
import com.miniBili.mappers.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniBili.entity.enums.PageSize;
import com.miniBili.entity.query.UserFocusQuery;
import com.miniBili.entity.po.UserFocus;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.query.SimplePage;
import com.miniBili.mappers.UserFocusMapper;
import com.miniBili.service.UserFocusService;
import com.miniBili.utils.StringTools;


/**
 *  业务接口实现
 */
@Service("userFocusService")
public class UserFocusServiceImpl implements UserFocusService {

	@Resource
	private UserFocusMapper<UserFocus, UserFocusQuery> userFocusMapper;
    @Autowired
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserFocus> findListByParam(UserFocusQuery param) {
		return this.userFocusMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserFocusQuery param) {
		return this.userFocusMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserFocus> findListByPage(UserFocusQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserFocus> list = this.findListByParam(param);
		PaginationResultVO<UserFocus> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserFocus bean) {
		return this.userFocusMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserFocus> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userFocusMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserFocus> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userFocusMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserFocus bean, UserFocusQuery param) {
		StringTools.checkParam(param);
		return this.userFocusMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserFocusQuery param) {
		StringTools.checkParam(param);
		return this.userFocusMapper.deleteByParam(param);
	}

	/**
	 * 根据UserIdAndFocusUserId获取对象
	 */
	@Override
	public UserFocus getUserFocusByUserIdAndFocusUserId(String userId, String focusUserId) {
		return this.userFocusMapper.selectByUserIdAndFocusUserId(userId, focusUserId);
	}

	/**
	 * 根据UserIdAndFocusUserId修改
	 */
	@Override
	public Integer updateUserFocusByUserIdAndFocusUserId(UserFocus bean, String userId, String focusUserId) {
		return this.userFocusMapper.updateByUserIdAndFocusUserId(bean, userId, focusUserId);
	}

	/**
	 * 根据UserIdAndFocusUserId删除
	 */
	@Override
	public Integer deleteUserFocusByUserIdAndFocusUserId(String userId, String focusUserId) {
		return this.userFocusMapper.deleteByUserIdAndFocusUserId(userId, focusUserId);
	}

	@Override
	public void focusUser(String userId, String focusUserId) {
		if(userId.equals(focusUserId)){
			throw new BusinessException("不能对自己进行操作");
		}
		UserFocus dbInfo = userFocusMapper.selectByUserIdAndFocusUserId(userId,focusUserId);
		if(dbInfo!=null){
			return;
		}
		UserInfo userInfo = userInfoMapper.selectByUserId(focusUserId);
		if(userInfo==null){
			throw new BusinessException(ResponseCodeEnum.CODE_600);
		}
		UserFocus userFocus = new UserFocus();
		userFocus.setFocusTime(new Date());
		userFocus.setUserId(userId);
		userFocus.setFocusUserId(focusUserId);
		userFocusMapper.insert(userFocus);
	}

	@Override
	public void cancleFocus(String userId, String focusUserId) {
		userFocusMapper.deleteByUserIdAndFocusUserId(userId,focusUserId);
	}
}
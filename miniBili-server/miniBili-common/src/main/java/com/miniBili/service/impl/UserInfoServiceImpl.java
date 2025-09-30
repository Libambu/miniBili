package com.miniBili.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.enums.UserSexEnum;
import com.miniBili.entity.enums.UserStatusEnum;
import com.miniBili.entity.po.UserInfo;
import com.miniBili.entity.query.UserInfoQuery;
import com.miniBili.exception.BusinessException;
import com.miniBili.utils.CopyTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniBili.entity.enums.PageSize;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.entity.query.SimplePage;
import com.miniBili.mappers.UserInfoMapper;
import com.miniBili.service.UserInfoService;
import com.miniBili.utils.StringTools;


/**
 * 用户信息表 业务接口实现
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private RedisComponent redisComponent;

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<UserInfo> findListByParam(UserInfoQuery param) {
		return this.userInfoMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(UserInfoQuery param) {
		return this.userInfoMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<UserInfo> list = this.findListByParam(param);
		PaginationResultVO<UserInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserInfo bean) {
		return this.userInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 多条件更新
	 */
	@Override
	public Integer updateByParam(UserInfo bean, UserInfoQuery param) {
		StringTools.checkParam(param);
		return this.userInfoMapper.updateByParam(bean, param);
	}

	/**
	 * 多条件删除
	 */
	@Override
	public Integer deleteByParam(UserInfoQuery param) {
		StringTools.checkParam(param);
		return this.userInfoMapper.deleteByParam(param);
	}

	/**
	 * 根据UserId修改
	 */
	@Override
	public Integer updateUserInfoByUserId(UserInfo bean, String userId) {
		return this.userInfoMapper.updateByUserId(bean, userId);
	}


	/**
	 * 根据UserId获取对象
	 */
	@Override
	public UserInfo getUserInfoByUserId(String userId) {
		return this.userInfoMapper.selectByUserId(userId);
	}


	/**
	 * 根据UserId删除
	 */
	@Override
	public Integer deleteUserInfoByUserId(String userId) {
		return this.userInfoMapper.deleteByUserId(userId);
	}

	/**
	 * 根据NickName获取对象
	 */
	@Override
	public UserInfo getUserInfoByNickName(String nickName) {
		return this.userInfoMapper.selectByNickName(nickName);
	}

	/**
	 * 根据NickName修改
	 */
	@Override
	public Integer updateUserInfoByNickName(UserInfo bean, String nickName) {
		return this.userInfoMapper.updateByNickName(bean, nickName);
	}

	/**
	 * 根据NickName删除
	 */
	@Override
	public Integer deleteUserInfoByNickName(String nickName) {
		return this.userInfoMapper.deleteByNickName(nickName);
	}

	/**
	 * 根据Email获取对象
	 */
	@Override
	public UserInfo getUserInfoByEmail(String email) {
		return this.userInfoMapper.selectByEmail(email);
	}

	/**
	 * 根据Email修改
	 */
	@Override
	public Integer updateUserInfoByEmail(UserInfo bean, String email) {
		return this.userInfoMapper.updateByEmail(bean, email);
	}

	/**
	 * 根据Email删除
	 */
	@Override
	public Integer deleteUserInfoByEmail(String email) {
		return this.userInfoMapper.deleteByEmail(email);
	}


	@Override
	public void register(String email, String nickName, String password) {
		UserInfo userInfo = userInfoMapper.selectByEmail(email);
		if(userInfo!=null){
			throw  new BusinessException("邮箱账号已存在");
		}
		UserInfo userInfo1 = userInfoMapper.selectByNickName(nickName);
		if(userInfo1!=null){
			throw  new BusinessException("昵称已存在");
		}
		UserInfo newUser = new UserInfo();
		newUser.setUserId(StringTools.getRandomString(Constants.LENGTH_10));
		newUser.setNickName(nickName);
		newUser.setEmail(email);
		newUser.setPassword(StringTools.encodeByMd5(password));
		newUser.setSex(UserSexEnum.secret.getType());
		newUser.setJoinTime(new Date());
		newUser.setStatus(UserStatusEnum.enable.getStatus());
		newUser.setTheme(Constants.ONE);
		//TODO 初始化用户银币
		newUser.setTotalCoinCount(10);
		newUser.setCurrentCoinCount(10);
		userInfoMapper.insert(newUser);
	}

	@Override
	public TokenInfoDto login(String email, String password, String ip) {
		UserInfo userInfo = userInfoMapper.selectByEmail(email);
		if(userInfo==null){
			throw new BusinessException("用户不存在");
		}
		if(!userInfo.getPassword().equals(password)){
			throw new BusinessException("用户密码错误");
		}
		if(userInfo.getStatus()==UserStatusEnum.disable.getStatus()){
			throw new BusinessException("账号已禁用");
		}
		UserInfo upteUser = new UserInfo();
		upteUser.setLastLoginTime(new Date());
		upteUser.setLastLoginIp(ip);
		userInfoMapper.updateByUserId(upteUser,userInfo.getUserId());
		TokenInfoDto tokenInfoDto = CopyTools.copy(userInfo,TokenInfoDto.class);
		TokenInfoDto tokenInfoRes = redisComponent.saveTokenInfo(tokenInfoDto);
		return tokenInfoRes;

	}
}
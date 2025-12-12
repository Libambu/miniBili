package com.miniBili.service.impl;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.CountInfoDto;
import com.miniBili.entity.dto.SysSettingDto;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.dto.UserCountInfoDto;
import com.miniBili.entity.enums.PageSize;
import com.miniBili.entity.enums.ResponseCodeEnum;
import com.miniBili.entity.enums.UserSexEnum;
import com.miniBili.entity.enums.UserStatusEnum;
import com.miniBili.entity.po.UserFocus;
import com.miniBili.entity.po.UserInfo;
import com.miniBili.entity.query.SimplePage;
import com.miniBili.entity.query.UserFocusQuery;
import com.miniBili.entity.query.UserInfoQuery;
import com.miniBili.entity.vo.PaginationResultVO;
import com.miniBili.exception.BusinessException;
import com.miniBili.mappers.UserFocusMapper;
import com.miniBili.mappers.UserInfoMapper;
import com.miniBili.mappers.VideoInfoMapper;
import com.miniBili.service.UserInfoService;
import com.miniBili.utils.CopyTools;
import com.miniBili.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 用户信息表 业务接口实现
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private RedisComponent redisComponent;

	@Resource
	private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

	@Autowired
	private UserFocusMapper<UserFocus, UserFocusQuery> userFocusMapper;
    @Autowired
    private VideoInfoMapper videoInfoMapper;

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
		SysSettingDto sysSettingDto = redisComponent.getSystemSetting();
		newUser.setTotalCoinCount(sysSettingDto.getRegisterCoinCount());
		newUser.setCurrentCoinCount(sysSettingDto.getRegisterCoinCount());
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

	@Override
	public UserInfo getUserDetail(String currentUserId, String userId) {
		UserInfo userInfo = getUserInfoByUserId(userId);
		if(userInfo==null){
			throw new BusinessException(ResponseCodeEnum.CODE_404);
		}
		CountInfoDto countInfoDto = videoInfoMapper.selectSumCountInfo(userInfo.getUserId());
		Integer fansCount = userFocusMapper.selectFansCount(userId);
		Integer focusCount = userFocusMapper.selectFocusCount(userId);
		userInfo.setLikeCount(countInfoDto.getLikeCount());
		userInfo.setPlayCount(countInfoDto.getPlayCount());
		userInfo.setFansCount(fansCount);
		userInfo.setFocusCount(focusCount);
		if(currentUserId==null){
			userInfo.setHaveFocus(false);
		}else{
			UserFocus userFocus = userFocusMapper.selectByUserIdAndFocusUserId(currentUserId, userId);
			if(userFocus==null){
				userInfo.setHaveFocus(false);
			}else {
				userInfo.setHaveFocus(true);
			}
		}
		return userInfo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserInfo(UserInfo userInfo, TokenInfoDto tokenInfoDto) {
		UserInfo dbInfo = userInfoMapper.selectByUserId(userInfo.getUserId());
		if(!dbInfo.getNickName().equals(userInfo.getNickName())||tokenInfoDto.getCurrentCoinCount()<Constants.UPDATE_NICK_NAME_COIN){
			throw new BusinessException("硬币不足，无法修改名称");
		}
		Integer count = userInfoMapper.updateCoinCountInfo(userInfo.getUserId(),-Constants.UPDATE_NICK_NAME_COIN);
		if(count==0){
			throw new BusinessException("硬币不足，无法修改昵称");
		}
		userInfoMapper.updateByUserId(userInfo,userInfo.getUserId());
		//将更新的信息重新传到token里面
		Boolean updateToken = false;
		if(tokenInfoDto.getAvatar()==null||!tokenInfoDto.getAvatar().equals(userInfo.getAvatar())){
			tokenInfoDto.setAvatar(userInfo.getAvatar());
			updateToken=true;
		}
		if(!tokenInfoDto.getNickName().equals(userInfo.getNickName())){
			tokenInfoDto.setNickName(userInfo.getNickName());
			updateToken=true;
		}
		if(updateToken){
			redisComponent.updateTokenInfo(tokenInfoDto);
		}
	}

	@Override
	public UserCountInfoDto getUserCountInfo(TokenInfoDto tokenInfoDto) {
		UserInfo user = getUserInfoByUserId(tokenInfoDto.getUserId());
		Integer fansCount =  userFocusMapper.selectFansCount(user.getUserId());
		Integer focusCount = userFocusMapper.selectFocusCount(user.getUserId());
		UserCountInfoDto userCountInfoDto = new UserCountInfoDto();
		userCountInfoDto.setFansCount(fansCount);
		userCountInfoDto.setFocusCount(focusCount);
		userCountInfoDto.setCurrentCoinCount(user.getCurrentCoinCount());
		return userCountInfoDto;
	}
}
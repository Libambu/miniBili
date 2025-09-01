package com.miniBili.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.query.UserInfoQuery;
import com.miniBili.entity.po.UserInfo;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.exception.BusinessException;
import com.miniBili.service.UserInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户信息表 Controller
 */
@RestController("userInfoController")
@RequestMapping("/account")
@Validated//检验参数是否为null的包
public class AccountController extends ABaseController{

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private RedisComponent redisComponent;

	/**
	 * 获取验证码
	 * @return
	 */
	//理论上登录用session，redis都可以，但是redis中另外一个浏览器会覆盖掉原来的key
	//但是session不会啊，两个浏览器不会有影响的，因为不同浏览器有session ID不影响当前用户的session
	@RequestMapping("/checkCode")
	public ResponseVO checkCode(){
		//参数是生成图的长，宽 px
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(100,42);
		//code 为计算答案
		String code = captcha.text();
		String CheckCodeKey = redisComponent.saveCheckCode(code);
		String checkCodeBase64 = captcha.toBase64();

		Map<String,String> result = new HashMap<>();
		result.put("checkCode",checkCodeBase64);
		result.put("checkCodeKey",CheckCodeKey);
		return getSuccessResponseVO(result);
	}

	@RequestMapping("/register")
	public ResponseVO register(@NotEmpty @Email @Size(max = 150) String email,
							   @NotEmpty @Size String nickName,
							   @NotEmpty String password,
							   @NotEmpty String checkCodeKey,
							   @NotEmpty String checkCode){

		try{
			if(!redisComponent.getCheckCode(checkCodeKey).equalsIgnoreCase(checkCode)){
				throw new BusinessException("图片验证码不正确");
			}
			userInfoService.register(email,nickName,password);
			return getSuccessResponseVO(null);
		}finally {
			redisComponent.cleanCheckCode(checkCodeKey);
		}
	}

	@RequestMapping("/login")
	public ResponseVO login(HttpServletRequest request,
							@NotEmpty @Email @Size(max = 150) String email,
							@NotEmpty String password,
							@NotEmpty String checkCodeKey,
							@NotEmpty String checkCode,
							HttpServletResponse response){
		try {
			if (!redisComponent.getCheckCode(checkCodeKey).equalsIgnoreCase(checkCode)) {
				throw new BusinessException("图片验证码不正确");
			}
			String ip = getIpAddr();
			TokenInfoDto tokenInfoDto = userInfoService.login(email, password, ip) ;
			saveCookie(response, tokenInfoDto.getToken());
			//TODO 设置粉丝硬币数关注数
			return getSuccessResponseVO(tokenInfoDto);
		} catch (NullPointerException e) {
			throw new BusinessException("验证码已过期");
		}
		finally {
			redisComponent.cleanCheckCode(checkCodeKey);
			String token = null;
			Cookie[] cookies = request.getCookies();
			//用户退出后所有Cookie都清除了，下次登录不这样会报空的
			if(cookies!=null){
				for(Cookie cookie : cookies){
					if(cookie.getName().equals(Constants.TOKEN_WEB)){
						token=cookie.getValue();
					}
				}
				redisComponent.cleanToken(token);
			}
		}
	}

	@RequestMapping("/autoLogin")
	public ResponseVO autoLogin(HttpServletResponse response){
		TokenInfoDto tokenInfoDto = getTokenInfoDto();
		if(tokenInfoDto==null){
			return getSuccessResponseVO(null);
		}
		if(tokenInfoDto.getExpireAt()-System.currentTimeMillis() < Constants.REDIS_KEY_EXPIRE_ONE_DAY){
			TokenInfoDto tokenInfoDto1 = redisComponent.saveTokenInfo(tokenInfoDto);
			saveCookie(response,tokenInfoDto1.getToken());
		}
		//TODo 设置硬币粉丝关注
		return getSuccessResponseVO(tokenInfoDto);
	}

	@RequestMapping("/logout")
	public ResponseVO logout(HttpServletResponse response){
		cleanCookie(response);
		return getSuccessResponseVO(null);
	}


}
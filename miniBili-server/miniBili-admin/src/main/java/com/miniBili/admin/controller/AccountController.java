package com.miniBili.admin.controller;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.config.AppConfig;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.entity.vo.ResponseVO;
import com.miniBili.exception.BusinessException;
import com.miniBili.service.UserInfoService;
import com.miniBili.utils.StringTools;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息表 Controller
 */
@RestController("userInfoController")
@RequestMapping("/admin/account")
@Validated//检验参数是否为null的包
public class AccountController extends ABaseController{

	@Autowired
	private AppConfig appConfig;
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


	@RequestMapping("/login")
	public ResponseVO login(HttpServletRequest request,
							@NotEmpty  @Size(max = 150) String account,
							@NotEmpty String password,
							@NotEmpty String checkCodeKey,
							@NotEmpty String checkCode,
							HttpServletResponse response){
		try {
			if (!redisComponent.getCheckCode(checkCodeKey).equalsIgnoreCase(checkCode)) {
				throw new BusinessException("图片验证码不正确");
			}
			if(!account.equals(appConfig.getAdminAccount())||!password.equals(StringTools.encodeByMd5(appConfig.getAdminPassword()))){
				throw new BusinessException("账号密码错误");
			}
			String token = redisComponent.saveTokenInfo4admin(account);
			saveCookieAdmin(response,token);

			return getSuccessResponseVO(account);
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
					if(cookie.getName().equals(Constants.TOKEN_ADMIN)){
						token=cookie.getValue();
					}
				}
				redisComponent.cleanToken4Admin(token);
			}
		}
	}


	@RequestMapping("/logout")
	public ResponseVO logout(HttpServletResponse response){
		cleanCookie(response);
		return getSuccessResponseVO(null);
	}


}
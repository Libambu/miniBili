package com.miniBili.component;

import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.dto.TokenInfoDto;
import com.miniBili.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RedisComponent {

    @Autowired
    private RedisUtils redisUtils;

    public String saveCheckCode(String code){
        UUID uuid = UUID.randomUUID();
        String key =  uuid.toString();
        redisUtils.setex(Constants.REDIS_KEY_CHECK_CODE + key,code,Constants.REDIS_KEY_EXPIRE_ONE_MIN*10);
        return key;
    }

    public String getCheckCode(String checkCodeKey){
        return (String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey);
    }

    public void cleanCheckCode(String checkCodeKey){
        redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE+checkCodeKey);
    }

    public TokenInfoDto saveTokenInfo(TokenInfoDto tokenInfoDto){
        String  token = UUID.randomUUID().toString();
        tokenInfoDto.setExpireAt(System.currentTimeMillis()+Constants.REDIS_KEY_EXPIRE_ONE_DAY*7l);
        tokenInfoDto.setToken(token);
        redisUtils.setex(Constants.REDIS_KEY_TOKEN_WEB+token,tokenInfoDto,Constants.REDIS_KEY_EXPIRE_ONE_DAY*7l);
        return tokenInfoDto;
    }

    public void cleanToken(String token){
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_WEB+token);
    }

    public TokenInfoDto getTokenInfoDtoByToken(String token){
        return (TokenInfoDto) redisUtils.get(Constants.REDIS_KEY_TOKEN_WEB+token);
    }

    public String saveTokenInfo4admin(String account){
        String  token = UUID.randomUUID().toString();
       redisUtils.setex(Constants.REDIS_KEY_TOKEN_ADMIN+token,account,Constants.REDIS_KEY_EXPIRE_ONE_DAY);
       return token;
    }


    public void cleanToken4Admin(String token){
        redisUtils.delete(Constants.REDIS_KEY_TOKEN_ADMIN+token);
    }


    public String  getToken4Admin(String token) {
        return (String) redisUtils.get(Constants.REDIS_KEY_TOKEN_ADMIN + token);
    }
}

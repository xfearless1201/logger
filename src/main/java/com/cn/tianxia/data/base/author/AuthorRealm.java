package com.cn.tianxia.data.base.author;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName AuthorRealm
 * @Description 校验授权
 * @author Hardy
 * @Date 2019年5月27日 下午3:25:07
 * @version 1.0.0
 */
@Component
public class AuthorRealm extends AuthorizingRealm{
    
    /**
     * 强转自定义token
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof AuthorToken;
    }

    /**
     * 登录授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
    
    
    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        
        if(token.getCredentials() == null || token.getPrincipal() == null){
            throw new UnknownAccountException();
        }
        
        //获取用户请求的token
        String authorToken = (String) token.getPrincipal();
        
        //获取用户的信息
        AuthorUser user = null;
        
        return null;
    }

}

package com.cn.tianxia.data.base.author;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * 
 * @ClassName AuthorMatcher
 * @Description 权限校验器
 * @author Hardy
 * @Date 2019年5月27日 下午3:24:55
 * @version 1.0.0
 */
public class AuthorMatcher implements CredentialsMatcher{

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        // TODO Auto-generated method stub
        return false;
    }

}

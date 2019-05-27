package com.cn.tianxia.data.base.author;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName AuthorToken
 * @Description 授权实体类
 * @author Hardy
 * @Date 2019年5月27日 下午3:33:23
 * @version 1.0.0
 */
public class AuthorToken implements AuthenticationToken {

    private static final long serialVersionUID = -5973397275170900921L;

    private String token;

    private String ip;

    public AuthorToken(String token, String ip) {
        super();
        this.token = token;
        this.ip = ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.ip;
    }

}

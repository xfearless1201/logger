package com.cn.tianxia.data.base.author;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cn.tianxia.data.base.common.ResponseResult;
import com.cn.tianxia.data.consts.CommonConsts;
import com.cn.tianxia.data.utils.HttpContextUtils;
import com.cn.tianxia.data.utils.IPUtils;
import com.cn.tianxia.data.utils.MD5Utils;
import com.cn.tianxia.data.utils.ParamsUtil;

import cn.hutool.json.JSONUtil;

/**
 * 
 * @ClassName AuthorFilter
 * @Description 权限过滤器
 * @author Hardy
 * @Date 2019年5月27日 下午3:24:43
 * @version 1.0.0
 */
public class AuthorFilter extends AuthenticatingFilter{

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String authorizationToken = ParamsUtil.getHeader(request, CommonConsts.AUTHORIZATION_TOKEN);
        if(StringUtils.isBlank(authorizationToken)){
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
            ParamsUtil.responseWrite(JSONUtil.toJsonStr(ResponseResult.fail("无效请求")),response);
            return false;
        }
        //发起认证请求
        return executeLogin(request, response);
    }
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())){
            return true;
        }
        return false;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
            ServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
        if(e.getMessage().equalsIgnoreCase(CommonConsts.EXPIRED_JWT)){
            ParamsUtil.responseWrite(JSONUtil.toJsonStr(ResponseResult.fail("无效请求")),response);
            return false;
        }
        ParamsUtil.responseWrite(JSONUtil.toJsonStr(ResponseResult.fail("无权访问")),response);
        return false;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //从请求头获取认证标识
        String authorizationToken = ParamsUtil.getHeader(request, CommonConsts.AUTHORIZATION_TOKEN);
        //获取用户请求IP地址
        String reqIp = MD5Utils.md5toUpCase_16Bit(IPUtils.getIpFromRequest(WebUtils.toHttp(request)));
        return new AuthorToken(reqIp, authorizationToken);
    }

}

package com.cn.tianxia.data.web.sys;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cn.tianxia.data.base.common.ResponseResult;

/**
 * 
 * @ClassName SysLoginController
 * @Description 系统登录接口
 * @author Hardy
 * @Date 2019年5月27日 下午3:42:26
 * @version 1.0.0
 */
@RestController
@RequestMapping("sys")
public class SysLoginController {

    
    /**
     * 
     * @Description 登入
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public ResponseResult login(String username,String password){
        return ResponseResult.success("登录成功", "");
    }
    
    
    
    /**
     * 
     * @Description 登出
     * @return
     */
    @PostMapping("/logout")
    public ResponseResult logout(){
        return ResponseResult.success("登出成功");
    }
}

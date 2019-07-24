package com.travel.service;

import com.travel.domain.User;

public interface UserService {

    /**
     * 用户注册
     * @param user
     * @return
     */
    public abstract boolean register(User user)  throws Exception ;

    /**
     * 激活账号
     * @param code
     */
    public abstract  Boolean active(String code) ;


    /**
     * 登录
     * @param user
     * @return
     */
    public abstract User login(User user) throws Exception   ;
}



















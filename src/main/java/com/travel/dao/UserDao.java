package com.travel.dao;

import com.travel.domain.User;

public interface UserDao {
    /**
     * 保存用户数据
     * @param user
     * @return
     */
    public int register(User user);
    /**
     * 根据code查询用户信息；
     * @param code
     * @return
     */
    public User findByCode(String code);

    /**
     * 更改用户状态
     * @param user
     * @return
     */
    public int update(User user);

    /**
     * 登录
     * @param user
     * @return
     */
    public User login(User user);
}

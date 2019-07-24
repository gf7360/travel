package com.travel.dao.impl;

import com.travel.dao.UserDao;
import com.travel.domain.User;
import com.travel.utils.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    /**
     * 保存用户数据
     * @param user
     * @return
     */
    public int register(User user) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
            String sql = "insert into tab_user values(null,?,?,?,?,?,?,?,?,?) ";
            Object [] params = {user.getUsername(),user.getPassword(),user.getName(),
                                user.getBirthday(),user.getSex(),user.getTelephone(),
                                user.getEmail(),user.getStatus(),user.getCode()};
            return jdbcTemplate.update(sql,params);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据code查询用户信息；
     * @param code
     * @return
     */
    public User findByCode(String code) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
            String sql = "select * from tab_user where code = ? ";
            return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),code);
        } catch (DataAccessException e) {
          return null;
        }

    }

    /**
     * 更改用户状态
     * @param user
     * @return
     */
    public int update(User user) {
        try {
            JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
            String sql = "update tab_user set status = ?,code = null where uid = ?   ";
            return template.update(sql,"Y",user.getUid());
        } catch (DataAccessException e) {
            return 0;
        }
    }

    /**
     * 登录
     * @param user
     * @return
     */
    public User login(User user) {
        try {
            JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
            String sql = "select * from tab_user where  username = ? and password = ? ";
            return template.queryForObject(sql,new BeanPropertyRowMapper<User>(User.class),user.getUsername(),user.getPassword());
        } catch (DataAccessException e) {
            return null;
        }
    }
}












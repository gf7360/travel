package com.travel.service.impl;

import com.travel.dao.UserDao;
import com.travel.domain.User;
import com.travel.service.UserService;
import com.travel.utils.BeanFactoryUtils;
import com.travel.utils.MD5Utils;
import com.travel.utils.MailUtils;
import com.travel.utils.UUIDUtils;

public class UserServiceImpl implements UserService {
     //private  UserDao userDao = new UserDao();
    //使用工厂创建对象；
    private  UserDao userDao = (UserDao) BeanFactoryUtils.getBean("UserDao");

    /**
     * 用户注册
     * @param user
     * @return
     */
    public boolean register(User user) throws Exception {
      //组装dao层需要的数据；
        //对用户输入的密码进行加密  MD5;
        String psw = MD5Utils.encodeByMd5(user.getPassword());
        user.setPassword(psw);
        //设置激活状态， 默认为不激活  “N”
         user.setStatus("N");
         //借用UUID生成唯一标识作为激活码；
        String code = UUIDUtils.getUuid();
        user.setCode(code);
        //调用dao层保存数据
        int count = userDao.register(user);
        if (count==1){
            //注册成功 向用户发送邮件 激活； code
            String toEmail = user.getEmail();
            String emailMsg = "<a href = 'loclahost:80/travel/user?action=active&code="+code+"'>点击激活旅游账号</a>";
            String subject = "激活邮件";
            MailUtils.sendMail(toEmail,subject,emailMsg);
            return true;
        }
            return false;

    }

    /**
     * 激活账号
     * @param code
     */
    public Boolean active(String code) {
            //调用dao层根据code查询用户；
            User user = userDao.findByCode(code);
        if(user!=null){
            //查询到后将状态修改为“Y”
            user.setStatus("Y");
           int count =  userDao.update(user);
           return count ==1?true:false;
        }
            //表示没有此用户；
        return false;
        }

    /**
     * 登录
     * @param user
     * @return
     */
    public User login(User user) throws Exception {
        //对密码进行加密；
        user.setPassword( MD5Utils.encodeByMd5(user.getPassword()));
        return userDao.login(user);

    }
}



















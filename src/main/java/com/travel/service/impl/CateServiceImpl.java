package com.travel.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.dao.CateDao;
import com.travel.domain.Category;
import com.travel.service.CateService;
import com.travel.utils.BeanFactoryUtils;
import com.travel.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

public class CateServiceImpl implements CateService {

    /**
     * 查询所有分类信息
     *
     * @return
     */
    public String findCateAll() throws Exception {
        //使用redis做缓存；先查redis；
        Jedis jedis = JedisUtils.getJedis();
        String cateListJson = jedis.get("cateListJson");
        if(cateListJson == null) {
            //证明redis中没有，先查询数据库并保存到redis；
            //调用dao层查询所有分类信息；
            CateDao dao = (CateDao)BeanFactoryUtils.getBean("CateDao");

            List<Category> list = dao.findCateAll();
            ObjectMapper om = new ObjectMapper();
            cateListJson = om.writeValueAsString(list);
            jedis.set("cateListJson", cateListJson);
        }
        //关闭连接
        jedis.close();
        return cateListJson;
    }
}

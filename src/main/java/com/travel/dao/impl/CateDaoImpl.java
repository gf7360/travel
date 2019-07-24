package com.travel.dao.impl;

import com.travel.dao.CateDao;
import com.travel.domain.Category;
import com.travel.utils.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CateDaoImpl implements CateDao {
    /**
     * 查询所有分类信息；
     * @return
     */
    public List<Category> findCateAll() throws Exception {

            JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
            String sql = "select * from tab_category ORDER BY cid ";
            return template.query(sql,new BeanPropertyRowMapper<Category>(Category.class));

    }
}

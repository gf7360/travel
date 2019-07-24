package com.travel.dao;

import com.travel.domain.Category;

import java.util.List;

public interface CateDao {
    /**
     * 查询所有分类信息；
     * @return
     */
    public List<Category> findCateAll() throws Exception;
}

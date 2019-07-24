package com.travel.service;

import com.travel.domain.Route;
import com.travel.utils.PageBean;

public interface RouteService {
    /**
     * 分页查询所有线路信息；
     * @param cid
     * @param pageNumber
     * @param pageSize
     * @param rname
     * @return
     * @throws Exception
     */
    public PageBean findByPage(String cid, int pageNumber, int pageSize, String rname)throws Exception;
    /**
     * 查询线路详情
     * @param rid
     * @return
     */
    public Route findRouteById(String rid)throws Exception;
}

package com.travel.dao;

import com.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * 查询线路总条数
     * @param cid
     * @return
     * @throws Exception
     */
    public int findTotalCount(String cid,String rname) throws Exception;
    /**
     * 分页查询当前页面上的数据信息；
     * @param startIndex
     * @param pageSize
     * @param cid
     * @return
     */
    public List<Route> findPage(int startIndex, int pageSize, String cid, String rname) throws Exception;
    /**
     * 查询线路详情
     * @param rid
     */
    public Route findRouteById(String rid) throws Exception;

}

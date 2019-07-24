package com.travel.dao.impl;

import com.travel.dao.RouteDao;
import com.travel.domain.Category;
import com.travel.domain.Route;
import com.travel.domain.RouteImg;
import com.travel.domain.Seller;
import com.travel.utils.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    /**
     * 查询线路总条数
     * @param cid
     * @return
     * @throws Exception
     */
    public int findTotalCount(String cid,String rname) throws Exception{
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select count(*) from tab_route where rflag = 1  ";
        ArrayList<Object> list = new ArrayList<>();
        //拼接sql；
        if(cid!=null && !"".equalsIgnoreCase(cid)){
            sql+="and cid = ?   ";
            list.add(cid);
        }
        if(rname != null && !"".equals(rname)){
            //根据rname模糊匹配；
            sql+="and rname LIKE ? ";
            list.add("%"+rname+"%");
        }
        Object[] params = list.toArray();
        return template.queryForObject(sql,int.class,params);
    }

    /**
     * 分页查询当前页面上的数据信息；
     * @param startIndex
     * @param pageSize
     * @param cid
     * @return
     */
    public List<Route> findPage(int startIndex, int pageSize, String cid,String rname) throws Exception{
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select * from tab_route where rflag = 1 ";
        //拼接Sql；
        ArrayList<Object> list = new ArrayList<>();
        //拼接sql；
        if(cid!=null && !"".equalsIgnoreCase(cid)){
            sql+="and cid = ? ";
            list.add(cid );
        }
        if(rname != null && !"".equals(rname)){
            //根据rname模糊匹配；
            sql+="and rname LIKE ? ";
            list.add("%"+rname+"%");
        }
        sql+="limit ?,? ";
        list.add(startIndex);
        list.add(pageSize);
        Object[] params = list.toArray();
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),params);

    }

    /**
     * 查询线路详情
     * @param rid
     */
    public Route findRouteById(String rid) throws Exception{
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        //查询route表实体；
        String sql = "select * from tab_route where rid = ? ";
        Route route = template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
        //查询category 表数据；
        sql = "SELECT  * FROM  tab_category where cid = ? ";
        Category category = template.queryForObject(sql,new BeanPropertyRowMapper<Category>(Category.class),route.getCid());
        //查询商家信息；
        sql = "SELECT * FROM tab_seller WHERE sid = ? ";
        Seller seller = template.queryForObject(sql, new BeanPropertyRowMapper<Seller>(Seller.class), route.getSid());
        //查询route_img
        sql = "SELECT * FROM tab_route_img WHERE rid = ? ";
        List<RouteImg> routeImgs = template.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class), rid);
        route.setCategory(category);
        route.setSeller(seller);
        route.setRouteImgList(routeImgs);
        return route;
    }
}

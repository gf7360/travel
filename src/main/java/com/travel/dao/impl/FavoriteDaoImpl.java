package com.travel.dao.impl;

import com.travel.domain.Favorite;
import com.travel.domain.Route;
import com.travel.utils.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class FavoriteDaoImpl implements com.travel.dao.FavoriteDao {
    /**
     * 查询线路是否被用户收藏；
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public Favorite isFavorite(String rid, int uid)  {
        try {
            JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
            String sql = "select * from tab_favorite where rid = ? and uid = ? ";
            return template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
            return null;
        }

    }

    /**
     * 添加收藏
     * @param favorite
     * @param template
     */
    @Override
    public void addFavorite(Favorite favorite, JdbcTemplate template) throws Exception{
         String sql = "insert into tab_favorite values(? ,? ,? ) ";
          template.update(sql, favorite.getRid(),favorite.getDate(),favorite.getUid());
    }

    /**
     * 修改收藏次数
     * @param rid
     * @param template
     */
    @Override
    public void updateRouteCount(String rid, JdbcTemplate template)throws Exception {
        String sql = "update tab_route set count = count + 1 where rid = ? ";
        template.update(sql, rid);
    }

    /**
     * 查询用户收藏的线路综合；
     * @param uid
     * @return
     */
    @Override
    public int findTotalCountById(int uid)throws Exception {
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select count(*) from tab_favorite where  uid = ? ";
        return template.queryForObject(sql,int.class,uid);
    }

    /**
     * 查询用户收藏的线路
     *分页展示；
     * @param startIndex
     * @param pageSize
     * @param uid
     * @return
     */
    @Override
    public List<Favorite> findMyFavoriteByPage(int startIndex, int pageSize, int uid) throws Exception{
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select * from tab_favorite where  uid = ? limit ?,? " ;
        //查询用户收藏的线路信息；
        List<Favorite> listFavorite = template.query(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), uid,startIndex,pageSize);
        //根据线路信息 查询收藏信息；
        for(Favorite favorite : listFavorite){
            sql = "select * FROM  tab_route where rid = ? ";
            Route route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), favorite.getRid());
            favorite.setRoute(route);
        }
        return listFavorite;
    }

    /**
     * 查询收藏排行榜总条数
     * @return
     */
    @Override
    public int findTotalCountByCount() throws Exception {
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select count(*) from tab_route where  rflag = 1  " ;
        return template.queryForObject(sql, int.class);

    }

    /**
     *分页查询收藏排行榜总条数
     * @param startIndex
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> findFavoriteRankByPage(int startIndex, int pageSize) throws Exception {
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select * from tab_route where rflag = 1 ORDER BY count desc limit ?,?  " ;
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),startIndex,pageSize);
    }
}
















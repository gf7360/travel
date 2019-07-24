package com.travel.dao;

import com.travel.domain.Favorite;
import com.travel.domain.Route;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface FavoriteDao {
    /**
     * 查询用户是否收藏了该条线路
     * @param rid
     * @param uid
     * @return
     */
    Favorite isFavorite(String rid, int uid)  ;

    /**
     * 添加收藏；
     * @param favorite
     * @param template
     */
    void addFavorite(Favorite favorite, JdbcTemplate template) throws Exception;

    /**
     * 修改收藏次数
     * @param rid
     * @param template
     */
    void updateRouteCount(String rid, JdbcTemplate template) throws Exception;

    /**
     * 查询用户收藏的线路总条数；
     * @param uid
     * @return
     */
    int findTotalCountById(int uid) throws Exception;

    /**
     * 查询用户收藏的线路；
     *
     * @param startIndex
     * @param pageSize
     * @param uid
     * @return
     */
    List<Favorite> findMyFavoriteByPage(int startIndex, int pageSize, int uid) throws Exception;

    /**
     * 计算收藏排行榜总条数；
     * @return
     */
    int findTotalCountByCount() throws Exception;

    /**
     * 分页查询收藏排行榜
     * @param startIndex
     * @param pageSize
     * @return
     */

    List<Route> findFavoriteRankByPage(int startIndex, int pageSize) throws Exception;
}

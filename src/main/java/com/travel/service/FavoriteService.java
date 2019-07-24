package com.travel.service;

import com.travel.domain.Favorite;
import com.travel.domain.Route;
import com.travel.domain.User;
import com.travel.utils.PageBean;

public interface FavoriteService {
    /**
     * 查询用户是否收藏了该路线
     * @param rid
     * @param user
     * @return
     */
    boolean isFavorite(String rid, User user) throws Exception;

    /**
     * 添加收藏
     * @param rid
     * @param user
     */
    void addFavorite(String rid, User user)throws Exception;

    /**
     * 分页查询用户收藏信息；
     * @param pageNumber
     * @param pageSize
     * @param user
     * @return
     */
    PageBean<Favorite> findMyFavoriteByPage(int pageNumber, int pageSize, User user) throws Exception;

    /**
     * 分页查询收藏排行榜
     * @param pageNumber
     * @param pageSize
     * @return
     */
    PageBean<Route> findFavoriteRankByPage(int pageNumber, int pageSize) throws Exception;
}

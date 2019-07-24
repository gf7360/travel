package com.travel.service.impl;

import com.travel.dao.FavoriteDao;
import com.travel.domain.Favorite;
import com.travel.domain.Route;
import com.travel.domain.User;
import com.travel.service.FavoriteService;
import com.travel.utils.BeanFactoryUtils;
import com.travel.utils.JDBCUtils;
import com.travel.utils.PageBean;
import com.travel.utils.PageUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {
    //使用工厂创建dao对象；
    private FavoriteDao dao = (FavoriteDao)BeanFactoryUtils.getBean("FavoriteDao");
    /**
     * 查询用户是否收藏了该路线
     * @param rid
     * @param user
     * @return
     */
    @Override
    public boolean isFavorite(String rid, User user) throws Exception {

        //调用dao层查询 中间表 tab_favorite 是否rid 有匹配的uid；
        Favorite favorite = dao.isFavorite(rid,user.getUid());

        return favorite!=null ;
    }

    /**
     * 添加收藏
     * @param rid
     * @param user
     * @throws Exception
     */
    @Override
    public void addFavorite(String rid, User user) throws Exception {
        //封装favorite数据；
        Favorite favorite = new Favorite();
        favorite.setRid(Integer.parseInt(rid));
        favorite.setUid(user.getUid());
        //创建时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        favorite.setDate(format.format(date));
        //调用dao层添加收藏
        //修改收藏次数；
        // 事务控制
        //一: 创建JdbcTemplate对象
        //1.获取连接池对象
        DataSource dataSource = JDBCUtils.getDataSource();
        //2.创建jdbcTemplate实例 // template从连接池中获取一个连接
        JdbcTemplate template = new JdbcTemplate(dataSource);
        //二: 启动事务管理器 手动提交事务
        //3.启动事务管理器(将conn和当前线程做绑定)
        TransactionSynchronizationManager.initSynchronization();
        //4.获取连接 : 获取JdbcTemplate所使用的连接对象
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            //5.将连接的事务,设置为手动事务提交
            conn.setAutoCommit(false);
            //===== 业务处理
            //1.添加中间表数据信息
            dao.addFavorite(favorite,template);
            //2.修改收藏次数
            dao.updateRouteCount(rid,template);
            // 提交事务
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // 事务回顾
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            // 将异常抛给调用者,告知执行失败
            throw e;
        } finally {
            // 将conn对象和当前线程解除绑定
            TransactionSynchronizationManager.clearSynchronization();
            // 修改为自动事务提交
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 分页查询用户的收藏信息；
     * @param pageNumber
     * @param pageSize
     * @param user
     * @return
     */
    @Override
    public PageBean<Favorite> findMyFavoriteByPage(int pageNumber, int pageSize, User user) throws Exception {
        //分页查询tab_favorite 根据表中uid查询收藏的线路总条数；
        int totalCount = dao.findTotalCountById(user.getUid());//总条数；
        //创建pageBean对象 封装数据；
        PageBean<Favorite> pb = new PageBean<>(pageNumber,pageSize,totalCount);
        //查询当前页面的数据信息；
        //计算起始索引
        int startIndex = pb.getStartIndex();
        //调用dao层查询 收藏信息；
        List<Favorite> list = dao.findMyFavoriteByPage(startIndex,pageSize,user.getUid());
        pb.setData(list);
        //计算前四后五
        int[] ints = PageUtils.pagination(pageNumber, pb.getTotalPage());
        pb.setStart(ints[0]);
        pb.setEnd(ints[1]);
        return pb;
    }

    /**
     * 分页查询收藏排行榜；
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Route> findFavoriteRankByPage(int pageNumber, int pageSize)throws Exception {
        //查询总条数；
        int totalCount = dao.findTotalCountByCount();
        //封装pb对象
        PageBean<Route> pb = new PageBean<>(pageNumber, pageSize, totalCount);
        //计算起始索引
        int startIndex = pb.getStartIndex();
        //查询收藏排行榜 倒序；
         List<Route> list = dao.findFavoriteRankByPage(startIndex ,pageSize );
         pb.setData(list);
        int[] ints = PageUtils.pagination(pageNumber, pb.getTotalPage());
        //计算前四后五
        pb.setStart(ints[0]);
        pb.setEnd(ints[1]);
        return pb;
    }
}

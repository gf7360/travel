package com.travel.service.impl;



import com.travel.dao.RouteDao;
import com.travel.domain.Route;
import com.travel.service.RouteService;
import com.travel.utils.BeanFactoryUtils;
import com.travel.utils.PageBean;
import com.travel.utils.PageUtils;

public class RouteServiceImpl implements RouteService {
    //使用工厂创建dao对象；
    private RouteDao dao = (RouteDao) BeanFactoryUtils.getBean("RouteDao");

    /**
     * 分页查询所有线路信息；
     * @param cid
     * @param pageNumber
     * @param pageSize
     * @param rname
     * @return
     * @throws Exception
     */
    public PageBean findByPage(String cid, int pageNumber, int pageSize,String rname)throws Exception {
        //查询当前分类下的线路总条数；

        int totalCount = dao.findTotalCount(cid,rname);
        //组装pageBean
        PageBean<Route> pb = new PageBean<Route>(pageNumber,pageSize,totalCount);
        //获取起始索引；
        int startIndex = pb.getStartIndex();
        //查询当前分类下的所有信息；封装到data中
        pb.setData(dao.findPage(startIndex,pageSize,cid,rname));
        //前五后四判断；
        int[] ints = PageUtils.pagination(pageNumber, pb.getTotalPage());
        pb.setStart(ints[0]);
        pb.setEnd(ints[1]);
        return pb;
    }
    /**
     * 查询线路详情
     * @param rid
     * @return
     */
    public Route findRouteById(String rid)throws Exception {
        //dao层查询数据；
         return dao.findRouteById(rid);


    }

}

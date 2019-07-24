package com.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.domain.Favorite;
import com.travel.domain.ResultInfo;
import com.travel.domain.Route;
import com.travel.domain.User;
import com.travel.service.FavoriteService;
import com.travel.service.RouteService;
import com.travel.service.impl.RouteServiceImpl;
import com.travel.utils.BeanFactoryUtils;
import com.travel.utils.PageBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "FavoriteServlet", urlPatterns = "/favorite")
public class FavoriteServlet extends BaseServlet {
    private FavoriteService service = (FavoriteService)BeanFactoryUtils.getBean("FavoriteService");

    /**
     * 分页查询收藏排行榜
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findFavoriteRankByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;
        try {
            int pageNumber = 1;
            try{
                //获取请求参数
                pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            }catch (Exception e ){
                pageNumber = 1 ;
            }
            //自定义每页显示条数；
            int pageSize = 4;
            //调用service查询所有收藏信息
            PageBean<Route> pb = service.findFavoriteRankByPage(pageNumber,pageSize);
            //返回信息给前台；
            info = new ResultInfo(true);
            info.setData(pb);

        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false,"当前功能正在维护");

        }
        ObjectMapper om = new ObjectMapper();
        String infoJson = om.writeValueAsString(info);
        //System.out.println(infoJson);
        response.getWriter().print(infoJson);
    }
    /**
     * 根据id查询我的收藏；
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findMyFavoriteByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;
        try {
            //从session中获取用户信息;
            User user = (User) request.getSession().getAttribute("user");
            //获取请求参数；
            int pageNumber = 1;
            try {
                pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            } catch (Exception e) {
                pageNumber = 1;
            }
            //定义每页展示收藏条数；
            int pageSize = 5;
            //调用service查找用户收藏信息；
            PageBean<Favorite> pb = service.findMyFavoriteByPage(pageNumber,pageSize,user);
            //返回查询信息到前台；
            info = new ResultInfo(true);
            info.setData(pb);
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false,"当前功能正在维护...");
        }
        ObjectMapper om = new ObjectMapper();
        String infoJson = om.writeValueAsString(info);
        System.out.println(infoJson);
        response.getWriter().print(infoJson);
    }
    /**
     * 添加收藏；
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;
        try {
            ObjectMapper om = new ObjectMapper();
            //获取rid的值
            String rid = request.getParameter("rid");
            //获取用户信息
            User user = (User) request.getSession().getAttribute("user");
            if(user==null){
                info = new ResultInfo(false);
                info.setData(-1);
                String infoJeson = om.writeValueAsString(info);
                response.getWriter().print(infoJeson);
                return;
            }
            //添加收藏；
            service.addFavorite(rid ,user);
            //返回结果
            info = new ResultInfo(true);
            Route route = new RouteServiceImpl().findRouteById(rid);
            info.setData(route.getCount());
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false,"当前功能正在维护...");
        }
        ObjectMapper om = new ObjectMapper();
        String infoJson = om.writeValueAsString(info);
        System.out.println(infoJson);
        response.getWriter().print(infoJson);
    }
    /**
     * 判断收藏按钮是否可用
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info ;

        try {
            //判断用户是否登录：根据session中是否有user判断；
            User user = (User) request.getSession().getAttribute("user");
            //获取rid
            String rid = request.getParameter("rid");
            if(user==null){
                //证明没有登录；让用户到登录页面
               info = new ResultInfo(false);
               info.setData(-1);//-1 代表未登录；
            }else{
                //证明用户已经登录，
                //判断是否收藏了改线路；
                boolean flag = service.isFavorite(rid,user);
                //处理返回结果；
                if (flag) {
                    info = new ResultInfo(flag);
                } else {
                    info = new ResultInfo(flag);
                    info.setData(1);//1表示登录未收藏；
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false,"当前功能正在维护...");
        }
        ObjectMapper om = new ObjectMapper();
        String infoJeson = om.writeValueAsString(info);
        System.out.println(infoJeson);
        response.getWriter().print(infoJeson);

    }


}
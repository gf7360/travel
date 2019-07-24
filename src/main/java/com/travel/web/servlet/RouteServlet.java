package com.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.domain.ResultInfo;
import com.travel.domain.Route;
import com.travel.service.CateService;
import com.travel.service.RouteService;
import com.travel.utils.BeanFactoryUtils;
import com.travel.utils.PageBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectOutputStream;

@WebServlet(name = "RouteServlet", urlPatterns = "/route")
public class RouteServlet extends  BaseServlet {
    //使用工厂创建dao对象；
    private RouteService service = (RouteService) BeanFactoryUtils.getBean("RouteService");

    /**
     * 分页查询线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //定义返回结果信息对象；
        ResultInfo info ;

        try {
            //获取请求参数；
            String cid = request.getParameter("cid");
            String rname = request.getParameter("rname");
            int pageNumber = 1;
            try {
                  pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            } catch (Exception e) {
                pageNumber = 1;
            }
            //自定义每页显示条数；
            int pageSize = 3;
            //调用service 完成分析查询的业务；

            PageBean<Route> pb = service.findByPage(cid,pageNumber,pageSize,rname);
            //将查询结果返回
            info = new ResultInfo(true);
            info.setData(pb);
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false ,"当前功能正在维护...");
        }
        //将查询结果转成json返回个浏览器；
        ObjectMapper om = new ObjectMapper();
        String infoJson = om.writeValueAsString(info);
        System.out.println(infoJson);
        response.getWriter().print(infoJson);

    }

    /**
     * 查询展示详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findRouteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //定义返回结果信息对象；
        ResultInfo info ;
        try {
            //获取请求参数
            String rid = request.getParameter("rid");
            //调用service查询；
            Route route = service.findRouteById(rid);
            //处理返回结果；
            info = new ResultInfo(true);
            info.setData(route);
          } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false,"当前功能正在维护");
        }
        ObjectMapper om = new ObjectMapper();
        String infoJson = om.writeValueAsString(info);
        response.getWriter().print(infoJson);
    }

}
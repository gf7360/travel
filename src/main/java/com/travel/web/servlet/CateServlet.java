package com.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.domain.ResultInfo;
import com.travel.service.CateService;
import com.travel.service.impl.CateServiceImpl;
import com.travel.utils.BeanFactoryUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CateServlet", urlPatterns = "/category")
public class CateServlet extends BaseServlet {
    //使用工厂创建dao对象；
    private CateService service = (CateService) BeanFactoryUtils.getBean("CateService");
    /**
     * 查询分类表的所有内容
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findCateAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResultInfo info;
        try {
            //调用service层查询分类表

            String  listJson = service.findCateAll();
            //表示查询成功 返回结果到前台；
            info = new ResultInfo(true);
            info.setData(listJson);
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false ,"当前功能正在维护...");
        }
        // 将返回结果对象转成json
        ObjectMapper om = new ObjectMapper();
        String infoJson = om.writeValueAsString(info);
        // System.out.println(infoJson);
        response.getWriter().print(infoJson);
    }


}
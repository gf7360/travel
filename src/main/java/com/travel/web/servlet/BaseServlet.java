package com.travel.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet(name = "BaseServlet", urlPatterns = "/BaseServlet")
public class BaseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //获取请求携带的action 即方法名称；
            String methodName = request.getParameter("action");
            //获取执行类的字节码文件；
            Class clazz = this.getClass();
            //根据标识获取要执行的方法；
            Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //让方法执行；
            method.invoke(this,request,response);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
package com.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.domain.ResultInfo;
import com.travel.domain.User;
import com.travel.service.UserService;
import com.travel.service.impl.UserServiceImpl;
import com.travel.utils.BeanFactoryUtils;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends BaseServlet {
    //private  UserService service = new UserServiceImpl();
    //使用工厂创建对象；
    private  UserService service = (UserService) BeanFactoryUtils.getBean("UserService");
    /**
     *   获取请求标识
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
   /* protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取请求标识
        String action = request.getParameter("action");
        //根据请求标识 调用方法；
        if(action.equals("register")){
            register(request,response);
        }else if(action.equals("active")){
            active(request,response);
        }else if(action.equals("login")){
            login(request,response);
        }else if(action.equals("getUserInfo")){
            getUserInfo(request,response);
        }else if(action.equals("back")){
            back(request,response);
        }

    }*/

    /**
     * 退出功能
     * @param request
     * @param response
     */
    public void back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
        //清空session空间即可
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() );
    }

    /**
     * 获取当前登陆的用户信息；
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info;
        //获取session中的用户信息；
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        //判断user是否为空，
        if(user==null){
            //user 不存在
            info = new ResultInfo(false);
        }else{
            //user 存在
            info = new ResultInfo(true );
            info.setData(user);
        }
        ObjectMapper om = new ObjectMapper();
        String infoJson = om.writeValueAsString(info);
        response.getWriter().print(infoJson);
    }

    /**
     * 登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException  {
        ResultInfo info;
        ObjectMapper om = new ObjectMapper();

        try {
            //1.校验验证码； 先获取请求携带的验证码；
            String ucode = request.getParameter("ucode");
            //获取session中的验证码；
            HttpSession session = request.getSession();
            String scode = (String)session.getAttribute("scode");
            //校验
            if(ucode==""){
                info = new ResultInfo(false,"验证码不可为空");
                //将信息转成json格式；
                String infoJson = om.writeValueAsString(info);
                //将信息返回
                response.getWriter().print(infoJson);
                return;
            }
            if(scode!=null && !scode.equalsIgnoreCase(ucode)){
                info = new ResultInfo(false,"验证码输入错误");
                //将信息转成json格式；
                String infoJson = om.writeValueAsString(info);
                //将信息返回
                response.getWriter().print(infoJson);
                return;
            }
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            BeanUtils.populate(user,map);
            System.out.println(user);
            //2.调用service层查询用户名和密码是否正确；
                User u  = service.login(user);
            //3.判断查询结果
            if(u!=null){
                //证明用户名或密码 正确；进一步判断用户是否激活；
                if("N".equals(u.getStatus())){
                    //证明用户未激活；不允许登录
                    info = new ResultInfo(false,"当前账号尚未激活,请先移步邮箱进行激活");
                }else{
                    //已经激活
                    info = new ResultInfo(true);
                    //登录成功后保存用户登录成功的状态到session中；
                    session.setAttribute("user",u);
                }
            }else{
                info = new ResultInfo(false,"用户名或密码错误");
            }
            String infoJson = om.writeValueAsString(info);
            System.out.println(infoJson);
            response.getWriter().print(infoJson);
        } catch (Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false,"当前功能正在维护");
            String infoJson = om.writeValueAsString(info);
            System.out.println(infoJson);
            response.getWriter().print(infoJson);

        }
    }

    /**
     * 激活账号
     * @param request
     * @param response
     */
    public void active(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
         //调用service层查询激活码状态根据code；唯一标识；
        String code = request.getParameter("code");
         boolean flag = service.active(code);
         if(flag){
             //重定向到登录页面；
             response.sendRedirect(request.getContextPath()+"/login.html");
         }else{
             //重定向到错误友好页面；
             response.sendRedirect(request.getContextPath()+"/error/500.html");
         }
    }

    /**
     *   注册的servlet
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        //定义返回结果信息对象；
        ResultInfo info = null;
        try {
            //获取请求参数，并封装到实体中；
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            BeanUtils.populate(user,map);
            // System.out.println(user);
            //调用service层

            boolean flag =  service.register(user);
            if(flag){
                //返回成功或失败的标志，和信息；
                info = new ResultInfo(true);
            }else{
                info = new ResultInfo(false,"当前功能正在维护");
            }
        } catch ( Exception e) {
            e.printStackTrace();
            info = new ResultInfo(false,"当前功能正在维护");
        }
        //将返回结果对象转成json返回给浏览器；
        ObjectMapper objectMapper = new ObjectMapper();
        String infoJson = objectMapper.writeValueAsString(info);
        System.out.println(infoJson);
        response.getWriter().print(infoJson);
    }

    /*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }*/
}
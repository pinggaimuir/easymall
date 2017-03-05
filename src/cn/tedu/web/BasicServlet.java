package cn.tedu.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by gaojian on 2016/9/11.
 */
public class BasicServlet extends HttpServlet {
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       String methodStr=req.getParameter("method");
        if(methodStr==null||methodStr.trim().isEmpty()){
            throw new RuntimeException("您没有输入method参数，或者输入的参数不正确！");
        }
        Class c=this.getClass();
        Method method=null;
        try {
            method=c.getMethod(methodStr,HttpServletRequest.class,HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("您输入的方法不存在");
        }
        try {
            method.invoke(this,req,resp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("您调用的方法内部发生错误！");
        }
//        String msg="";
//        if(method!=null){
//            try {
//
//                if(msg!=null||!"".equals(msg)) {
//                    if (msg.contains(":")) {
//                        String msg1 = msg.split(":")[0];
//                        String msg2 = msg.split(":")[1];
//                        if ("r".equalsIgnoreCase(msg1)) {
//                            resp.sendRedirect(msg2);
//                        } else if ("f".equalsIgnoreCase(msg1)) {
//                            req.getRequestDispatcher(req.getContextPath() + msg2).forward(req, resp);
//                        }
//                    } else {
//                        req.getRequestDispatcher(req.getContextPath() + msg).forward(req, resp);
//                    }
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.getTargetException().printStackTrace();
//
//            }
//        }


    }
}

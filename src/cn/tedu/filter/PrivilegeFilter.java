package cn.tedu.filter;

import cn.tedu.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarena on 2016/9/14.
 */
@WebFilter(filterName = "PrivilegeFilter",urlPatterns = "/*")
public class PrivilegeFilter implements Filter {
    private List<String> adminPri=new ArrayList<String>();
    private List<String> userPri=new ArrayList<String>();
    public void init(FilterConfig config) throws ServletException {
        try {
            BufferedReader reader1=new BufferedReader(new FileReader(
                    new File(PrivilegeFilter.class.getClassLoader().getResource("admin.txt").getPath())));
            BufferedReader reader2=new BufferedReader(new FileReader(
                    new File(PrivilegeFilter.class.getClassLoader().getResource("user.txt").getPath())));
            String len=null;
            while((len=reader1.readLine())!=null){
                adminPri.add(len);
            }
            while((len=reader2.readLine())!=null){
                userPri.add(len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request=(HttpServletRequest)req;
        HttpServletResponse response=(HttpServletResponse)resp;
        String uri=request.getRequestURI();
        if(request.getSession(false)==null||request.getSession(false ).getAttribute("user")==null){
            if(userPri.contains(uri)){//如果访问为用户才能访问的页面，则禁止
                response.getWriter().write("请先登陆才能执行该操作！");
            }else{
                chain.doFilter(request,response);
            }
        }else{
            String role=((User)request.getSession().getAttribute("user")).getRole();
            if("user".equals(role)&&adminPri.contains(uri)){//如果是普通用户还想访问特殊页面提示权限不够
                response.getWriter().write("您的权限不够！");
            }else{
                chain.doFilter(request,response);
            }
        }
    }



}

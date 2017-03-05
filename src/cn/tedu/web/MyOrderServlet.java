package cn.tedu.web;

import cn.tedu.domain.Order;
import cn.tedu.domain.User;
import cn.tedu.exception.MsgException;
import cn.tedu.factory.BasicFactory;
import cn.tedu.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *       我的订单
 * Created by tarena on 2016/9/11.
 */
@WebServlet(name = "MyOrderServlet",urlPatterns = {"/MyOrderServlet"})
public class MyOrderServlet extends BasicServlet {
    private OrderService service= BasicFactory.getFactory().getInstance(OrderService.class);
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    /**
     * 加载订单列表
     */
    public void loadOrderList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user=(User)request.getSession().getAttribute("user");
        if(user==null){
            request.getRequestDispatcher(request.getContextPath()+"/login.jsp");
        }
        List<Order> list=service.findOrderListById(user.getId());
        request.setAttribute("orderList",list);

        request.getRequestDispatcher(request.getContextPath()+"/orderList.jsp").forward(request,response);
    }
    /**
     * 删除订单
     */
    public void  deleteOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String order_id=request.getParameter("order_id");
        try {
            boolean b=service.deleteOrder(order_id);
        } catch (MsgException e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath()+"/MyOrderServlet?method=loadOrderList");
//        request.getRequestDispatcher(request.getContextPath()+"/MyOrderServlet?method=loadOrderList").forward(request,response);
    }

}

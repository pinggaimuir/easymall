package cn.tedu.web.back;

import cn.tedu.domain.Product;
import cn.tedu.exception.MsgException;
import cn.tedu.factory.BasicFactory;
import cn.tedu.service.ProdService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tarena on 2016/9/8.
 */
@WebServlet(name = "BackProdEditPreServlet",urlPatterns = {"/back/BackProdEditPreServlet"})
public class BackProdEditPreServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id=request.getParameter("id");
        ProdService service=BasicFactory.getFactory().getInstance(ProdService.class);
        Product prod= null;
        try {
            prod = service.findProdById(id);
        } catch (MsgException e) {
            e.printStackTrace();
        }
        request.setAttribute("prod",prod);
        request.getRequestDispatcher(request.getContextPath()+"/back/manageProdEdit.jsp").forward(request,response);
    }
}

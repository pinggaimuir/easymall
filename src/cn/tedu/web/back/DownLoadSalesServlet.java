package cn.tedu.web.back;

import cn.tedu.factory.BasicFactory;
import cn.tedu.service.OrderService;
import cn.tedu.web.BasicServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by tarena on 2016/9/12.
 */
@WebServlet(name = "DownLoadSalesServlet",urlPatterns = {"/back/DownLoadSalesServlet"})
public class DownLoadSalesServlet extends BasicServlet {
   private static OrderService service= BasicFactory.getFactory().getInstance(OrderService.class);
    /*下载销售榜单*/
    public void downLoad(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Map<String,Object>> list=service.findSales();
        String data="商品id，商品名称，销售数量\r\n";
        if(list!=null){
            for(Map<String,Object> map:list){
                for(Map.Entry<String,Object> entry:map.entrySet()){
                    data+=entry.getValue().toString()+",";
                }
                data+="\r\n";
            }
        }
        String fname="EasyMall销售榜单" + new Date().toLocaleString()+".csv";
        response.setHeader("Content-Type",this.getServletContext().getMimeType(fname));
        String name= URLEncoder.encode(fname,"utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name);
        response.getWriter().write(data);
/*        InputStream in=new FileInputStream(new File(fname));
        OutputStream out=response.getOutputStream();
        IOUtils.copy(in,out);
        int len=-1;
        byte[] bs=new byte[1024];
        while((len=in.read(bs))!=-1){
            out.write(bs);
        }
        out.flush();
        in.close();*/

//        request.getRequestDispatcher("ce.jpg").forward(request,response);//第二种方式
    }
    /*跳转到销售榜单，并且显示销售榜单*/
    public void showSales(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Map<String,Object>> list=service.findSales();
        request.setAttribute("sales",list);
        request.getRequestDispatcher(request.getContextPath()+"/back/manageSaleList.jsp").forward(request,response);
    }
    public void POI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

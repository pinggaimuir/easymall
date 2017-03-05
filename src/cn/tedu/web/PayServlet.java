package cn.tedu.web;

import cn.tedu.factory.BasicFactory;
import cn.tedu.service.OrderService;
import cn.tedu.utils.PaymentUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by tarena on 2016/9/12.
 */
@WebServlet(name = "PayServlet",urlPatterns = {"/PayServlet"})
public class PayServlet extends BasicServlet {
    private static Properties prop=null;
    static{
         prop=new Properties();
        try {
            String path=PayServlet.class.getClassLoader().getResource("merchantInfo.properties").getPath();
            prop.load(new FileReader(path));
        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public void Back(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String p1_MerId = request.getParameter("p1_MerId");
        String r0_Cmd = request.getParameter("r0_Cmd");
        String r1_Code = request.getParameter("r1_Code");
        String r2_TrxId = request.getParameter("r2_TrxId");
        String r3_Amt = request.getParameter("r3_Amt");
        String r4_Cur = request.getParameter("r4_Cur");
        String r5_Pid = request.getParameter("r5_Pid");
        String r6_Order = request.getParameter("r6_Order");
        String r7_Uid = request.getParameter("r7_Uid");
        String r8_MP = request.getParameter("r8_MP");
        String r9_BType = request.getParameter("r9_BType");
//        String rb_BankId = request.getParameter("rb_BankId");
//        String ro_BankOrderId = request.getParameter("ro_BankOrderId");
//        String rp_PayDate = request.getParameter("rp_PayDate");
//        String rq_CardNo = request.getParameter("rq_CardNo");
//        String ru_Trxtime = request.getParameter("ru_Trxtime");
        String hmac = request.getParameter("hmac");
        String keyValue = prop.getProperty("keyValue");
        boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
                r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
                r8_MP, r9_BType, keyValue);
        if(isValid){
            // 响应数据有效
            if (r9_BType.equals("1")) {
                // 浏览器重定向
                response.setContentType("text/html;charset=utf-8");
                OrderService service= BasicFactory.getFactory().getInstance(OrderService.class);
                service.updatePayState(r6_Order,1);
                response.getWriter().println("<h1>付款成功！等待商城进一步操作！慢慢等发货...</h1>");
                response.setHeader("refresh","2;url="+request.getContextPath()+"/index.jsp");
            } else if (r9_BType.equals("2")) {
                // 服务器点对点 --- 支付公司通知你
                System.out.println("付款成功！");
                // 修改订单状态 为已付款
                OrderService service= BasicFactory.getFactory().getInstance(OrderService.class);
                service.updatePayState(r6_Order,1);
                // 回复支付公司
                response.getWriter().print("success");
            }
        }else{
            throw new RuntimeException("捣乱！");
        }
        response.getWriter().write("success");
    }
    public void Pay(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String p0_Cmd="Buy";
        String p1_MerId=prop.getProperty("p1_MerId");//支付的易宝账户
        String p2_Order=request.getParameter("order_id");//点单编号
        String p3_Amt="0.01";//支付金额
        String p4_Cur="CNY";//支部币种
        String p5_Pid="";//商品名称
        String p6_Pcat="";//商品种类
        String p7_Pdesc="";//商品信息
        String p8_Url=prop.getProperty("responseURL");//回访网址
        String p9_SAF="";
        String pa_MP="";
        String pd_FrpId=request.getParameter("pd_FrpId");//支部通道编号
//       String pd_FrpId="CCB-NET-B2C";
        String pr_NeedResponse="1";//回访形式
        String keyValue=prop.getProperty("keyValue");//keyValue

        String hmac=PaymentUtil.buildHmac(p0_Cmd,p1_MerId, p2_Order,p3_Amt,p4_Cur,p5_Pid,p6_Pcat,
                p7_Pdesc,p8_Url,p9_SAF,pa_MP,pd_FrpId, pr_NeedResponse,keyValue);

        request.setAttribute("pd_FrpId", pd_FrpId);
        request.setAttribute("p0_Cmd", p0_Cmd);
        request.setAttribute("p1_MerId", p1_MerId);
        request.setAttribute("p2_Order", p2_Order);
        request.setAttribute("p3_Amt", p3_Amt);
        request.setAttribute("p4_Cur", p4_Cur);
        request.setAttribute("p5_Pid", p5_Pid);
        request.setAttribute("p6_Pcat", p6_Pcat);
        request.setAttribute("p7_Pdesc", p7_Pdesc);
        request.setAttribute("p8_Url", p8_Url);
        request.setAttribute("p9_SAF", p9_SAF);
        request.setAttribute("pa_MP", pa_MP);
        request.setAttribute("pr_NeedResponse", pr_NeedResponse);
        request.setAttribute("hmac", hmac);

        request.getRequestDispatcher(request.getContextPath()+"/confirm.jsp").forward(request, response);

       /* StringBuilder builder=new StringBuilder(prop.getProperty("Url"));
        builder.append("?p0_Cmd=").append(p0_Cmd);
        builder.append("&p1_MerId=").append(p1_MerId);
        builder.append("&p2_Order=").append(p2_Order);
        builder.append("p3_Amt=").append(p3_Amt);
        builder.append("&p4_Cur=").append(p4_Cur);
        builder.append("&p5_Pid=").append(p5_Pid);
        builder.append("&p6_Pcat=").append(p6_Pcat);
        builder.append("&p7_Pdesc=").append(p7_Pdesc);
        builder.append("&p8_Url=").append(p8_Url);
        builder.append("&p9_SAF=").append(p9_SAF);
        builder.append("&pa_MP=").append(pa_MP);
        builder.append("&pd_FrpId=").append(pd_FrpId);
        builder.append("&pr_NeedResponse=").append(pr_NeedResponse);
        builder.append("&hmac=").append(hmac);
        response.sendRedirect(builder.toString());*/
    }
}

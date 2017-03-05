package cn.tedu.service;

import cn.tedu.Annotation.Tran;
import cn.tedu.dao.OrderDao;
import cn.tedu.dao.ProdDao;
import cn.tedu.domain.Order;
import cn.tedu.domain.OrderItem;
import cn.tedu.domain.Product;
import cn.tedu.exception.MsgException;
import cn.tedu.factory.BasicFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by tarena on 2016/9/9.
 */
public class OrderServiceImpl implements OrderService {
    OrderDao orderDao= BasicFactory.getFactory().getInstance(OrderDao.class);
    ProdDao prodDao= BasicFactory.getFactory().getInstance(ProdDao.class);
    public void addOrder(Order order, List<OrderItem> list) throws MsgException {

            orderDao.addOrder(order);
            for(OrderItem item:list){
                /*根据id查找数据库中的*/
                Product prod= prodDao.findProdById(item.getProduct_id());
                if(prod.getPnum()>=item.getBuynum()){
                        orderDao.editPnumtx(prod.getId(),prod.getPnum()-item.getBuynum());
                        orderDao.addOrderItem(item);
                }else{
                    throw  new MsgException("商品库存不足！商品id:"+prod.getId()+",商品名："+prod.getName());
                }
            }

    }

    /**
     * 根据用户id查询用户所有的订单
     * @param user_id
     * @return
     */
    public List<Order> findOrderListById(int user_id) {
        List<Order> list=orderDao.findOrdersByid(user_id);
        return list;
    }

    /**
     * 根据订单id删除订单,先查询订单状态是否已支付，然后还原商品数量，删除订单和订单项
     * @param order_id 订单编号
     * @return 是否删除成功
     */
    @Tran
    public boolean deleteOrder(String order_id) throws MsgException {
        if(orderDao.findOrderByOrder_id(order_id).getPaystate()!=0){
            throw new MsgException("已支付的订单不能删除！");
        }
        //遍历订单项，把订单项的商品数量还原
        List<OrderItem> itemlist= orderDao.findOrderItemById(order_id);
        for(OrderItem item:itemlist){
            prodDao.updateOrderPnum(item.getProduct_id(),item.getBuynum());
        }
        //删除订单
        int delOrder=orderDao.deleteOrder(order_id);
        int delOrderItem=orderDao.deleteOrderItem(order_id);
        if(delOrder>0&&delOrderItem>0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 修改订单状态为已支付
     * @param i 订单状态代码
     */
    @Tran
    public void updatePayState(String id,int i) {
        int state=orderDao.findPayState(id);
        if(state==0){
            orderDao.updatePayState(id,i);
        }
    }

    /**
     * 查询销售榜单
     * @return
     */
    public List<Map<String, Object>> findSales() {
        return orderDao.findSales();
    }
}

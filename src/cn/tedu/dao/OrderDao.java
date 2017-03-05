package cn.tedu.dao;

import cn.tedu.domain.Order;
import cn.tedu.domain.OrderItem;

import java.util.List;
import java.util.Map;

/**
 * Created by tarena on 2016/9/9.
 */
public interface OrderDao extends Dao {
    /*t添加订单*/
    void addOrder(Order order);
    /*添加订单项*/
    void addOrderItem(OrderItem item);
    /*根据id修改商品数量*/
    int editPnumtx(String id,int newNum);

    /*根据用户的id查找所有订单*/
    List<Order> findOrdersByid(int uid);

    /*根据订单id删除订单*/
    int deleteOrder(String order_id);
    /*根据订单id删除订单项*/
    int deleteOrderItem(String order_id);
    /*根据订单id查找订单*/
    Order findOrderByOrder_id(String order_id);
    /*根据订单id查找订单项*/
    List<OrderItem> findOrderItemById(String order_id);
    /*根据订单id查询订单加悲观锁*/
    int findPayState(String id);
    /*根据订单id修改订单砸金蛋支付状态*/
    void updatePayState(String id,int i);
    /*查找销售榜单 商品id 商品名 商品数量*/
    List<Map<String,Object>> findSales();
}

package cn.tedu.service;

import cn.tedu.Annotation.Tran;
import cn.tedu.domain.Order;
import cn.tedu.domain.OrderItem;
import cn.tedu.exception.MsgException;

import java.util.List;
import java.util.Map;

/**
 * Created by tarena on 2016/9/9.
 */
public interface OrderService extends Service {
    /**
     * 增加订单的方法
     * @param order 订单id
     * @param list 订单项列表
     * @throws MsgException
     */
    @Tran
    void addOrder(Order order, List<OrderItem> list) throws MsgException;

    /**
     * 根据用户id查询用户所有订单
     * @param user_id
     * @return
     */
    List<Order> findOrderListById(int user_id);

    boolean deleteOrder(String order_id) throws MsgException;
    /*跟新订单状态*/
    void updatePayState(String id,int i);
    /*查询销售榜单*/
    List<Map<String,Object>> findSales();
}

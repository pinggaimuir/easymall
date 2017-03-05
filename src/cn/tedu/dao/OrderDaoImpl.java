package cn.tedu.dao;

import cn.tedu.domain.Order;
import cn.tedu.domain.OrderItem;
import cn.tedu.utils.TxQueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by tarena on 2016/9/9.
 */

public class OrderDaoImpl implements OrderDao {
    TxQueryRunner qr=new TxQueryRunner();

    /**
     * 添加订单
     * @param order 订单对象
     */
    public void addOrder(Order order) {
        String sql="insert into orders (id,money,receiverinfo,paystate,ordertime,user_id) values(?,?,?,?,?,?)";
        Timestamp timestamp=new Timestamp(order.getOrdertime().getTime());
        Object[] params={order.getId(),order.getMoney(),order.getReceiverinfo(),order.getPaystate(),timestamp,order.getUser_id()};
        qr.update(sql,params);
    }

    /**
     * 添加订单项的方法
     * @param item
     */
    public void addOrderItem(OrderItem item) {
        String sql="insert into orderitem (order_id,product_id,buynum) values(?,?,?)";
        Object[] params={item.getOrder(),item.getProduct_id(),item.getBuynum()};
        qr.update(sql,params);

    }

    /**
     * 更改根据商品id和商品的新数量商品数量
     * @param id 商品id
     * @param newNum 跟新后的数量
     * @return
     */
    public int editPnumtx(String id, int newNum) {
        String sql="update products set pnum=? where id=?";
        Object[] params={newNum,id};
       return  qr.update(sql,params);
    }

    /**
     * 通过用户的id加载订单
     * @param uid 用户id
     * @return 订单列表
     */
    public List<Order> findOrdersByid(int uid) {
        String sql="select * from orders where user_id=?";
        Object[] params={uid};
        List<Order> orderlist=qr.query(sql,new BeanListHandler<Order>(Order.class),params);
        /*为灭一个订单加载订单项*/
        for(Order order:orderlist){
            loadOrderItem(order);
        }
        return orderlist;
    }

    /**
     * 根据订单编号删除订单
     * @param order_id 订单编号
     * @return 删除记录数
     */
    public int deleteOrder(String order_id) {
        String sql="delete from orders where id=?";
        Object[] params={order_id};
        Number n=(Number)qr.update(sql,params);
        return n.intValue();
    }

    /**
     * 根据订单id删除订单项
     * @param order_id
     * @return
     */
    public int deleteOrderItem(String order_id) {
        String sql="delete from orderitem where order_id=?";
        Object[] params={order_id};
        Number n=(Number)qr.update(sql,params);
        return n.intValue();
    }

    @Override
    public Order findOrderByOrder_id(String order_id) {
        String sql="select * from orders where id=?";
        Object[] params={order_id};
        return qr.query(sql,new BeanHandler<Order>(Order.class),params);
    }

    /**
     * 根据订单编号查找订单项
     * @param order_id 订单编号
     * @return 订单项
     */
    public List<OrderItem> findOrderItemById(String order_id) {
        String sql="select * from orderitem where order_id=?";
        Object[] params={order_id};
        List<OrderItem> list=qr.query(sql,new BeanListHandler<OrderItem>(OrderItem.class),params);
        return list;
    }

    /**
     * 根据订单id查询订单状态加悲观锁
     * @param id 订单id
     * @return 订单状态
     */
    public int findPayState(String id) {
        String sql="select * from orders where id=? for update";
        Object[] params={id};
        Order order=qr.query(sql,new BeanHandler<Order>(Order.class),id);
        return order.getPaystate();
    }

    /**
     * 修改订单状态
     * @param id 订单id
     * @param i 订单状态
     */
    public void updatePayState(String id, int i) {
        String sql="update orders set paystate=? where id=?";
        Object[] params={i,id};
        qr.update(sql,params);
    }

    /**
     * 查找销售榜单
     * @return
     */
    public List<Map<String, Object>> findSales() {
       /* String sql="select products.name,products.id,sum(orderitem.buynum) from products,orderitem,orders where " +
                "orderitem.order_id=orders.id and orderitem.product_id=products.id and orders.paystate=1 group by " +
                "products.id order by sum(orderitem.buynum)";*/
        String sql2="select pd.id pid,pd.name pname,sum(oi.buynum) sale_num from products pd,orderitem oi,orders os " +
                "where oi.product_id=pd.id and oi.order_id=os.id and os.paystate=1 group by pid order by sale_num desc";
        List<Map<String,Object>> list=qr.query(sql2,new MapListHandler());
        return list;
    }

    /*根据订单id加载订单项*/
    private void loadOrderItem(Order order) {
        String sql=" select * from orderitem,products where orderitem.product_id=products.id and order_id=?";
        List<Map<String,Object>> maplist= (List<Map<String, Object>>) qr.query(sql,new MapListHandler(),order.getId());
        order.setMaplist(maplist);
    }

}

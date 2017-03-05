package cn.tedu.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单
 * Created by tarena on 2016/9/9.
 */
public class Order implements Serializable {
    private String  id;//订单编号
    private double money;//点单金额
    private String receiverinfo;//收货地址
    private int paystate;//订单状态
    private Date ordertime; //下订单时间
    private  int user_id;//用户id
    private List<Map<String,Object>> maplist;

    public List<Map<String, Object>> getMaplist() {
        return maplist;
    }

    public void setMaplist(List<Map<String, Object>> maplist) {
        this.maplist = maplist;
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Date getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }

    public int getPaystate() {
        return paystate;
    }

    public void setPaystate(int paystate) {
        this.paystate = paystate;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getReceiverinfo() {
        return receiverinfo;
    }

    public void setReceiverinfo(String receiverinfo) {
        this.receiverinfo = receiverinfo;
    }
}

package cn.tedu.domain;

import java.io.Serializable;

/**
 * 订单项
 * Created by tarena on 2016/9/9.
 */
public class OrderItem implements Serializable {
    private String order;//订单id
    private String product_id;//商品id
    private int buynum;//订单项中商品数量


    public int getBuynum() {
        return buynum;
    }

    public void setBuynum(int buynum) {
        this.buynum = buynum;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}

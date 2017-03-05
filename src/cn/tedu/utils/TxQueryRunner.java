package cn.tedu.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 自带链接的QueryRunner,从获得的链接有两种 ，一种是事物准用链接，一种是非事物专用
 *         事物专用链接由事物关闭，非事物专用链接在发调用releaseTransaction后断开链接
 * Created by tarena on 2016/9/10.
 */
public class TxQueryRunner extends QueryRunner {
    @Override
    public int[] batch(String sql, Object[][] params) {
        try {
            Connection con= TransactionManager.getCon();
            int[] result=super.batch(con,sql,params);
            TransactionManager.releaseTransaction(con);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }

    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh)  {
        try {
            Connection con = TransactionManager.getCon();
            T t = super.query(con, sql, rsh);
            TransactionManager.releaseTransaction(con);
            return t;
        }catch(SQLException e){
                e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) {
        try{
        Connection con= TransactionManager.getCon();
        T t=super.query(con,sql,rsh,params);
        TransactionManager.releaseTransaction(con);
        return t;
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(String sql)  {
        try{
        Connection con= TransactionManager.getCon();
        int n=super.update(con,sql);
        TransactionManager.releaseTransaction(con);
        return n;
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(String sql, Object param){
        try{
        Connection con= TransactionManager.getCon();
        int n=super.update(con,sql,param);
        TransactionManager.releaseTransaction(con);
        return n;
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(String sql, Object... params){
        try {
            Connection con = TransactionManager.getCon();
            int n = super.update(con, sql, params);
            TransactionManager.releaseTransaction(con);
            return n;
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

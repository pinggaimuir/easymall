package cn.tedu.factory;

import cn.tedu.Annotation.Tran;
import cn.tedu.dao.Dao;
import cn.tedu.service.Service;
import cn.tedu.utils.PropUtils;
import cn.tedu.utils.TransactionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 通过反射获取Dao实现类的实例
 * Created by tarena on 2016/9/3.
 */
public class BasicFactory {

    private static BasicFactory factory=new BasicFactory();
    private BasicFactory(){}
    public static BasicFactory getFactory(){
        return factory;
    }

    /**
     * 通过类获取类的实例
     * @param clz 类
     * @return 类实例
     */
    @SuppressWarnings("unchecked")
    public  <T>T getInstance(Class<T> clz) {
        try {
            if (Service.class.isAssignableFrom(clz)) {//是业务层接口
                //生成该service的动态代理对象
                String daoImplStr = PropUtils.getProp(clz.getSimpleName());
                Class daoImplClz = Class.forName(daoImplStr);
                final T t = (T) daoImplClz.newInstance();//委托类对象
                T proxy =(T) Proxy.newProxyInstance(t.getClass().getClassLoader(),
                        t.getClass().getInterfaces(), new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.isAnnotationPresent(Tran.class)) {//判断该方法上是否有@Tran注解
                            try {
                                TransactionManager.startTransaction();//开启事物
                                Object obj = method.invoke(t, args);//执行委托类的方法
                                TransactionManager.commitTranscation();//提交事物
                                return obj;
                            } catch (InvocationTargetException e1) {
                                TransactionManager.rollbackTransaction();//回滚事物
                                e1.getTargetException().printStackTrace();
                                throw e1.getTargetException();
                            } catch (Exception e) {
                                TransactionManager.rollbackTransaction();//回滚事物
                                e.printStackTrace();
                                throw new RuntimeException();
                            }
                        } else {//该方法上没有注解
                            try {
                                return method.invoke(t, args);
                            } catch (InvocationTargetException e1) {
                                e1.getTargetException().printStackTrace();
                                throw e1.getTargetException();
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException();
                            }
                        }
                    }
                });
                return proxy;
            }else if (Dao.class.isAssignableFrom(clz)) {
                String daoImplStr = PropUtils.getProp(clz.getSimpleName());
                return (T) Class.forName(daoImplStr).newInstance();
            } else {
                throw new RuntimeException("不是Dao层也不是service层的接口");
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}

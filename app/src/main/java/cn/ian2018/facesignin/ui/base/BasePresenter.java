package cn.ian2018.facesignin.ui.base;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description:
 * Author:chenshuai
 * E-mail:chenshuai@amberweather.com
 * Date:2018/12/12
 */
public class BasePresenter<V extends BaseView> {
    private WeakReference<V> mViewReference;
    private V mProxyView;

    // 将绑定和解绑放到基类里
    public void attach(V view) {
        this.mViewReference = new WeakReference<>(view);

        // 通过动态代理  减少每次获取view都到判空的操作
        mProxyView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (mViewReference == null || mViewReference.get() == null) {
                    return null;
                }
                // 如何还没解绑  调用的是原始方法
                return method.invoke(mViewReference.get(), args);
            }
        });
    }

    // 解绑
    public void detach() {
        this.mViewReference.clear();
        this.mViewReference = null;
        this.mProxyView = null;
    }

    public V getView() {
        return mProxyView;
    }
}

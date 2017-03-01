package com.lebron.mvp.factory;

import android.support.annotation.Nullable;

import com.lebron.mvp.presenter.Presenter;

/**
 * 通过反射来创建 P extends Presenter 的P的实例对象
 * Created by wuxiangkun on 2017/1/2.
 * Contact way wuxiangkun2015@163.com
 */

public class ReflectionPresenterFactory<P extends Presenter> implements PresenterFactory<P> {
    private Class<P> presenterClass;

    /**
     * This method returns a {@link ReflectionPresenterFactory} instance if a given view(Fragment or
     * Activity) class has a {@link RequiresPresenter} annotation, or null otherwise.
     *
     * @param viewClass a class of the view
     * @param <P>       a type of the presenter
     * @return a {@link ReflectionPresenterFactory} instance that is supposed to create a presenter from
     * {@link RequiresPresenter} annotation.
     */
    @Nullable
    public static <P extends Presenter> ReflectionPresenterFactory<P> fromViewClass(Class<?> viewClass) {
        RequiresPresenter annotation = viewClass.getAnnotation(RequiresPresenter.class); // 获取viewClass的注解
        Class<P> presenterClass = annotation == null ? null : (Class<P>) annotation.value(); // 获取注解的默认参数值,得到presenterClass
        return presenterClass == null ? null : new ReflectionPresenterFactory<>(presenterClass);
    }

    private ReflectionPresenterFactory(Class<P> presenterClass) {
        this.presenterClass = presenterClass;
    }

    @Override
    public P createPresenter() {
        try {
            return presenterClass.newInstance(); // 通过类对象 创建P具体的实例
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.lebron.mvp.factory;

import com.lebron.mvp.presenter.Presenter;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 自定义注解,在Fragment or Activity的具体实现类前加注解
 * Created by wuxiangkun on 2017/1/4.
 * Contact way wuxiangkun2015@163.com
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPresenter {
    Class<? extends Presenter> value();  // value()是默认函数名称
}

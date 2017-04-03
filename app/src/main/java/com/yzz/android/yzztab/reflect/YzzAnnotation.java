package com.yzz.android.yzztab.reflect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yzz on 2017/three/22 0022.
 */
//作用域
@Target({ElementType.FIELD,ElementType.METHOD})
//生命周期
@Retention(RetentionPolicy.RUNTIME)
public @interface YzzAnnotation {
    public int id();
    public boolean click()default false;
}

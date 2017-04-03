package com.yzz.android.yzztab.reflect;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by yzz on 2017/three/22 0022.
 */
public class YzzAnn<T> {
    private SoftReference<T> softReference;
    private Class<? extends Object> aClass;

    public YzzAnn() {
    }

    public void bind(T t) {
        softReference = new SoftReference<>(t);
        if (t == null) return;
        if (t instanceof ViewGroup || t instanceof Activity || t instanceof Fragment) {
            reflect();
        }
    }

    /**
     * 反射获取字段
     */
    private void reflect() {
        T t = softReference.get();
        if (t == null) {
            throw new RuntimeException("null entity");
        }
        aClass = t.getClass();
        try {
            init(aClass.getDeclaredFields());
            init(aClass.getFields());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("========", "=========" + e.toString());
        }
    }


    /**
     * 为字段设置id
     *
     * @param f
     * @throws Exception
     */
    public void init(Field[] f) throws Exception {
        if (f == null) return;
        for (Field field : f) {
            if (field.isAnnotationPresent(YzzAnnotation.class)) {
                //这里要
                field.setAccessible(true);
                YzzAnnotation yzz = field.getAnnotation(YzzAnnotation.class);
                Method m = aClass.getMethod("findViewById", int.class);
                Object ob = m.invoke(softReference.get(), yzz.id());
                field.set(softReference.get(), ob);
                //设置监听
                Class<?> inter[] = aClass.getInterfaces();
                for (Class<?> c:inter){
                    if (c.getName().equals(View.OnClickListener.class.getName())){
                        if (yzz.click()) {
                            Method setOnclick = field.getType().getMethod("setOnClickListener",View.OnClickListener.class);
                            setOnclick.invoke(field.get(softReference.get()),softReference.get());
                        }
                    }
                }
            }
        }
    }

}

package com.sum.hi.ui.demo.handler;

import android.content.Context;

import com.sum.hi.hilibrary.annotation.Filed;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author smy
 * @date 2022/7/15 16:36
 * @desc 热修复
 */
public class HotFix {

    public static void fix(Context context, File patchDexFile) throws IllegalAccessException, InvocationTargetException {

        ClassLoader classLoader = context.getClassLoader();
        Field pathListField = findFiled(classLoader, "pathList");
        //获取实例对象,原有的pathList
        Object pathList = pathListField.get(classLoader);

        List<File> files = Arrays.asList(patchDexFile);
        //把需要修复的dex文件加载到内存中
        Object[] pathDexElements = makeDexElements(pathList, files, classLoader);

        combineDexElements(pathList, pathDexElements);
    }

    /**
     * 实现插队，需要将pathDexElements 和pathList进行合并
     *
     * @param pathList
     * @param pathDexElements
     */
    private static void combineDexElements(Object pathList, Object[] pathDexElements) throws IllegalAccessException {
        //得到之前的dexElement数组
        Field dexElementsFiled = findFiled(pathList, "dexElements");
        Object[] original = (Object[]) dexElementsFiled.get(pathList);

        //新数组
        Object combined = Array.newInstance(original.getClass().getComponentType(), pathDexElements.length + original.length);

        //插入数组
        System.arraycopy(pathDexElements, 0, combined, 0, pathDexElements.length);
        System.arraycopy(original, 0, combined, pathDexElements.length, original.length);

        //把新的数组设置进去
        dexElementsFiled.set(pathList, combined);
    }

    private static Object[] makeDexElements(Object pathList, List<File> files, ClassLoader classLoader) throws InvocationTargetException, IllegalAccessException {

        Method method = findMethod(pathList, "makeDexElements", List.class, File.class, List.class, ClassLoader.class);
        if (method != null) {
            ArrayList<IOException> exceptions = new ArrayList<>();
            return (Object[]) method.invoke(pathList, files, null, exceptions, classLoader);
        } else {
            return null;
        }
    }

    private static Method findMethod(Object instance, String methodName, Class<?>... parameterTypes) {

        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                if (method.isAccessible()) {//如果不可操作
                    method.setAccessible(true);
                }
                return method;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 查找pathList
     */
    private static Field findFiled(Object instance, String fileName) {
        //当前类没有就去父类里面查找
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(fileName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

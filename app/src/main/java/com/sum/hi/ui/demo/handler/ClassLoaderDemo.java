package com.sum.hi.ui.demo.handler;

import java.io.File;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
/**
 * @author smy
 * @date 2022/7/14 16:30
 * @desc ClassLoader类加载机制
 */
public class ClassLoaderDemo {
    /*

     */
    /**
     * 任何一个ClassLoader都必须有一个parent对象，getSystemClassLoader()会获取一个当作parent对象，
     * 这个最终返回的是PathClassLoader，如果创建的任意ClassLoader，如果没有指定的话都是PathClassLoader，
     * 也指定了它的父加载器BootClassLoader，它是安卓中顶级加载器，他是没有parent的。
     * 所以双亲委派的关联就是通过构造函数来关联起来的，
     *//*

    ClassLoader customerClassLoader = new ClassLoader() {

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {

            return super.loadClass(name);
        }
    };

    class ClassLoader {

        protected ClassLoader() {

            this(checkCreateClassLoader(), getSystemClassLoader());//pathclassloader
        }

        protected ClassLoader(java.lang.ClassLoader parent) {

            this(checkCreateClassLoader(), parent);
        }

        */
    /**
     * getSystemClassLoader()最终返回的是PathClassLoader
     *//*

        private static ClassLoader createSystemClassLoader() {

            String classPath = System.getProperty("java.class.path", ".");
            String librarySearchPath = System.getProperty("java.library.path", "");

            // TODO Make this a java.net.URLClassLoader once we have those?
            return new PathClassLoader(classPath, librarySearchPath, BootClassLoader.getInstance());
        }
    }

    */

    /**
     * 核心实现源码，对于任意一个classLoader对象它在加载文件的时候都会执行loadclass方法。
     * 先去检查自己是否加载过，如果没有则委托父类加载，父类也会调用loadClass，直到这条链路的顶层，
     * 如果让然没有加载过，因为顶层没有parent所以调用findClass方法，来尝试加载class文件，如果顶级的加载失败了
     * 就会向下传递，交给调用方来实现文件的加载。所以本质上双亲委派就是递归调用的过程，
     *//*

    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        // 先检查是否加载过-- findLoaded
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            //如果自己没有加载过，如果存在父类，则委托父类
            if (parent != null) {
                c = parent.loadClass(name, false);
            } else {
                c = findBootstrapClassOrNull(name);
            }

            if (c == null) {
                // 如果父类也没有加载过，则尝试本级classloader加载
                c = findClass(name);
            }
        }
        return c;
    }
*/
 /*   public class PathClassLoader extends BaseDexClassLoader {
        public PathClassLoader(String dexPath, ClassLoader parent) {
            super(dexPath, (File) null, (String) null, parent);
        }

        public PathClassLoader(String dexPath, String librarySearchPath, ClassLoader parent) {
            super(dexPath, (File) null, librarySearchPath, parent);
        }
    }

    public class DexClassLoader extends BaseDexClassLoader {
        //dexPath:dex文件以及包含dex的apk文件或jar文件的路径，多个路径用分隔符分隔，默认文件分隔符为：
        //optimizedDirectory：Android系统将dex文件进行优化后所生成的ODEX文件的存放路径，该路径必须是一个内
        //                   PathClassLoader中使用默认路径："/data/dalvik-cache"
        //                   而DexClassLoader则需要我们指定ODEX优化文件的存放路径
        //                   librarySearchPath：所使用到c、c++库存放的路径
        //                   parent：这个参数作用主要是保留Java中的ClassLoader的委托机制
        public DexClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
            super(dexPath, (File) null, librarySearchPath, parent);
        }
    }
*/

    /**
     *在准备阶段，value的值默认为0，初始化阶段才会被赋值为3，
     * 而value2只有在随着对象实例化才会被赋值，所以静态方法不能访问非静态变量。
     */
    public static class MainActivity {
        //在准备阶段它的值为默认值为0，初始化阶段才会赋值为3

        //因为把value赋值为3的public static语句在编译后的指令是在类结构<clinit>()方法之中调用
        static int value = 3;//0x0001

        int value2 = 3;//随着对象实例化才会被赋值

        static void test() {
//            value 2 = 100;//静态方法为什么不能访问非静态变量
            System.out.println("test");
        }
    }
}

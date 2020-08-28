package com.cheng.illegaljson;

import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

/**
 * 反射帮助类
 *
 * @param <T>
 */
public class ReflectHelp<T> {
    private Class<T> tClass;

    private ReflectHelp(Class<T> tClass) {
        this.tClass = tClass;
    }

    public static <T> ReflectHelp<T> deal(Class<T> tClass) {
        if (tClass == null) {
            return null;
        }
        return new ReflectHelp<>(tClass);
    }

    public boolean isByte() {
        return is(byte.class) || is(Byte.class);
    }

    public boolean isShort() {
        return is(short.class) || is(Short.class);
    }

    public boolean isChar() {
        return is(char.class) || is(Character.class);
    }

    public boolean isInt() {
        return is(int.class) || is(Integer.class);
    }

    public boolean isLong() {
        return is(long.class) || is(Long.class);
    }

    public boolean isFloat() {
        return is(float.class) || is(Float.class);
    }

    public boolean isDouble() {
        return is(double.class) || is(Double.class);
    }

    public boolean isBoolean() {
        return is(boolean.class) || is(Boolean.class);
    }

    public boolean isString() {
        return is(String.class);
    }

    public boolean isArray() {
        return tClass.isArray();
    }

    public boolean isCollection() {
        return isFrom(Collection.class);
    }

    public boolean isMap() {
        return isFrom(Map.class);
    }

    /**
     * 是否为容器
     *
     * @return true of false
     */
    public boolean isContainer() {
        return isColl() || isMap();
    }

    /**
     * 是否为数组或者集合
     *
     * @return
     */
    public boolean isColl() {
        return isArray() || isCollection();
    }

    /**
     * 是否是一个实体对象(包含容器)
     *
     * @return
     */
    public boolean isObject() {
        return isContainer() || isPojo();
    }

    /**
     * @return 当前对象是否为枚举
     */
    public boolean isEnum() {
        return tClass.isEnum();
    }

    /**
     * @return 当前对象是否为CharSequence的子类
     */
    public boolean isStringLike() {
        return isFrom(CharSequence.class);
    }

    /**
     * @return 当前对象是否在表示日期或时间
     */
    public boolean isDateTimeLike() {
        return isFrom(Calendar.class)
                || isFrom(java.util.Date.class)
                || isFrom(java.sql.Date.class)
                || isFrom(java.sql.Time.class);
    }

    /**
     * @return 当前对象是否为原生类型
     */
    public boolean isPrimitive() {
        return isInt() || isLong() || isFloat() || isDouble() || isByte() || isShort() || isBoolean() || isChar();
    }

    /**
     * 判断当前类型是否为POJO。 除了下面的类型，其他均为 POJO
     * <ul>
     * <li>原生以及所有包裹类
     * <li>类字符串
     * <li>类日期
     * <li>非容器
     * </ul>
     *
     * @return true or false
     */
    private boolean isPojo() {
        return !isPrimitive() && !isEnum() && !isStringLike() && !isDateTimeLike() && !isContainer();
    }

    public boolean is(Class<?> tClass) {
        return null != tClass && this.tClass == tClass;
    }

    private boolean isFrom(Class<?> tClass) {
        return tClass.isAssignableFrom(this.tClass);
    }
}

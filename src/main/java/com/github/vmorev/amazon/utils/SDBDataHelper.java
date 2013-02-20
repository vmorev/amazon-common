package com.github.vmorev.amazon.utils;

import com.amazonaws.services.simpledb.util.SimpleDBUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * User: Valentin_Morev
 * Date: 20.02.13
 */
public class SDBDataHelper {
    private static List<? extends Class<? extends Number>> paddedClasses = Arrays.asList(Byte.class, byte.class, Short.class, short.class,
            Integer.class, int.class, Long.class, long.class, Float.class, float.class, Double.class, double.class);

    public static List<Field> getClassFields(Class clazz) {
        List<Field> list = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields())
            if (!field.isSynthetic() && !Modifier.isStatic(field.getModifiers()))
                list.add(field);

        Class superClazz = clazz.getSuperclass();
        if (superClazz != null)
            list.addAll(getClassFields(superClazz));
        return list;
    }

    public static String convertValueToString(Object obj, Class clazz) throws IOException {
        if (obj == null)
            return null;

        if (clazz == String.class || clazz == Character.class || clazz == char.class || clazz == Boolean.class || clazz == boolean.class)
            return obj.toString();

        if (clazz == Date.class)
            return SimpleDBUtils.encodeDate((Date) obj);

        if (clazz.isEnum())
            return ((Enum) obj).name();

        if (paddedClasses.contains(clazz)) {
            String convertedString = ConvertUtils.convert(obj, clazz).toString();
            StringBuilder paddedString = new StringBuilder();
            paddedString.append(new String(new char[19 - paddedString.length()]).replace('\0', '0')).append(convertedString);
            return paddedString.toString();
        }
        return new ObjectMapper().writeValueAsString(obj);
    }

    public static Object convertStringToValue(String string, Class clazz) throws Exception {
        if (string == null || string.equals(""))
            return null;

        if (clazz == String.class)
            return string;

        if (clazz == Byte.class || clazz == byte.class) {
            return (byte) SimpleDBUtils.decodeZeroPaddingInt(string);
        } else if (clazz == Short.class || clazz == short.class) {
            return (short) SimpleDBUtils.decodeZeroPaddingInt(string);
        } else if (clazz == Integer.class || clazz == int.class) {
            return SimpleDBUtils.decodeZeroPaddingInt(string);
        } else if (clazz == Long.class || clazz == long.class) {
            return SimpleDBUtils.decodeZeroPaddingLong(string);
        } else if (clazz == Float.class || clazz == float.class) {
            return SimpleDBUtils.decodeZeroPaddingFloat(string);
        } else if (clazz == Double.class || clazz == double.class) {
            return new Double(string);
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return new Boolean(string);
        } else if (clazz == Character.class || clazz == char.class) {
            return string.charAt(0);
        } else if (clazz == Date.class) {
            return SimpleDBUtils.decodeDate(string);
        } else if (clazz.isEnum()) {
            return Enum.valueOf(clazz, string);
        }

        return new ObjectMapper().readValue(string, clazz);
    }
}

package com.jin;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AnnotatedArgsParser {

    public <T> T parse(Class<T> clazz, String[] args) throws Exception {
        // parse
        ArrayList<String> annotatedNames = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Argument annotation = field.getAnnotation(Argument.class);
            annotatedNames.add(annotation.value());
        }

        ArgsParser parser = new ArgsParser(args);
        ArrayList<String> argNames = parser.getArgNames();
        for (String argName : argNames) {
            if (!annotatedNames.contains(argName)) {
                String format = String.format("用法错误, 不支持的参数<%s>. 请使用--help选项查看用法", argName);
                throw new IllegalArgumentException(format);
            }
        }

        // set properties
        T argsObj = clazz.newInstance();
        for (Field field : fields) {
            Argument annotation = field.getAnnotation(Argument.class);
            String annotatedName = annotation.value();
            String value = parser.getArgByName(annotatedName);
            if (value == null) {
                if (annotation.required()) {
                    String format = String.format("用法错误, 没有指定<%s>参数. 请使用--help选项查看用法", annotatedName);
                    throw new IllegalArgumentException(format);
                }
            } else {
                Object fieldValue;
                if (field.getType().equals(String.class)) {
                    fieldValue = value;
                } else {
                    fieldValue = convertToType(field.getType().getName(), value);
                }
                field.set(argsObj, fieldValue);
            }
        }
        return argsObj;
    }

    private Object convertToType(String type, String value) {
        switch (type) {
            case "boolean":
                return Boolean.parseBoolean(value);
            case "char":
                return value.charAt(0);
            case "int":
                return Integer.parseInt(value);
            case "short":
                return Short.parseShort(value);
            case "long":
                return Long.parseLong(value);
            case "float":
                return Float.parseFloat(value);
            case "double":
                return Double.parseDouble(value);
            default:
                return null;
        }
    }
}

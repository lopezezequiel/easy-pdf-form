package com.lopezezequiel.EasyPDFForm;

import com.lopezezequiel.EasyPDFForm.annotation.Format;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {

    public static final String format(Field field, String value) {
        String format = field.isAnnotationPresent(Format.class) ?
                ((Format) field.getAnnotation(Format.class)).value() :
                "%s";
        return String.format(format, value);
    }

    public static final String format(Field field, Integer value) {
        String format = field.isAnnotationPresent(Format.class) ?
                ((Format) field.getAnnotation(Format.class)).value() :
                "%d";
        return String.format(format, value);
    }

    public static final String format(Field field, Long value) {
        String format = field.isAnnotationPresent(Format.class) ?
                ((Format) field.getAnnotation(Format.class)).value() :
                "%d";
        return String.format(format, value);
    }

    public static final String format(Field field, Float value) {
        String format = field.isAnnotationPresent(Format.class) ?
                ((Format) field.getAnnotation(Format.class)).value() :
                "%.2f";
        return String.format(format, value);
    }

    public static final String format(Field field, Double value) {
        String format = field.isAnnotationPresent(Format.class) ?
                ((Format) field.getAnnotation(Format.class)).value() :
                "%.2f";
        return String.format(format, value);
    }

    public static final String format(Field field, Boolean value) {
        String format = field.isAnnotationPresent(Format.class) ?
                ((Format) field.getAnnotation(Format.class)).value() :
                "%b";
        return String.format(format, value);
    }

    public static final String format(Field field, Date value) {
        String format = field.isAnnotationPresent(Format.class) ?
                ((Format) field.getAnnotation(Format.class)).value() :
                "yyyy/MM/dd";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(value);
    }
}

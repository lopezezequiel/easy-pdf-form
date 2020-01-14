package com.lopezezequiel.EasyPDFForm.setter;

import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;

public abstract class GenericSetter {

    public abstract void setPDField(PDField pdField, Field field, Object form) throws IOException;
    public abstract void setPDField(PDField pdField, Field field, Object form, String value) throws IOException;
    public abstract void setPDField(PDField pdField, Field field, Object form, Integer value) throws IOException;
    public abstract void setPDField(PDField pdField, Field field, Object form, Long value) throws IOException;
    public abstract void setPDField(PDField pdField, Field field, Object form, Float value) throws IOException;
    public abstract void setPDField(PDField pdField, Field field, Object form, Double value) throws IOException;
    public abstract void setPDField(PDField pdField, Field field, Object form, Boolean value) throws IOException;
    public abstract void setPDField(PDField pdField, Field field, Object form, Date value) throws IOException;
    public abstract void preSet(PDField pdField, Field field, Object form) throws IOException;

}

package com.lopezezequiel.EasyPDFForm.setter;

import com.lopezezequiel.EasyPDFForm.Formatter;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;

public class TextFieldSetter extends GenericSetter {

    @Override
    public void setPDField(PDField pdField, Field field, Object form) throws IOException {
        pdField.setValue("");
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, String value) throws IOException {
        pdField.setValue(Formatter.format(field, value));
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Integer value) throws IOException {
        pdField.setValue(Formatter.format(field, value));
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Long value) throws IOException {
        pdField.setValue(Formatter.format(field, value));
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Float value) throws IOException {
        pdField.setValue(Formatter.format(field, value));
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Double value) throws IOException {
        pdField.setValue(Formatter.format(field, value));
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Boolean value) throws IOException {
        pdField.setValue(Formatter.format(field, value));
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Date value) throws IOException {
        pdField.setValue(Formatter.format(field, value));
    }

    @Override
    public void preSet(PDField pdField, Field field, Object form) throws IOException {

    }

}

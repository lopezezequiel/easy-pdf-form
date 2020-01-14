package com.lopezezequiel.EasyPDFForm.setter;

import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;

public class RadioButtonSetter extends GenericSetter {

    @Override
    public void setPDField(PDField pdField, Field field, Object form) throws IOException {
    }

    private void setValue(PDField pdField, String value) {
        try {
            pdField.setValue(value);
        } catch (Exception e) {

        }
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, String value) throws IOException {
        this.setValue(pdField, value);
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Integer value) throws IOException {
        this.setValue(pdField, String.valueOf(value));
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Long value) throws IOException {
        this.setValue(pdField, String.valueOf(value));
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Float value) throws IOException {
        this.setValue(pdField, String.valueOf(value));
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Double value) throws IOException {
        this.setValue(pdField, String.valueOf(value));
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Boolean value) throws IOException {
        this.setValue(pdField, String.valueOf(value));
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Date value) throws IOException {
        this.setValue(pdField, String.valueOf(value));
    }

    @Override
    public void preSet(PDField pdField, Field field, Object form) throws IOException {
    }
}

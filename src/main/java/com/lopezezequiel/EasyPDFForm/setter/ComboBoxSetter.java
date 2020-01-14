package com.lopezezequiel.EasyPDFForm.setter;

import com.lopezezequiel.EasyPDFForm.Formatter;
import com.lopezezequiel.EasyPDFForm.annotation.Options;
import org.apache.pdfbox.pdmodel.interactive.form.PDComboBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;

import static com.lopezezequiel.EasyPDFForm.Formatter.format;

public class ComboBoxSetter extends GenericSetter {

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
        if(field.isAnnotationPresent(Options.class)) {
            String[] options = field.getAnnotation(Options.class).values();
            PDComboBox pdComboBox = (PDComboBox) pdField;
            pdComboBox.setOptions(Arrays.asList(options), Arrays.asList(options));
        }
    }
}

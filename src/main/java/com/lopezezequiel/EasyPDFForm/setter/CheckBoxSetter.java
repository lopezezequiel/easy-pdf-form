package com.lopezezequiel.EasyPDFForm.setter;

import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;

public class CheckBoxSetter extends GenericSetter {


    private void check(PDCheckBox pdCheckBox, Boolean value) throws IOException {
        if(value != null && value) {
            pdCheckBox.check();
        } else {
            pdCheckBox.unCheck();
        }
    }


    @Override
    public void setPDField(PDField pdField, Field field, Object form) throws IOException {
        this.check((PDCheckBox) pdField, false);
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, String value) throws IOException {
        this.check((PDCheckBox) pdField, value != null && !value.isEmpty());
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Integer value) throws IOException {
        this.check((PDCheckBox) pdField, value != null && value != 0);
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Long value) throws IOException {
        this.check((PDCheckBox) pdField, value != null && value != 0L);
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Float value) throws IOException {
        this.check((PDCheckBox) pdField, value != null && value != 0F);
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Double value) throws IOException {
        this.check((PDCheckBox) pdField, value != null && value != 0D);
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Boolean value) throws IOException {
        this.check((PDCheckBox) pdField, value);
    }

    @Override
    public void setPDField(PDField pdField, Field field, Object form, Date value) throws IOException {
        this.check((PDCheckBox) pdField, false);
    }

    @Override
    public void preSet(PDField pdField, Field field, Object form) throws IOException {

    }
}

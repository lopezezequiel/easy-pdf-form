package com.lopezezequiel.EasyPDFForm;


import com.lopezezequiel.EasyPDFForm.annotation.Ignore;
import com.lopezezequiel.EasyPDFForm.annotation.Lock;
import com.lopezezequiel.EasyPDFForm.annotation.Tag;
import com.lopezezequiel.EasyPDFForm.annotation.Unlock;
import com.lopezezequiel.EasyPDFForm.exception.EasyPDFFormTagNotFoundException;
import com.lopezezequiel.EasyPDFForm.setter.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import javax.security.auth.login.Configuration;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

public class EasyPDFForm {

	private Object form;
	private EasyPDFConfiguration configuration;


	public void showFields() throws Exception {
		PDDocument pdDocument = this.getDocument(this.configuration.getTemplatePath());
		PDAcroForm pdAcroForm = this.getAcroForm(pdDocument);

		List<PDField> fields = pdAcroForm.getFields();


		for(PDField field: fields) {
			String pdType = "Unknown";
			String values = "";

			if(field instanceof PDCheckBox) {
				pdType = "CheckBox";
			} else if(field instanceof PDComboBox) {
				pdType = "ComboBox";
			} else if(field instanceof PDTextField) {
				pdType = "TextField";
			} else if(field instanceof PDListBox) {
				pdType = "ListBox";
			} else if(field instanceof PDRadioButton) {
				pdType = "RadioButton";
			} else if(field instanceof PDSignatureField) {
                pdType = "SignatureField";
            }

			System.out.println(String.format("%s : %s = %s - Values: %s", pdType, field.getFullyQualifiedName(), field.getValueAsString(), values));
		}
	}

	public EasyPDFForm(Object form, EasyPDFConfiguration configuration) {
		this.form = form;
		this.configuration = configuration;
	}

    private PDDocument getDocument(String path) throws Exception {
        File inputFile = new File(path);
        PDDocument pdDocument = PDDocument.load(inputFile);

        // Cargo las fuentes
        if(this.configuration.getFonts() != null) {
			for (String fontPath: this.configuration.getFonts()) {
				PDFont pdFont = PDTrueTypeFont.load(pdDocument, new File(fontPath), WinAnsiEncoding.INSTANCE);
			}
		}

        return pdDocument;
    }

	private PDAcroForm getAcroForm(PDDocument pdDocument) throws Exception {
		PDDocumentCatalog docCatalog = pdDocument.getDocumentCatalog();
		return docCatalog.getAcroForm();
	}

	private Boolean ignoreField(Field field) {
		return field.isAnnotationPresent(Ignore.class);
	}

	//get tag name, default is field name
	private String getTagName(Field field) {
		return field.isAnnotationPresent(Tag.class) ?
				field.getAnnotation(Tag.class).value():
				field.getName();
	}

	private PDField getPDField(PDAcroForm acroForm, String tag) throws EasyPDFFormTagNotFoundException {
		PDField pdField = acroForm.getField(tag);
		return  pdField;
	}


	private GenericSetter getSetter(PDField pdField) {

        if(pdField instanceof PDCheckBox) {
            return new CheckBoxSetter();
        } else if(pdField instanceof PDComboBox) {
            return new ComboBoxSetter();
        } else if(pdField instanceof PDListBox) {
            return new ListBoxSetter();
        } else if(pdField instanceof PDRadioButton) {
            return new RadioButtonSetter();
        } else if(pdField instanceof PDTextField) {
            return new TextFieldSetter();
        }

        return null;
    }


	private void setPDField(PDField pdField, Field field, Object form) throws IllegalAccessException, IOException {

	    GenericSetter setter = this.getSetter(pdField);

	    if(setter == null) return;

	    setter.preSet(pdField, field, form);

		if(field.get(form) == null) {
			setter.setPDField(pdField, field, form);
		} else if(field.getType().isAssignableFrom(Boolean.class)) {
			setter.setPDField(pdField, field, form, (Boolean) field.get(form));
		} else if(field.getType().isAssignableFrom(String.class)) {
			setter.setPDField(pdField, field, form, field.get(form).toString());
		} else if(field.getType().isAssignableFrom(Integer.class)) {
			setter.setPDField(pdField, field, form, (Integer) field.get(form));
		} else if(field.getType().isAssignableFrom(Long.class)) {
			setter.setPDField(pdField, field, form, (Long) field.get(form));
		} else if(field.getType().isAssignableFrom(Float.class)) {
			setter.setPDField(pdField, field, form, (Float) field.get(form));
		} else if(field.getType().isAssignableFrom(Double.class)) {
			setter.setPDField(pdField, field, form, (Double) field.get(form));
		} else if(field.getType().isAssignableFrom(Date.class)) {
			setter.setPDField(pdField, field, form, (Date) field.get(form));
		}

	}

	private void lockField(PDField pdField, Field field, Object form) {
		if(field.isAnnotationPresent(Lock.class)) {
			pdField.setReadOnly(true);
		} else if(field.isAnnotationPresent(Unlock.class)) {
			pdField.setReadOnly(false);
		} else if(form.getClass().isAnnotationPresent(Lock.class)) {
			pdField.setReadOnly(true);
		} else if(form.getClass().isAnnotationPresent(Unlock.class)) {
			pdField.setReadOnly(false);
		}
	}

	private void setFields(Object form, PDAcroForm pdAcroForm) throws Exception {

		for (Field field : form.getClass().getDeclaredFields()) {
			field.setAccessible(true);

			if(this.ignoreField(field)) {
				continue;
			}

			String tag = this.getTagName(field);
			PDField pdField = this.getPDField(pdAcroForm, tag);

			//skip if tag does not exists
			if(pdField == null) {
				continue;
			}

			//seteo el valor
			this.setPDField(pdField, field, form);

			//bloqueo el campo si es necesario
			this.lockField(pdField, field, form);
		}
	}

	private PDDocument getFilledDocument() throws Exception {
        PDDocument pdDocument = this.getDocument(this.configuration.getTemplatePath());
        PDAcroForm pdAcroForm = this.getAcroForm(pdDocument);
        this.setFields(this.form, pdAcroForm);
        return  pdDocument;
    }

	public void save(String outputPath) throws Exception {
        PDDocument pdDocument = this.getFilledDocument();
		File outputFile = new File(outputPath);
		pdDocument.save(outputFile);
		pdDocument.close();
	}

    public ByteArrayOutputStream save() throws Exception {
        PDDocument pdDocument = this.getFilledDocument();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        pdDocument.save(stream);
		stream.close();

		return stream;
    }
	

}

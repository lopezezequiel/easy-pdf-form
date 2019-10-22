package com.lopezezequiel.EasyPDFForm;

import com.lopezezequiel.EasyPDFForm.exception.EasyPDFFormException;
import com.lopezezequiel.EasyPDFForm.exception.EasyPDFFormNullFieldException;
import com.lopezezequiel.EasyPDFForm.exception.EasyPDFFormTagNotFoundException;
import com.lopezezequiel.EasyPDFForm.exception.EasyPDFFormTypeException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class EasyPDFForm {

	public String getName() {
		Form form = this.getClass().getAnnotation(Form.class);
	    return (form == null) ? null : form.name();
	}
	
	private String getInputPath() {
		//TODO what if annotation does not exists?
	    return this.getClass().getAnnotation(Form.class).path();
	}

	private void validateType(Field field, Class cls) throws EasyPDFFormTypeException {
		Boolean valid = field.getType().isAssignableFrom(cls);

		if(!valid) {
			throw new EasyPDFFormTypeException(field.getName(), cls);
		}
	}

	//get tag name, default is field name
	private String getTagName(Field field) {
		return field.isAnnotationPresent(Tag.class) ?
				field.getAnnotation(Tag.class).value():
				field.getName();
	}


	private void handleComboBox(Field field, PDComboBox pdComboBox) throws EasyPDFFormTypeException, IllegalAccessException, IOException {
		//must be String
		this.validateType(field, String.class);
		String value = (String) field.get(this);

		if(field.isAnnotationPresent(Options.class)) {
			String[] options = field.getAnnotation(Options.class).values();
			pdComboBox.setOptions(Arrays.asList(options), Arrays.asList(options));
		}

		List<String> opt = pdComboBox.getOptions();

		if(!pdComboBox.getOptions().contains(value)) {
			throw 	new IllegalArgumentException();
		}

		pdComboBox.setValue(value);
	}

	private Boolean isNumber(Object value) {
		return (value instanceof Integer ||
				value instanceof Long ||
				value instanceof Float ||
				value instanceof Double);
	}

	private SimpleDateFormat getDateFormatter(Field field) {
		String format = field.isAnnotationPresent(DateFormat.class) ?
				((DateFormat) field.getAnnotation(DateFormat.class)).value() :
				"yyyy/MM/dd";
		return new SimpleDateFormat(format);
	}

	private void handleTextField(Field field, PDTextField pdTextField) throws IllegalAccessException, IOException {

		Object value = field.get(this);
        String stringValue = null;

		if(field.getType().isAssignableFrom(String.class)) {
            stringValue = (String) value;

			if(field.isAnnotationPresent(ValidateRegex.class)) {
				ValidateRegex validateRegex = (ValidateRegex) field.getAnnotation(ValidateRegex.class);
				Boolean valid = stringValue != null && stringValue.matches(validateRegex.value());
				if(!valid) {
					throw  new IllegalArgumentException();
				}
			}

			//TODO make pattern better
			if(field.isAnnotationPresent(Email.class)) {
				String emailPattern = "[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
				Boolean valid = stringValue != null && stringValue.matches(emailPattern);
				if(!valid) {
					throw  new IllegalArgumentException();
				}
			}

		} else if(this.isNumber(value)) {
		    stringValue = String.valueOf(value);

			if(field.isAnnotationPresent(Min.class)) {
				Min min = field.getAnnotation(Min.class);
				if((Double) value < min.value()) {
					throw  new IllegalArgumentException();
				}
			}

			if(field.isAnnotationPresent(Max.class)) {
				Max max = field.getAnnotation(Max.class);
				if((Double) value > max.value()) {
					throw  new IllegalArgumentException();
				}
			}
		} else if(field.getType().isAssignableFrom(Date.class)) {
			stringValue = getDateFormatter(field).format((Date) value);
        }

		pdTextField.setValue(stringValue);
	}

	private void handleCheckBox(Field field, PDCheckBox pdCheckBox) throws IllegalAccessException, IOException {
		Boolean value = (Boolean) field.get(this);
		if(value != null) {
			if(value) {
				pdCheckBox.check();
			} else {
				pdCheckBox.unCheck();
			}
		}
	}

	private void setDefaultValue(Field field) throws IllegalAccessException, ParseException {
		if(field.get(this) != null) {
			return;
		}

		String value = field.getAnnotation(Default.class).value();

		if(field.getType().isAssignableFrom(Boolean.class)) {
			field.set(this, Boolean.parseBoolean(value));
		} else if(field.getType().isAssignableFrom(String.class)) {
			field.set(this, value);
		} else if(field.getType().isAssignableFrom(Integer.class)) {
			field.set(this, Integer.parseInt(value));
		} else if(field.getType().isAssignableFrom(Long.class)) {
         	field.set(this, Long.parseLong(value));
		} else if(field.getType().isAssignableFrom(Float.class)) {
			field.set(this, Float.parseFloat(value));
		} else if(field.getType().isAssignableFrom(Double.class)) {
         	field.set(this, Double.parseDouble(value));
		} else if(field.getType().isAssignableFrom(Date.class)) {
			field.set(this, this.getDateFormatter(field).parse(value));
		}

	}

	private void setValues(PDAcroForm acroForm) throws IllegalArgumentException, IllegalAccessException, IOException, ParseException, EasyPDFFormException {

		for(Field field : this.getClass().getDeclaredFields()) {
			field.setAccessible(true);

			//skip ignored fields
			if(field.isAnnotationPresent(Ignore.class)) {
				continue;
			}

			//set default value
			if(field.isAnnotationPresent(Default.class)) {
				this.setDefaultValue(field);
			}

			if(field.get(this) == null) {
				if(field.isAnnotationPresent(NotNull.class)) {
					throw new EasyPDFFormNullFieldException(field.getName());
				}
				continue;
			}

			//get pdField
			String tag = this.getTagName(field);
			PDField pdField = this.getPDField(acroForm, tag);

			//handle different pdfield types
			if(pdField instanceof PDTextField) {
				this.handleTextField(field, (PDTextField) pdField);
			} else if(pdField instanceof PDComboBox) {
				this.handleComboBox(field, (PDComboBox) pdField);
			} else if(pdField instanceof  PDCheckBox) {
				this.handleCheckBox(field, (PDCheckBox) pdField);
			}

 			if(field.isAnnotationPresent(Lock.class)) {
				pdField.setReadOnly(true);
			}

 			if(field.isAnnotationPresent(Unlock.class)) {
				pdField.setReadOnly(false);
			}

		}
	}

	private PDField getPDField(PDAcroForm acroForm, String tag) throws EasyPDFFormTagNotFoundException {
		PDField pdField = acroForm.getField(tag);

		if(pdField == null) {
			throw new EasyPDFFormTagNotFoundException(tag);
		}

		return  pdField;
	}


	public void showFields() throws IOException {
		PDDocument pdDocument = this.getDocument();
		PDAcroForm pdAcroForm = this.getAcroForm(pdDocument);

		List<PDField> fields = pdAcroForm.getFields();


		for(PDField field: fields) {
            String pdType = "Unknown";

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
            }

            System.out.println(String.format("%s %s = %s", pdType, field.getFullyQualifiedName(), field.getValueAsString()));
        }

	}

	private PDDocument getDocument() throws IOException {
		File inputFile = new File(this.getInputPath());
		return PDDocument.load(inputFile);
	}

	private PDAcroForm getAcroForm(PDDocument pdDocument) throws IOException {
		PDDocumentCatalog docCatalog = pdDocument.getDocumentCatalog();
		PDAcroForm acroForm = docCatalog.getAcroForm();
		return acroForm;
	}
	

	public void save(String outputPath) throws EasyPDFFormException, IOException, ParseException, IllegalAccessException {
		PDDocument pdDocument = this.getDocument();
		PDAcroForm pdAcroForm = this.getAcroForm(pdDocument);
		this.setValues(pdAcroForm);

		File outputFile = new File(outputPath);
		pdDocument.save(outputFile);
		pdDocument.close();
	}
	

}

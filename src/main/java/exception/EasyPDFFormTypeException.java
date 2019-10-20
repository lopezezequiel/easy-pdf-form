package exception;

public class EasyPDFFormTypeException extends EasyPDFFormException {

    private String fieldName;
    private Class expectedType;


    public EasyPDFFormTypeException(String fieldName, Class expectedType) {
        super(String.format("Field %s must be %s type", fieldName, expectedType.getName()));
        this.fieldName = fieldName;
        this.expectedType = expectedType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class getExpectedType() {
        return expectedType;
    }
}

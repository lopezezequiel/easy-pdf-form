package exception;

public class EasyPDFFormNullFieldException extends EasyPDFFormException {

    private  String fieldName;

    public EasyPDFFormNullFieldException(String fieldName) {
        super(String.format("Field %s can not be null", fieldName));
        this.fieldName = fieldName;
    }
}

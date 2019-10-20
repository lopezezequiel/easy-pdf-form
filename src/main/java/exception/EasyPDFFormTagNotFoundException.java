package exception;

public class EasyPDFFormTagNotFoundException extends EasyPDFFormException {

    private String tagName;

    public EasyPDFFormTagNotFoundException(String tagName) {
        super(String.format("Tag %s does not exists in PDF Document", tagName));
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }
}

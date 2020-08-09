package com.lopezezequiel.EasyPDFForm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class EasyPDFConfiguration {

    private List<String> fonts;
    private InputStream template;

    public EasyPDFConfiguration(String templatePath) throws FileNotFoundException {
        File file = new File(templatePath);
        this.template = new FileInputStream(templatePath);
    }

    public EasyPDFConfiguration(InputStream template) {
        this.template = template;
    }

    public List<String> getFonts() {
        return fonts;
    }

    public void setFonts(List<String> fonts) {
        this.fonts = fonts;
    }

    public InputStream getTemplate() {
        return template;
    }

    public void setTemplate(InputStream template) {
        this.template = template;
    }
}

package com.lopezezequiel.EasyPDFForm;

import java.util.List;

public class EasyPDFConfiguration {

    private List<String> fonts;
    private String templatePath;

    public List<String> getFonts() {
        return fonts;
    }

    public void setFonts(List<String> fonts) {
        this.fonts = fonts;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
}

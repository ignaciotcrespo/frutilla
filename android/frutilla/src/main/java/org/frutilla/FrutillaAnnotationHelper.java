package org.frutilla;


import org.frutilla.annotations.Frutilla;

class FrutillaAnnotationHelper {

    static String getText(Frutilla ann) {
        String value = "";
        if (ann != null) {
            StringBuffer text = new StringBuffer();
            addTexts("GIVEN", text, ann.Given());
            text.append("\n");
            addTexts("WHEN", text, ann.When());
            text.append("\n");
            addTexts("THEN", text, ann.Then());
            value = text.toString();
        }
        return value;
    }

    static void addTexts(String header, StringBuffer text, String[] sentences) {
        text.append(header).append(" ");
        int i = 0;
        for (String sentence : sentences) {
            if (i > 0) {
                text.append("\n");
                text.append(" AND ");
            }
            text.append(sentence);
            i++;
        }
    }
}

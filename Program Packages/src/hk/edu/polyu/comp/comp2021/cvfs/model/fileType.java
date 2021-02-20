package hk.edu.polyu.comp.comp2021.cvfs.model;

/**
 * enum for type of files
 */
public enum fileType {
    /**
     * type of directory
     */
    DIR("dir"),

    /**
    *type of txt
    */
    TXT("txt"),

    /**
     * type of java
     */
    JAVA("java"),

    /**
     * type of html
     */
    HTML("html"),

    /**
     * type of css
     */
    CSS("css");

    private final String type;

    fileType(String s) {
        this.type = s;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }
}


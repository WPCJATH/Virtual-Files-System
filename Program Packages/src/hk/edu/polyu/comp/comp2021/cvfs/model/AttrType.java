package hk.edu.polyu.comp.comp2021.cvfs.model;

/**
 *enum for attrbute names
 */
public enum AttrType {
    /**
     * AttrType of name
     */
    NAME("name"),

    /**
     * AttrType of type
     */
    TYPE("type"),

    /**
     * AttrType of size
     */
    SIZE("size");

    private final String type;

    AttrType(String s) {
        this.type = s;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

}

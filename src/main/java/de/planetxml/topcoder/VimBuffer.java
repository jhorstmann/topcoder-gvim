package de.planetxml.topcoder;

public class VimBuffer {
    private int    id;
    private String name;
    private String content;

    public VimBuffer(int id, String name) {
        this(id, name, "");
    }

    public VimBuffer(int id, String name, String content) {
        this.id      = id;
        this.name    = name;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
       this.content = content;
    }
}

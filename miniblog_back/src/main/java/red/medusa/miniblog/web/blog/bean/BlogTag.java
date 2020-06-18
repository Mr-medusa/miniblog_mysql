package red.medusa.miniblog.web.blog.bean;

import java.io.Serializable;

public class BlogTag implements Serializable {
    private String id;
    private String[][] tags;

    public BlogTag() {
    }

    public BlogTag(String id, String[][] tags) {
        this.id = id;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[][] getTags() {
        return tags;
    }

    public void setTags(String[][] tags) {
        this.tags = tags;
    }
}

package red.medusa.miniblog.web.blog.bean;

import java.io.*;

public class TagBlob {
    private byte[] tags;

    public TagBlob() {
    }

    public TagBlob(BlogTag blogTag) {
        this.tags = getBlobByBlogTag(blogTag);
    }

    public byte[] getTags() {
        return tags;
    }

    public void setTags(byte[] tags) {
        this.tags = tags;
    }

    public BlogTag getBlogTag() {
        return getBlogTagByBlob();
    }

    private byte[] getBlobByBlogTag(BlogTag blogTag) {
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        try (ObjectOutputStream obs = new ObjectOutputStream(ops)) {
            obs.writeObject(blogTag);
            return ops.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private BlogTag getBlogTagByBlob() {
        try {
            try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(this.tags))) {
                return (BlogTag) in.readObject();
            } catch (IOException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}

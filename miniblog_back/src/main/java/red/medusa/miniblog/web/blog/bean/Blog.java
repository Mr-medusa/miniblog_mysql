package red.medusa.miniblog.web.blog.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.util.StringUtils;

import java.util.Date;

public class Blog {
    private String id;
    private String title;
    private String author;
    private String[] tags;
    private String blogTags;
    private String previewContent;
    private String htmlContent;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private boolean isShowBanner;


    public String getBlogTags() {
        return this.blogTags;
    }

    public String[] getTags() {
        return this.tags;
    }

    /**
     * 前台传入后端时设置blogTags
     */
    public void setTags(String[] tags) {
        if (this.blogTags == null)
            this.blogTags = StringUtils.arrayToCommaDelimitedString(tags);
        if (this.tags != null)
            return;
        this.tags = tags;
    }

    /**
     * 从数据库查询时设置tags
     */
    public void setBlogTags(String blogTags) {
        if (this.tags == null)
            this.tags = StringUtils.tokenizeToStringArray(blogTags, ",");
        if (this.blogTags != null)
            return;
        this.blogTags = blogTags;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getPreviewContent() {
        return previewContent;
    }

    public void setPreviewContent(String previewContent) {
        this.previewContent = previewContent;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean getIsShowBanner() {
        return isShowBanner;
    }

    public void setIsShowBanner(boolean showBanner) {
        isShowBanner = showBanner;
    }
}

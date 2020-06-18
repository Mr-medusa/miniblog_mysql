package red.medusa.miniblog.web.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import red.medusa.miniblog.web.blog.bean.Blog;
import red.medusa.miniblog.web.blog.bean.BlogParam;
import red.medusa.miniblog.web.blog.bean.BlogTag;
import red.medusa.miniblog.web.blog.mapper.BlogMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class BlogService {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private TagService tagService;

    public List<Blog> getBlogCatalog() {
        return blogMapper.findAllByFields(excludeSomeField(null));
    }

    public Blog getBlogPreview(String id) {
        ArrayList<String> params = new ArrayList<>();
        params.add("htmlContent");
        BlogParam blog = excludeSomeField(params);
        blog.setId(id);
        List<Blog> blogs = blogMapper.findAllByFields(blog);
        if (blogs.isEmpty())
            return new Blog();
        else
            return blogs.get(0);
    }

    public List<Blog> getPreviewByIds(String[] ids) {
        List<String> params = new ArrayList<>();
        params.add("htmlContent");
        BlogParam blog = excludeSomeField(params);
        blog.setIds(ids);
        return blogMapper.findAllByFields(blog);
    }

    public List<Blog> getBlogCatalogByKeyword(String keyword) {
        BlogParam blogParam = excludeSomeField(null);
        blogParam.setKeyword("%" + keyword + "%");
        List<Blog> blogs = blogMapper.getBlogCatalogByKeyword(blogParam);
        return blogs;
    }

    private BlogParam excludeSomeField(List<String> excludes) {
        BlogParam blog = new BlogParam();
        if (excludes == null) {
            blog.setHtmlContent("1");
            blog.setPreviewContent("1");
        } else {
            if (excludes.contains("htmlContent")) {
                blog.setHtmlContent("1");
            }
            if (excludes.contains("previewContent")) {
                blog.setPreviewContent("1");
            }
        }
        return blog;
    }

    public void putBlog(String id, Blog blog) {
        if (Objects.isNull(blogMapper.findById(id))) {
            blogMapper.insert(blog);
        } else {
            blogMapper.update(blog);
        }
    }

    public int deleteBlog(String id) {
        return blogMapper.delete(id);
    }


    public Blog findById(String id) {
        return blogMapper.findById(id);
    }


    public void putTags(BlogTag blogTag) {
        tagService.putTag(blogTag);
    }
}

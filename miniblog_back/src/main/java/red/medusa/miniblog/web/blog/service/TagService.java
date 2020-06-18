package red.medusa.miniblog.web.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import red.medusa.miniblog.web.blog.bean.BlogTag;
import red.medusa.miniblog.web.blog.bean.TagBlob;
import red.medusa.miniblog.web.blog.mapper.TagMapper;

import java.util.Objects;

@Service
@Transactional
public class TagService {
    @Autowired
    private TagMapper tagMapper;

    public BlogTag findById() {
        TagBlob blogTag = tagMapper.findBlogTag();
        if (blogTag != null)
            return blogTag.getBlogTag();
        return null;
    }

    public void putTag(BlogTag blogTag) {
        TagBlob tb = tagMapper.findBlogTag();
        if (Objects.isNull(tb)) {
            tagMapper.insert(new TagBlob(blogTag));
        } else {
            tagMapper.update(new TagBlob(blogTag));
        }
    }


}

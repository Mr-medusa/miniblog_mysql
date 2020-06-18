package red.medusa.miniblog.web.blog.mapper;

import red.medusa.miniblog.web.blog.bean.Blog;
import red.medusa.miniblog.web.blog.bean.BlogParam;
import red.medusa.miniblog.web.common.BaseMapper;

import java.util.List;

public interface BlogMapper extends BaseMapper<Blog,String> {
    List<Blog> findAllByFields(BlogParam blog);

    List<Blog> getBlogCatalogByKeyword(BlogParam blogParam);
}

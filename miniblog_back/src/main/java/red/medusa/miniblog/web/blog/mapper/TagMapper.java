package red.medusa.miniblog.web.blog.mapper;

import red.medusa.miniblog.web.blog.bean.TagBlob;
import red.medusa.miniblog.web.common.BaseMapper;

public interface TagMapper extends BaseMapper<TagBlob, String> {
    TagBlob findBlogTag();
}

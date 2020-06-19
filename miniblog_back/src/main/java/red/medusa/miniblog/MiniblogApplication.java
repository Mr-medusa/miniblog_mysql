package red.medusa.miniblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import red.medusa.miniblog.web.blog.bean.BlogTag;
import red.medusa.miniblog.web.blog.bean.TagBlob;
import red.medusa.miniblog.web.blog.mapper.TagMapper;
import red.medusa.miniblog.web.pad.bean.Pad;
import red.medusa.miniblog.web.pad.mapper.PadMapper;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@SpringBootApplication()
@MapperScan("red.medusa.miniblog.web.**.mapper")
public class MiniblogApplication {

    @Autowired
    private PadMapper padMapper;

    @Autowired
    private TagMapper tagMapper;


    public static void main(String[] args)  {
        SpringApplication.run(MiniblogApplication.class, args);
    }

    /**
     * 初始化数据库
     */
    @PostConstruct
    public void init() {
        long defaultPadNum = padMapper.findAll().size();
        if (defaultPadNum == 0) {
            Pad defaultPad = new Pad("0f49491c-4509-4bd6-a1d8-84f6de7b4b84", null, "DEFAULT", "JavaScript", true, false, true, new Date(), new Date(), 0, true, new ArrayList<>());
            padMapper.insert(defaultPad);
        }

        TagBlob tagBlob = tagMapper.findBlogTag();
        if (Objects.isNull(tagBlob)) {
            BlogTag blogTag = new BlogTag("blogTags", new String[][]{});
            tagBlob = new TagBlob(blogTag);
            tagMapper.insert(tagBlob);
        }
    }
}


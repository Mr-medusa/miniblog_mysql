package red.medusa.miniblog.web.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import red.medusa.miniblog.web.blog.bean.BlogTag;
import red.medusa.miniblog.web.blog.service.TagService;

@RestController
@RequestMapping("/miniblog/data")
public class TagDataController {
    @Autowired
    private TagService tagService;

    @GetMapping("/blogTag")
    public BlogTag getTags(){
        return tagService.findById();
    }

    @GetMapping(value = "/tag/{id}")
    public BlogTag getTag() {
        return tagService.findById();
    }

    @PutMapping(value = "/tag/{id}")
    public BlogTag putTag(@RequestBody BlogTag tag) {
        tagService.putTag(tag);
        return tag;
    }
    // 作用同上,就不修改前端了,多一个方法就多一个吧,难得改
    @PatchMapping(value = "/blogTag/{id}")
    public BlogTag putBlogTag(@RequestBody BlogTag tag) {
        tagService.putTag(tag);
        return tag;
    }



}

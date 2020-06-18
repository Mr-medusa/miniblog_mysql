package red.medusa.miniblog.web.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import red.medusa.miniblog.web.blog.bean.Blog;
import red.medusa.miniblog.web.blog.bean.BlogTag;
import red.medusa.miniblog.web.blog.service.BlogService;

@RestController
@RequestMapping("/miniblog/data")
public class BlogDataController {
    @Autowired
    private BlogService blogService;

    @GetMapping(value = "/blog/{id}")
    public Blog getBlog(@PathVariable String id) {
        return blogService.findById(id);
    }

    @PutMapping(value = "/blog/{id}")
    public Blog putBlog(@PathVariable String id, @RequestBody Blog blog) {
        blogService.putBlog(id, blog);
        return blog;
    }

    @DeleteMapping("/blog/{id}")
    public String deleteBlog(@PathVariable String id) {
        int i = blogService.deleteBlog(id);
        return "成功删除了第 - " + i + " - 行Blog数据";
    }

    @PatchMapping("/Tag{blogTags}")
    public void blogTags(@RequestBody BlogTag blogTag) {
        blogService.putTags(blogTag);
    }


}

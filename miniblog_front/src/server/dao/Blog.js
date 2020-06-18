import Vue from "vue"

const BLOG_REST = "/miniblog/data/blog";
const BLOG_CATALOG = "/getBlogCatalog";
const BLOG_CATALOG_KEYWORD = "/getBlogCatalogByKeyword";

function Blog() {

    this.get = function (id) {
        return Vue.http.get(BLOG_REST + "/" + id);
    };
    this.getAll = function () {
        return Vue.http.get(BLOG_CATALOG);
    };
    this.put = function (blog) {
        return Vue.http.put(BLOG_REST + "/" + blog.id, this.normalBlog(blog));
    };

    this.delete = function (id) {
        return Vue.http.delete(BLOG_REST + "/" + id);
    };
    this.getBlogCatalogByKeyword = function (keyword) {
        return Vue.http.get(BLOG_CATALOG_KEYWORD, {params: {keyword: keyword}});
    };
    this.normalBlog = function (blog) {
        return {
            id: blog.id || null,
            title: blog.title || null,
            author: blog.author || null,
            tags: blog.tags || null,
            blogTags: blog.blogTags || null,
            previewContent: blog.previewContent || null,
            htmlContent: blog.htmlContent || null,
            updateTime: blog.updateTime || null,
            isShowBanner: blog.isShowBanner || null
        }
    }
}

export default Blog;
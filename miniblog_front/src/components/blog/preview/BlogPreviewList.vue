<template>
    <div class="blog-preview-list">
        <!--列表-->
        <blog-preview v-for="blog in blogs" :blog="blog"></blog-preview>
        <!--分页-->
        <pagination :pageInfo="pageInfo" @change="getPageBlogs"></pagination>
    </div>
</template>

<script>
    import pagination from "../../../components/pagination/vue-pagination"
    import BlogPreview from "./BlogPreview";
    import EventHub from "../../../utils/EventHub";
    import BlogCache from "../../../utils/BlogCache";
    import BlogService from "../../../server/service/BlogService";

    export default {
        name: "Book",
        data() {
            return {
                pageInfo: EventHub.pageInfo,
                blogs: [],
             }
        },
        components: {
            BlogPreview,
            pagination,
        },
        mounted() {
            this.pageInfo.current = this.$route.params.page || 1;
            this.pageInfo.current = parseInt(this.pageInfo.current);
            if(isNaN(this.pageInfo.current))
                this.pageInfo.current = 1;
            this.getPageBlogs(this.pageInfo.current);
        },
        methods: {
            getPageBlogs: function (current) {
                current = current == 0 ? current : current - 1;
                this.blogs = [];
                var blogPage = [];
                var pageSize = EventHub.pageInfo.pagenum;
                var blogKey = current;

                //标签过滤
                if(EventHub.blogSearch.isTagsSearch){
                    blogKey = "TAG_FILTER_"+EventHub.blogSearch.tag + current;
                    blogPage = EventHub.blogs.filter(function (blog) {
                        return (blog.tags.indexOf(EventHub.blogSearch.tag) > -1);
                    });
                }else{
                    if(EventHub.blogSearch.iskeywordSearch){    //关键字查询缓存的key
                        blogKey = "KEYWORD_FILTER_"+EventHub.blogSearch.keyword+current;
                    }
                    blogPage = EventHub.blogs;
                }
                var searchKeys = [],
                    size = current * pageSize,
                    nextSize = current * pageSize + pageSize;

                for (let i = size; i < blogPage.length; i++) {
                    if((i >= size) && (i < nextSize)){
                        searchKeys.push(blogPage[i].id);
                    }
                }
                this.doPageSearch(searchKeys,blogKey,current);
            },

            doPageSearch(searchKeys,blogKey,current){
                var that = this;
                //搜索详细页
                var blogCache = BlogCache.getPage(blogKey);
                if(blogCache && blogCache.length){
                    that.blogs = blogCache;
                }else{
                    if(searchKeys.length){
                        BlogService.getBlogContentByIds(searchKeys).then(function (res) {
                            if(res.data && res.data.length){
                                that.blogs = res.data;
                                BlogCache.addBlogPage(blogKey,res.data);
                            }else{
                                that.blogs = [];
                            }
                        });
                    }
                }

                //记录上一次的页码
                if(!EventHub.blogSearch.isTagsSearch && !EventHub.blogSearch.iskeywordSearch){
                    EventHub.pageInfo.lastCurrent = current + 1;
                }

                //改变导航栏上URL的页码
                this.$router.push(`/blog/preview_list/${EventHub.pageInfo.current}`);
            },
            initBlogsCompleted(){
                this.getPageBlogs(this.pageInfo.current);
            },
        },
        created: function () {
            EventHub.$on('initBlogsCompleted', this.initBlogsCompleted);
        },
        beforeDestroy: function () {
            EventHub.$off('initBlogsCompleted', this.initBlogsCompleted);
        },
    }
</script>

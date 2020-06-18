import Vue from "vue"

const BLOG_PREVIEW = "/getBlogPreview";
const BLOG_PREVIEW_BY_IDS = "/getPreviewByIds";

function BlogContent() {

    this.getPreview = function (id) {
        return Vue.http.get(BLOG_PREVIEW, {params: {id: id}});
    };

    this.getPreviewByIds = function (ids) {
        return Vue.http.post(BLOG_PREVIEW_BY_IDS,ids);
    };
}

export default BlogContent;
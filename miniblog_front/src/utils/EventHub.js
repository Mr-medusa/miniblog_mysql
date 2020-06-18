import Vue from 'vue'
import FrontConfig from "../config/FrontConfig";

var EventHub = new Vue({
    data: {
        /*padCards*/
        folders: [],
        padCards: null,
        padsTop: FrontConfig.showTopOperationBar ? 120 : 60,

        /*pad*/
        defaultFolder: {},
        defaultFolderId: "0f49491c-4509-4bd6-a1d8-84f6de7b4b84",
        colors: FrontConfig.colors,
        classify: [
            {
                id: 1,
                name: '选择语言:'
            },
            {
                id: 2,
                name: 'Math'
            },{
                id: 3,
                name: 'JavaScript'
            },
            {
                id: 4,
                name:
                    'HTML'
            },{
                id: 5,
                name: 'JAVA'
            },
            // {
            //     id: 5,
            //     name: 'SQL'
            // },
            // {
            //     id: 6,
            //     name: 'C'
            // }, {
            //     id: 7,
            //     name: 'C++'
            // },
            // {
            //     id: 8,
            //     name: 'XML'
            // },
            // {
            //     id: 9,
            //     name: 'JSON'
            // },
            // {
            //     id: 10,
            //     name: 'CSS'
            // },
            // {
            //     id: 11,
            //     name: 'KOTLIN'
            // }, {
            //     id: 12,
            //     name: 'SCALA'
            // },
            {
                id: 13,
                name: 'TEXT'
            }
        ],
        codeMirrors: new Map(),

        /*blogs*/
        blogs: [],
        blogTags: [],
        blogTagMap: new Map(),
        pageInfo: {
            total: 0,
            current: 1,
            pagenum: 10,     //一页十条
            pagegroup: 5,
            lastCurrent: 1,
        },
        blogSearch: {
            iskeywordSearch: false,
            keyword: null,
            isTagsSearch: false,
            tag: null,
        },
        user: null,
        // allPermissionBtn: new Map(),
    },
    created() {
        var user = sessionStorage.getItem('miniblogUser');
        if (user) {
            this.user = JSON.parse(user);
        }
    },
    methods: {
        getPadParent(child) {
            if (!child.parentId)
                return {children: this.folders};
            for (var i = 0; i < this.folders.length; i++) {
                var queue = [this.folders[i]];
                var item;
                while (queue.length) {
                    item = queue.shift();
                    if (item.id === child.parentId) {
                        return item;
                    }
                    if (item.children && item.children.length) {
                        item.children.forEach(function (son) {
                            queue.push(son);
                        });
                    }
                }
            }
        },
        /*获取当前子节点平级的所有元素*/
        getOrders(miniSelected, orders, parent) {
            orders.push({id: -1, name: "排序:"});
            var children = parent.children || 0;
            for (var j = 0; j < children.length; j++) {
                if (children[j].isShow)
                    orders.push({
                        id: children[j].id,
                        name: j + ' - ' + children[j].name,
                        isMiniSelected: miniSelected.name === children[j].name
                    });
            }
            orders.push({id: "LAST", name: children.length + " - 最后一位"});
        },
        getTypes(miniSelected) {
            var myType = null;
            if (miniSelected.type && typeof miniSelected.type == "string") {
                myType = miniSelected.type.toLocaleUpperCase();
            }
            for (var i = 0; i < EventHub.classify.length; i++) {
                EventHub.classify[i].isMiniSelected = false;
                if (EventHub.classify[i].name.toLocaleUpperCase() === myType) {
                    EventHub.classify[i].isMiniSelected = true;
                }
            }
        },
        interchange(swapper, exchangeeId) {
            var result = [];
            var parent = EventHub.getPadParent(swapper);
            var you, me, exchangee;
            var children = parent.children ? parent.children : parent;

            if (exchangeeId === "LAST") {
                for (let i = 0; i < children.length; i++) {
                    var child = children[i];
                    if (child.id === swapper.id) {
                        child.order = children.length + 1;
                        children.splice(i, 1);
                        children.push(child);
                        return [child];
                    }
                }
            }

            var find = false;
            for (var i = 0; i < children.length; i++) {
                var child = children[i];
                if (child.id === exchangeeId) {
                    you = i;
                    exchangee = child;
                    find = true;
                }
                if (child.id === swapper.id) {
                    me = i;
                }
                if (find && i != me) {         //重新构建序号(无论是从前往后 还是从后往前都+1)
                    child.order = i + 1;
                    result.push(child);
                }
            }

            swapper.order = you;
            // exchangee.order = me;

            //交换位置并不好用,直接排序吧 -----------
            //                                    -
            //交换位置：                           -
            // children.splice(me, 1, exchangee); -
            // children.splice(you, 1, swapper);  -

            //排序：
            if (me < you)
                you--;
            children.splice(me, 1);
            children.splice(you, 0, swapper);

            result.push(swapper);

            return result;
        },
        insertPad(pad, parent, pos) {
            if (!pos) {
                parent.children.push(pad);
            } else {
                var isFinished = false;
                for (var i = 0; i < parent.children.length; i++) {
                    if (parent.children[i].id === pos) {
                        parent.children.splice(i, 0, pad);
                        isFinished = true;
                        break;
                    }
                }
                if (!isFinished) {
                    parent.children.push(pad);
                }
            }
        },
        getMyType(type) {
            if (!type)
                return null;
            var mode = null;
            switch (type.toLocaleUpperCase()) {
                // case  "CSS":
                //     mode = "text/css";
                //     break;
                // case  "XML":
                //     mode = "text/xml";
                //     break;
                case  "HTML":
                    mode = "text/html";
                    break;
                // case  "JSON":
                //     mode = "application/json";
                //     break;
                case  "JAVASCRIPT":
                    mode = "application/javascript";
                    break;
                case  "JAVA":
                    mode = "text/x-java";
                    break;
                // case  "KOTLIN":
                //     mode = "text/x-kotlin";
                //     break;
                // case  "SCALA":
                //     mode = "text/x-scala";
                //     break;
                // case  "C":
                //     mode = "text/x-c";
                //     break;
                // case  "C++":
                //     mode = "text/x-c++src";
                // case  "SQL":
                //     mode = "text/x-sql";
                //     break;
                case  "TEXT":
                    mode = null;
                    break;
            }
            return mode;
        },
        putBlogTag(tag, isAdd = true) {
            tag = tag.trim();
            if (tag) {
                var count = this.blogTagMap.get(tag) || 0;
                count = isAdd ? count + 1 : count - 1;
                if (!isAdd && count <= 0)
                    this.blogTagMap.delete(tag);
                else
                    this.blogTagMap.set(tag, count);
            }
        },
        makeBlogTags() {
            this.blogTags.splice(0, this.blogTags.length);
            this.blogTags.push(...this.blogTagMap.keys());
        },
        changePageInfo(pages, turnLast = false) {
            this.pageInfo.total = pages.length;
            this.pageInfo.current = 1;
            if (turnLast)
                this.pageInfo.current = this.pageInfo.lastCurrent;
        },
        changeBlogKeyWord(who, param) {
            switch (who) {
                case "keyword":
                    this.blogSearch.iskeywordSearch = true;
                    this.blogSearch.keyword = param;
                    break;
                case "tag":
                    this.blogSearch.isTagsSearch = true;
                    this.blogSearch.tag = param;
                    break;
                case "clearKeyword":
                    this.blogSearch.iskeywordSearch = false;
                    this.blogSearch.keyword = null;
                    break;
                case "clearTag":
                    this.blogSearch.isTagsSearch = false;
                    this.blogSearch.tag = null;
                    break;
                default:
                    this.blogSearch.iskeywordSearch = false;
                    this.blogSearch.isTagsSearch = false;
                    this.blogSearch.keyword = null;
                    this.blogSearch.tag = null;
            }

        },
        /**
        refreshPermissionBtn() {
            var that = this;
            this.allPermissionBtn.forEach(function (value, key, map) {
                if (that.user && that.user.username && that.user.bearer) {
                    key.style.display = '';
                } else {
                    key.style.display = 'none';
                }
            });
        }*/
    },


});

export default EventHub;
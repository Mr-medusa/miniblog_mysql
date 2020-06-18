<template>
    <div id="icp-code">
        <div style="height: 30px">
            <!--备案号-->
            <a href="http://www.beian.miit.gov.cn/" target="_blank" rel="nofollow">黔ICP备19001774号</a>
        </div>
    </div>
</template>
<script>
    export default {
        name: "IcpCode",
    }
</script>
<style scoped>
    #icp-code div {
        flex: 1;
    }

    #icp-code a {
        font-family: "Microsoft YaHei UI", 微软雅黑, 宋体, 华文宋体;
        font-style: normal;
        font-size: 14px;
        color: rgba(173, 173, 173, 0.91);
    }
</style>
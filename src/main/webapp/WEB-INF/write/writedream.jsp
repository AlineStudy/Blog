<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>写梦</title>
    <link href="${ctx}/css/zui/css/zui.min.css" rel="stylesheet"/>
    <link href="${ctx}/css/zui/css/zui-theme.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="${ctx}/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/css/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/css/zui/js/zui.min.js"></script>
    <script src="${ctx}/css/zui/lib/kindeditor/kindeditor.min.js"></script>
    <style>
        body,html{
            background-color: #EBEBEB;
            padding: 0;
            margin: 0;
            height:100%;
        }
        .writedream-context{
            background-color: white;
            margin-top: 30px;
            margin-left: 30px;
            margin-right: 30px;
            min-height:800px;
        }

    </style>
</head>
<body>
<nav class="navbar navbar-inverse" role="navigation">
    <div class="container-fluid" style="background-color: #2f2f2f">
        <!-- 导航头部 -->
        <div class="navbar-header">
            <!-- 移动设备上的导航切换按钮 -->
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse-example">
                <span class="sr-only">切换导航</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <!-- 品牌名称或logo -->
            <a class="navbar-brand" href="${ctx}/list?id=${user.id}">个人空间</a>
        </div>
        <!-- 导航项目 -->
        <div class="collapse navbar-collapse navbar-collapse-example">
            <!-- 一般导航项目 -->
            <ul class="nav navbar-nav">
                <li><a href="#">我的文章</a></li>
                <li><a href="${ctx}/index_list">首页</a></li>
                ...
                <!-- 导航中的下拉菜单 -->
                <li class="dropdown">
                    <a href="your/nice/url" class="dropdown-toggle" data-toggle="dropdown">设置 <b class="caret"></b></a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="your/nice/url">任务</a></li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-nav">
                <li><a href="your/nice/url">消息</a></li>
            </ul>

            <ul class="nav navbar-nav">
                <li style="background-color: black"><a href="javascript:void(0);">写文章</a></li>
            </ul>

            <ul class="nav navbar-nav" style="margin-left: 680px">
                <li><a href="${ctx}/list?id=${user.id}">${user.nickName}

                </a></li>
            </ul>
            <img src="images/q.png" width="30" style="margin-top: 4px"/>
        </div><!-- END .navbar-collapse -->
    </div>

</nav>

<!--中间内容-->
<form id="write_form" name="w_form" role="w_form" class="writedream-form" action="doWritedream" method="post">

    <input hidden="hidden" name="cid" value="${cont.id}"/>

<div class="writedream-context">
    <div style="margin-top: 20px;margin-left: 20px;position: absolute;">
        <div class="dropdown dropdown-hover">
            <button class="btn" type="button" data-toggle="dropdown" id="dream-diff" style="background-color:#EBEBEB"><span id="fen" >
                <c:if test="${cont.category != null}">
                    ${cont.category}
                </c:if>
                <c:if test="${cont.category == '' || cont.category == null}">
                    分类
                </c:if>

            </span> <span class="caret"></span></button>
            <input id="hidden_cat" hidden="hidden" name="category" value="${cont.category}"/>


            <ul class="dropdown-menu" id="dreamland-category">
                <li><a>前端</a></li>
                <li><a>后端</a></li>
                <li><a>编程语言</a></li>
                <li><a>服务器</a></li>
                <li><a>算法</a></li>
                <li><a>网络</a></li>
                <li><a>其他</a></li>
            </ul>
        </div>
    </div>

    <div style="float: left;margin-top: 20px;margin-left: 110px;background-color: #EBEBEB">
        <input type="text" id="txtTitle" name="txtT_itle" value="${cont.title}" class="input-file-title" maxlength="100" placeholder="&nbsp;&nbsp;&nbsp;&nbsp;输入文章标题"  style="height: 33px;width: 1080px;background-color:#EBEBEB;border: 0px" >
    </div>
    <!--富文本编辑器-->
    <div style="margin-top:20px ;float: left;margin-left: 20px">
        <textarea id="content" name="content" class="form-control kindeditor" style="height:600px;width: 1170px">${cont.content}</textarea>

    </div>

    <br/>
    <div class="switch" style="float: left;margin-top: 670px;margin-left: 20px;position: absolute">
        <input type="checkbox" name="private_dream" id="private_dream" value="off">
        <label>私密</label>
        <span style="color: red">${error}</span>
    </div>
    <br/>
    <div style="float: left;margin-top: 700px;margin-left:20px;position: absolute">
        <button class="btn btn-primary"  type="button" id="sub_dream">发表</button>
        <button class="btn btn-primary" id="go_back" type="button" >返回</button>
    </div>
</div>
</form>
</body>
<script>

    //li标签的点击事件
    /**
     * 通过点击 li 标签获取该 li 标签内的值，
     * 将其赋值给 id 为 fen 的 span 标签和 id 为 hidden_cat 的隐藏 input 标签，
     * input 标签可通过 name 属性传给后台
     */
    $("#dreamland-category li").click(function () {
        var val = $(this).text();
        $("#fen").html(val);
        $("#hidden_cat").val(val);
    });


    /**
     basePath：指定 kindEditor 的路径。
     uploadJson：指定上传文件的后台映射 URL 路径。
     fileManagerJson：指定浏览远程图片的后台映射 URL 路径。
     allowFileManager：true 表示显示浏览远程服务器按钮。
     bodyClass：指定编辑器的 className。
     items：配置编辑器的工具栏。
     *
     */
    $(function () {
        editor =  KindEditor.create('#content', {
            basePath: 'css/zui/lib/kindeditor/',
            uploadJson : '${ctx}/fileUpload',
            fileManagerJson : '${ctx}/fileManager',
            allowFileManager : true,
            bodyClass : 'article-content',
            items : ['source', '|', 'undo', 'redo', '|', 'preview', 'template', 'cut', 'copy', 'paste',
                'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image','multiimage',
                'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
                'anchor', 'link', 'unlink']

        });
        KindEditor.sync();
    });


    /**
     * 然后根据元素 id 获取元素对象，如果该元素的 checked 属性等于 true，
     * 代表被选中状态，则将 on 赋值给该 inputk 框的 value 属性，
     * 否则将 off 赋值给 input 框的 value 属性。
     */
    //私密梦开关点击事件
    $(".switch").click(function () {
        var pd = document.getElementById('private_dream');
        if(pd.checked == true){
            $("#private_dream").val("on");
        }else{
            $("#private_dream").val("off");
        }
    });

    //私密开关回显
    $(function () {
        var v = '${cont.personal}';
        if(v == "1"){
            var pd = document.getElementById('private_dream');
            pd.checked = true;
        }
    });


    //返回
    $("#go_back").click(function () {
        location.href = "javascript:history.go(-1);";
    });



    //发表
    $("#sub_dream").click (function(){
        var val =  $("#fen").html();
        if(val.trim()=='梦分类'){
            alert("请选择梦分类！");
            return;
        }

        var tit = $("#txtTitle").val();
        if(tit == null || tit.trim() == ""){
            alert("请输入文章标题！");
            return;
        }
        editor.sync();
        var v1 = $("#content").val();
        if(v1 == null || v1.trim() == ""){
            alert("文章内容为空！");
            return;
        }
        $("#write_form").submit();

    });


</script>
</html>
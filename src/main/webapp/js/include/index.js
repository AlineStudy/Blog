//点赞或者踩
function upvote_click(id,cont) {

    $.ajax({
        type:'post',
        url:'upvote',
        data: {"id":id,"uid":'${user.id}',"upvote":cont},
        dataType:'json',
        success:function (data) {
            var up = data["data"];

            if(up == "success"){
                if (cont == -1){
                    var down = document.getElementById("down_"+id);
                    var num = down.innerHTML;
                    var value = parseInt(num) + cont;
                    down.innerHTML = value;
                }else {
                    var num = document.getElementById(id).innerHTML;
                    var value = parseInt(num) + cont;
                    document.getElementById(id).innerHTML = value;
                    document.getElementById("up_"+id).innerHTML = value;
                }
            }else if (up == "done"){
                alert("已经点赞");
            }else if(up == "down"){
                alert("已踩!");
            }else {
                window.location.href = "/login.jsp";
            }
        }
    });
}



// 前端日期格式化函数 index.jsp 中的代码
function FormatDate (strTime) {
    var date = new Date(strTime);
    var h=date.getHours();       //获取当前小时数(0-23)
    var m=date.getMinutes();     //获取当前分钟数(0-59)
    if(m<10) m = '0' + m;
    var s=date.getSeconds();
    if(s<10) s = '0' + s;
    return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+h+':'+m+":"+s;
}


//点击评论或者回复图标
function reply(id,uid) {
    $("div").remove("#comment_reply_"+id+" .comment-show");
    $("div").remove("#comment_reply_"+id+" .comment-show-con");
    if(showdiv_display = document.getElementById('comment_reply_'+id).style.display=='none'){//如果show是隐藏的

        document.getElementById('comment_reply_'+id).style.display='block';//show的display属性设置为block（显示）
        $.ajax({
            type:'post',
            url:'/reply',
            data: {"content_id":id},
            dataType:'json',
            success:function(data){
                var list =  data["list"];

                var okHtml;
                if(list!=null && list!=""){
                    $(list).each(function () {
                        var chtml = "<div class='comment-show'>"+
                            "<div class='comment-show-con clearfix'>"+
                            "<div class='comment-show-con-img pull-left'><img src='"+this.user.imgUrl+"' alt=''></div>"+
                            "<div class='comment-show-con-list pull-left clearfix'>"+
                            "<div class='pl-text clearfix'>"+
                            "<a  class='comment-size-name'>"+this.user.nickName+" :</a>"+
                            "<span class='my-pl-con'>&nbsp;"+this.comContent+"</span>"+
                            "</div> <div class='date-dz'><span class='date-dz-left pull-left comment-time'>"+FormatDate(this.commTime)+"</span>"+
                            "<div class='date-dz-right pull-right comment-pl-block'>"+
                            "<a onclick='deleteComment("+id+","+uid+","+this.id+",null)' id='comment_dl_"+this.id+"' style='cursor:pointer' class='removeBlock'>删除</a>"+
                            "<a style='cursor:pointer' onclick='comment_hf("+id+","+this.id+","+null+","+this.user.id+","+uid+")' id='comment_hf_"+this.id+"' class='date-dz-pl pl-hf hf-con-block pull-left'>回复</a>"+
                            "<span class='pull-left date-dz-line'>|</span>"+
                            "<a onclick='reply_up("+this.id+")' style='cursor:pointer' class='date-dz-z pull-left' id='change_color_"+this.id+"'><i class='date-dz-z-click-red'></i>赞 (<i class='z-num' id='comment_upvote_"+this.id+"'>"+this.upvote+"</i>)</a>"+
                            "</div> </div> <div class='hf-list-con' id='hf-list-con-"+this.id+"'>";


                        var ehtml =   "</div> </div> </div></div>";
                        var parentComm_id = this.id;

                        okHtml = chtml;
                        //alert(this.children)
                        if(this.children != null && this.children != ''){
                            var commentList = this.comList;
                            $(commentList).each(function () {
                                // alert(this.id);
                                var oHtml = "<div class='all-pl-con'><div class='pl-text hfpl-text clearfix'>"+
                                    "<a class='comment-size-name'>"+this.user.nickName+"<a class='atName'>@"+this.byUser.nickName+" :</a> </a>"+
                                    "<span class='my-pl-con'>"+this.comContent+"</span>"+
                                    "</div><div class='date-dz'> <span class='date-dz-left pull-left comment-time'>"+FormatDate(this.commTime)+"</span>"+
                                    "<div class='date-dz-right pull-right comment-pl-block'>"+
                                    "<a style='cursor:pointer' onclick='deleteComment("+id+","+uid+","+this.id+","+parentComm_id+")' id='comment_dl_"+this.id+"' class='removeBlock'>删除</a>"+
                                    "<a onclick='comment_hf("+id+","+this.id+","+parentComm_id+","+this.user.id+","+uid+")' id='comment_hf_"+this.id+"' style='cursor:pointer' class='date-dz-pl pl-hf hf-con-block pull-left'>回复</a> <span class='pull-left date-dz-line'>|</span>"+
                                    "<a onclick='reply_up("+this.id+")' id='change_color_"+this.id+"' style='cursor:pointer' class='date-dz-z pull-left'><i class='date-dz-z-click-red'></i>赞 (<i class='z-num' id='comment_upvote_"+this.id+"'>"+this.upvote+"</i>)</a>"+
                                    "</div></div> </div>";

                                okHtml = okHtml + oHtml;
                            });


                        }

                        okHtml = okHtml+ehtml;
                        $("#comment-show-" + id).append(okHtml);

                    });
                }

            }
        });


    }else{//如果show是显示的

        document.getElementById('comment_reply_'+id).style.display='none';//show的display属性设置为none（隐藏）

    }
}


//限制字数
function keyUP(t){
    var len = $(t).val().length;
    if(len > 139){
        $(t).val($(t).val().substring(0,140));
    }
}




function _comment(content_id,uid,cid) {
    var myDate = new Date();
    //获取当前年
    var year=myDate.getFullYear();
    //获取当前月
    var month=myDate.getMonth()+1;
    //获取当前日
    var date=myDate.getDate();
    var h=myDate.getHours();       //获取当前小时数(0-23)
    var m=myDate.getMinutes();     //获取当前分钟数(0-59)
    if(m<10) m = '0' + m;
    var s=myDate.getSeconds();
    if(s<10) s = '0' + s;
    var now=year+'-'+month+"-"+date+" "+h+':'+m+":"+s;
    //获取输入内容
    var oSize = $("#comment_input_"+content_id).val();
    console.log(oSize);
    //动态创建评论模块

    if(oSize.replace(/(^\s*)|(\s*$)/g, "") != ''){

        $.ajax({
            type : 'post',
            url : '/comment',
            data:{"content_id":content_id,"uid":uid,"oSize":oSize,"comment_time":now},
            dataType:'json',
            success:function (data) {
                var comm_data = data["data"];
                if (comm_data =="fail")
                    window.location.href = "/login.jsp";
                else {
                    var i = comm_data.id;
                    oHtml = '<div class="comment-show-con clearfix"><div class="comment-show-con-img pull-left"><' +
                        'img src="${user.imgUrl}" alt=""></div> <div class="comment-show-con-list pull-left clearfix">' +
                        '<div class="pl-text clearfix"> <a  class="comment-size-name">${user.nickName} : </a> ' +
                        '<span class="my-pl-con">&nbsp;'+ oSize +'</span> </div> <div class="date-dz"> ' +
                        '<span class="date-dz-left pull-left comment-time">'+now+'</span> ' +
                        '<div class="date-dz-right pull-right comment-pl-block">' +
                        '<a style="cursor:pointer"  onclick="deleteComment('+content_id+','+cuid+','+id+','+null+')" id="comment_dl_'+id+'"  class="removeBlock">删除</a> ' +
                        '<a style="cursor:pointer" onclick="comment_hf('+content_id+','+id+','+null+','+comm_data.user.id+','+cuid+')" id="comment_hf_'+id+'" class="date-dz-pl pl-hf hf-con-block pull-left">回复</a> ' +
                        '<span class="pull-left date-dz-line">|</span> <a onclick="reply_up('+id+')" id="change_color_'+id+'" style="cursor:pointer"  class="date-dz-z pull-left" >' +
                        '<i class="date-dz-z-click-red"></i>赞 (<i class="z-num" id="comment_upvote_'+id+'">0</i>)</a> </div> </div><div class="hf-list-con"></div></div> </div>';


                    $("#comment_"+content_id).parents('.reviewArea ').siblings('.comment-show-first').prepend(oHtml);
                    $("#comment_"+content_id).siblings('.flex-text-wrap').find('.comment-input').prop('value','').siblings('pre').find('span').text('');

                    $("#comment_input_"+content_id).val('');

                    var num = document.getElementById("comment_num_"+content_id).innerHTML;
                    document.getElementById("comment_num_"+content_id).innerHTML = (parseInt(num) + 1)+"";

                }
            }
        });
    }
}





//1）判断用户是否是文章作者，如果不是就不做处理，如果是则提示是否确认删除

//删除评论块
function deleteComment(con_id,uid,id,fid) {
    // alert(uid)
    if('${user.id}'==uid){

        if (!confirm("确认要删除？")) {
            window.event.returnValue = false;
        }else{

            //发送ajax请求
            $.ajax({
                type:'post',
                url:'/deleteComment',
                data: {"id":id,"uid":uid,"con_id":con_id,"fid":fid},
                dataType:'json',
                success:function(data){
                    var comm_data =  data["data"];
                    //alert(comm_data);
                    if(comm_data=="fail"){
                        window.location.href = "/login.jsp";
                    }else if(comm_data=="no-access"){
                        //alert("没有权限！");
                    }else {
                        //alert(comm_data)
                        var oThis = $("#comment_dl_"+id);
                        var oT = oThis.parents('.date-dz-right').parents('.date-dz').parents('.all-pl-con');
                        if(oT.siblings('.all-pl-con').length >= 1){
                            oT.remove();
                        }else {
                            oThis.parents('.date-dz-right').parents('.date-dz').parents('.all-pl-con').parents('.hf-list-con').css('display','none')
                            oT.remove();
                        }
                        oThis.parents('.date-dz-right').parents('.date-dz').parents('.comment-show-con-list').parents('.comment-show-con').remove();


                        //评论数comment_num_con_id
                        document.getElementById("comment_num_"+con_id).innerHTML = parseInt(comm_data)+"";

                    }
                }
            });
        }
    }
}



//一级评论  点击回复创建回复块
function comment_hf(content_id,comment_id,fid,by_id,cuid) {
    // alert(cuid)
    //获取回复人的名字
    var oThis = $("#comment_hf_"+comment_id);
    var fhName = oThis.parents('.date-dz-right').parents('.date-dz').siblings('.pl-text').find('.comment-size-name').html();
    //回复@
    var fhN = '回复@' + fhName;
    var fhHtml = '<div class="hf-con pull-left"> <textarea id="plcaceholder_'+comment_id+'"  class="content comment-input " placeholder="'+fhN+'" onkeyup="keyUP(this)"></textarea> <a id="comment_pl_'+comment_id+'" onclick="comment_pl('+content_id+','+comment_id+','+fid+','+by_id+','+cuid+')" class="hf-pl" style="color: white;cursor:pointer">评论</a></div>';
    //显示回复
    if (oThis.is('.hf-con-block')) {
        oThis.parents('.date-dz-right').parents('.date-dz').append(fhHtml);
        oThis.removeClass('hf-con-block');
        $('.content').flexText();
        oThis.parents('.date-dz-right').siblings('.hf-con').find('.pre').css('padding', '6px 15px');

        //input框自动聚焦
        oThis.parents('.date-dz-right').siblings('.hf-con').find('.hf-input').val('').focus().val(fhN);
    } else {
        oThis.addClass('hf-con-block');
        oThis.parents('.date-dz-right').siblings('.hf-con').remove();
    }
}



//点赞
function reply_up(id) {
    var num = document.getElementById("comment_upvote_"+id).innerHTML;
    if($("#change_color_"+id).is('.date-dz-z-click')){
        num--;
        $("#change_color_"+id).removeClass('date-dz-z-click red');
        $("#change_color_"+id).find('.z-num').html(num);
        $("#change_color_"+id).find('.date-dz-z-click-red').removeClass('red');

    }else {
        num++;
        $("#change_color_"+id).addClass('date-dz-z-click');
        $("#change_color_"+id).find('.z-num').html(num);
        $("#change_color_"+id).find('.date-dz-z-click-red').addClass('red');
    }

    $.ajax({
        type:'post',
        url:'/comment',
        data: {"id":id,"upvote":num},
        dataType:'json',
        success:function(data){
            var comm_data =  data["data"];
            if(comm_data=="fail"){
                window.location.href = "/login.jsp";
            }
        }
    });
}





//点击一级评论块的评论按钮
function comment_pl(content_id,comment_id,fid,by_id,cuid) {
    if(fid==null){
        fid = comment_id
    }
    var oThis = $("#comment_pl_"+comment_id);
    var myDate = new Date();
    //获取当前年
    var year=myDate.getFullYear();
    //获取当前月
    var month=myDate.getMonth()+1;
    //获取当前日
    var date=myDate.getDate();
    var h=myDate.getHours();       //获取当前小时数(0-23)
    var m=myDate.getMinutes();     //获取当前分钟数(0-59)
    if(m<10) m = '0' + m;
    var s=myDate.getSeconds();
    if(s<10) s = '0' + s;
    var now=year+'-'+month+"-"+date+" "+h+':'+m+":"+s;
    //获取输入内容
    var oHfVal = oThis.siblings('.flex-text-wrap').find('.comment-input').val();
    console.log(oHfVal)
    var oHfName = oThis.parents('.hf-con').parents('.date-dz').siblings('.pl-text').find('.comment-size-name').html();
    //alert(oHfName)
    var oAllVal = '回复@'+oHfName;

    if(oHfVal.replace(/^ +| +$/g,'') == '' || oHfVal == oAllVal){

    }else {
        $.ajax({
            type:'post',
            url:'/comment_child',
            data: {"content_id":content_id,"uid":'${user.id}',"oSize":oHfVal,"comment_time":now,"by_id":by_id,"id":fid},
            dataType:'json',
            success:function(data){
                var comm_data =  data["data"];
                //alert(comm_data);
                if(comm_data=="fail"){
                    window.location.href = "/login.jsp";
                }else {
                    var id = comm_data.id;
                    //alert(id)
                    var oAt = '回复<a class="atName">@'+oHfName+'</a>  '+oHfVal;
                    var oHtml = '<div class="all-pl-con"><div class="pl-text hfpl-text clearfix"><a class="comment-size-name">${user.nickName} : </a><span class="my-pl-con">'+oAt+'</span></div><div class="date-dz"> <span class="date-dz-left pull-left comment-time">'+now+'</span> <div class="date-dz-right pull-right comment-pl-block"> <a style="cursor:pointer" onclick="deleteComment('+content_id+','+cuid+','+id+','+fid+')" id="comment_dl_'+id+'" class="removeBlock">删除</a> <a onclick="comment_hf('+content_id+','+id+','+fid+','+comm_data.user.id+','+cuid+')" id="comment_hf_'+id+'" style="cursor:pointer" class="date-dz-pl pl-hf hf-con-block pull-left">回复</a> <span class="pull-left date-dz-line">|</span> <a onclick="reply_up('+id+')" id="change_color_'+id+'" style="cursor:pointer" class="date-dz-z pull-left"><i class="date-dz-z-click-red"></i>赞 (<i class="z-num" id="comment_upvote_'+id+'">0</i>)</a> </div> </div></div>';
                    $("#comment_pl_"+comment_id).parents('.hf-con').parents('.comment-show-con-list').find('.hf-list-con').css('display','block').prepend(oHtml) && oThis.parents('.hf-con').siblings('.date-dz-right').find('.pl-hf').addClass('hf-con-block') && oThis.parents('.hf-con').remove();

                    var num = document.getElementById("comment_num_"+content_id).innerHTML;
                    document.getElementById("comment_num_"+content_id).innerHTML = (parseInt(num) + 1)+"";
                }
            }
        });
    }
}












var p_flag = false;
//手机验证码检查
function checkPhoneCode(){
    var reg = /^\d{6}\b/;
    var code = $("#verifyCode").val();
    if(reg.test(code)){
        p_flag =  true;
    }else {
        p_flag =  false;
    }

    return p_flag;
}



//登录
$("#phone_btn").click(function () {

    if(checkPhone()&& checkPhoneCode()){
        // 校验用户名和密码
        $("#phone_span").text("").css("color","red");
        $("#phone_form").submit();
    }else {
        alert("请输入手机号和6位验证码!");
    }

});





//获取验证码
$(function () {
    var go = document.getElementById('go');

    go.onclick = function (ev){
        if(!flag2){
            $("#phone_span").text("手机号码非法或者未注册！").css("color","red");
        }else {
            //  发送短信给用户手机..
            // 1 发送一个HTTP请求，通知服务器 发送短信给目标用户
            var telephone =$("input[name='telephone']").val();// 用户输入的手机号
            // 用户输入手机号校验通过
            $("#go").attr("disabled", "disabled");
            countDown(60);

            $.ajax({
                method: 'POST',
                url: '${ctx}/sendSms',
                data : {
                    telephone : telephone
                },
                success:function(data) {
                    var tt = data["msg"];
                    if(tt){
                        alert("发送短信成功!");

                    }else{
                        alert("发送短信出错，请联系管理员");
                    }
                }
            });
        }


        var oEvent = ev || event;
        //js阻止链接默认行为，没有停止冒泡
        oEvent.preventDefault();

    }
});

//倒计时
function countDown(s){
    if(s <= 0){
        $("#go").text("重新获取");
        $("#go").removeAttr("disabled");
        return;
    }
    /* $("#go").val(s + "秒后重新获取");*/
    $("#go").text(s + "秒后重新获取");
    setTimeout("countDown("+(s-1)+")",1000);
}






//校验用户名
function checkUserName() {
    $("#back_data").text("");
    $("#back_active").text("");
    $("#back_phone").text("");
    var username = $("#username").val();
    username = username.replace(/^\s+|\s+$/g,"");
    if(username == ""){
        $("#normal_span").text("请输入用户名或密码！").css("color","red");
        return false;
    }else{
        $("#normal_span").text("").css("color","green");
        return true;
    }
}

//校验密码
function checkPassword() {
    $("#back_data").text("");
    $("#back_active").text("");
    $("#back_phone").text("");
    var password = $("#password").val();
    password = password.replace(/^\s+|\s+$/g, "");
    if (password == "") {
        $("#normal_span").text("请输入密码！").css("color", "red");
        return false;
    }
    if (password.length < 6) {
        $("#normal_span").text("密码长度少于6位，请重新输入！").css("color", "red");
        return false;
    }

    $("#normal_span").text("").css("color", "green");
    return true;
}



//验证码校验
var flag_c = false;
function checkCode() {
    $("#back_data").text("");
    $("#back_active").text("");
    $("#back_phone").text("");
    var code = $("#code").val();
    code = code.replace(/^\s+|\s+$/g,"");
    if(code == ""){
        $("#code_span").text("请输入验证码！").css("color","red");
        flag_c = false;
    }else{
        $.ajax({
            type: 'post',
            url: '/checkCode',
            data: {"code": code},
            dataType: 'json',
            success: function (data) {
                var val = data['message'];
                if (val == "success") {
                    $("#code_span").text("√").css("color","green");
                    $("#reg_span").text("");
                    flag_c = true;
                }else {
                    $("#code_span").text("验证码错误！").css("color","red");
                    flag_c = false;
                }
            }
        });

    }
    return flag_c;
}







//登录按钮对应的点击事件 normal_login 方法
function normal_login() {
    if(checkUserName() && checkPassword() && checkCode()) {
        $("#normal_form").submit();
    }
}




//密码框回车事件
$("#password").bind('keypress',function(event){
    if(event.keyCode == 13)
    {
        normal_login();
    }
});

//验证码框回车事件
$("#code").bind('keypress',function(event){

    if(event.keyCode == 13)
    {
        normal_login();
    }
});


//登录
$("#phone_btn").click(function () {

    if(checkPhone()&& checkPhoneCode()){
        // 校验用户名和密码
        $("#phone_span").text("").css("color","red");
        $("#phone_form").submit();
    }else {
        alert("请输入手机号和6位验证码!");
    }

});
$(document).ready(function(){

    //Toast
    var toastHideTimer = null;
    function toast(msg){
    	clearTimeout(toastHideTimer);
    	$('#toast p').text(msg);
    	$('#toast').fadeIn(400);
    	toastHideTimer = setTimeout(function(){
    		$('#toast').fadeOut(700);
    	},2000)
	}
	
	//login-register-btn 点击时隐藏popover 并且清除值
	$(".login-register-btn").click(function(){
		$("[data-toggle='popover']").popover('hide');
		$("input").val("");
	});

	//正则表达式验证用户名、邮箱、密码格式是否正确
	var userNameReg =  /^[A-Za-z0-9_\u4e00-\u9fa5]{2,10}$/;
	var emailReg = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/; 
    var passwordReg = /^[a-zA-Z0-9]{6,16}$/;

    $('#login-modal').on('shown.bs.modal', function () {
    	  $('#userName').focus()
    })	
    $('#register-modal').on('shown.bs.modal', function () {
    	$('#register-userName').focus()
    })
    
	//登录按钮点击时事件
	$("#login").click(function(){
		//保存用户名
		var userName = $("#userName").val();
		//用户名或密码是否为空
		if ($("#userName").val()=="") {
			$("#userNameTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>请输入用户名！</b>").popover("show");
			$("#userName").focus();
			$("#password").val("");
		}else if ($("#password").val()=="") {
			$("#passwordTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>请输入密码！</b>").popover("show");
			$("#password").focus();
		}else{
			$.ajax({
				url : 'user/login',
				type : 'post',
				data : {
							username:$("#userName").val(),
							password:$("#password").val()
						},
				dataType : 'json',
				success : function(msg){
					if(msg == '登录成功'){
						if($('.main-head h3').text() != '')
							window.location.reload();
						$('#login-modal').modal('hide');
						$("#login-register").html("<li class='dropdown'><a href='#' class='dropdown-toggle' data-toggle='dropdown'>"+userName+"<b class='caret'></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a><span class='dropdown-arrow'></span><ul class='dropdown-menu'><li><a href='userinfo'>个人资料</a></li><li><a href='javascript:exit();''><span class='glyphicon glyphicon-log-out'></span> 退出</a></li>");
						$(".insert-message-header h1").text("留言");
						$(".insert-message-body").html("<div id='message-text' class='textarea'  contenteditable='true'></div>");
						$(".insert-message-footer").html("<a href='javascript:insertMessage();' class='btn btn-inverse '>发表留言</a>");
					}else{
						Avgrund.show("#login-info");
						$(window).scrollTop(0);
						$("#password").val('');
					}
				},
				error : function(){
					toast('系统错误');
				}
			})
		}
	});

	//注册按钮点击时事件
	$("#register").click(function(){
		//保存用户名
		var userName = $("#register-userName").val();

		if ($("#register-userName").val()=="") {
			$("#register-userNameTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>请输入用户名！</b>").popover("show");
			$("#register-userName").focus();
		}else if(!userNameReg.test($("#register-userName").val())){
			$("#register-userNameTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>2-10位、中英文、数字或下划线</b>").popover("show");
			$("#register-userName").focus();
		}else if(!passwordReg.test($("#register-password").val())){
			$("#register-passwordTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>6-16位、由字母或数字组成</b>").popover("show");
			$("#register-password").val("");
			$("#register-confirm").val("");
			$("#register-confirmTip").popover('hide');
			$("#register-password").focus();
		}else if ($("#register-password").val()=="") {
			$("#register-passwordTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>请输入密码！</b>").popover("show");
			$("#register-password").focus();
		}else if ($("#register-confirm").val()=="") {
			$("#register-confirmTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>请确认密码！</b>").popover("show");
			$("#register-confirm").focus();
		}else if($("#register-password").val()!=$("#register-confirm").val()){
			$("#register-password").val("");
			$("#register-confirm").val("");
			$("#register-password").focus();
			$("#register-confirmTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>两次密码不一致！</b>").popover("show");
		}else if($("#register-email").val()==""){
			$("#register-emailTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>请输入邮箱地址！</b>").popover("show");
			$("#register-email").focus();
		}else if(!emailReg.test($("#register-email").val())){
			$("#register-emailTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>请输入正确格式的邮箱地址</b>").popover("show");
			$("#register-email").focus();
		}else{
			$.ajax({
				url : 'user/register',
				type : 'post',
				data : {
							username : $("#register-userName").val(),
							password : $("#register-password").val(),
							email : $("#register-email").val()
						},
				dataType : 'json',
				success : function(msg){
					if(msg == "注册成功"){
						location.reload();
						$("#register-modal").modal('hide');
						$("input").val("");
						$("#login-register").html("<li class='dropdown'><a href='#' class='dropdown-toggle' data-toggle='dropdown'>"+userName+"<b class='caret'></b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a><span class='dropdown-arrow'></span><ul class='dropdown-menu'><li><a href='userinfo'>个人资料</a></li><li><a href='javascript:exit();''><span class='glyphicon glyphicon-log-out'></span> 退出</a></li>");
					}else if(msg == '用户名已存在'){
						$("#register-userName").focus();
						$("#register-userNameTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>用户名已存在！</b>").popover("show");
					}else if(msg == '邮箱已被注册'){
						$("#register-email").focus();
						$("#register-emailTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>邮箱已被注册！</b>").popover("show");
					}
					toast(msg)
				},
				error : function(){
					toast('系统错误');
				}
			});
		}
	});

	//登陆时提示信息
	$("#userName").bind("input propertychange",function(){
		if ($("#userName").val()!="") {
			$("#userNameTip").popover('hide');
		}
	})
	$("#password").bind("input propertychange",function(){
		if ($("#password").val()!="") {
			$("#passwordTip").popover('hide');
		}
	})


	//注册时提示信息
	$("#register-userName").bind("input propertychange",function(){
		if(!userNameReg.test($("#register-userName").val())){
			$("#register-userNameTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>2-10位、中英文、数字或下划线</b>").popover("show");
		}else{
			$.ajax({
				url : 'user/checkusername',
				type : 'post',
				data : {username:$("#register-userName").val()},
				dataType : 'json',
				success : function(msg){
					if(msg == '用户名已存在'){
						$("#register-userNameTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>用户名已存在！</b>").popover("show");
					}else{
						$("#register-userNameTip").popover('hide');
					}
				},
				error : function(){
					toast('系统错误');
				}
			});
		}
	})
	$("#register-password").bind("input propertychange",function(){
		if(!passwordReg.test($("#register-password").val())){
			$("#register-passwordTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>6-16位、由字母或数字组成</b>").popover("show");
		}else{
			$("#register-passwordTip").popover("hide");
			if($("#register-confirm").val()!=$("#register-password").val()&&$("#register-confirm").val()!=""&&$("#register-password").val().length>=6){
				$("#register-confirmTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>两次密码不一致！</b>").popover("show");
			}else{
				$("#register-confirmTip").popover("hide");
			}
		}
	})
	$("#register-confirm").bind("input propertychange",function(){
		if(!passwordReg.test($("#register-password").val())){
			$("#register-passwordTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>6-16位、由字母或数字组成</b>").popover("show");
		}else{
			$("#register-passwordTip").popover("hide");
			if($("#register-confirm").val()!=$("#register-password").val()){
				$("#register-confirmTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>两次密码不一致！</b>").popover("show");
			}
			else{
				$("#register-confirmTip").popover("hide");
			}
		}
	})
	$("#register-email").bind("input propertychange",function(){
		if(!emailReg.test($("#register-email").val())){
			$("#register-emailTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>请输入正确格式的邮箱地址</b>").popover("show");
			$("#register-email").focus();
		}else{
			$.ajax({
				url : 'user/checkemail',
				type : 'post',
				data : {email:$("#register-email").val()},
				dataType : 'json',
				success : function(msg){
					if(msg == '邮箱已被注册'){
						$("#register-emailTip").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>邮箱已被注册！</b>").popover("show");
					}else{
						$("#register-emailTip").popover('hide');
					}
				}
			});
		}
	})

	//阻止popover响应
	 $("[data-toggle='popover']").popover({
            trigger : 'manual'
        });
	
	$("#contact").click(function(){
		 Avgrund.show("#contact-info");
	})
	
});
function exit(){
	$.ajax({
		url : 'user/logout',
		method : 'post',
		dataType : 'json',
		success : function(msg){
			if(msg == '退出成功')
				location.reload();
		},
		error : function(){
			toast('系统错误');
		}
	});
}
function closeAvgrund(){
	Avgrund.hide();
}
function tologin(){
	if(event.keyCode==13) 
		$("#login").click();
}
function toregister(){
	if(event.keyCode==13) 
		$("#register").click();
}
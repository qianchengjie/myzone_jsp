
//正则表达式验证用户名、密码格式是否正确
	var userNameReg =  /^[A-Za-z0-9_\u4e00-\u9fa5]{2,10}$/;
    var passwordReg = /^[a-zA-Z0-9]{6,16}$/;

//修改昵称
var userName = null;
var userNameStep = 1;
function modifyUserName(){

	if(userNameStep == 1){
		$("#info-userName+a").text("确认修改");
		userName = $("#info-userName").val();
		$("#info-userName").val("").removeAttr("disabled").focus();
		userNameStep = 2;
	}else{
		if ($("#info-userName").val()=="") {
			$("#info-userName").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>昵称不能为空！</b>").popover('show').focus();
		}else if(!userNameReg.test($("#info-userName").val())){
			$("#info-userName").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>2-10位、中英文、数字或下划线</b>").popover('show').select();
		}else if ($("#info-userName").val() == userName) {
			$("#info-userName").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>昵称未改变！</b>").popover('show').select();
		}else{
			//数据库修改昵称
			$.ajax({
					url : 'userinfo/checkUsername',
					method : 'post',
					data : {newUsername : $("#info-userName").val()},
					success : function(msg){
						if(msg == '用户名未改变'){
							$("#info-userName").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>与原昵称相同！</b>").popover("show");
							$("#info-userName").select();
						}else if(msg == '该用户名已存在'){
							$("#info-userName").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>该昵称已被使用！</b>").popover("show");
							$("#info-userName").select();
							alert($("#info-userName").val())
						}else{
							$.ajax({
								url : 'userinfo/modifyUsername',
								method : 'post',
								data : { newUsername : $("#info-userName").val() },
								dataType : 'json',
								success : function(msg){ 
									if(msg == "更名成功"){
										$("#info-userName").popover('hide');
										$("#info-userName+a").text("修改昵称");
										$("#info-userName").attr("disabled","disabled");
										$("#login-register .dropdown-toggle span").html($("#info-userName").val());
										userNameStep = 1;
									}
								},
								error : function(){
									toast('服务器错误');
								}
								
							})
						}
					}			
			})
		}
	}

}

//修改密码
var password = null;
var confirmPassword = null;
var passwordStep = 1;
function modifyPassword(){

	if(passwordStep == 1){
		$("#info-password+a").text("首次确定");
		password = $("#info-password").val();
		$("#info-password").val("").removeAttr("disabled").focus();
		passwordStep = 2;
	}else{
		if (passwordStep == 2) {
			if ($("#info-password").val()=="") {
				$("#info-password").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>密码不能为空！</b>").popover('show').focus();
			}else if(!passwordReg.test($("#info-password").val())){
				$("#info-password").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>6-16位、由字母或数字组成</b>").popover('show').select();
			}else if ($("#info-password").val() == password) {
				$("#info-password").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>密码未改变！</b>").popover('show').select();
			}else{
				confirmPassword = $("#info-password").val();
				$("#info-password+a").text("确认修改");
				$("#info-password").val("").focus();
				passwordStep = 3;
			}
		}else{
			if ($("#info-password").val()!=confirmPassword) {
				$("#info-password+a").text("首次确定");
				$("#info-password").val("").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>两次密码不同！</b>").popover('show').focus();
				passwordStep = 2;
			}else{
				//数据库修改密码
				$.post(
					'userinfo/checkPassword',
					{newPassword : $("#info-password").val()},
					function(msg){
						if(msg == '密码未改变'){
							$("#info-password").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>与原密码相同！</b>").popover("show");
							$("#info-password").select();
						}else{
							$.ajax({
								url : 'userinfo/modifyPassword',
								method : 'post',
								data : {newPassword : $("#info-password").val()},
								dataType : 'json',
								success : function(msg){
									if(msg == '密码未改变'){
										$("#info-password").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>与原密码相同！</b>").popover("show");
										$("#info-password").select();
									}else{
										$("#info-password").popover('hide');
										$("#info-password+a").text("修改密码");
										$("#info-password").attr("disabled","disabled");
										passwordStep = 1;
									}
								},
								error : function(){
									toast('服务器错误');
								}
							})
						}
					}			
				)

			}
		}
	}

}

$(document).ready(function(){
	$("#info-password").bind("input propertychange",function(){
		$("#info-password").popover('hide');
	})
	$("#info-userName").bind("input propertychange",function(){
		$.ajax({
				url : 'userinfo/checkUsername',
				method : 'post',
				data : {newUsername : $("#info-userName").val()},
				dataType : 'json',
				success : function(msg){
					if(msg == '用户名未改变'){
						$("#info-userName").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>与原昵称相同！</b>").popover("show");
					}else if(msg == '该用户名已存在'){
						$("#info-userName").attr("data-content","<b style='white-space:nowrap;color:red;font-size:13px'>该昵称已被使用！</b>").popover("show");
					}else{
						$("#info-userName").popover('hide');
					}
				},
				error : function(){
					toast('服务器错误');
				}
		});
	})
});

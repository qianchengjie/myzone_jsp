<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<title>用户信息</title>
	<c:import url="base.jsp"></c:import>
	<!-- Loading my css -->
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/userinfo.css">
	<!-- Loading my js -->
	<script src="${pageContext.request.contextPath}/static/js/userinfo.js"></script>
</head>
<body>

	<c:import url="topnav.jsp"></c:import>

	<div class="container">
		<div class="row">
			<div class="userinfo-header">
				<div class="col-md-6 userinfo-header-left">
					<h1>用户信息</h1>
				</div>
				<div class="col-md-6 userinfo-header-right">
					<p>你能在这里修改你的昵称、密码</p>
					<p>头像修改功能还在开发中</p>
				</div>
			</div>
			<div class="userinfo-body">
				<div class="control-group">
					<label class="control-label" for="info-userName">昵称</label>
					<input id="info-userName" class="control-input" disabled="disabled" type="text" value="${username}" data-toggle="popover" data-html="true" data-placement="top" maxlength="10">
					<a href="javascript:modifyUserName()" class="btn btn-primary" >修改昵称</a>
				</div>
				<div class="control-group error">
					<label class="control-label" for="info-password">密码</label>
					<input id="info-password" class="control-input" disabled="disabled" type="password" value="123456"  data-toggle="popover" data-html="true" data-placement="top" maxlength="16">
					<a href="javascript:modifyPassword()" class="btn btn-primary">修改密码</a>
				</div>
				<div class="control-group error">
					<label class="control-label" for="info-email">邮箱地址</label>
					<strong id="info-email">${user.email}</strong>
				</div>
				<div class="control-group error">
					<label class="control-label" for="info-regesited-date">注册时间</label>
					<strong id="info-regesited-date">${user.regDate}</strong>
				</div>
			</div>
		</div>
	</div>
</body>
</html>

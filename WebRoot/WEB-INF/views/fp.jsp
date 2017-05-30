<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<title>忘记密码</title>
	<meta name="baidu-site-verification" content="3TrUNal9aB">
	<c:import url="base.jsp"></c:import>
	<style type="text/css">
	.main-content .col-xs-12{
		display: table;
		width: 100%;
		height: 100vh;
	}
	.form-content{
		display: table-cell;
		vertical-align: middle;
	}
	.panel{
		max-width: 400px;
		margin: 0 auto;
	
	}
	.panel-body{
		padding: 40px 20px;
	}
	
	</style>
</head>
<body>
	
	<c:import url="topnav.jsp" />

	<div class="main">
		<div class="container">
			<div class="main-content">
				<div class="row">
					<div class="col-xs-12">
						<div class="form-content">
							<div class="panel panel-default">
								<div class="panel-heading">
									找回密码
								</div>
								<div class="panel-body">
								<c:choose>
								<c:when test="${msg == '邮箱不存在' }">
									<div class="alert alert-danger alert-dismissible" role="alert">
									  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
									  <strong>错误！</strong> 请检查您的邮箱是否输入无误！
									</div>
								</c:when>
								<c:when test="${msg == '邮件发送成功，请稍等' }">
									<div class="alert alert-success alert-dismissible" role="alert">
									  <p>${msg }</p>
									</div>
								</c:when>
								</c:choose>
								<c:if  test="${msg != '邮件发送成功，请稍等' }">
									<form action="forgetPassword" method="get">
										<div class="input-group">
											<input class="form-control" type="email" name="email" placeholder="请输入您的邮箱" required />
											 <span class="input-group-btn">
										        <button type="submit" class="btn btn-default glyphicon glyphicon-send" type="button"></button>
										      </span>
										</div>
									</form>
								</c:if>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
</body>
</html>

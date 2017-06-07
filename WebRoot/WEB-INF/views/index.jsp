<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<title>首页</title>
	<meta name="baidu-site-verification" content="3TrUNal9aB">
	<c:import url="base.jsp"></c:import>
	<!-- Loading my css -->
	<link rel="stylesheet" type="text/css" href="static/css/index.css">
	<!-- Loading my js -->
	<script src="static/js/onloadImgs.js"></script>
	<script src="static/js/index.js"></script>
</head>
<body>
	
	<c:import url="topnav.jsp"></c:import>
	
	<div class="loading-container">
		<h2></h2>
		<div class="progress progress-loadImgs">
			<div id="loadImg" class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100">
				<span>loading...</span>
			</div>
		</div>
	</div>
	
	<!-- cover -->
	<div class="cover-site">
		<div class="cover-site-inner">
			<div class="cover-container">
				<div class="inner cover">
					<h1 id="cover-heading" class="cover-heading">Hi</h1>
					<p id="text" class="lead">这是我第一次用bootstrap做的响应式网站，还有很多内容需要优化,有什么意见可以和我联系。</p>
					<p id="contactBtn" class="lead">
						<a id="contact" href="tencent://message/?uin=330616153" class="button button-glow button-rounded button-caution">Contact Me</a>
					</p>
				</div>
				<div class="cover-foot">
					<div class="inner">
						<!-- <p>Designed by<a href="#"> @钱程杰</a>.</p> -->
						<small style="color:#DDD;">备案号：<a href="http://www.miitbeian.gov.cn" style="color:#AAA;margin-top:-2em;">浙ICP备17012199号</a></small>
					</div>
				</div>
			</div>
		</div>
	</div>
<!-- Avgrund -->
<div id="contact-info" class="avgrund-info avgrund-popup">
	<p>QQ:330616153</p>
	<button class="btn btn-warning" onClick="closeAvgrund()">确定</button>
</div>

	<div id="toast">
		<p></p>
	</div>
</body>
</html>
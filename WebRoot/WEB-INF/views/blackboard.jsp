<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
	<title>留言板</title>
	<c:import url="base.jsp"></c:import>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/wangEditor.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/blackboard.css">
	<!-- 导入js -->
    <script src="${pageContext.request.contextPath}/static/js/wangEditor.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/plupload.full.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/zh_CN.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/qiniu.js"></script>
	<script src="${pageContext.request.contextPath}/static/js/blackboard.js"></script>
	
	<c:choose>
	<c:when test="${right == 0 }"><script src="${pageContext.request.contextPath}/static/js/blackboard/0.js"></script></c:when>
	<c:when test="${right == 2 }"><script src="${pageContext.request.contextPath}/static/js/blackboard/2.js"></script></c:when>
	<c:when test="${right == 3 }"><script src="${pageContext.request.contextPath}/static/js/blackboard/3.js"></script></c:when>
	</c:choose>
</head>
<body>
	<c:import url="topnav.jsp"></c:import>
	
	<!-- 主面板 -->
	<div class="main">
		<div class="container">
			<div class="main-container">
				<c:if test="${currentPage == 1 }">
				<!-- 主头部 -->
				<div class="main-head">
					<c:choose>
						<c:when test="${username != null}">
							<h3 class="animated fadeIn">既然来了，就说点什么吧！</h3>
							<div id="leave-message"></div>
							<div class="leave-message-foot">
								<button id="leave-message-btn" class="btn btn-primary">留言</button>
							</div>
						</c:when>
						<c:otherwise>
							<h3 class="animated bounce" style="text-align: center;">登录后才能留言哦！</h3>
						</c:otherwise>
					</c:choose>
				</div>
				</c:if>
				<div class="floor-content">
					<!-- 楼层 -->
				<c:forEach items="${floors}" var="floor">
					<div class="floor">
						<div class="row">
							<div class="col-xs-12">
								<!-- 留言 -->
								<div class="floor-container">
									<!-- 留言头部 -->
									<div class="floor-head">
										<img class="head-sculpture" src="${floor.imgSrc}" >
										<span class="name">${floor.username}</span>
										<!-- <small th:text="${floor.flNum}+'楼'">221楼</small> -->
										<c:if test="${right == 2 || (floor.username == username && right == 3)}">
										<div class="dropdown tools">
											<button class="btn btn-default dropdown-toggle btn-sm" data-toggle="dropdown">
												<span class="caret"></span>
											</button>
											<ul class="dropdown-menu">
												<li><input type="hidden" value="${floor.id}"><a href="javascript:void(0)" class="delete">删除</a></li>
											</ul>
										</div>
										</c:if>
									</div>
									<!-- 留言内容 -->
									<div class="floor-body">
										<p>${floor.content}</p>
									</div>
									<!-- 留言尾部 -->
									<div class="floor-foot">
										<input type="hidden" value="${floor.id}">
										<p>${floor.time}</p>
										<a class="btn-zan" value="${floor.id}"><span class="zan glyphicon glyphicon-thumbs-up"></span><span class="zan-count">${floor.zanCount}</span></a>
										<a class="btn-comment"><span class="comment glyphicon glyphicon-comment"></span><span class="reply-count">${floor.rpCount}</span></a>
									</div>
									<!-- 回复层 -->
									<div class="reply-container">
										<div class="loader-anim">
											<div class="loader-inner pacman"></div>
										</div> 
										<!-- 回复 -->
										<div class="replys"></div>
										<!-- 回复框 -->
										<c:choose>
										<c:when test="${ username ==null }">
										<div class="reply-frame"><h4>登录后才可回复哦！</h4></div>
										</c:when>
										<c:otherwise>
										<div class="reply-frame">
											<div class="reply-frame-head">回复</div>
											<!-- 回复文本域 -->
											<div class="reply-textarea">
											</div>
											<div class="reply-frame-foot">
												<a class="btn btn-primary btn-reply">回复</a>
												<input type="hidden" value="${floor.id}">
											</div>
										</div>
										</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:forEach>
				</div>
				<div id="reply-textarea-content">
					<div id="reply-textarea"></div>
				</div>
				<nav class="pagination-nav">
				  <ul class="pagination">
				  	<c:if test = "${currentPage != 1 }">
				    <li>
				      <a href = "blackboard?pageNum=${currentPage-1}">
				        <span >&laquo;</span>
				      </a>
				    </li>
				    </c:if>
				    
					<c:forEach begin="1"  end="${pageSum }" var="i">
				  	<c:choose>
				  	<c:when test = "${currentPage == i }">
					<li class='active'><a href='javascript:void(0)'>${i }</a></li>
				    </c:when>
				    <c:otherwise>
				    <li><a href='blackboard?pageNum=${i }'>${i }</a></li>
				    </c:otherwise>
				    </c:choose>
					</c:forEach>
					
				  	<c:if test = "${currentPage != pageSum }">
				    <li>
				      <a href = "blackboard?pageNum=${currentPage+1}">
				        <span>&raquo;</span>
				      </a>
				    </li>
				    </c:if>
				  </ul>
				</nav>
			</div>
		</div>
	</div>
	<div id="toast">
		<p></p>
	</div>
	<input id="thisisusername" type="hidden" value="${ username}">
</body>
</html>
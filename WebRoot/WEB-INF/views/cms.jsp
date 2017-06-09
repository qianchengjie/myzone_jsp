<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <title>后台管理中心</title>
  	<meta charset="utf-8">
  	<base href="${pageContext.request.contextPath}/" />
	<meta name="viewport" content="width=device-width, initial-scale=0.7,user-scalable=no">
	
	<!-- Loading jquery-->
	<script src="static/js/jquery.min.js"></script>
	
	<!-- Loading bootstrap-->
	<link rel="stylesheet" type="text/css" href="static/css/bootstrap.min.css">
	<script src="static/js/bootstrap.min.js"></script>
	
	<!-- Loading animated -->
	<link rel="stylesheet" type="text/css" href="static/css/animate.min.css">
	
	<!-- Shortcut Icon -->
	<link rel="Shortcut Icon" href="http://oq7avxrj8.bkt.clouddn.com/images/web.ico"/>
	
    <script src="static/js/plupload.full.min.js"></script>
    <script src="static/js/zh_CN.js"></script>
    <script src="static/js/qiniu.js"></script>
    
	  <script src="static/js/cms.js"></script>
	  <link rel="stylesheet" type="text/css" href="static/css/cms.css">
	  
	  
	  <c:if test="${right7 }">
	  <script src="static/js/cms/file7.js"></script>
	  </c:if>
	  <c:if test="${right8 }">
	  <script src="static/js/cms/file8.js"></script>
	  </c:if>
	  <c:if test="${right5 }">
	  <script src="static/js/cms/user5.js"></script>
	  </c:if>
	  <c:if test="${right6 }">
	  <script src="static/js/cms/user6.js"></script>
	  </c:if>
	  
</head>
<body>
      <!-- 头部 -->
      <header>
        <div class="top">
        </div>
        <!-- 页头 -->
        <div class="page-header text-center">
          <h1>后台管理中心</h1>
        </div>

      </header>
      <!-- 主面板 -->
      <div class="main">
        <div class="container">
          <!-- 主内容 -->
          <div class="main-content">
            <!-- 主内容头部 -->
            <div class="main-header">
              <!-- 导航 -->
              <nav>
                <ul class="nav nav-tabs">
            	  <c:if test="${right6 }">
                  <li class="active"><a href="javascript:void(0)">用户管理</a></li>
               	  </c:if>
            	  <c:if test="${right7 }">
                  <li><a href="javascript:void(0)">文件管理</a></li>
               	  </c:if>
                  <li><a href="javascript:void(0)">文档测试</a></li>
                  <li class="pull-right clearfix">
                    <!-- 搜索框 -->
                    <div id="search-input" class="input-group">
                      <input id="search" type="text" class="form-control" placeholder="搜索用户">
                      <span class="input-group-btn">
                        <button class="btn btn-default" type="button">  <span class="glyphicon glyphicon-search"></span>
                        </button>
                      </span>
                    </div>
                  </li>
                </ul>
              </nav>
            </div>
            <!-- 主内容主体 -->
            <div class="main-body">
              <c:if test="${right6 }">
              <!-- 用户表格 -->
              <table id="user-table" class="table main-table table-bordered">
                <thead>
                  <tr class="bg-primary">
                    <td>用户名</td>
                    <td>角色</td>
                    <td>邮箱</td>
                    <td>注册时间</td>
                    <td>操作</td>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${userList }" var="user">
                  <c:if test="${username != user.username }">
                  <tr>
                    <td>${user.username}</td>
                    <td>${user.rolename}</td>
                    <td>${user.email}</td>
                    <td>${user.regDate }</td>
                    <td>
                      <div class="btn-group">
                        <button class="btn btn-sm btn-danger delete-user">
                          	删除用户
                        </button>
                        <button class="btn btn-sm btn-info dropdown-toggle" data-toggle="dropdown">
                         	<span>${user.rolename} </span>
                          	<span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                          <li><a href="javascript:void(0)">超级管理员</a></li>
                          <li><a href="javascript:void(0)">管理员</a></li>
                          <li><a href="javascript:void(0)">普通用户</a></li>
                        </ul>
                      </div>
                    </td>
                  </tr>
                  </c:if>
                  </c:forEach>
                </tbody>
              </table>
              </c:if>
              
              
            <c:if test="${right7 }">
              <div id="file">
	              <ol id="path" class="breadcrumb">
				  </ol>
				  <div>
				  	<ul id="file-list">
				  	</ul>
				  </div>
              </div>
              </c:if>
              
              <input id="hiddene" type="hidden"/>
              <div id="data" class="container-fluid">
                <div class="row">
                  <div class="col-xs-3">
                    <div id="get-excel">
                      <a href="data/getUserExcel" target="_blank">
                        <img src="static/svg/excel.svg"><br />
                      	  获取用户Excel
                      </a>
                    </div>
                  </div>
                  <div class="col-xs-3">
                    <div id="get-pdf">
                      <a href="data/getUserPdf" target="_blank">
                        <img src="static/svg/pdf.svg"><br />
                     	   获取用户及日志PDF
                      </a>
                    </div>
                  </div>
                  <div class="col-xs-3">
                    <div id="upload-excel">
                      <a href="data" target="_blank">
                        <img src="static/svg/excel.svg"><br />
                        	上传用户Excel
                      </a>
                    </div>
                  </div>
                  <div class="col-xs-3">
                      <div id="get-word">
                        <a href="data/getWord" target="_blank">
                          <img src="static/svg/word.svg"><br />
                        	获得WORD模板
                        </a>
                      </div>
                  </div>
                </div>
              </div>
            </div>
            <!-- 主面板尾部 -->
            <div class="main-content-footer">
            	<div id="user-tools">
            	<c:if test="${right5 }">
            		<span class="tooltips glyphicon glyphicon-cog" data-toggle="tooltip" data-placement="top" title="设置角色权限" data-tigger="hover"></span>
				</c:if>
            	</div>
            	<c:if test="${right7 }">
            	<div id="file-tools">
            		<span id="mkdir-tool" class="tooltips" data-toggle="tooltip" data-placement="top" title="新建文件夹" data-tigger="hover">
            			<img src="static/svg/newfolder.svg" />
            		</span>
            		<span id="upload-tool" class="tooltips" data-toggle="tooltip" data-placement="top" title="上传文件" data-tigger="hover">
            			<img src="static/svg/upload.svg" />
            		</span>
            		<span id="download-tool" class="tooltips" data-toggle="tooltip" data-placement="top" title="下载文件" data-tigger="hover">
            			<img src="static/svg/download.svg" />
            		</span>
            		<span id="select-tool" class="tooltips" data-toggle="tooltip" data-placement="top" title="全选" data-tigger="hover">
            			<img src="static/svg/selectNone.svg" />
            		</span>
            		<span id="selectFan-tool" class="tooltips" data-toggle="tooltip" data-placement="top" title="反选" data-tigger="hover">
            			<img src="static/svg/selectFan.svg" />
            		</span>
            		<c:if test="${right8 }">
            		<span id="delete-tool" class="tooltips" data-toggle="tooltip" data-placement="top" title="删除选择项" data-tigger="hover">
            			<img src="static/svg/delete.svg" />
            		</span>
            		</c:if>
            	</div>
            	</c:if>
                  <nav>
            	<c:if test="${right6 }">
                    <ul class="pagination">
                </c:if>
            	<c:if test="${!right6 }">
                    <ul class="pagination" style="visibility: hidden">
                </c:if>
                      <li>
                        <a href="javascript:void(0)" aria-label="Previous">
                          <span aria-hidden="true">&laquo;</span>
                        </a>
                      </li>
                      <li><a href="javascript:void(0)">1</a></li>
                      <li><a href="javascript:void(0)">2</a></li>
                      <li><a href="javascript:void(0)">3</a></li>
                      <li><a href="javascript:void(0)">4</a></li>
                      <li><a href="javascript:void(0)">5</a></li>
                      <li>
                        <a href="javascript:void(0)" aria-label="Next">
                          <span aria-hidden="true">&raquo;</span>
                        </a>
                      </li>
                    </ul>
                  </nav>
            </div>
          </div>
        </div>
      </div>

      <div id="toast">
        <p></p>
      </div>

      <div id="confirm">
        <div id="mask"></div>
        <div id="confirm-content">
          <p>确认删除该用户？</p>
          <ul>
            <li>确认</li>
            <li>取消</li>
          </ul>
        </div>
      </div>
      
      <div id="delete-confirm">
        <div id="mask"></div>
        <div id="confirm-content">
          <p>确认删除文件：<span></span></p>
          <input type="hidden">
          <ul>
            <li>确认</li>
            <li>取消</li>
          </ul>
        </div>
      </div>
      
      <div id="select-delete-confirm">
        <div id="mask"></div>
        <div id="confirm-content">
          <p></p>
          <ul>
            <li>确认</li>
            <li>取消</li>
          </ul>
        </div>
      </div>
      
       <div id="mkdir-confirm">
        <div class="mask"></div>
        <div class="confirm-content">
          <input type="text" placeholder="输入文件夹名">
          <ul>
            <li>新建</li>
            <li>取消</li>
          </ul>
        </div>
      </div>
      
      <div id="rename-confirm">
        <div class="mask"></div>
        <div class="confirm-content">
          <input type="text" placeholder="输入新的文件名">
          <input type="hidden">
          <ul>
            <li>重命名</li>
            <li>取消</li>
          </ul>
        </div>
      </div>
      
      <div id="file-info">
      	<ul>
      		<li>名称：<span>1</span></li>
      		<li>类型：<span>2</span></li>
      		<li>大小：<span>3</span></li>
      		<li>路径：<span>4</span></li>
      		<li>最后修改日期：<span>5</span></li>
      	</ul>
      </div>
      
      <div id="folder-info">
      		<li><span></span></li>
      </div>
      
      <div id="file-menu">
      	<ul>
      		<li>查看</li>
	      	<li>下载</li>
            <c:if test="${right8 }">
	      	<li>删除</li>
	      	</c:if>
	      	<li>重命名</li>
      	</ul>
      	<input type="hidden" />
      </div>
      
      <div id="folder-menu">
      	<ul>
      		<li>打开</li>
            <c:if test="${right8 }">
	      	<li>删除</li>
	      	</c:if>
	      	<li>重命名</li>
      	</ul>
      	<input type="hidden" />
      </div>

      <div id="role-right-setting" class="modal fade" tabindex="-1">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <button class="close" data-dismiss="modal">&times;</button>
              <div class="modal-title">角色权限设置</div>
            </div>
            
            <div class="modal-body">
	            <div id="role-list" class="btn-group" data-toggle="buttons">
				  <label class="btn btn-primary active">
				    <input type="radio" name="role" autocomplete="off" checked> 超级管理员
				  </label>
				  <label class="btn btn-primary">
				    <input type="radio" name="role" autocomplete="off"> 管理员
				  </label>
				  <label class="btn btn-primary">
				    <input type="radio" name="role" autocomplete="off"> 普通用户
				  </label>
				</div>
				<div id="role-right-panel">
					<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
					<c:forEach items="${rights }" var="right" varStatus="i">
					  <div class="panel panel-default">
					    <div class="panel-heading" role="tab" id="heading${right.pRight[0].id }">
							<input type="checkbox" class="pull-left"/>
					      <h4 class="panel-title">
					        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse${right.pRight[0].id }" aria-expanded="false" aria-controls="collapse${right.pRight[0].id }">
							 ${right.pRight[0].rname }
					        </a>
					      </h4>
					    </div>
					    
					    <div id="collapse${right.pRight[0].id }" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${right.pRight[0].id }">
					      <div class="panel-body">
					      	<c:forEach items="${right.cRights }" var="rightChild" varStatus="j">
							<div class="checkbox">
								<label  class="tooltips" data-toggle="tooltip" data-placement="right" title="${rightChild.description }" data-tigger="hover" >
									<input class="child-right" type="checkbox" name="rightId${rightChild.id }" value="${rightChild.id }"> ${rightChild.rname }
								</label>
							</div>
							</c:forEach>
					    </div>
					    </div>
					  </div>
					  </c:forEach>
					</div>
				</div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-default" data-dismiss="modal">关闭</button>
              <button class="btn btn-primary">应用</button data-dismiss="modal">
			</div>
          </div>
        </div>
      </div>
      <input type="hidden" id="currentUsername" value="${username }">
    </body>
    </html>

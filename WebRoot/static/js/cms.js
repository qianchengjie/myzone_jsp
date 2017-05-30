 $(document).ready(function() {
	 
	 //界面选择
	 $('.main-header .nav li').on('click',function(){
		 var name = $(this).text().trim();
		 $('.main-header .nav li').removeClass('active');
		 $(this).addClass('active');
		 switch(name){
		 case '用户管理':
			 $('#user-table').fadeIn();
			 $('.main-content-footer').fadeIn();
			 $('#search-input').show();
			 $('#file').hide();
			 $('#data').hide();
			 $('#user-tools').show();
			 $('#file-tools').hide();
			 $('.main-content-footer nav').css('visibility','');
			 break;
		 case '文件管理':
			 $('#file').fadeIn();
			 $('#user-table').hide();
			 $('#search-input').hide();
			 $('#data').hide();
			 $('#file-tools').fadeIn();
			 $('#user-tools').hide();
			 $('.main-content-footer').fadeIn();
			 $('.main-content-footer nav').css('visibility','hidden');
			 break;
		 case '文档测试':
			 $('#data').fadeIn();
			 $('#user-table').hide();
			 $('.main-content-footer').hide();
			 $('#file').hide();
			 $('#search-input').hide();
			 $('#file-tools').hide();
			 $('#user-tools').hide();
			 break;
		 }
		 
	 })
     $('.main-header').find('.nav li:first-child').click();
   
   
   
   setUserPagination(1);
	$(".tooltips").tooltip();
 })

 
 
 //Toast
 var toastHideTimer;
 function toast(msg) {
   clearTimeout(toastHideTimer);
   $('#toast p').text(msg);
   $('#toast').fadeIn(400);
   toastHideTimer = setTimeout(function() {
     $('#toast').fadeOut(700);
   }, 2000)
 }
 
 //Confirm
 function confirmShow(){
	 $("#confirm").fadeIn(100);
 }
 function confirmHide(){
	 $("#confirm").fadeOut(100);
 }
 
//设置用户界面页码
function setUserPagination(currentPage){
	 var condition = $("#search").val();
	$.ajax({
		url : 'cms/getUserPageSum',
		method : 'post',
		data : {condition : condition},
		dataType : 'json',
		success : function(pageSum){
			setPagination(currentPage,pageSum);
		},
		 error : function(){
			 toast('服务器错误');
		 }
	})
}
 
//重新设置页数
function setPagination(currentPage,pageSum){
	//当此页无留言时
	if(pageSum < currentPage){
		gotoUserPage(pageSum);
	}
	
	var html = "";
	if(currentPage != 1){
		html += "<li><a href='javascript:gotoUserPage(" + ( currentPage-1 )+ ")'><span>&laquo;</span></a></li>";
	}
	for( var i = 1; i <= pageSum; i++){
		if( i == currentPage)
			html += "<li class='active'><a href='#'>"+i+"</a></li>";
		else
			html += "<li><a href='javascript:gotoUserPage(" + i + ")'>"+i+"</a></li>";
	}
	if(pageSum != currentPage){
		html += "<li><a href='javascript:gotoUserPage(" + ( currentPage+1 )+ ")'><span >&raquo;</span></a></li>";
	}
	$('.pagination').html(html);
}
//跳转到用户页码
function gotoUserPage(pageNum){
	 var condition = $("#search").val();
	$.ajax({
		url : 'cms/getUserPage',
		method : 'post',
		data : {pageNum : pageNum , condition : condition},
		dataType : 'json',
		success : function(json){
			var html = '';
			 $('#user-table tbody').html('');
			for(var i = 0; i < json.length ; i++){
				var user = json[i];
				html += "<tr><td>" + user.username + "</td><td>" + user.rolename + "</td><td>" + user.email + "</td><td>" + user.regDate + "</td><td><div class='btn-group'><button class='btn btn-sm btn-danger delete-user'> 删除用户</button><button class='btn btn-sm btn-info dropdown-toggle' data-toggle='dropdown'><span>" + user.rolename + " </span><span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#'>超级管理员</a></li><li><a href='#'>管理员</a></li><li><a href='#'>普通用户</a></li></ul></div></td></tr>";
			}
			$('#user-table tbody').append(html);
		},
		 error : function(){
			 toast('服务器错误');
		 }
	})
	setUserPagination(pageNum)
}
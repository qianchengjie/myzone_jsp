 $(document).ready(function() {
 
	 //搜索用户
	 $('#search-input').on('click', 'span', function(){
		 gotoUserPage(1);
	 });
	 document.onkeydown = function(e){
		 if(e.which  == 13 && $('#search').is(':focus'))
			 gotoUserPage(1);
	 }
	 
	 //用户界面设置角色
   $('#user-table').on('click', '.dropdown-menu a', function() {
	 var obj = $(this);
     var rolename = $(this).text();
     var username = $(this).parents('tr').find('td:eq(0)').text();
     $.ajax({
    	 url : 'cms/changeRole',
    	 method : 'post',
    	 data : {username : username, rolename : rolename},
    	 dataType : 'json',
    	 success : function(msg){
    		 if(msg == '设置成功'){
    			 obj.parents('.dropdown-menu').siblings('.dropdown-toggle').html(rolename + " <span class='caret'></span>");
    			 obj.parents('tr').find('td:eq(1)').text(rolename);
    		 }else{
    			 msg == '设置失败';
    		 }
    		 toast(msg)
    	 },
		 error : function(){
			 toast('服务器错误');
		 }
     })
   })
   
   //删除角色
   $('#user-table').on('click', '.delete-user', function(){
	   var obj = $(this).parents('tr');
	   var username = $(this).parents('tr').find('td:eq(0)').text();
	   $("#confirm").find('p').html("删除角色："+"<span>" + username + "</span>");
	   confirmShow();
   })

   $('#mask').click(function() {
    confirmHide();
   })
   $('#confirm ul li:last-child').click(function() {
    confirmHide();
   })
   //确认删除角色
   $('#confirm ul li:first-child').click(function() {
	 var username = $("#confirm").find('span').text();
	 var currentPage = $('.pagination').find('.active').text();
	 var condition = $("#search").val();
	 $.ajax({
		 url : 'cms/deleteUser',
		 method : 'post',
		 async : false,
		 data : {username : username, currentPage : currentPage, condition : condition},
		dataType : 'json',
		 success : function(json_obj){
			 var tds = $('#user-table').find('td:contains('+username+')');
			 var obj = null;
			 for(var i = 0; i < tds.length; i++){
			 	if (tds.text() == username) {
			 		obj = $(tds[i]).parents('tr');
			 	}
			 }
			 if(json_obj.msg == '删除成功，仅剩一页' || json_obj.msg == '删除成功，此页为尾页'){
				 obj.addClass("animated bounceOutLeft");
				 setTimeout(function(){
					 obj.remove();
				 },500)
				 toast('删除成功');
			 }
			 else if(json_obj.msg == '删除成功'){
				 obj.addClass("animated bounceOutLeft");
				 setTimeout(function(){
		   			if(currentUsername!=json_obj.username)
					 var html = "<tr><td>" + json_obj.user.username + "</td><td>" + json_obj.rolename + "</td><td>" + json_obj.user.email + "</td><td>" + json_obj.user.regDate + "</td><td><div class='btn-group'><button class='btn btn-sm btn-danger delete-user'> 删除用户</button><button class='btn btn-sm btn-info dropdown-toggle' data-toggle='dropdown'><span>" + json_obj.rolename + "</span><span class='caret'></span></button><ul class='dropdown-menu'><li><a href='#'>超级管理员</a></li><li><a href='#'>管理员</a></li><li><a href='#'>普通用户</a></li></ul></div></td></tr>";
					 $('#user-table tbody').append(html);
					 obj.remove();
				 },500)
			 }else{
				 toast('删除失败');
			 }
		 },
		 error : function(){
			 toast('服务器错误');
		 }
	 })
	 setUserPagination(currentPage);
    confirmHide();
   })
   
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
	   var currentUsername = $('#currentUsername').val();
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
   				if(currentUsername!=user.username)
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
   
})
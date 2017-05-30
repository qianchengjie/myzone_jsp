$(document).ready(function() {
	
	
	//删除留言
	$('.main').on('click','.delete',function(){
		var floorId = $(this).siblings('input').val();
		var floor_obj = $(this).parents('.floor');
		var currentPage = $('.pagination').find('li.active').index();
		if(currentPage == 0)
			currentPage = 1;
		$.ajax({
			url : 'blackboard/deleteMessage',
			method : 'post',
			data : {floorId : floorId, currentPage : currentPage},
			success : function(data){
				setPagination(currentPage);
				var json = JSON.parse(data)
				if (json.msg == '删除成功，仅剩一页')
					toast('删除成功')
				else if (json.msg == '删除成功') {
					toast(json.msg)
				}
				else
					toast('删除失败')
				floor_obj.removeClass('bounceInRight').addClass('animated bounceOutLeft');
				setTimeout(function(){
					floor_obj.remove();
					if (json.msg == '删除成功'){
						var html = "<div class='floor'><div class='row'><div class='col-xs-12'><div class='floor-container'><div class='floor-head'><img class='head-sculpture' src='" + json.floor.imgSrc + "'><span class='name'>" + json.floor.username + "</span><div class='dropdown tools'><button class='btn btn-default dropdown-toggle btn-sm' data-toggle='dropdown'><span class='caret'></span></button><ul class='dropdown-menu'><li><input type='hidden' value='" + json.floor.id + "'><a href='javascript:void(0)' class='delete'>删除</a></li></ul></div></div><div class='floor-body'><p>" + json.floor.content + "</p></div><div class='floor-foot'><input type='hidden' value='" + json.floor.id + "'><p>" + json.floor.time + "</p><a class='btn-zan' value='" + json.floor.id + "'><span class='zan glyphicon glyphicon-thumbs-up'></span><span class='zan-count'>0</span></a><a class='btn-comment'><span class='comment glyphicon glyphicon-comment'></span><span class='reply-count'>0</span></a></div><div class='reply-container'><div class='replys'></div><div class='loader-anim'><div class='loader-inner pacman'><div></div><div></div><div></div><div></div><div></div></div></div><div class='reply-frame'><div class='reply-frame-head'>回复</div><div class='reply-textarea'></div><div class='reply-frame-foot'><a class='btn btn-default btn-reply'>回复</a><input type='hidden' value='" + json.floor.id + "'></div></div></div></div></div></div></div>";
						$(".floor-content").append(html)
					}
				},500)
			}
		})
	})
	//留言
	$('#leave-message-btn').click(function() {
		var content = $('#leave-message').html();
		var username = $('#thisisusername').val();
		if (content == '<p><br></p>')
			toast('请输入留言内容');
		else
			$.ajax({
				url: 'blackboard/leaveMessage',
				method: 'post',
				data: {
					username: username,
					content: content
				},
				dataType: 'json',
				success: function(json) {
					setPagination(1)
					if (json.msg == '留言成功') {
						$('#leave-message').html('<p><br></p>');
						var html = "<div class='floor animated bounceInRight'><div class='row'><div class='col-xs-12'><div class='floor-container'><div class='floor-head'><img class='head-sculpture' src='" + json.floor.imgSrc + "'><span class='name'>" + json.floor.username + "</span><div class='dropdown tools'><button class='btn btn-default dropdown-toggle btn-sm' data-toggle='dropdown'><span class='caret'></span></button><ul class='dropdown-menu'><li><input type='hidden' value='" + json.floor.id + "'><a href='javascript:void(0)' class='delete'>删除</a></li></ul></div></div><div class='floor-body'><p>" + json.floor.content + "</p></div><div class='floor-foot'><input type='hidden' value='" + json.floor.id + "'><p>" + json.floor.time + "</p><a class='btn-zan' value='" + json.floor.id + "'><span class='zan glyphicon glyphicon-thumbs-up'></span><span class='zan-count'>0</span></a><a class='btn-comment'><span class='comment glyphicon glyphicon-comment'></span><span class='reply-count'>0</span></a></div><div class='reply-container'><div class='replys'></div><div class='loader-anim'><div class='loader-inner pacman'><div></div><div></div><div></div><div></div><div></div></div></div><div class='reply-frame'><div class='reply-frame-head'>回复</div><div class='reply-textarea'></div><div class='reply-frame-foot'><a class='btn btn-default btn-reply'>回复</a><input type='hidden' value='" + json.floor.id + "'></div></div></div></div></div></div></div>";
						$(".floor-content").prepend(html)
						setTimeout(function() {
							$('.main').find('.floor').removeClass('animated')
						}, 1000)
						if($('.floor-content').find('.floor').length > 6)
							$('.floor-content .floor:last-child').remove();
					}
				},
				error: function(){
					toast('服务器错误');
				}
			})
	})
	
})

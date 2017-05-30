
$(document).ready(function(){
	
	//多项删除
	$('#file-tools').on('click', '#delete-tool', function(){
		$('#select-delete-confirm').find('p').text('确认删除' + $('#file-list').find('input[type=checkbox]:checked').length + "项文件")
		selectdeleteshow();
	})
	$('#select-delete-confirm').on('click', 'ul li:first-child', function(){
		var selects = $('#file-list').find('input[type=checkbox]:checked').parent();
		for( var i = 0; i < selects.length; i++){
			if($(selects[i]).hasClass('file'))
				deleteFile(getNowPath()+$(selects[i]).find('span').text())
			else
				deleteFolder(getNowPath()+$(selects[i]).find('span').text())
		}
		selectdeletehide();
	})
	$('#select-delete-confirm').on('click', '.mask', function(){
		selectdeletehide();
	})
	$('#select-delete-confirm').on('click', 'ul li:last-child', function(){
		selectdeletehide();
	})
	
	$('#file-menu').on('click', 'li', function(){
		var index = $(this).text();
		switch(index){
		case '删除':
			deleteshow();
			$('#delete-confirm').find('input[type="hidden"]').val(getNowPath() + $('#file-menu').find('input').val())
			$('#delete-confirm').find('p').html("确认删除文件<br /><span>" + $('#file-menu').find('input').val() + "</span>");
			break;
		}
	})
	
	//删除
	$('#delete-confirm').on('click', 'ul li:first-child', function(){
		if($('#delete-confirm').find('p').text().indexOf('文件夹') == -1)
			deleteFile( $('#delete-confirm').find('input[type="hidden"]').val())
		else
			deleteFolder( $('#delete-confirm').find('input[type="hidden"]').val() +  $('#delete-confirm').find('p span').text())
	})
	$('#delete-confirm').on('click', '.mask', function(){
		deletehide();
	})
	$('#delete-confirm').on('click', 'ul li:last-child', function(){
		deletehide();
	})
	
	$('#files').on('click','.delete',function(){
		deleteFile(this);
	})
	
	
	$('#folder-menu').on('click', 'li', function(){
		var index = $(this).text();
		switch(index){
		case '删除':
			$('#file-info').hide()
			deleteshow();
			$('#delete-confirm').find('input[type="hidden"]').val(getNowPath() + $('#file-menu').find('input').val())
			$('#delete-confirm').find('p').html("确认删除文件夹<br /><span>" + $('#folder-menu').find('input').val() + "</span>");
			break;
		}
	})
})
	
	
 	//删除文件
    function deleteFile(key){
	   	$.ajax({
	   		url : 'file/deleteFile',
	   		method : 'post',
	   		data : {key : key},
	   		dataType : 'json',
	   		success : function(msg){
	   			deletehide();
	   			getFileList(getNowPath().substring(0,getNowPath().length-1));
	   			clearTimeout(infoTimer)
	   			toast(msg)
	   		}
	   	})
    }
    //删除文件夹
    function deleteFolder(path){
	   	$.ajax({
	   		url : 'file/deleteFolder',
	   		method : 'post',
	   		data : {path : path},
	   		dataType : 'json',
	   		success : function(msg){
	   			deletehide();
	   			getFileList(getNowPath().substring(0,getNowPath().length-1));
	   			toast(msg)
	   		}
	   	})
    }


   
	//confirm
	function selectdeletehide(){
		$('#select-delete-confirm').fadeOut(200);
	}
	function selectdeleteshow(){
		$('#select-delete-confirm').fadeIn(200);
	}
	function deletehide(){
		$('#delete-confirm').fadeOut(200);
	}
	function deleteshow(){
		$('#delete-confirm').fadeIn(200);
	}
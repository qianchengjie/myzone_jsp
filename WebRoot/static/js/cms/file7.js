	var pe = false;
	//判断是否是移动端
	$(function(){
			var userAgentInfo = navigator.userAgent;
		    var Agents = ["Android", "iPhone",
		                "SymbianOS", "Windows Phone",
		                "iPad", "iPod"];
		    for (var v = 0; v < Agents.length; v++) {
		        if (userAgentInfo.indexOf(Agents[v]) > 0) {
		        	pe = true;
		        	break;
	        }
	    }
    })


	//移动
	//允许拖动
	function allowDrop(e){
		e.preventDefault();
	}
	//开始移动
	function drag(e){
		e.dataTransfer.setData("fromKey", getNowPath()+$(e.target).find('span').text());
		e.dataTransfer.setData("isFile", $(e.target).hasClass('file'));
	}
	//放置对象
	function drop(e){
		e.preventDefault();
		var fromKey = e.dataTransfer.getData("fromKey");
		
		var toKey = getNowPath()+$(e.target).parent().find('span').text();
		if($(e.target).parent().hasClass('file')){
			return false;
		}
		else if(e.dataTransfer.getData("isFile") == 'true'){
			removeFile(fromKey, toKey);
		}else{
			removeFolder(fromKey, toKey);
		}
	}
	//移动文件
	function removeFile(fromKey, toKey){
		$.ajax({
			url : 'file/removeFile',
			method : 'post',
			data : { fromKey : fromKey , toKey : toKey },
			dataType : 'json',
			success : function(msg){
				refreshFolder();
				toast(msg);
			},
			error : function(){
				toast("服务器错误");
			}
		})
	}
	//移动文件夹
	function removeFolder(fromKey, toKey){
		$.ajax({
			url : 'file/removeFolder',
			method : 'post',
			data : { fromKey : fromKey , toKey : toKey },
			dataType : 'json',
			success : function(msg){
				refreshFolder();
				toast(msg);
			},
			error : function(){
				toast("服务器错误");
			}
		})
	}


$(document).ready(function(){
	getFileList("root");
	setNowPath("root");
	$('#files').on('click','span',function(){
		choose(this);
	})
	//文件夹双击事件
	$('#file-list').on('dblclick', 'li.folder', function(){
		var name = $(this).find('span').text();
		getFileList(getNowPath() + name);
		setNowPath(getNowPath() + name);
		clearTimeout(infoFolderTimer);
		$('#folder-info').hide();
	})
	//文件夹单击事件
	$('#file-list').on('click', 'li.folder', function(){
		$('#file-list').find('input[type=checkbox]').show();
		var checkbox = $(this).find('input[type=checkbox]');
		$(checkbox).click();
	})
	//文件点击事件
	$('#file-list').on('click', 'li.file', function(){
		$('#file-list').find('input[type=checkbox]').show();
		var checkbox = $(this).find('input[type=checkbox]');
		$(checkbox).click();
	})
	
	//全选
	$('#file-tools').on('click', '#select-tool', function(){
		$('#file-list').find('input[type=checkbox]').show();
		if($(this).find('img').attr('src') == 'static/svg/selectNone.svg'){
			$('#file-list').find('input[type=checkbox]:not(:checked)').click();
			$(this).attr('data-original-title','取消全选');
			$(this).find('img').attr('src','static/svg/selectAll.svg');
		}
		else{
			$('#file-list').find('input[type=checkbox]:checked').click();
			$(this).find('img').attr('src','static/svg/selectNone.svg');
			$(this).attr('data-original-title','全选');
		}
	})
	//反选
	$('#file-tools').on('click', '#selectFan-tool', function(){
		$('#file-list').find('input[type=checkbox]').click();
	})
	$('#file-list').on('click', 'input[type=checkbox]', function(){
		$(this).click();
	})
	
	
	
	$('#download-tool').on('click',function(){
		var selects = $('#file-list').find('input[type=checkbox]:checked').parent();
		for( var i = 0; i < selects.length; i++){
			if($(selects[i]).hasClass('file'))
				download($(selects[i]).find('span').text());
			else
				toast('暂不支持下载文件夹内容');
		}
	})
	
	//新建文件夹
	$('#file-tools').on('click', '#mkdir-tool', function(){
		$('#mkdir-confirm').find('input').val('');
		mkshow();
		$('#mkdir-confirm').find('input').focus();
	})
	$('#mkdir-confirm').on('click', 'ul li:first-child', function(){
		mkDir( getNowPath() + $('#mkdir-confirm').find('input').val()+"/")
	})
	$('#mkdir-confirm').on('click', '.mask', function(){
		mkhide();
	})
	$('#mkdir-confirm').on('click', 'ul li:last-child', function(){
		mkhide();
	})
	
	//路径点击事件
	$('#path').on('click', 'li:not(.active)', function(){
		var index = $(this).index()+1;
		var as = $('#path').find('li a');
		var path = "";
		for(var i = 0; i < index; i++){
			var name = $(as[i]).text();
			path += name + "/";
		}
		path = path.substring(0,path.length-1);
		getFileList(path);
		setNowPath(path);
	})
	//显示信息
	var infoTimer;
	$('#file').on('mouseenter','.file' ,function(){
		var name = $(this).find('span').text();
		clearTimeout(infoTimer);
		clearTimeout(infoFolderTimer);
		var y = $(this).offset().top - $(window).scrollTop();
		var x = $(this).offset().left - $(window).scrollLeft();
		
		if(x + 300 > $(window).width())
			x -= 300;
		infoTimer = setTimeout(function(){
			getFileInfo(getNowPath()+name);
			$('#file-info').css({
				top :  y+60,
				left :  x+60
			}).delay(100).fadeIn(300);
		},500)
	})
	$('#file').on('mouseleave','.file' ,function(e){
		clearTimeout(infoTimer);
		$('#file-info').hide();
	})
	var infoFolderTimer;
	$('#file').on('mouseenter','.folder' ,function(){
		var name = $(this).find('span').text();
		clearTimeout(infoTimer);
		clearTimeout(infoFolderTimer);
		var y = $(this).offset().top - $(window).scrollTop();
		var x = $(this).offset().left - $(window).scrollLeft();
		
		if(x + 300 > $(window).width())
			x -= 300;
		infoFolderTimer = setTimeout(function(){
			$('#folder-info').find('li span').text(name)
			$('#folder-info').css({
				top :  y+60,
				left :  x+60
			}).delay(100).fadeIn(200);
		},300)
	})
	$('#file').on('mouseleave','.folder' ,function(e){
		clearTimeout(infoFolderTimer);
		$('#folder-info').hide();
	})
	
	//文件右键
	$('#file').on('contextmenu', '.file',function(e){
		$('#folder-menu').hide();
		e.preventDefault();
		clearTimeout(infoTimer);
		$('#file-info').hide();
		var y = e.pageY - $(window).scrollTop();
		var x = e.pageX - $(window).scrollLeft();
		$('#file-menu').css({
			top : y,
			left : x
		}).fadeIn(100);
		$('#file-menu').find('input').val($(this).find('span').text());
	});
	$('#file-menu').on('click', 'li', function(){
		var index = $(this).text();
		switch(index){
		case '查看':
			window.open('http://opw7cwrmm.bkt.clouddn.com/' + getNowPath() + $('#file-menu').find('input').val())
			break;
		case '下载':
			download($('#file-menu').find('input').val());
			break;
		case '重命名':
			$('#rename-confirm').find('input[type="text"]').val('');
			renameshow();
			$('#rename-confirm').find('input[type="hidden"]').val(getNowPath() + $('#file-menu').find('input').val())
			$('#rename-confirm').find('input[type="text"]').val($('#file-menu').find('input').val()).attr('placeholder','请输入文件名');
			break;
		}
	})
	
	//重命名
	$('#rename-confirm').on('click', 'ul li:first-child', function(){
		if($('#rename-confirm').find('input[type="text"]').attr('placeholder') == '请输入文件名')
			renameFile( $('#rename-confirm').find('input[type="hidden"]').val() ,getNowPath() + $('#rename-confirm').find('input[type="text"]').val() )
		else
			renameFolder( $('#rename-confirm').find('input[type="hidden"]').val(), getNowPath() + $('#rename-confirm').find('input[type="text"]').val() )
	})
	$('#rename-confirm').on('click', '.mask', function(){
		renamehide();
	})
	$('#rename-confirm').on('click', 'ul li:last-child', function(){
		renamehide();
	})
	
	
	
	$('#folders').on('click','span',function(){
		choose(this);
	})
	$('#files').on('click','.download',function(){
		download(this);
	})
	
	//文件夹右键
	$('#file').on('contextmenu', '.folder',function(e){
		$('#file-menu').hide();
		e.preventDefault();
		var y = e.pageY - $(window).scrollTop();
		var x = e.pageX - $(window).scrollLeft();
		$('#folder-menu').css({
			top : y,
			left : x
		}).fadeIn(100);
		$('#folder-menu').find('input').val($(this).find('span').text());
	});
	$('#folder-menu').on('click', 'li', function(){
		var index = $(this).text();
		switch(index){
		case '重命名':
			renameshow();
			$('#rename-confirm').find('input[type="hidden"]').val(getNowPath() + $('#folder-menu').find('input').val())
			$('#rename-confirm').find('input[type="text"]').val($('#folder-menu').find('input').val()).attr('placeholder','请输入文件夹名');
			break;
		case '打开':
			$('#file-list').find("span:contains(" +$('#folder-menu').find('input').val()+ ")").dblclick();
			break;
		}
	})
	
	$(window).on('scroll',function(){
		$('#folder-menu').hide();
		$('#file-menu').hide();
	})
	$(window).on('click' ,function(){
		$('#folder-menu').hide();
		$('#file-menu').hide();
	})
	
	
	//实例化一个plupload上传对象
    var uploader = new plupload.Uploader({
    	drop_element : 'file',
    	container : 'file',
        browse_button : 'upload-tool', //触发文件选择对话框的按钮，为那个元素id
        url : '/myzone_jsp/file/doUpload', //服务器端的上传页面地址
        flash_swf_url : 'Moxie.swf', //swf文件，当需要使用swf方式进行上传时需要配置该参数
        silverlight_xap_url : 'Moxie.xap', //silverlight文件，当需要使用silverlight方式进行上传时需要配置该参数
		chunk_size: '4mb', // 分块上传时，每块的体积
        init : {
        	BeforeUpload:function(up,file){
        	    uploader.setOption("multipart_params",{"path":getNowPath()});
        	}, 
        	FilesAdded : function(up, files){
        		for(var i = 0, len = files.length; i<len; i++){
        			this.start();
        			var file_name = files[i].name; //文件名
        			//构造html来更新UI
        			var html = '<li class="file" draggable="true" ondragstart="drag(event)"  ondrop="drop(event)" ondragover="allowDrop(event)" id="file-' + files[i].id +'"><div class="hideimg"></div><p class="file-name">' + file_name + '</p><p class="progress"></p></li>';
        			$(html).appendTo('#file-list');
        		}
        	},
        	UploadProgress : function(up, file){        
        		$('#file-'+file.id+' .progress').css('width',file.percent + '%');//控制进度条 
        	},
        	FileUploaded : function(up, file, res){
        		$('#file-'+file.id+' .progress').remove();
        		var html = "<input type='checkbox' /><a href='javascript:void(0)'><img src='static/svg/file.svg' /><span>" + file.name + "</span></a>";
        		$('#file-'+file.id).html(html)
        	}
        }
    }); 
    uploader.init();
})


    //获得当前目录路径
	function getNowPath(){
		var lis = $('#path').find('li');
		var path = "";
		for(var i = 0; i < lis.length; i++){
			var name = $(lis[i]).text();
			path += name + "/";
		}
		return path;
	}
	//获得文件信息
	function getFileInfo(key){
		$.ajax({
			url : 'file/getFileInfo',
			method : 'post',
			data : { key : key},
			dataType : 'json',
			success : function(json){
				var spans = $('#file-info').find('span');
				$(spans[0]).text(json.key.substring(json.key.lastIndexOf('/')+1));
				$(spans[1]).text(json.mimeType);
				$(spans[2]).text(json.fsize);
				$(spans[3]).text(json.key.substring(0,json.key.lastIndexOf('/')+1));
				$(spans[4]).text(json.putTime);
			},
			error : function(){
				toast('服务器错误')
			}
		})
	}
	//刷新目录
	function refreshFolder(){
		getFileList(getNowPath().substring(0,getNowPath().length-1));
	}
	//取得目录列表
	function getFileList(path){
		$.ajax({
			url : 'file/getFileList',
			method : 'post',
			data : { path : path},
			dataType : 'json',
			success : function(json){
				$('#file-list').empty();
				for(var i = 0; i < json.folderList.length; i++){
					$('#file-list').append("<li class='folder' draggable='true' ondragstart='drag(event)'  ondrop='drop(event)' ondragover='allowDrop(event)'><div class='hideimg'></div><input type='checkbox' /><a href='javascript:void(0)'><img src='static/svg/folder.svg'/><span>" + json.folderList[i] + "</span></a></li>")
				}
				for(var i = 0; i < json.fileList.length; i++){
					if(json.fileList[i].key!="")
						$('#file-list').append("<li class='file' draggable='true' ondragstart='drag(event)'  ondrop='drop(event)' ondragover='allowDrop(event)'><div class='hideimg'></div><input type='checkbox' /><a href='javascript:void(0)'><img src='static/svg/file.svg'/><span>" + json.fileList[i].key + "</span></a></li>")
				}
			},
			error : function(){
				toast('服务器错误')
			}
		})
	}
	//设置当前路径
	function setNowPath(path){
		var lis = path.split('/');
		$('#path').html("");
		for(var i = 0; i < lis.length; i++){
			var html = "";
			if( i == lis.length-1)
				html = "<li class='active'>" + lis[i] + "</li>";
			else 
				html = "<li><a href='javascript:void(0)'>" + lis[i] + "</a></li>";
			$('#path').append(html);
		}
	}
	//上传文件
	function upload(){
		$.ajax({
			url : 'file/getFileList',
			method : 'post',
			data : { path : path},
			dataType : 'json',
			success : function(json){
				
			},
			error : function(){
				toast('服务器错误')
			}
		})
	}
	//新建文件夹
	function mkDir(path){
		if(path.indexOf("//") != -1){
			toast('文件夹名不能包含 /')
			return;
		}
		$.ajax({
			url : 'file/mkDir',
			method : 'post',
			data : { path : path},
			dataType : 'json',
			success : function(json){
				getFileList(getNowPath().substring(0,getNowPath().length-1));
				mkhide();
			},
			error : function(){
				toast('服务器错误')
			}
		})
	}
	//重命名文件
	function renameFile(key, newKey){
		if(newKey.indexOf("//") != -1){
			toast('文件名不能包含 /')
			return;
		}
		$.ajax({
			url : 'file/renameFile',
			method : 'post',
			data : { key : key , newKey : newKey },
			dataType : 'json',
			success : function(json){
				getFileList(getNowPath().substring(0,getNowPath().length-1));
				renamehide();
				toast(json);
			},
			error : function(){
				toast('服务器错误')
			}
		})
	}
	//重命名文件夹
	function renameFolder(path, newPath){
		if(newPath.indexOf("/") != -1){
			toast('文件夹名不能包含 /')
			return;
		}
		$.ajax({
			url : 'file/renameFolder',
			method : 'post',
			data : { path : path , newPath : newPath },
			dataType : 'json',
			success : function(json){
				getFileList(getNowPath().substring(0,getNowPath().length-1));
				renamehide();
				toast(json);
			},
			error : function(){
				toast('服务器错误')
			}
		})
	}
	//confirm
	
	function renamehide(){
		$('#rename-confirm').fadeOut(200);
	}
	function renameshow(){
		$('#rename-confirm').fadeIn(200);
		$('#rename-confirm').find('input[type="text"]').focus();
	}
	function mkhide(){
		$('#mkdir-confirm').fadeOut(200);
	}
	function mkshow(){
		$('#mkdir-confirm').fadeIn(200);
	}


//下载文件
function download(filename) {
	$.ajax({
		url : 'file/download',
		method : 'post',
		data : {prefix : getNowPath(),filename : filename},
		dataType : 'json',
		success : function(url){
          	var iframe = document.createElement("iframe");
           	document.body.appendChild(iframe);
            // alert(download_file.iframe);
            iframe.src = url;
            iframe.style.display = "none";
		}
	})
 }
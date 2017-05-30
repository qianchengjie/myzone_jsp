 $(document).ready(function() {

	 //用户工具
	 $('#user-tools').on('click', 'span', function(){
	 	var index = $(this).index();
	 	switch(index){
	 	case 0:
	 		$('#role-right-setting').modal('show');
	 		$.ajax({
	 			url : 'cms/getRoleRight',
	 			method : 'post',
	 			data : { rolename : '超级管理员'},
	 			dataType : 'json',
	 			success : function(json){
	 				setRights(json);
	 			},
	 			error : function(){
	 				toast('服务器错误');
	 			}
	 		})
	 		break;
	 	}
	 })
	 //选择角色
	 $('#role-list').on('click', 'label', function(){
		 var rolename = $(this).text().trim();
		 $.ajax({
	 			url : 'cms/getRoleRight',
	 			method : 'post',
	 			data : { rolename : rolename},
	 			dataType : 'json',
	 			success : function(json){
	 				setRights(json);
	 			},
	 			error : function(){
	 				toast('服务器错误');
	 			}
	 		})
	 })
	 //权限全选操作
	 $('.panel-heading input').on('click', function(){
		 if(!$(this).is(':checked'))
			 $(this).parents('.panel').find('.panel-body input:checked').click(); 
		 else
			 $(this).parents('.panel').find('.panel-body input:not(:checked)').click(); 
	 })
	 $('.panel-body .checkbox').on('click',function(){
		 var checked = $(this).parents('.panel-body').find('input:checked');
		 var unchecked = $(this).parents('.panel-body').find('input:not(:checked)');
		 if(checked.length == 0)
			 $(this).parents('.panel').find('.panel-heading input:checked').click(); 
		 else{
			 var flag = $(this).parents('.panel').find('.panel-heading input').is(':checked');
			 $(this).parents('.panel').find('.panel-heading input:not(:checked)').click();
			 if(!flag)
				 for(var i = 0; i < unchecked.length; i++){
					 $(unchecked[i]).click();
				 }
		 }
	})
	 //设置角色拥有的权限
	 function setRights(list){
		 var rs = $('#role-right-panel').find("input[type='checkbox']");
		 for( var i = 0; i < rs.length; i++){
			 if($(rs[i]).is(':checked'))
				 $(rs[i]).click();
		 }
		 for(var i = 0; i < list.length; i++){
			 var cbs = $('#role-right-panel').find("input[type='checkbox']");
			 var rightId = list[i];
			 var rights = $('#role-right-panel').find("input[value='" + rightId + "']");
			 for( var j = 0; j < rights.length; j++){
				 if(!$(rights[j]).is(':checked'))
					 $(rights[j]).click();
			 }
		 }
	 }
	
	 //更新权限
	 $('#role-right-setting .modal-footer').on('click', 'button:last-child', function(){
		 var rolename = $('#role-list').find('label.active').text().trim();
		 var rights = $('#role-right-panel .panel-body').find("input:checked");
		 var rightsId = new Array();
		 for( var i = 0; i < rights.length; i++){
			 rightsId.push($(rights[i]).val());
		 }
		 console.log(rightsId)
		 $.ajax({
			 url : 'cms/updateRoleRight',
			 method : 'post',
	         traditional : true,
			 data : { rolename : rolename, rightsId : rightsId},
			 dataType : 'json',
			 success : function(json){
				 toast(json)
			 },
			 error : function(){
				 toast('服务器错误');
			 }
		 })
	 })

 })
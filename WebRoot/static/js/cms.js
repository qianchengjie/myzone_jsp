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
 

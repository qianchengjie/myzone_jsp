$(document).ready(function(){
	loadImgs(
		[
		"http://oq7avxrj8.bkt.clouddn.com/images/bgPic.jpg",
		"http://oq7avxrj8.bkt.clouddn.com/images/bgPic.jpg"
		]
	,$("#loadImg"));
})

function loadComplete(delayTime){
	$(".loading-container").delay(1000).slideUp(500);
	setTimeout(function(){
		$('.cover').show();
		$('#cover-heading').addClass("animated bounce");
		$('#text').addClass("animated fadeInUp");
		$('#contactBtn').addClass("animated fadeInUp");
	},delayTime);
}
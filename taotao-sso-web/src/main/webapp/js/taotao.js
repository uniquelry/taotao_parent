var TT = TAOTAO = {
	checkLogin : function(){
		var _ticket = $.cookie("TT_TOKEN");
		if(!_ticket){
			return ;
		}
		$.ajax({
			//http://localhost:8089/user/token?callback=fun
			url : "http://localhost:8089/user/token/" + _ticket,//首先定义个fun()，在发送请求的时候带上?callback=fun
			dataType : "jsonp",
			type : "GET",
			success : function(data){//相当于fun
				if(data.status == 200){
					var username = data.data.username;
					var html = username + "，欢迎来到淘淘！<a href=\"http://www.taotao.com/user/logout.html\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});
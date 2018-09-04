var TT = TAOTAO = {
	checkLogin : function(){
		var _ticket = $.cookie("TT_TOKEN");//从cookie中获取数据token
		if(!_ticket){
			return ;
		}
		$.ajax({
			//http://localhost:8089/user/token/123?callback=fun
			url : "http://localhost:8089/user/token/" + _ticket,//首先定义一个函数fun()，在发送请求时带上callback=fun
			dataType : "jsonp",//jsonp自动添加callback
			type : "GET",
			success : function(data){
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
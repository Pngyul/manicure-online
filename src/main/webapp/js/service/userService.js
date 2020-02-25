//服务层
app.service('userService',function($http){
	
	//获取登录名
	this.getUserName=function(){
		return $http.get('../user/getUserName.do');		
	}  
	
	//用户注册
	this.add=function(entity,smscode){
		return $http.post('../user/add.do?smscode='+smscode,entity);		
	}  
	
	//发送验证码
	this.sendCode=function(phone){
		return $http.get("../user/sendCode.do?phone="+phone);
	}
	
	//获取用户信息
	this.getUserInfo=function(){
		return $http.get("../user/getUserInfo.do");
	}
	
	//获取用户信息
	this.update=function(entity){
		return $http.post("../user/update.do",entity);
	}

	//获取用户信息
	this.updatePsw=function(entity){
		return $http.post("../user/updatePsw.do",entity);
	}
		
});

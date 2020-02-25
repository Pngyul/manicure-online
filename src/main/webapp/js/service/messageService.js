//服务层
app.service('messageService',function($http){
	

	//添加留言
	this.add=function(entity){
		return $http.post('../message/add.do',entity);
	};

	//管理员查看留言
	this.search=function(page,rows,entity){
		return $http.post('../message/search.do?page='+page+'&rows='+rows,entity);
	};

	//管理员删除留言
	this.dele=function(ids){
		return $http.get('../message/dele.do?ids='+ids);
	};

	//管理员删除留言
	this.findOne=function(id){
		return $http.get('../message/findOne.do?id='+id);
	}


});

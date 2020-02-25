//服务层
app.service('addressService',function($http){
	 //查找所有的省
	this.findAll = function(){
		return $http.get("../address/findAll.do");
	}
	//查询省市
	this.findByProvenceId = function(parentId){
		return $http.get("../address/findByProvenceId.do?parentId="+parentId);
	}
	//查询地区
	this.findByCityId = function(parentId){
		return $http.get("../address/findByCityId.do?parentId="+parentId);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../address/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../address/update.do',entity );
	}
	//列举地址
	this.findAllAddress=function(entity){
		return  $http.get('../address/findAllAddress.do');
	}
	//删除
	this.dele=function(id){
		return  $http.get('../address/delete.do?id='+id);
	}
	
	this.findById=function(id){
		return  $http.get('../address/findById.do?id='+id);
	}
	
	//设置默认
	this.isDefault=function(entity){
		return  $http.post('../address/isDefault.do',entity);
	}
});

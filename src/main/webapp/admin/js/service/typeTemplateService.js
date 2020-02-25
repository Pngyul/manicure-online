//文件上传服务层
app.service("typeTemplateService",function($http){
	//查询规格列表
	this.findSpecList=function(id){
		return $http.get('../typeTemplate/findSpecList.do?id='+id);
	}
});
//控制层 
app.controller('bookController' ,function($scope,$controller,bookService){

	$controller('baseController',{$scope:$scope});//继承

	//用户预约服务
	$scope.entity={};
	$scope.book=function(){
		$scope.entity.type='1';
		if($scope.entity.contact==null){
			alert("联系人姓名不能为空！");
			return;
		}else if($scope.entity.mobile==null){
			alert("电话号码不能为空！");
			return;
		}else if($scope.entity.arriveTime==null){
			alert("到达日期不能为空");
			return;
		}
		bookService.add($scope.entity).success(
			function(response){
				alert(response.message);
				$scope.entity={};
				$scope.getByUsername();
			}
		);
	};

	$scope.strStatus=["未接单","已接单","已拒绝","交易已完成","交易已关闭"];
	//获取用户预约 记录
	$scope.getByUsername=function(){
		bookService.getByUsername().success(
			function(response){
				$scope.mylist=response;
			}
		);
	};

	$scope.searchEntity={};//定义搜索对象

	//搜索
	$scope.search=function(page,rows){
		bookService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	};


    //查看预约详情
	$scope.findOne=function(id){
		bookService.findOne(id).success(
			function(response){
				$scope.entity=response;
			}
		);
	};

	//批量修改预约状态
	$scope.updateStatus=function(status){
		bookService.updateStatus($scope.selectIds,status).success(
			function(response){
				$scope.reloadList()
			}
		);
	};

	//修改预约状态
	$scope.updateOneStatus=function(status,id){
		$scope.id=[id];
		bookService.updateStatus($scope.id,status).success(
			function(response){
				$scope.reloadList()
			}
		);
	};
	//用户线下预约，管理员添加预约记录
	$scope.unlineAdd=function(){
		$scope.entity.type='2';
		bookService.add($scope.entity).success(
			function(response){
				alert(response.message);
				$scope.reloadList()
			}
		);
	};

	$scope.type=['网上预约','线下预约'];


});

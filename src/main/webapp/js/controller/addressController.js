 //控制层 
app.controller('addressController' ,function($scope,$controller,$location,addressService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
	// 查询一级分类列表:
	$scope.selectprovenceList = function(){
		addressService.findAll().success(function(response){
			$scope.provinceList = response;
		});
	} 
	// 查询二级分类列表:
	$scope.$watch("entity.provinceId",function(newValue,oldValue){
		addressService.findByProvenceId(newValue).success(function(response){
			$scope.cityList = response;
			$scope.areaList={};
		});
	});
	
	// 查询三级分类列表:
	$scope.$watch("entity.cityId",function(newValue,oldValue){
		addressService.findByCityId(newValue).success(function(response){
			$scope.areaList = response;
		});
	});
	//别名
	$scope.inputAlias = function(type){
		if(type==0){
			$scope.entity.alias="家里";
		}else if(type==1){
			$scope.entity.alias="宿舍";
		}else if(type==2){
			$scope.entity.alias="公司";
		}
	}
	
	//保存 
	$scope.save=function(){	
		var serviceObject;//服务层对象  
		if($scope.entity.id!=null){//如果有ID
			serviceObject=addressService.update( $scope.entity ); //修改  
		}else{
			serviceObject=addressService.add( $scope.entity  );//增加 
		}	
		serviceObject.success(
			function(response){
				if(response.flag){
					//重新查询 
		        	alert(response.message);
		        	location.href="home-setting-address.html";
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	// 查询地址列表:
	$scope.findAllAddress = function(){
		addressService.findAllAddress().success(function(response){
			$scope.addressList = response;
		});
	}
	
	// 根据id查看地址
	$scope.findById = function(id){
		addressService.findById(id).success(function(response){
			$scope.entity = response;
		});
	}
	
	// 删除
	$scope.dele = function(id){
		addressService.dele(id).success(function(response){
			if(response.success){
				$scope.findAllAddress();
			}else{
				alert(response.message);
			}
		});
	}
	
	// 设为默认
	$scope.isDefault = function(entity){
		addressService.isDefault(entity).success(function(response){
			if(response.success){
				$scope.findAllAddress();
			}else{
				alert(response.message);
			}
		});
	}
	
});	

//控制层 
app.controller('userController' ,function($scope,$controller,userService,uploadService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
	//发送验证码
	$scope.sendCode=function(){
		if($scope.entity.phone==null){
			alert("请输入手机号！");
			return ;
		}		
		userService.sendCode($scope.entity.phone).success(
			function(response){
				alert(response.message);								
			}				
		);
	}
	
	//注册
	$scope.reg=function(){	
		if($scope.entity.phone==null){
			alert("请输入手机号！");
			return ;
		}
	    if($scope.entity.password!=$scope.password)  {
	      	alert("两次输入的密码不一致，请重新输入");		    	
	      	return ;
	    }
		userService.add( $scope.entity , $scope.smscode ).success(
			function(response){
				alert(response.message);
				if(response.flag){
					location.href="login.html";
				}
			}		
		);				
	}   ;
	 
	//获取用户信息
	$scope.getUserInfo=function(){	
		userService.getUserInfo().success(
			function(response){
				$scope.entity = response;
				
				var str = $scope.entity.birthday.split(" ")[0].split("-");
				$scope.bir_year = str[0];
				if(parseInt(str[1])<10){
					$scope.bir_month=str[1].substring(1);
				}else{
					$scope.bir_month=str[1]
				}
				if(parseInt(str[2])<10){
					
					$scope.bir_day=str[2].substring(1);
				}else{
					$scope.bir_day=str[2]
				}
			}		
		);				
	}  ;
	
	//更新
	$scope.entity={};
	$scope.update=function(){	
		
		var str = $scope.bir_year;
		if($scope.bir_month<10){
			str = str + "-0"+$scope.bir_month;
		}else{
			str = str + "-"+$scope.bir_month;
		}
		if($scope.bir_day<10){
			str = str + "-0"+$scope.bir_day
		}else{
			str = str + "-"+$scope.bir_day;
		}
		$scope.entity.birthday = str;
		userService.update($scope.entity).success(
			function(response){
				alert(response.message);
				if(response.flag){
					location.href="home-setting-info.html";
				}
			}		
		);		
	};
	
	/**
	 * 上传图片
	 */
	$scope.uploadFile=function(){	  
		uploadService.uploadFile().success(function(response) {        	
        	if(response.success){//如果上传成功，取出url
        		$scope.entity.headPic=response.message;//设置文件地址
        	}else{
        		alert(response.message);
        	}
        }).error(function() {           
        	     alert("上传发生错误");
        });        
    };
	/**
	 * 修改密码
	 */
	$scope.updatePsw=function(){

		if($scope.repeatPassword!=$scope.entity.password){
			alert("两次密码不匹配");
			return;
		}
		userService.updatePsw($scope.entity).success(function(response){
			alert(response.message);
			if(response.success){
				location.href = "/logout";
			}else{
				location.href = "/home-setting-safe.html";
			}

		});
	};


});	

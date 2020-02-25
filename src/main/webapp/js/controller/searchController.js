app.controller('searchController',function($scope,$location,$controller,searchService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
	$scope.searchMap={'keywords':'','category':'','efficiency':'','price':'','pageNo':1,'pageSize':5,'sortField':'','sort':''  };//搜索对象
	
	//搜索
	$scope.search=function(){
		$scope.searchMap.pageNo= parseInt($scope.searchMap.pageNo);
		searchService.search( $scope.searchMap ).success(
			function(response){						
				$scope.resultMap=response;//搜索返回的结果
				buildPageLabel();//调用
			}
		);	
	}
	
	
	//添加搜索项
	$scope.addSearchItem=function(key,value){
		$scope.searchMap[key]=value;
		$scope.search();//执行搜索 
	}
	
	//移除复合搜索条件
	$scope.removeSearchItem=function(key){
		$scope.searchMap[key]="";
		$scope.search();//执行搜索 
	}
	
	//构建分页栏	
	buildPageLabel=function(){
		//构建分页栏
		$scope.pageLabel=[];
		var firstPage=1;//开始页码
		var lastPage=$scope.resultMap.totalPages;//截止页码
		$scope.firstDot=true;//前面有点
		$scope.lastDot=true;//后边有点
		if($scope.resultMap.totalPages>5){  //如果页码数量大于5
			if($scope.searchMap.pageNo<=3){//如果当前页码小于等于3 ，显示前5页
				lastPage=5;
				$scope.firstDot=false;//前面没点
			}else if( $scope.searchMap.pageNo>= $scope.resultMap.totalPages-2 ){//显示后5页
				firstPage=$scope.resultMap.totalPages-4;	
				$scope.lastDot=false;//后边没点
			}else{  //显示以当前页为中心的5页
				firstPage=$scope.searchMap.pageNo-2;
				lastPage=$scope.searchMap.pageNo+2;
			}			
		}else{
			$scope.firstDot=false;//前面无点
			$scope.lastDot=false;//后边无点
		}
		//构建页码
		for(var i=firstPage;i<=lastPage;i++){
			$scope.pageLabel.push(i);
		}
	}

	//分页查询
	$scope.queryByPage=function(pageNo){
		if(pageNo<1 || pageNo>$scope.resultMap.totalPages){
			return ;
		}		
		$scope.searchMap.pageNo=pageNo;
		$scope.search();//查询
	}
	
	//判断当前页是否为第一页
	$scope.isTopPage=function(){
		if($scope.searchMap.pageNo==1){
			return true;
		}else{
			return false;
		}		
	}
	
	//判断当前页是否为最后一页
	$scope.isEndPage=function(){
		if($scope.searchMap.pageNo==$scope.resultMap.totalPages){
			return true;
		}else{
			return false;
		}	
	}
	
	//设置排序规则
	$scope.sortCondition=0;
	$scope.sortSearch=function(sortField,sort,actice){
		$scope.searchMap.sortField=sortField;	
		$scope.searchMap.sort=sort;	
		$scope.sortCondition=actice;
		$scope.search();
	}
	
	//加载查询字符串
	$scope.loadSearchMap=function(){
		$scope.searchMap.keywords=  $location.search()['keywords'];
		$scope.searchMap.category=  $location.search()['category'];
		$scope.searchMap.sortField=  $location.search()['sortField'];
		if($scope.searchMap.keywords==null){
			$scope.searchMap.keywords="";
		}
		if($scope.searchMap.category==null){
			$scope.searchMap.category="";
		}
		if($scope.searchMap.sortField==null){
			$scope.searchMap.sortField="";
		}else{
			$scope.searchMap.sort="DESC";
		}
		$scope.search();
	}
	
	/*$scope.sortCondition="综合";
	$scope.slectSortCondition=function(value){
		$scope.sortCondition=value;
	}
	$scope.checkSortCondition=function(value){
		if($scope.sortCondition==value){
			alert(value+"00");
			return true;
		}else{
			return false;
			
		}
	}*/
	
	
});
<%--
  ================================================================================
  eCOMP Portal
  ================================================================================
  Copyright (C) 2017 AT&T Intellectual Property
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ================================================================================
  --%>
<script src= "static/ebz/angular_js/angular.js"></script> 
<script src= "static/ebz/angular_js/angular-sanitize.js"></script>

<script src= "static/ebz/angular_js/app.js"></script>
<script src= "static/ebz/angular_js/gestures.js"></script>

<script src="static/js/jquery-1.10.2.js"></script>
<script src="static/js/modalService.js"></script>
<script src="static/js/jquery.mask.min.js" type="text/javascript"></script>
<script src="static/js/jquery-ui.js" type="text/javascript"></script>
<script src="static/ebz/sandbox/att-abs-tpls.js" type="text/javascript"></script>
<%@ include file="/WEB-INF/fusion/jsp/popup_modal.html" %>
<div ng-controller="profileSearchController">
	<div>
		<h1 class="heading1" style="margin-top:20px;">Profile Search</h1>
		<div style="margin-top:30px">
			<table att-table table-data="tableData" view-per-page="viewPerPage" current-page="currentPage" search-category="searchCategory" search-string="searchString" total-page="totalPage">

			    <thead  att-table-row type="header">
					<tr>
						<th att-table-header key="id">User ID</th>
			            <th att-table-header key="last_name">Last Name</th>        
			            <th att-table-header key="first_name">First Name</th>    
			            <th att-table-header key="email">Email</th>
			            <th att-table-header key="sbcid">ATTUID</th>        
			            <th att-table-header key="manager_attuid">Manager ATTUID</th> 
			            <th att-table-header >Edit</th>
			            <th att-table-header key="active">Active?</th>         
			        </tr>
			    </thead>
			    <tbody att-table-row type="body" row-repeat="rowData in tableData">
			        <tr>
		            	<td att-table-body >{{rowData.id}}</td>
		            	<td att-table-body >{{rowData.lastName}}</td>
		            	<td att-table-body >{{rowData.firstName}}</td>
		            	<td att-table-body >{{rowData.email}}</td>
		            	<td att-table-body >{{rowData.sbcid}}</td>
		            	<td att-table-body >{{rowData.managerId}}</td>
		            	<td att-table-body ><a href="" ng-click="editRow(rowData.id)" class="icon-edit" style="color: #888;font-size:20px;"></a></td>
		        		<td att-table-body >
		        		   <div ng-click="toggleProfileActive(rowData.id)">
		        		     <input type="checkbox"  ng-model="rowData.active" att-toggle-main>
		        		   </div>
		        	   </td>
			        </tr>     
			    </tbody>	  
			</table>
		</div>
	</div>	
	<input ng-model="currentPage"></input>
</div>
	
<script>
app.controller("profileSearchController", function ($scope,$http,modalService, $modal) { 
	// Table Data
	$scope.tableData=${model.profileList};
	$scope.viewPerPage = 5;
    $scope.scrollViewsPerPage = 2;
    $scope.currentPage = 1;
    $scope.totalPage;
    $scope.searchCategory = "";
    $scope.searchString = "";
    modalService.showSuccess('','Modal Sample') ;
	for(x in $scope.tableData){
		if($scope.tableData[x].active_yn=='Y')
			$scope.tableData[x].active_yn=true;
		else
			$scope.tableData[x].active_yn=false;
	}
    $scope.editRow = function(profileId){
        window.location = 'profile?profile_id=' + profileId;
    }
   
    $scope.toggleProfileActive = function(profileId) {
    	if (confirm("You are about to change user's active status. Do you want to continue?")) {
                 $http.get("profile/toggleProfileActive?profile_id="+profileId).success(function(){});
    	}
    };

});
</script>

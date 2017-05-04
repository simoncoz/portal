/*-
 * ================================================================================
 * ECOMP Portal
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ================================================================================
 */
'use strict';
(function () {
    class GetAccessCtrl {
        constructor($log, $scope,  $stateParams, getAccessService, userProfileService, ExternalRequestAccessService, applicationsService, ngDialog) {
        	// $log.debug('GetAccessCtrl: appService param is: ' + applicationsService.goGetAccessAppName);
        	var resultAccessValue = null;
        	
        	$scope.openAppRoleModal = (itemData) => {    	
        		if(resultAccessValue){
        		let data = null;
                    data = {
                        dialogState: 2,
                        selectedUser:{
                            attuid: $scope.attuid,
                            firstName: $scope.firstName,
                            lastName: $scope.lastName,
                            headerText: itemData.app_name
                        }
                    }
                ngDialog.open({
                    templateUrl: 'app/views/catalog/add-catalog-dialogs/new-catalog.modal.html',
                    controller: 'NewCatalogModalCtrl',
                    controllerAs: 'userInfo',
                    data: data
                });
        		}
            }
        	
            userProfileService.getUserProfile().then(
        			function(profile) {
        				$scope.attuid = profile.orgUserId;
        				$scope.firstName = profile.firstName;
        				$scope.lastName = profile.lastName;
        	  });
            
        	this.updateAppsList = () => {
        		ExternalRequestAccessService.getExternalRequestAccessServiceInfo().then(
			  			function(property) {
			  				resultAccessValue = property.accessValue;
				}).catch(err => {
                    $log.error('GetAccessCtrl: failed getExternalRequestAccessServiceInfo: ' + JSON.Stringify(err));
                });
                getAccessService.getListOfApp().then(res=> {
                	var tableData=[];
                	// $log.info('GetAccessCtrl::updateAppsList: getting res');
                	var result = (typeof(res.data) != "undefined" && res.data!=null)?res.data:null;
                	// $log.info('GetAccessCtrl::updateAppsList: result',result);
                	// $log.info('GetAccessCtrl::updateAppsList: done');
                	var source = result;
                	// $log.info('GetAccessCtrl::updateAppsList source: ', source);
                	for(var i=0;i<source.length; i++){
                		var dataArr = source[i];
                		var checkEcompFuncAvail = 'Ecomp Function Not Available' ; 
                		var reqStatus = 'Pending'; 
                		dataArr.ecompFunction = (dataArr.ecompFunction === null) ? checkEcompFuncAvail : dataArr.ecompFunction;
                		dataArr.reqType = (dataArr.reqType === 'P') ? reqStatus : dataArr.reqType;
                    	var dataTemp={
                    		ecomp_function: dataArr.ecompFunction,
                    		app_name:dataArr.appName,
                    		role_name:dataArr.roleName,
                    		current_role:dataArr.roleActive,
                    		request_type:dataArr.reqType
                    	}
                    	tableData.push(dataTemp);
                	} 
                	this.appTable=tableData;
                	if(tableData!=null){
                		var len = tableData.length;
                		this.totalPage =  Math.ceil(len/this.viewPerPage);     
                	}
                	if( $stateParams.appName != null)
                		this.searchString = $stateParams.appName;
                	else
                		this.searchString = applicationsService.goGetAccessAppName;
                	// the parameter has been used; clear the value.
                	applicationsService.goGetAccessAppName = '';
                }).catch(err=> {
                    $log.error('GetAccessCtrl:error:: ', err);
                }).finally(() => {
                    this.isLoadingTable = false;
                });
            };
            
            this.updateTable = (num) => {
                this.startIndex=this.viewPerPage*(num-1);
                this.currentPage = num;
            };
        	let init = () => {
                // $log.info('GetAccessCtrl:: initializing...');
                this.searchString = '';
                this.getAccessTableHeaders = ['ECOMP Function', 'Application Name', 'Role Name', 'Current Role', 'Request Status'];
                this.appTable=[];
                this.updateAppsList();             
                this.viewPerPage=20;
                this.startIndex=0;
                this.currentPage = 1;
                this.totalPage=0;
            };
            init();
        }
    }
    GetAccessCtrl.$inject = ['$log', '$scope', '$stateParams', 'getAccessService', 'userProfileService', 'ExternalRequestAccessService','applicationsService', 'ngDialog'];
    angular.module('ecompApp').controller('GetAccessCtrl', GetAccessCtrl);
})();
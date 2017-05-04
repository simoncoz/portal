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
	class HeaderCtrl {
        constructor($log, $window, userProfileService, menusService, $scope, ECOMP_URL_REGEX, $cookies, $state,auditLogService,notificationService) {
            this.firstName = '';
            this.lastName = '';
            this.$log = $log;
            this.menusService = menusService;
            this.$scope = $scope;
            this.favoritesMenuItems = '';
            $scope.favoriteItemsCount = 0;
            $scope.favoritesMenuItems = '';
            $scope.showFavorites = false;
            $scope.emptyFavorites = false;
            $scope.favoritesWindow = false;
            $scope.notificationCount=0;
            $scope.showNotification = true;

            $scope.hideMenus = false;

            $scope.menuItems = [];
            $scope.activeClickSubMenu = {
                x: ''
            }; 
            $scope.activeClickMenu = {
                x: ''
            };
            $scope.megaMenuDataObject =[];
            $scope.notificationCount= notificationService.notificationCount;
            this.isLoading = true;
            this.ECOMP_URL_REGEX = ECOMP_URL_REGEX;
            
            var unflatten = function( array, parent, tree ){
            
                tree = typeof tree !== 'undefined' ? tree : [];
                parent = typeof parent !== 'undefined' ? parent : { menuId: null };
                var children = _.filter( array, function(child){ return child.parentMenuId == parent.menuId; });
                
                if( !_.isEmpty( children )  ){
                  if( parent.menuId === null ){
                    tree = children;
                  }else{
                    parent['children'] = children
                  }
                  _.each( children, function( child ){ unflatten( array, child ) } );
                }
            
                return tree;
            }
            
            userProfileService.getFunctionalMenuStaticInfo()
            .then(res=> {
            	// $log.debug('HeaderCtrl::getFunctionalMenuStaticInfo: getting Functional Menu Static Info init');
            	if(res==null || res.firstName==null || res.firstName=='' || res.lastName==null || res.lastName=='' ){
            		// $log.info('HeaderCtrl::getFunctionalMenuStaticInfo: failed getting userinfo from shared context.. ');
            		$log.info('HeaderCtrl: failed to get all required data, trying user profile');
            		userProfileService.getUserProfile()
                    .then(profile=> {
                    	// $log.debug('HeaderCtrl:: getting userinfo from session success');
                        this.firstName = profile.firstName;
                        this.lastName = profile.lastName;                     
                        // $log.debug('HeaderCtrl::getFunctionalMenuStaticInfo: user has the following roles: ' + profile.roles);
                    }).catch(err=> {
                        $log.error('Header Controller:: getUserProfile() failed: ' + err);
                    });
            	} else {
            		// $log.debug('HeaderCtrl: fetched Functional Menu Static Info successfully',res);
            		this.firstName = res.firstName;
            		this.lastName = res.lastName;           	   	  	  
            	}

            	menusService.GetFunctionalMenuForUser()
            	.then(jsonHeaderMenu=> {
            		$scope.menuItems = unflatten( jsonHeaderMenu );
            		$scope.megaMenuDataObject = $scope.menuItems;
            	}).catch(err=> {
            		$log.error('HeaderCtrl::GetFunctionalMenuForUser: HeaderCtrl json returned: ' + err);
            	});      
            	
            }).catch(err=> {
            	$log.error('HeaderCtrl::getFunctionalMenuStaticInfo failed: ' + err);
            });
            
          //store audit log
            $scope.auditLog = function(app,type) {
            	var comment = 'type: '+type+ ',title: '+app.text+",url: "+app.url;
        		auditLogService.storeAudit(app.appid,'functional',comment);
        	};

            $scope.loadFavorites = function () {
                $scope.hideMenus = false;
                // $log.debug('HeaderCtrl::loadFavorites: loadFavorites has happened.');
                if ($scope.favoritesMenuItems == '') {
                    generateFavoriteItems();
                    // $log.debug('HeaderCtrl::loadFavorites: loadFavorites is calling generateFavoriteItems()');
                } else {
                    // $log.debug('HeaderCtrl::loadFavorites: loadFavorites is NOT calling generateFavoriteItems()');
                }
            }

            $scope.goToUrl = (item) =>  {
               //  $log.error('HeaderCtrl::goToUrl has started',item);
                let url = item.url;
                let restrictedApp = item.restrictedApp;
                if (!url) {
                    $log.warn('HeaderCtrl::goToUrl: No url found for this application, doing nothing..');
                    return;
                }
                if (restrictedApp) {
                    $window.open(url, '_blank');
                } else {
                	if(item.url=="getAccess" || item.url=="contactUs"){
                	    // if (url = window.location.href)
                		$state.go("root."+url);
                	} else {
                    	var tabContent = { id: new Date(), title: item.text, url: item.url,appId:item.appid };
                    	$cookies.putObject('addTab', tabContent );
                    }
                    // $log.debug('HeaderCtrl::goToUrl: url = ', url);
                }
                $scope.hideMenus = true;
            }
            
            
            
            $scope.submenuLevelAction = function(index, column) {
                if ($scope.favoritesMenuItems == '') {
                    generateFavoriteItems();
                    // $log.debug('HeaderCtrl::submenuLevelAction: submenuLevelAction is calling generateFavoriteItems()');
                } else {
                    // $log.debug('submenuLevelAction is NOT calling generateFavoriteItems()');
                }
                // $log.debug('item hovered: ' + index + '; column = ' + column);
                // if (column == 2) {  // 2 is Design
                //     // This is an admitted hack. See aw3218 for reasons why
                //     $log.debug('submenuLevelAction column == 2');
                //     $scope.favoritesWindow = false;
                //     $scope.showFavorites = false;
                //     $scope.emptyFavorites = false;
                // }
                if (index=='Favorites' && $scope.favoriteItemsCount != 0) {
                    // $log.debug('HeaderCtrl::submenuLevelAction: Showing Favorites window');
                    // generateFavoriteItems();
                    $scope.favoritesWindow = true;
                    $scope.showFavorites = true;
                    $scope.emptyFavorites = false;
                }
                if (index=='Favorites' && $scope.favoriteItemsCount == 0) {
                    // $log.debug('HeaderCtrl::submenuLevelAction: Hiding Favorites window in favor of No Favorites Window');
                    // generateFavoriteItems();
                    $scope.favoritesWindow = true;
                    $scope.showFavorites = false;
                    $scope.emptyFavorites = true;
                }
                if (index!='Favorites' ) {
                    $scope.favoritesWindow = false;
                    $scope.showFavorites = false;
                    $scope.emptyFavorites = false;
                }

            };
            
            $scope.hideFavoritesWindow = function() {
                $scope.showFavorites = false;
                $scope.emptyFavorites = false;
                // $scope.thirdFourthMenus = true;
            }
            
            $scope.isUrlFavorite = function (menuId) {
                // $log.debug('array objects in menu favorites = ' + $scope.favoriteItemsCount + '; menuId=' + menuId);
                var jsonMenu =  JSON.stringify($scope.favoritesMenuItems);
                var isMenuFavorite =  jsonMenu.indexOf('menuId\":' + menuId);
                // $log.debug('jsonMenu.indexOf(menuId:' + jsonMenu.indexOf('menuId\":'+menuId));
                // $log.debug('isMenuFavorite= ' + isMenuFavorite);
                if (isMenuFavorite==-1) {
                    return false;
                } else {
                    return true;
                }

            }
            
            let generateFavoriteItems  = () => {
                menusService.getFavoriteItems()
                    .then(favorites=> {
                        // $log.debug('HeaderCtrl.getFavoriteItems:: ' + JSON.stringify(favorites));
                        $scope.favoritesMenuItems = favorites;
                        $scope.favoriteItemsCount = Object.keys(favorites).length;
                        // $log.info('HeaderCtrl.getFavoriteItems:: number of favorite menus: ' + $scope.favoriteItemsCount);
                    }).catch(err=> {
                        $log.error('HeaderCtrl.getFavoriteItems:: Error retrieving Favorites menus: ' + err);
                });
            }

           $scope.setAsFavoriteItem =  function(event, menuId){
               var jsonMenuID = angular.toJson({'menuId': + menuId });
               // $log.debug('HeaderCtrl::setFavoriteItems: ' + jsonMenuID  + " - " +  event.target.id);

               menusService.setFavoriteItem(jsonMenuID)
               .then(() => {
                   // var elementId = '#'+ event.currentTarget.id;
                   angular.element('#' + event.target.id).css('color', '#fbb313');
                   generateFavoriteItems();
               }).catch(err=> {
                   $log.error('HeaderCtrl::setFavoriteItems:: API setFavoriteItem error: ' + err);
               });
           };

            $scope.removeAsFavoriteItem =  function(event, menuId){
                // $log.debug('-----------------------------removeAsFavoriteItem: ' + menuId + " - " +  event.target.id);
                menusService.removeFavoriteItem(menuId)
                .then(() => {
                    angular.element('#' + event.target.id).css('color', '#666666');
                    generateFavoriteItems();
                }).catch(err=> {
                    $log.error('HeaderCtrl::removeAsFavoriteItem: API removeFavoriteItem error: ' + err);
                });
            };

            $scope.goToPortal = (headerText, url) => {
                if (!url) {
                    $log.warn('HeaderCtrl::goToPortal: No url found for this application, doing nothing..');
                    return;
                }
                if (!ECOMP_URL_REGEX.test(url)) {
                    url = 'http://' + url;
                }

                if(headerText.startsWith("vUSP")) {
                	window.open(url, '_blank');//, '_self'
                }
                else {
                	var tabContent = { id: new Date(), title: headerText, url: url };
                	$cookies.putObject('addTab', tabContent );
                }
            };

        }
    }
    class LoginSnippetCtrl {
        constructor($log, $scope, $cookies, $timeout, userProfileService, sessionService) {
            $scope.firstName="";
            $scope.lastName="";
            $scope.displayUserAppRoles=false; 
            $scope.allAppsLogout = function(){
            	
            	var cookieTabs = $cookies.getObject('visInVisCookieTabs');
             	if(cookieTabs!=null){
             		for(var t in cookieTabs){
             		
             			var url = cookieTabs[t].content;
             			if(url != "") {
             				sessionService.logout(url);
             	      	}
             		}
             	}
             	// wait for individual applications to log out before the portal logout
             	$timeout(function() {
             		window.location = "logout.htm";
             	}, 2000);
            }
            
            
            try {
            	userProfileService.getFunctionalMenuStaticInfo()
                .then(res=> {
              	  // $log.info('HeaderCtrl::LoginSnippetCtrl: Login information: ' + JSON.stringify(res));
              	  $scope.firstName = res.firstName;
              	  $scope.lastName = res.lastName;
              	  $scope.loginSnippetEmail = res.email;
              	  $scope.loginSnippetUserid = res.userId;
              	  $scope.lastLogin = res.last_login;
                }).catch(err=> {
              	  $log.error('HeaderCtrl::LoginSnippetCtrl: failed in getFunctionalMenuStaticInfo: ' + err);
                });
            } catch (err) {
                $log.error('HeaderCtrl::LoginSnippetCtrl caught exception: ' + err);
            }
            
            $scope.getUserApplicationRoles= function(){
          	  $scope.userapproles = [];
          	  if($scope.displayUserAppRoles)
	         		$scope.displayUserAppRoles = false;
	         		 else
	         			$scope.displayUserAppRoles = true;
          	  
        	        userProfileService.getUserAppRoles($scope.loginSnippetUserid)
        	          .then(res=>{
        			
       		 for(var i=0;i<res.length;i++){              			
	            	var userapprole ={
	            		App:res[i].appName,
	            		Roles:res[i].roleNames,	
	            	};
	            	
	            	$scope.userapproles.push(userapprole); 
       		}
       		 
        	});
        	
          }
        }        
    }
    class NotificationCtrl{
    	constructor($log, $scope, $cookies, $timeout, sessionService,notificationService,$interval,ngDialog) {
    		 $scope.notifications=[];   
    		 var intervalPromise = null;
             $scope.notificationCount= notificationService.notificationCount;
             
             $scope.getNotification = function(){            	 
            	 notificationService.getNotification()
                 .then(res=> {
                	notificationService.decrementRefreshCount();
                	var count = notificationService.getRefreshCount();
                 	if (res==null || res.data==null || res.data.message!='success') {
                 		$log.error('NotificationCtrl::updateNotifications: failed to get notifications');
                 		if (intervalPromise != null)
                 			$interval.cancel(intervalPromise);
                 	} else if(count<=0){
                 		if (intervalPromise != null)
                 			$interval.cancel(intervalPromise);
                 	} else {
                 		$scope.notifications = [];
                 		notificationService.setNotificationCount(res.data.response.length);
                 		for(var i=0;i<res.data.response.length;i++){
                 			var data = res.data.response[i];                			
			            	var notification ={
			            		id:data.notificationId,
			            		title:data.msgHeader,
			            		message:data.msgDescription,
			            		source:data.msgSource,
			            		time:data.createdDate,
			            		priority:data.priority
			            	};
			            	$scope.notifications.push(notification);       
			             }  
                 	}   	
                 }).catch(err=> {
                 	$log.error('NotificationCtrl::getNotification: caught exception: ' + err);
                 	if (intervalPromise != null)
                 		$interval.cancel(intervalPromise);
                 });      
             }
             $scope.getNotification();
             function updateNotifications() {
            	 $scope.getNotification();
             }
             $scope.start = function(rate) {
 				// stops any running interval to avoid two intervals running at the same time
 				$scope.stop(); 	
 				// store the interval promise
 				intervalPromise = $interval(updateNotifications, rate);
 			 };

 			 $scope.stop = function() {
 				$interval.cancel(intervalPromise);
 			 };
 			 
   			
  			 $scope.showDetailedJsonMessage=function (selectedAdminNotification) {
   				if (selectedAdminNotification.source!=='EP'){
				 var messageObject=JSON.parse(selectedAdminNotification.message);
				 var html="";
				 html+='<p>'+'Message Source'+' : '+selectedAdminNotification.source+'</p>';
				 html+='<p>'+'Message Title'+' : '+selectedAdminNotification.title+'</p>';
				 for(var field in  messageObject){
					 if(field=='eventDate'||field=='lastModifiedDate'){
						 html+='<p>'+field+' : '+new Date(+messageObject[field])+'</p>';
						  
					 }else{
					 html+='<p>'+field+' : '+messageObject[field]+'</p>';
					 
					 }
				 }
 			
 		     var modalInstance = ngDialog.open({
 				    templateUrl: 'app/views/user-notifications-admin/user.notifications.Json.details.modal.page.html',
 				    controller: 'userNotificationCtrl',
 				    resolve: {
 				    	message: function () {
 				    		var message = {
 				    			   title:    '',
 		                       		text:    html
 		                       		
 		                           	};
 				          return message;
 				        },
 				     
 				      }
 				  }); 
 			
   				} 
 			 };
 			 
 			notificationService.getNotificationRate().then(res=> {
            	if (res == null || res.response == null) {
            		$log.error('NotificationCtrl: failed to notification update rate or duration, check system.properties file.');
            	} else {
            		var rate = parseInt(res.response.updateRate);
					var duration = parseInt(res.response.updateDuration);
					notificationService.setMaxRefreshCount(parseInt(duration/rate)+1);
					notificationService.setRefreshCount(notificationService.maxCount);
           			if (rate != NaN && duration != NaN) {
						$scope.updateRate=rate;
						$scope.start($scope.updateRate);
           			}            			
            	}
            }).catch(err=> {
            	$log.error('NotificationCtrl: getNotificationRate() failed: ' + err);
            });
             
             $scope.deleteNotification = function(index){
            	 if ($scope.notifications[index].id == null || $scope.notifications[index].id == '') {
             		$log.error('NotificationCtrl: failed to delete Notification.');
             		return;
            	 }
            	 notificationService.setNotificationRead($scope.notifications[index].id);
            	 $scope.notifications.splice(index,1);
            	 notificationService.setNotificationCount($scope.notifications.length);     	 
             }
    	}
    }
    NotificationCtrl.$inject = ['$log', '$scope', '$cookies', '$timeout', 'sessionService','notificationService','$interval','ngDialog'];
    LoginSnippetCtrl.$inject = ['$log', '$scope', '$cookies', '$timeout','userProfileService', 'sessionService'];
    HeaderCtrl.$inject = ['$log', '$window', 'userProfileService', 'menusService', '$scope', 'ECOMP_URL_REGEX','$cookies','$state','auditLogService','notificationService'];
    angular.module('ecompApp').controller('HeaderCtrl', HeaderCtrl);
    angular.module('ecompApp').controller('loginSnippetCtrl', LoginSnippetCtrl);
    angular.module('ecompApp').controller('notificationCtrl', NotificationCtrl);

})();
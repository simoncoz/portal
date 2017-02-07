/*-
 * ================================================================================
 * eCOMP Portal
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
    const HTTP_PROTOCOL_RGX = /https?:\/\//;
    class  TabsCtrl {
        constructor(applicationsService, $log, $window, conf, userProfileService, $scope,$cookies,$rootScope,confirmBoxService) {

            var counter = 1;
            var tabLimit = 6;
            this.conf = conf;
            var cookieDomain = this.conf.cookieDomain;
            $scope.tabs = [];
            $scope.notificationShow=true;
            $rootScope.showFooter = "";
            console.log("****************** get cookie domain from config json file " + this.conf.cookieDomain);
            $cookies.putObject('show_app_header', false,{domain: cookieDomain, path : '/'});


            let noRefresh = function () {
                    window.onbeforeunload = function(e) {
                    	
                    	var isQtoHref = false;
                    	try{
                    		isQtoHref = e.srcElement.activeElement.href.includes("mailto");
                    	} catch(err) {
                    		
                    	}
                    	
                        if ($scope.tabs.length > 1 && isQtoHref == false) {
                            return "Changes you made may not be saved. Are you sure you want to refresh?";
                        } else {
                            return null;
                        }
                    }
            }
            var addTab = function (title, content) {
            	if($scope.tabs.length==tabLimit){
            		confirmBoxService.showInformation('You have reached your maximum limit of tabs allowed.').then(isConfirmed => {});
            	} else {
            		if(title!=='Home' && content.indexOf('https') == -1){
            			console.log('App URL: '+content+'. The application URL you are trying to open is not HTTPS. We recommend to use secured HTTPS URL while on-boarding the application.');
            		}
            		
                    $scope.tabs.push({ title: title, content: content });
                    counter++;
                    $scope.selectedIndex = $scope.tabs.length - 1;
                    if ($scope.tabs.length > 1) {
                        noRefresh();
                    }
                    $cookies.putObject('cookieTabs', $scope.tabs,{domain: cookieDomain, path : '/'});
                    $cookies.putObject('visInVisCookieTabs', $scope.tabs,{domain: cookieDomain, path : '/'});
            	}
            };
            
            var adjustTitle = function (title) {
            	var index = 15;
            	var nonEmptyCharPattern = /(\s|\w)/;
            	var adjustedTitle = title.substring(0,index);
            	var ext = title.charAt(index).replace(nonEmptyCharPattern,'...');
            	return adjustedTitle.concat(ext);
            	
            	
            };
            
            var removeTab = function (event, index) {
              event.preventDefault();
              event.stopPropagation();
              $scope.tabs.splice(index, 1);
              $cookies.putObject('cookieTabs', $scope.tabs,{domain: cookieDomain, path : '/'});
            };
            
            var selectTab = function (title) {
              if(title=='Home') {
            	  $rootScope.ContentModel.IsVisible=true;
                  $rootScope.showFooter = true;
                    $rootScope.tabBottom = 75;
              }
              else {
            	  $rootScope.ContentModel.IsVisible=false;
                  $rootScope.showFooter = false;
                    $rootScope.tabBottom = 0;
              }
            };

            $scope.addTab    = addTab;
            $scope.removeTab = removeTab;
            $scope.selectTab = selectTab;
            $scope.adjustTitle = adjustTitle;
            

            $rootScope.ContentModel = {
            	    IsVisible : false,
            	    ViewUrl : null,
            	};
            
            
        	var sessionActive = applicationsService.ping()
        	.then(sessionActive => {
            $log.debug('TabsCtrl::addTab: applicationsService.ping() = ' + JSON.stringify(sessionActive));

            var cookieTabs = $cookies.getObject('cookieTabs');
        	if(cookieTabs!=null){
        		for(var t in cookieTabs){
        			console.log('TabsCtrl::addTab: cookieTabs title: '+cookieTabs[t].title);
        			if(cookieTabs[t].title!=null && cookieTabs[t].title==='Home'){
        				cookieTabs[t].content = "";
        				$rootScope.ContentModel.IsVisible=true;
        			}
        				
        			addTab( cookieTabs[t].title, cookieTabs[t].content) ;
        		}
        	} else {
          		for (var i = 0; i < 1; i++) {
            		var content="";
            		var title="";
            		if(i==0){
            			title="Home";
            			$rootScope.ContentModel.IsVisible=true;
            		}
            	  addTab(title, content);
            	}
        	}

            $scope.selectedIndex = 0;
        	});
        	
            $scope.$watchCollection(function() { return $cookies.getObject('addTab'); }, function(newValue) {
            	var tabContent = $cookies.getObject('addTab');
            	if(tabContent!=null && tabContent.url!=null){
            		var tabExists = false;
            		for(var x in $scope.tabs){
            			console.log($scope.tabs[x].content);
            			if($scope.tabs[x].title==tabContent.title){
            				tabExists = true;
            				$scope.selectedIndex = x;
            			}
            		}
            		if(!tabExists){
            	         addTab( tabContent.title, tabContent.url) ;
            		}
            		$cookies.remove('addTab');
            	}
            });
            
            
        }
        
       
    }
    
    TabsCtrl.$inject = ['applicationsService', '$log', '$window', 'conf','userProfileService', '$scope','$cookies','$rootScope','confirmBoxService'];
    angular.module('ecompApp').controller('TabsCtrl', TabsCtrl);

    angular.module('ecompApp').directive('mainArea', function() {
        return {
            restrict: "E",
            templateUrl: 'app/views/tabs/tabframe.html',
            link: function(scope, element) {
            	           	
            }
        }
    });
    
   
    
    angular.module('ecompApp').directive('tabHighlight', [function () {
        return {
          restrict: 'A',
          link: function (scope, element) {
            var x, y, initial_background = '#c3d5e6';

            element
              .removeAttr('style')
              .mousemove(function (e) {
                if(!element.hasClass('md-active'))
                {
                  x = e.pageX - this.offsetLeft;
                  y = e.pageY - this.offsetTop;

                  element
                    .css({ background: '-moz-radial-gradient(circle at ' + x + 'px ' + y + 'px, rgba(255,255,255,0.4) 0px, rgba(255,255,255,0.0) 45px), ' + initial_background })
                    .css({ background: '-webkit-radial-gradient(circle at ' + x + 'px ' + y + 'px, rgba(255,255,255,0.4) 0px, rgba(255,255,255,0.0) 45px), ' + initial_background })
                    .css({ background: 'radial-gradient(circle at ' + x + 'px ' + y + 'px, rgba(255,255,255,0.4) 0px, rgba(255,255,255,0.0) 45px), ' + initial_background });
                }
              })
              .mouseout(function () {
                element.removeAttr('style');
              });
          }
        };
      }]);


    
})();

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return '';
    if (!results[2]) return '';
    return (results[2].replace(/\+/g, " "));
}

function isCascadeFrame(ref) {
   if (self != top) {
       var e = document.body;
       e.parentNode.removeChild(e);
       window.location = "unKnownError";
   }
}

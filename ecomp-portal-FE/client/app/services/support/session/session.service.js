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
    class SessionService {
        constructor($q, $log, $http, conf,uuid,$sce) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.$sce = $sce;
        }
        
        logout(appStr) {
            this.$log.info('SessionService::logout from App');
            let deferred = this.$q.defer();
            this.$log.info('SessionService appStr: ', appStr);
           
                        var eaccessPattern = '\https?\:\/\/[^/]+/[^/]+/[^/]+';
            var standardPattern = '\https?\:\/\/[^/]+/[^/]+';
            
            if(appStr.includes("e-access")) {
            	standardPattern = eaccessPattern;
            }
            	
            var contextUrl = appStr.match(new RegExp(standardPattern));
            var logoutUrl  = contextUrl + "/logout.htm" ;
            this.$sce.trustAsResourceUrl(logoutUrl);
			console.log('logoutUrl ' + logoutUrl);
			jQuery('#reg-logout-div').append("<iframe style='display:none' src='" + logoutUrl + "' />");
        }

    }
    SessionService.$inject = ['$q', '$log', '$http', 'conf','uuid4','$sce'];
    angular.module('ecompApp').service('sessionService', SessionService)
})();

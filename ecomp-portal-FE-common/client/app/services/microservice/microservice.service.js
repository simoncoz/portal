/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * 
 */
'use strict';

(function () {
    class MicroserviceService {
        constructor($q, $log, $http, conf,uuid, utilsService) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
            this.utilsService = utilsService;   
        }
        
        createService(newService) {
        	let deferred = this.$q.defer();
        	this.$http({
                method: "POST",
                url: this.conf.api.widgetCommon,
                data: newService,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('MicroserviceService::createService Failed: Result or result.data is null');
                     deferred.reject("MicroserviceService::createService Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('MicroserviceService::createService Failed: Invalid JSON format');
                    deferred.reject("MicroserviceService::createService Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        
        updateService(serviceId, newService) {
        	let deferred = this.$q.defer();
        	this.$http({
                method: "PUT",
                url: this.conf.api.widgetCommon + "/" + serviceId,
                data: newService,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('MicroserviceService::updateService Failed: Result or result.data is null');
                     deferred.reject("MicroserviceService::updateService Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('MicroserviceService::updateService Failed: Invalid JSON format');
                    deferred.reject("MicroserviceService::updateService Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        deleteService(serviceId) {
        	let deferred = this.$q.defer();
        	this.$http({
                method: "DELETE",
                url: this.conf.api.widgetCommon + "/" + serviceId,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('MicroserviceService::deleteService Failed: Result or result.data is null');
                     deferred.reject("MicroserviceService::deleteService Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('MicroserviceService::deleteService Failed: Invalid JSON format');
                    deferred.reject("MicroserviceService::deleteService Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        getServiceList() {
          	let deferred = this.$q.defer();
        	this.$http({
                method: "GET",
                url: this.conf.api.widgetCommon,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('MicroserviceService::getServiceList Failed: Result or result.data is null');
                     deferred.reject("MicroserviceService::getServiceList Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('MicroserviceService::getServiceList Failed: Invalid JSON format');
                    deferred.reject("MicroserviceService::getServiceList Failed: Invalid JSON format");
                }  else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        getWidgetListByService(serviceId) {
          	let deferred = this.$q.defer();
        	this.$http({
                method: "GET",
                url: this.conf.api.widgetCommon + '/' + serviceId,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('MicroserviceService::getWidgetListByService Failed: Result or result.data is null');
                     deferred.reject("MicroserviceService::getWidgetListByService Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('MicroserviceService::getWidgetListByService Failed: Invalid JSON format');
                    deferred.reject("MicroserviceService::getWidgetListByService Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        getUserParameterById(paramId){
        	let deferred = this.$q.defer();
        	this.$http({
                method: "GET",
                url: this.conf.api.widgetCommon + '/services/' +  paramId,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('MicroserviceService::getUserParameterById Failed: Result or result.data is null');
                     deferred.reject("MicroserviceService::getUserParameterById Failed: Result or result.data is null");
                } else if (!this.utilsService.isValidJSON(res.data)) {
                 	this.$log.error('MicroserviceService::getUserParameterById Failed: Invalid JSON format');
                    deferred.reject("MicroserviceService::getUserParameterById Failed: Invalid JSON format");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
        
        deleteUserParameterById(paramId){
        	let deferred = this.$q.defer();
        	this.$http({
                method: "DELETE",
                url: this.conf.api.widgetCommon + '/services/' +  paramId,
                cache: false,
                headers:{
            		'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
            	if (res == null || res.data == null) {
        	 		this.$log.error('MicroserviceService::deleteUserParameterById Failed: Result or result.data is null');
                     deferred.reject("MicroserviceService::deleteUserParameterById Failed: Result or result.data is null");
                } else {
                    deferred.resolve(res.data);
                }
            })
            .catch(errRes => {
                deferred.reject(errRes);
            });
            return deferred.promise;
        }
    }
    
    MicroserviceService.$inject = ['$q', '$log', '$http', 'conf','uuid4', 'utilsService'];
    angular.module('ecompApp').service('microserviceService', MicroserviceService)
})();

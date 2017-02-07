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
    class UsersService {
        constructor($q, $log, $http, conf,uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
        }


        searchUsers(queryString) {
            let canceller = this.$q.defer();
            let isActive = false;

            let cancel = () => {
                if(isActive){
                    this.$log.debug('UsersService::searchUsers: canceling the request');
                    canceller.resolve();
                }
            };

            let promise = () => {
                let deferred = this.$q.defer();
                if(!queryString){
                    return deferred.reject(new Error('query string is mandatory'));
                }
                isActive = true;
                this.$http({
                    method: "GET",
                    url: this.conf.api.queryUsers,
                    params: {search: queryString},
                    cache: false,
                    timeout: canceller.promise,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then( res => {
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("UsersService::queryUsers Failed");
                    } else {
                        this.$log.info('UsersService::queryUsers Succeeded');
                        isActive = false;
                        deferred.resolve(res.data);
                    }
                }).catch( status => {
                    isActive = false;
                    deferred.reject('UsersService::searchUsers:: API Failed with status: ' + status);
                });
                return deferred.promise;
            };

            return {
                cancel: cancel,
                promise: promise
            };

        }

        getAccountUsers(appId) {
            let deferred = this.$q.defer();
            let log = this.$log;
            this.$log.debug('UsersService::getAccountUsers for appId: ' + appId);

            let url = this.conf.api.accountUsers.replace(':appId', appId);
            this.$http({
                method: "GET",
                url: url,
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
             if (Object.keys(res.data).length == 0) {
                    deferred.reject("UsersService::getAccountUsers for appId Failed");
                } else {
                    log.info('UsersService::getAccountUsers(appId) Succeeded');
                    deferred.resolve(res.data);
                }
            })
            .catch( status => {
                log.error('getAccountUsers(appId) $http error = ', status);
                deferred.reject(status);
            });
            return deferred.promise;
        }

        getUserAppRoles(appid, orgUserId){
            let deferred = this.$q.defer();
            this.$log.info('UsersService::getUserAppRoles');

            this.$http({
                method: "GET",
                url: this.conf.api.userAppRoles,
                params: {orgUserId: orgUserId, app: appid},
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("UsersService::getUserAppRoles: Failed");
                } else {
                    this.$log.info('UsersService::getUserAppRoles: Succeeded');
                    deferred.resolve(res.data);
                }
            })
            .catch( status => {
                this.$log.debug("UsersService::getUserAppRoles: status.headers(FEErrorString)");
                this.$log.debug('UsersService::getUserAppRoles status.headers(FEErrorString)', status.headers('FEErrorString'));
                deferred.reject(status);
            });
            return deferred.promise;
        }

        updateUserAppRoles(newUserAppRoles) {
            let deferred = this.$q.defer();
            this.$log.info('UsersService::updateUserAppRoles');
            this.$http({
                method: "PUT",
                url: this.conf.api.userAppRoles,
                data: newUserAppRoles,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then( res => {
                    deferred.resolve(res.data);
                })
                .catch( status => {
                    deferred.reject(status);
                });

            return deferred.promise;
        }

    }
    UsersService.$inject = ['$q', '$log', '$http', 'conf','uuid4'];
    angular.module('ecompApp').service('usersService', UsersService)
})();

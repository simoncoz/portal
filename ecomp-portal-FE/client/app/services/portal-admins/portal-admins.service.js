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
    class PortalAdminsService {
        constructor($q, $log, $http, conf, uuid) {
            this.$q = $q;
            this.$log = $log;
            this.$http = $http;
            this.conf = conf;
            this.uuid = uuid;
        }

        getPortalAdmins() {
            let deferred = this.$q.defer();
            this.$log.info('PortalAdminsService::get all portal admins list');
            this.$http({
                    url: this.conf.api.portalAdmins,
                    method: 'GET',
                    cache: false,
                    headers: {
                        'X-ECOMP-RequestID':this.uuid.generate()
                    }
                }).then(res => {
                    if (Object.keys(res.data).length == 0) {
                        deferred.reject("PortalAdminsService::getPortalAdmins Failed");
                    } else {
                        deferred.resolve(res.data);
                    }
                })
                .catch(status => {
                    deferred.reject(status);
                });
            return deferred.promise;
        }

        addPortalAdmin(userData) {
            let deferred = this.$q.defer();
            this.$log.info('PortalAdminsService:: add Portal Admin' + JSON.stringify(userData));
            this.$http({
                url: this.conf.api.portalAdmin,
                method: 'POST',
                cache: false,
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                },
                data: userData
            }).then(res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("PortalAdminsService::addPortalAdmin Failed");
                } else {
                    deferred.resolve(res.data);
                }
            })
                .catch(errRes => {
                    deferred.reject(errRes);
                });
            return deferred.promise;
        }

        removePortalAdmin(userId) {
            let deferred = this.$q.defer();
            let url = this.conf.api.portalAdmin + '/' + userId;
            this.$log.info('PortalAdminsService:: remove Portal Admin');
            this.$http({
                url: url,
                method: 'DELETE',
                cache: false,
                data: '',
                headers: {
                    'X-ECOMP-RequestID':this.uuid.generate()
                }
            }).then(res => {
                if (Object.keys(res.data).length == 0) {
                    deferred.reject("PortalAdminsService::removePortalAdmin Failed");
                } else {
                    deferred.resolve(res.data);
                }
            }).catch(errRes => {
                deferred.reject(errRes);
            });

            return deferred.promise;
        }
    }
    PortalAdminsService.$inject = ['$q', '$log', '$http', 'conf', 'uuid4'];
    angular.module('ecompApp').service('portalAdminsService', PortalAdminsService)
})();

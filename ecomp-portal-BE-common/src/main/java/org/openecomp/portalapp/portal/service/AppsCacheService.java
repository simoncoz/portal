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

package org.openecomp.portalapp.portal.service;

import org.openecomp.portalapp.portal.domain.EPApp;

public interface AppsCacheService {
	
	/**
	 * returns an app by id from the cache
	 * @param appId
	 * @return corresponding App
	 */
	EPApp getApp(Long appId);
	
	/**
	 * returns the corresponding application endpoint
	 * @param appId
	 * @return if appId exists in cache, then return corresponding application endpoint, null otherwise.
	 */
	String getAppEndpoint(Long appId);

	EPApp getAppForAnalytics(String appKey);
}
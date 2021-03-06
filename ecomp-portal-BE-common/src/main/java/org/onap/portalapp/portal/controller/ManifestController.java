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
package org.onap.portalapp.portal.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.onap.portalapp.portal.logging.aop.EPAuditLog;
import org.onap.portalapp.portal.service.ManifestService;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

/**
 * This controller responds to a request for the web application manifest,
 * returning a JSON with the information that was created at build time.
 * 
 * Manifest entries have names with hyphens, which means Javascript code can't
 * simply use the shorthand object.key; instead use object['key'].
 */
@RestController
@Configuration("manifestPortalController")
@EnableAspectJAutoProxy
@RequestMapping("/")
@EPAuditLog
public class ManifestController extends RestrictedBaseController {

	private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ManifestController.class);

	@Autowired
	private ManifestService manifestService;

	/**
	 * Gets the webapp manifest contents as a JSON object.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return A map of key-value pairs. On success:
	 * 
	 *         <pre>
	 * { "manifest" : { "key1": "value1", "key2": "value2" } }
	 *         </pre>
	 * 
	 *         On failure:
	 * 
	 *         <pre>
	 * { "error": "message" }
	 *         </pre>
	 */
	@RequestMapping(value = { "/portalApi/manifest" }, method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map<String, Object> getManifest(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			Attributes attributes = manifestService.getWebappManifest();
			response.put("manifest", attributes);
		} catch (Exception ex) {
			logger.error(EELFLoggerDelegate.errorLogger, "getManifest: failed to read manifest", ex);
			response.put("error", "failed to get manifest: " + ex.toString());
		}
		return response;
	}

}
/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the “License”);
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
 * under the Creative Commons License, Attribution 4.0 Intl. (the “License”);
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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.openecomp.portalapp.portal.scheduler.policy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.glassfish.jersey.client.ClientResponse;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
/*import org.openecomp.vid.policy.PolicyResponseWrapper;
import org.openecomp.vid.policy.PolicyUtil;
import org.openecomp.vid.policy.RestObject;*/

import com.fasterxml.jackson.databind.ObjectMapper;

public class PolicyUtil {
	
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PolicyUtil.class);
	
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	public static PolicyResponseWrapper wrapResponse ( String body, int statusCode ) {
		
		PolicyResponseWrapper w = new PolicyResponseWrapper();
		w.setStatus (statusCode);
		w.setEntity(body);
		
		return w;
	}
	
	public static PolicyResponseWrapper wrapResponse (ClientResponse cres) {	
		String resp_str = "";
		if ( cres != null ) {
			resp_str = cres.readEntity(String.class);
		}
		int statuscode = cres.getStatus();
		PolicyResponseWrapper w = PolicyUtil.wrapResponse ( resp_str, statuscode );
		return (w);
	}
	
	public static PolicyResponseWrapper wrapResponse (RestObject<String> rs) {	
		String resp_str = "";
		int status = 0;
		if ( rs != null ) {
			resp_str = rs.get();
			status = rs.getStatusCode();
		}
		PolicyResponseWrapper w = PolicyUtil.wrapResponse ( resp_str, status );
		return (w);
	}
	
	public static <T> String convertPojoToString ( T t ) throws com.fasterxml.jackson.core.JsonProcessingException {
		
		String methodName = "convertPojoToString";
		ObjectMapper mapper = new ObjectMapper();
		String r_json_str = "";
	    if ( t != null ) {
		    try {
		    	r_json_str = mapper.writeValueAsString(t);
		    }
		    catch ( com.fasterxml.jackson.core.JsonProcessingException j ) {
		    	logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " Unable to parse object as json");
		    }
	    }
	    return (r_json_str);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub		
	}
}

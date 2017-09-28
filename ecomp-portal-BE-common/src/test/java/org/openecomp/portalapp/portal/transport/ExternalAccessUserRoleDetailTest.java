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
package org.openecomp.portalapp.portal.transport;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.ExternalAccessUserRoleDetail;
import org.openecomp.portalapp.portal.transport.ExternalRoleDescription;

public class ExternalAccessUserRoleDetailTest {

	public ExternalAccessUserRoleDetail mockExternalAccessUserRoleDetail(){
		
		ExternalRoleDescription externalRoleDescription = new ExternalRoleDescription();
		externalRoleDescription.setId("test");
		externalRoleDescription.setName("test");
		externalRoleDescription.setActive("test");
		externalRoleDescription.setPriority("test");
		externalRoleDescription.setAppId("test");
		externalRoleDescription.setAppRoleId("test");
			    
		ExternalAccessUserRoleDetail externalAccessUserRoleDetail = new ExternalAccessUserRoleDetail("test", externalRoleDescription);
		
		externalAccessUserRoleDetail.setName("test");
		externalAccessUserRoleDetail.setDescription(externalRoleDescription);
		return externalAccessUserRoleDetail;
	}
	
	@Test
	public void externalAccessUserRoleDetailTest(){
		ExternalAccessUserRoleDetail externalAccessUserRoleDetail = mockExternalAccessUserRoleDetail();
		
		ExternalRoleDescription externalRoleDescription1 = new ExternalRoleDescription();
		externalRoleDescription1.setId("test");
		externalRoleDescription1.setName("test");
		externalRoleDescription1.setActive("test");
		externalRoleDescription1.setPriority("test");
		externalRoleDescription1.setAppId("test");
		externalRoleDescription1.setAppRoleId("test");
			    
		ExternalAccessUserRoleDetail externalAccessUserRoleDetail1 = new ExternalAccessUserRoleDetail("test", externalRoleDescription1);
		
		assertEquals(externalAccessUserRoleDetail.getName(), externalAccessUserRoleDetail1.getName());
		assertEquals(externalAccessUserRoleDetail.getDescription(), externalAccessUserRoleDetail1.getDescription());
		assertEquals(externalAccessUserRoleDetail.hashCode(), externalAccessUserRoleDetail1.hashCode());
		assertTrue(externalAccessUserRoleDetail.equals(externalAccessUserRoleDetail1));
	}
}

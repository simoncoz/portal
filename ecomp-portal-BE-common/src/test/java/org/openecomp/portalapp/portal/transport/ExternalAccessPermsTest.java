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
import org.openecomp.portalapp.portal.transport.ExternalAccessPerms;

public class ExternalAccessPermsTest {

	public ExternalAccessPerms mockExternalAccessPerms(){
		ExternalAccessPerms externalAccessPerms = new ExternalAccessPerms();
				
		externalAccessPerms.setType("test");
		externalAccessPerms.setInstance("test");
		externalAccessPerms.setAction("test");
		externalAccessPerms.setDescription("test");
		
		return externalAccessPerms;
	}
	
	@Test
	public void externalAccessPermsTest(){
		ExternalAccessPerms externalAccessPerms = mockExternalAccessPerms();
		
		ExternalAccessPerms externalAccessPerms1 = new ExternalAccessPerms("test", "test", "test");
		ExternalAccessPerms externalAccessPerms2 = new ExternalAccessPerms("test", "test", "test", "test");
		ExternalAccessPerms externalAccessPerms3 = new ExternalAccessPerms();
		externalAccessPerms3.setType("test");
		externalAccessPerms3.setInstance("test");
		externalAccessPerms3.setAction("test");
		externalAccessPerms3.setDescription("test");
		
		assertEquals(externalAccessPerms.getType(), "test");
		assertEquals(externalAccessPerms.getInstance(), "test");
		assertEquals(externalAccessPerms.getAction(), "test");
		assertEquals(externalAccessPerms.getDescription(), "test");
		assertEquals(externalAccessPerms.hashCode(), externalAccessPerms3.hashCode());
		
		assertTrue(externalAccessPerms1.equals(new ExternalAccessPerms("test", "test", "test")));
		assertTrue(externalAccessPerms2.equals(new ExternalAccessPerms("test", "test", "test", "test")));
	}
}

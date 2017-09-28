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
package org.openecomp.portalapp.portal.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.domain.CentralRoleFunction;

public class CentralRoleFunctionTest {

	public CentralRoleFunction mockCentralRoleFunction(){
		
		CentralRoleFunction centralRoleFunction = new CentralRoleFunction();
		   
		   centralRoleFunction.setCode("test");
		   centralRoleFunction.setName("test");
		   centralRoleFunction.setAppId((long)1);
		   centralRoleFunction.setEditUrl("test");
		   
		return centralRoleFunction;
	}
	
	@Test
	public void centralRoleFunctionTest(){
		CentralRoleFunction centralRoleFunction = mockCentralRoleFunction();
		
		assertEquals(centralRoleFunction.getAppId(), new Long(1));
		assertEquals(centralRoleFunction.getCode(), "test");
		assertEquals(centralRoleFunction.getName(), "test");
		assertEquals(centralRoleFunction.getEditUrl(), "test");
	}
}

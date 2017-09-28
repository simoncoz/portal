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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItem;

public class FunctionalMenuItemTest {
	
	public FunctionalMenuItem mockFunctionalMenuItem(){
		FunctionalMenuItem functionalMenuItem = new FunctionalMenuItem();
		
		List<Integer> roles = new ArrayList<Integer>();
		
		functionalMenuItem.setRestrictedApp(false);
		functionalMenuItem.setUrl("test");
		functionalMenuItem.setRoles(roles);
		
		return functionalMenuItem;
	}
	
	@Test
	public void functionalMenuItemTest(){
		FunctionalMenuItem functionalMenuItem = mockFunctionalMenuItem();
		
		FunctionalMenuItem functionalMenuItem1 = mockFunctionalMenuItem();
		
		List<Integer> roles = new ArrayList<Integer>();
		
		functionalMenuItem1.setRestrictedApp(false);
		functionalMenuItem1.setUrl("test");
		functionalMenuItem1.setRoles(roles);
		
		assertEquals(functionalMenuItem.getRoles(), functionalMenuItem1.getRoles());
		assertEquals(functionalMenuItem.toString(), "FunctionalMenuItem [menuId=null, column=null, text=null, parentMenuId=null, url=test, active_yn=null, appid=null, roles=[], restrictedApp=false]");
		// assertTrue(functionalMenuItem.normalize(), functionalMenuItem1.normalize());
	}

}

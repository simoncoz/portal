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
package org.openecomp.portalapp.portal.transport;

import java.io.Serializable;
import java.util.List;

// This type is used to read the Json in from the API call from the Front End
public class FunctionalMenuItemJson implements Serializable {
	private static final long serialVersionUID = 1L;

	public Long menuId;
	
	public Integer column;
	
	public String text;
	
	public Integer parentMenuId;
	
	public String url;

	public Integer appid;
	
	public List<Integer> roles;

	public void normalize() {
		if (this.column == null)
			this.column = new Integer(1);
		this.text = (this.text == null) ? "" : this.text.trim();
		if (this.parentMenuId == null)
			this.parentMenuId = new Integer(-1);
		this.url = (this.url == null) ? "" : this.url.trim();
	}

}

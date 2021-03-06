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
package org.onap.portalapp.portal.ecomp.model;

import java.io.Serializable;

public class UploadRoleFunctionExtSystem implements Serializable{
	
	private static final long serialVersionUID = -5543202387278296091L;
	private String roleName;
	private String type;
	private String instance;
	private String action;
	private String name;
	private boolean isGlobalRolePartnerFunc;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getIsGlobalRolePartnerFunc() {
		return isGlobalRolePartnerFunc;
	}
	public void setIsGlobalRolePartnerFunc(boolean isGlobalRolePartnerFunc) {
		this.isGlobalRolePartnerFunc = isGlobalRolePartnerFunc;
	}
	
	@Override
	public String toString() {
		return "UploadRoleFunctionExtSystem [roleName=" + roleName + ", type=" + type + ", instance=" + instance
				+ ", action=" + action + ", name=" + name + ", isGlobalRolePartnerFunc=" + isGlobalRolePartnerFunc
				+ "]";
	}
	
	
	
}

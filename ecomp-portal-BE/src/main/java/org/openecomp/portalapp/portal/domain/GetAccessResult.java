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
package org.openecomp.portalapp.portal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(GetAccessResultId.class)
public class GetAccessResult {
	
	@Id
	@Column(name="ecomp_function")
	private String ecompFunction;
 
	@Id
	@Column(name="app_name")
	private String appName; 
	
	@Column(name="app_mots_id",nullable=true)
	private Integer appMotsId;
	
	@Id
	@Column(name="role_name")
	private String roleName;
	
	public String getEcompFunction() {
		return ecompFunction;
	}
	public void setEcompFunction(String ecompFunction) {
		this.ecompFunction = ecompFunction;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Integer getAppMotsId() {
		return appMotsId;
	}
	public void setAppMotsId(Integer appMotsId) {
		this.appMotsId = appMotsId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}

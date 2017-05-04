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
package org.openecomp.portalapp.portal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude
public class GetAccessResult implements Serializable{


	private static final long serialVersionUID = 5239527705869613411L;

	@Id
	@Column(name="row_id")
	private String rowId;
	
	@Column(name="role_id")
	private Long roleId;
	
	@Column(name="ecomp_function",nullable=true)
	private String ecompFunction;
	
	@Column(name="app_name")
	private String appName; 
	
	@Column(name="app_mots_id",nullable=true)
	private Integer appMotsId;
	
	@Column(name="role_name")
	private String roleName;
	
	@Column(name="role_actv",nullable=true)
	private String roleActive;
	
	
	@Column(name="request_type",nullable=true)
	private String reqType;
	
	
	
	public final String getRowId() {
		return rowId;
	}
	public final void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public final Long getRoleId() {
		return roleId;
	}
	public final void setRoleId(Long roleId) {
		this.roleId = roleId;
	} 
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
	public final String getRoleActive() {
		return roleActive;
	}
	public final void setRoleActive(String roleActive) {
		this.roleActive = roleActive;
	}
	public final String getReqType() {
		return reqType;
	}
	public final void setReqType(String reqType) {
		this.reqType = reqType;
	}
	
}
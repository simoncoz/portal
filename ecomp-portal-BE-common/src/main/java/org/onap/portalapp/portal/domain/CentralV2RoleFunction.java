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
package org.onap.portalapp.portal.domain;

import java.io.Serializable;

import org.onap.portalsdk.core.domain.support.DomainVo;

import com.fasterxml.jackson.annotation.JsonIgnore;
 
@SuppressWarnings("rawtypes")
public class CentralV2RoleFunction extends DomainVo implements Serializable, Comparable{
	
	   /**
	 * 
	 */
	private static final long serialVersionUID = -4018975640065252688L;
	   private String code;
	   private String name;
	   @JsonIgnore
	   private Long appId;
	   @JsonIgnore
	   private Long roleId;
	   private String type;
	   private String action;
	   private String editUrl;
	   
	   
	public CentralV2RoleFunction() {
	
	}
	
	
	public CentralV2RoleFunction(Long id, String code, String name, Long appId, String type, String action,String editUrl) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.appId = appId;
		this.type = type;
		this.action = action;
		this.editUrl = editUrl;
	}
	
	public CentralV2RoleFunction(Long id, String code, String name, Long appId,String editUrl) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.appId = appId;
		this.editUrl = editUrl;
	}
	
	

	public CentralV2RoleFunction(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the appId
	 */
	public Long getAppId() {
		return appId;
	}
	/**
	 * @param appId the appId to set
	 */
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	/**
	 * @return the editUrl
	 */
	public String getEditUrl() {
		return editUrl;
	}
	/**
	 * @param editUrl the editUrl to set
	 */
	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}
	
	
    /**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}


	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}


	public int compareTo(Object obj){
        String c1 = getName();
        String c2 = ((CentralV2RoleFunction)obj).getName();

        return (c1 == null || c2 == null) ? 1 : c1.compareTo(c2);
      }
	
	
   
}

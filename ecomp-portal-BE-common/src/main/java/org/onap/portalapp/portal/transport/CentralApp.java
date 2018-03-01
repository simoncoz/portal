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
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.portal.transport;

import java.io.Serializable;
import java.util.Date;

public class CentralApp implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3325965646585871632L;
	private Long id;
	private Date created;
	private Date modified;
	private Long createdId;
	private Long modifiedId;
	private Long rowNum;
	private String name; // app_name
	private String imageUrl; // app_image_url
	private String description; // app_description
	private String notes; // app_notes
	private String url; // app_url
	private String alternateUrl; // app_alternate_url
	private String restEndpoint; // app_rest_endpoint
	private String mlAppName; // ml_app_name
	private String mlAppAdminId; // ml_app_admin_id;
	private String motsId; // mots_id
	private String appPassword; // app_password
	private String open;
	private String enabled;
	private byte[] thumbnail;
	private String username; // app_username
	private String uebKey; // ueb_key
	private String uebSecret; // ueb_secret
	private String uebTopicName; // ueb_topic_name
	
	public CentralApp(){
		
	}

	public CentralApp(Long id, Date created, Date modified, Long createdId, Long modifiedId, Long rowNum, String name,
			String imageUrl, String description, String notes, String url, String alternateUrl, String restEndpoint,
			String mlAppName, String mlAppAdminId, String motsId, String appPassword, String open, String enabled,
			byte[] thumbnail, String username, String uebKey, String uebSecret, String uebTopicName) {
		super();
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.createdId = createdId;
		this.modifiedId = modifiedId;
		this.rowNum = rowNum;
		this.name = name;
		this.imageUrl = imageUrl;
		this.description = description;
		this.notes = notes;
		this.url = url;
		this.alternateUrl = alternateUrl;
		this.restEndpoint = restEndpoint;
		this.mlAppName = mlAppName;
		this.mlAppAdminId = mlAppAdminId;
		this.motsId = motsId;
		this.appPassword = appPassword;
		this.open = open;
		this.enabled = enabled;
		this.thumbnail = thumbnail;
		this.username = username;
		this.uebKey = uebKey;
		this.uebSecret = uebSecret;
		this.uebTopicName = uebTopicName;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Long getCreatedId() {
		return createdId;
	}
	public void setCreatedId(Long createdId) {
		this.createdId = createdId;
	}
	public Long getModifiedId() {
		return modifiedId;
	}
	public void setModifiedId(Long modifiedId) {
		this.modifiedId = modifiedId;
	}
	public Long getRowNum() {
		return rowNum;
	}
	public void setRowNum(Long rowNum) {
		this.rowNum = rowNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAlternateUrl() {
		return alternateUrl;
	}
	public void setAlternateUrl(String alternateUrl) {
		this.alternateUrl = alternateUrl;
	}
	public String getRestEndpoint() {
		return restEndpoint;
	}
	public void setRestEndpoint(String restEndpoint) {
		this.restEndpoint = restEndpoint;
	}
	public String getMlAppName() {
		return mlAppName;
	}
	public void setMlAppName(String mlAppName) {
		this.mlAppName = mlAppName;
	}
	public String getMlAppAdminId() {
		return mlAppAdminId;
	}
	public void setMlAppAdminId(String mlAppAdminId) {
		this.mlAppAdminId = mlAppAdminId;
	}
	public String getMotsId() {
		return motsId;
	}
	public void setMotsId(String motsId) {
		this.motsId = motsId;
	}
	public String getAppPassword() {
		return appPassword;
	}
	public void setAppPassword(String appPassword) {
		this.appPassword = appPassword;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public byte[] getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUebKey() {
		return uebKey;
	}
	public void setUebKey(String uebKey) {
		this.uebKey = uebKey;
	}
	public String getUebSecret() {
		return uebSecret;
	}
	public void setUebSecret(String uebSecret) {
		this.uebSecret = uebSecret;
	}
	public String getUebTopicName() {
		return uebTopicName;
	}
	public void setUebTopicName(String uebTopicName) {
		this.uebTopicName = uebTopicName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alternateUrl == null) ? 0 : alternateUrl.hashCode());
		result = prime * result + ((appPassword == null) ? 0 : appPassword.hashCode());
		result = prime * result + ((createdId == null) ? 0 : createdId.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result + ((mlAppAdminId == null) ? 0 : mlAppAdminId.hashCode());
		result = prime * result + ((mlAppName == null) ? 0 : mlAppName.hashCode());
		result = prime * result + ((modifiedId == null) ? 0 : modifiedId.hashCode());
		result = prime * result + ((motsId == null) ? 0 : motsId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((open == null) ? 0 : open.hashCode());
		result = prime * result + ((restEndpoint == null) ? 0 : restEndpoint.hashCode());
		result = prime * result + ((rowNum == null) ? 0 : rowNum.hashCode());
		result = prime * result + ((uebKey == null) ? 0 : uebKey.hashCode());
		result = prime * result + ((uebSecret == null) ? 0 : uebSecret.hashCode());
		result = prime * result + ((uebTopicName == null) ? 0 : uebTopicName.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CentralApp other = (CentralApp) obj;
		if (alternateUrl == null) {
			if (other.alternateUrl != null)
				return false;
		} else if (!alternateUrl.equals(other.alternateUrl))
			return false;
		if (appPassword == null) {
			if (other.appPassword != null)
				return false;
		} else if (!appPassword.equals(other.appPassword))
			return false;
		if (createdId == null) {
			if (other.createdId != null)
				return false;
		} else if (!createdId.equals(other.createdId))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (enabled == null) {
			if (other.enabled != null)
				return false;
		} else if (!enabled.equals(other.enabled))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		if (mlAppAdminId == null) {
			if (other.mlAppAdminId != null)
				return false;
		} else if (!mlAppAdminId.equals(other.mlAppAdminId))
			return false;
		if (mlAppName == null) {
			if (other.mlAppName != null)
				return false;
		} else if (!mlAppName.equals(other.mlAppName))
			return false;
		if (modifiedId == null) {
			if (other.modifiedId != null)
				return false;
		} else if (!modifiedId.equals(other.modifiedId))
			return false;
		if (motsId == null) {
			if (other.motsId != null)
				return false;
		} else if (!motsId.equals(other.motsId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (open == null) {
			if (other.open != null)
				return false;
		} else if (!open.equals(other.open))
			return false;
		if (restEndpoint == null) {
			if (other.restEndpoint != null)
				return false;
		} else if (!restEndpoint.equals(other.restEndpoint))
			return false;
		if (rowNum == null) {
			if (other.rowNum != null)
				return false;
		} else if (!rowNum.equals(other.rowNum))
			return false;
		if (uebKey == null) {
			if (other.uebKey != null)
				return false;
		} else if (!uebKey.equals(other.uebKey))
			return false;
		if (uebSecret == null) {
			if (other.uebSecret != null)
				return false;
		} else if (!uebSecret.equals(other.uebSecret))
			return false;
		if (uebTopicName == null) {
			if (other.uebTopicName != null)
				return false;
		} else if (!uebTopicName.equals(other.uebTopicName))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
}
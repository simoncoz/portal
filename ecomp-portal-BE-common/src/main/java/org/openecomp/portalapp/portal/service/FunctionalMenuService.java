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
package org.openecomp.portalapp.portal.service;

import java.util.List;

import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.FunctionalMenuItemWithAppID;
import org.openecomp.portalapp.portal.transport.BusinessCardApplicationRole;
import org.openecomp.portalapp.portal.transport.FavoritesFunctionalMenuItem;
import org.openecomp.portalapp.portal.transport.FavoritesFunctionalMenuItemJson;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItem;
import org.openecomp.portalapp.portal.transport.FunctionalMenuItemWithRoles;
import org.openecomp.portalapp.portal.transport.FunctionalMenuRole;

public interface FunctionalMenuService {
	List<FunctionalMenuItem> getFunctionalMenuItems (EPUser user);
	// return all active menu items
	List<FunctionalMenuItem> getFunctionalMenuItems ();
	// return all active menu items. If all is true, return all active and inactive menu items.
	List<FunctionalMenuItem> getFunctionalMenuItems(Boolean all);
	// return all active Functional menu items for Notification Tree in User Notification . If all is true, return all active menu items.
	List<FunctionalMenuItem> getFunctionalMenuItemsForNotificationTree(Boolean all);
	List<FunctionalMenuItem> getFunctionalMenuItemsForApp (Integer appId);
	List<FunctionalMenuItem> getFunctionalMenuItemsForUser (String orgUserId);
	FunctionalMenuItem getFunctionalMenuItemDetails (Integer menuid);
	FieldsValidator createFunctionalMenuItem (FunctionalMenuItemWithRoles menuItemJson);
	FieldsValidator editFunctionalMenuItem (FunctionalMenuItemWithRoles menuItemJson);
	FieldsValidator deleteFunctionalMenuItem (Long menuId);
	FieldsValidator regenerateAncestorTable ();
	//Methods relevant to favorites
	FieldsValidator setFavoriteItem(FavoritesFunctionalMenuItem menuItemJson);
	List<FavoritesFunctionalMenuItemJson> getFavoriteItems(Long userId);
	FieldsValidator removeFavoriteItem (Long userId, Long menuId);
	List<FunctionalMenuItem> transformFunctionalMenuItemWithAppIDToFunctionalMenuItem(List<FunctionalMenuItemWithAppID> functionalMenuItemWithAppIDList);
	List<FunctionalMenuRole> getFunctionalMenuRole();
	//Assign URLs under Help Menu
	void assignHelpURLs(List<FunctionalMenuItem> menuItems);
	List<BusinessCardApplicationRole> getUserAppRolesList(String userId);
}
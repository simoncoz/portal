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
package org.onap.portalapp.portal.service;

import java.util.List;

import org.onap.portalapp.portal.domain.AdminUserApplications;
import org.onap.portalapp.portal.domain.AppIdAndNameTransportModel;
import org.onap.portalapp.portal.domain.AppsResponse;
import org.onap.portalapp.portal.domain.EPApp;
import org.onap.portalapp.portal.domain.EPUser;
import org.onap.portalapp.portal.domain.EcompApp;
import org.onap.portalapp.portal.domain.UserRoles;
import org.onap.portalapp.portal.ecomp.model.AppCatalogItem;
import org.onap.portalapp.portal.transport.EPAppsManualPreference;
import org.onap.portalapp.portal.transport.EPAppsSortPreference;
import org.onap.portalapp.portal.transport.EPDeleteAppsManualSortPref;
import org.onap.portalapp.portal.transport.EPWidgetsSortPreference;
import org.onap.portalapp.portal.transport.FieldsValidator;
import org.onap.portalapp.portal.transport.LocalRole;
import org.onap.portalapp.portal.transport.OnboardingApp;

public interface EPAppService {

	/**
	 * Get all applications adminId is an admin
	 * 
	 * @param user
	 *            the admin user
	 * @return the admin's applications
	 */
	List<EPApp> getUserAsAdminApps(EPUser user);

	List<EPApp> getUserByOrgUserIdAsAdminApps(String orgUserId);

	/**
	 * Gets all rows and all fields from the fn_app table.
	 * 
	 * @return list of EPApp objects
	 */
	List<EPApp> getAppsFullList();

	/**
	 * Gets all rows and most fields from the fn_app table.
	 * 
	 * @return list of EcompApp objects.
	 */
	List<EcompApp> getEcompAppAppsFullList();

	/**
	 * Get apps with app app admins
	 * 
	 * @return List of AdminUserApplications
	 */
	List<AdminUserApplications> getAppsAdmins();

	/**
	 * Get all apps from fn_app table (index, name, title only).
	 * 
	 * @param all
	 *            If all is true, returns active and inactive apps; otherwise,
	 *            just active apps.
	 * @return List of AppsResponse objects.
	 */
	List<AppsResponse> getAllApps(Boolean all);

	UserRoles getUserProfile(String loginId);

	UserRoles getUserProfileNormalized(EPUser user);

	List<LocalRole> getAppRoles(Long appId);

	List<AppIdAndNameTransportModel> getAdminApps(EPUser user);

	List<AppIdAndNameTransportModel> getAppsForSuperAdminAndAccountAdmin(EPUser user);

	/**
	 * Gets the applications accessible to the specified user, which includes
	 * all enabled open applications, plus all enabled applications for which
	 * the user has a defined role for that app.
	 * 
	 * @param user
	 *            EPUser object with the user's Org User ID
	 * @return the user's list of applications, which may be empty.
	 */
	List<EPApp> getUserApps(EPUser user);

	/**
	 * Gets the user-personalized list of applications for the Portal (super)
	 * admin, which includes enabled open applications, enabled applications for
	 * which the user has a defined role for that app, and/or enabled
	 * applications which the user has chosen to show.
	 * 
	 * @param user
	 *            EPUser object with the user's Org User ID
	 * @return the user's personalized list of applications, which may be empty.
	 */
	List<EPApp> getPersAdminApps(EPUser user);

	/**
	 * Gets the user-personalized list of accessible applications, which
	 * includes enabled open applications and/or enabled applications for which
	 * the user has a defined role for that app. Personalization means the user
	 * can indicate an accessible application should be excluded from this
	 * result.
	 * 
	 * @param user
	 *            EPUser object with the user's Org User ID
	 * @return the user's personalized list of applications, which may be empty.
	 */
	List<EPApp> getPersUserApps(EPUser user);

	/**
	 * Gets the application catalog for the specified user who is a super admin.
	 * This includes all enabled applications. Each item indicates whether the
	 * user has access (open or via a role), and whether the application is
	 * selected for showing in the user's home (applications) page. Admin sees
	 * slightly different behavior - can force an app onto the home page using
	 * the personalization feature (user-app-selection table).
	 * 
	 * @param user
	 * @return list of all enabled applications, which may be empty
	 */
	List<AppCatalogItem> getAdminAppCatalog(EPUser user);

	/**
	 * Gets the application catalog for the specified user, who is a regular
	 * user. This includes all enabled applications. Each item indicates whether
	 * the user has access (open or via a role), and whether the application is
	 * selected for showing in the user's home (applications) page.
	 * 
	 * @param user
	 * @return list of all enabled applications, which may be empty
	 */
	List<AppCatalogItem> getUserAppCatalog(EPUser user);

	List<OnboardingApp> getOnboardingApps();

	List<OnboardingApp> getEnabledNonOpenOnboardingApps();

	FieldsValidator modifyOnboardingApp(OnboardingApp modifiedOnboardingApp, EPUser user);

	FieldsValidator addOnboardingApp(OnboardingApp newOnboardingApp, EPUser user);

	/**
	 * Deletes the specified application from all tables where the app_id is
	 * used, and ultimately from the fn_app table.
	 * 
	 * @param user
	 *            Must be Portal (super) administrator
	 * @param onboardingAppId
	 *            ID of application to be deleted
	 * @return Status code
	 */
	FieldsValidator deleteOnboardingApp(EPUser user, Long onboardingAppId);

	List<EcompApp> transformAppsToEcompApps(List<EPApp> appsList);

	EPApp getApp(Long appId);

	EPApp getAppDetail(String appName);
	
	/**
	 * 
	 * It return app information 
	 * 
	 * @param appName it contains application name
	 * @return EPApp 
	 */
	EPApp getAppDetailByAppName(String appName);

	List<EPApp> getAppsOrderByName(EPUser user);

	FieldsValidator saveAppsSortPreference(EPAppsSortPreference appsSortPreference, EPUser user);

	FieldsValidator saveAppsSortManual(List<EPAppsManualPreference> appsSortManual, EPUser user);

	FieldsValidator saveWidgetsSortManual(List<EPWidgetsSortPreference> widgetsSortManual, EPUser user);

	/**
	 * Deletes the sort order of user apps by sort manual preference
	 * 
	 * @param delAppSortManual
	 *            User Apps Data
	 * @param user
	 *            LoggedIn User Data
	 * @return FieldsValidator
	 */
	FieldsValidator deleteUserAppSortManual(EPDeleteAppsManualSortPref delAppSortManual, EPUser user);

	FieldsValidator deleteUserWidgetSortPref(List<EPWidgetsSortPreference> delWidgetSortPref, EPUser user);

	String getUserAppsSortTypePreference(EPUser user);

	List<EPApp> getAppsOrderByLastUsed(EPUser user);

	List<EPApp> getAppsOrderByMostUsed(EPUser user);

	List<EPApp> getAppsOrderByManual(EPUser user);

	List<EPApp> getUserRemoteApps(String id);

	void createOnboardingFromApp(EPApp app, OnboardingApp onboardingApp);

	UserRoles getUserProfileNormalizedForLeftMenu(EPUser user);

	UserRoles getUserProfileForLeftMenu(String loginId);

	UserRoles getUserProfileForRolesLeftMenu(String loginId);

	UserRoles getUserProfileNormalizedForRolesLeftMenu(EPUser user);

}

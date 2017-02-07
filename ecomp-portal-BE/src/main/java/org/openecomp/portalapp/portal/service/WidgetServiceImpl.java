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
package org.openecomp.portalapp.portal.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.domain.EPUserApp;
import org.openecomp.portalapp.portal.domain.Widget;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.OnboardingWidget;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("widgetService")
@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class WidgetServiceImpl implements WidgetService {

	private static final String baseSqlToken = " widget.WIDGET_ID, widget.WDG_NAME, widget.APP_ID, app.APP_NAME, widget.WDG_WIDTH, widget.WDG_HEIGHT, widget.WDG_URL"
			+ " from FN_WIDGET widget join FN_APP app ON widget.APP_ID = app.APP_ID";

	private String validAppsFilter = "";

	private Long LONG_ECOMP_APP_ID = 1L;
	private Long ACCOUNT_ADMIN_ROLE_ID = 999L;
	private static final Long DUBLICATED_FIELD_VALUE_ECOMP_ERROR = new Long(EPSystemProperties.DUBLICATED_FIELD_VALUE_ECOMP_ERROR);

	private static final String urlField = "url";

	private static final String nameField = "name";
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(WidgetServiceImpl.class);

	@Autowired
	AdminRolesService adminRolesService;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DataAccessService dataAccessService;

	@PostConstruct
	private void init() {
		try {
			validAppsFilter = " AND app.ENABLED = 'Y' AND app.APP_ID != " + SystemProperties.getProperty(EPSystemProperties.ECOMP_APP_ID);
			ACCOUNT_ADMIN_ROLE_ID = Long.valueOf(SystemProperties.getProperty(EPSystemProperties.ACCOUNT_ADMIN_ROLE_ID));
			LONG_ECOMP_APP_ID = Long.valueOf(SystemProperties.getProperty(EPSystemProperties.ECOMP_APP_ID));
		} catch(Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, EcompPortalUtils.getStackTrace(e));
		}
	}
	
	private String sqlWidgetsForAllApps() {
		return "SELECT" + baseSqlToken + validAppsFilter;
	}

	private String sqlWidgetsForAllAppsWhereUserIsAdmin(Long userId) {
		return "SELECT" + baseSqlToken + " join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = app.APP_ID where FN_USER_ROLE.USER_ID = " + userId
				+ " AND FN_USER_ROLE.ROLE_ID = " + ACCOUNT_ADMIN_ROLE_ID + validAppsFilter;
	}

	private String sqlWidgetsForAllAppsWhereUserHasAnyRole(Long userId) {
		return "SELECT DISTINCT" + baseSqlToken + " join FN_USER_ROLE ON FN_USER_ROLE.APP_ID = app.APP_ID where FN_USER_ROLE.USER_ID = "
				+ userId + validAppsFilter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OnboardingWidget> getOnboardingWidgets(EPUser user, boolean managed) {
		List<OnboardingWidget> onboardingWidgets = new ArrayList<OnboardingWidget>();
		String sql = null;
		if (adminRolesService.isSuperAdmin(user)) {
			sql = this.sqlWidgetsForAllApps();
		} else if (managed) {
			if (adminRolesService.isAccountAdmin(user)) {
				sql = this.sqlWidgetsForAllAppsWhereUserIsAdmin(user.getId());
			}
		} else if (adminRolesService.isAccountAdmin(user) || adminRolesService.isUser(user)) {
			sql = this.sqlWidgetsForAllAppsWhereUserHasAnyRole(user.getId());
		}
		if (sql != null) {
			onboardingWidgets = dataAccessService.executeSQLQuery(sql, OnboardingWidget.class, null);
		}
		return onboardingWidgets;
	}

	private static final Object syncRests = new Object();

	private boolean isUserAdminOfAppForWidget(boolean superAdmin, Long userId, Long appId) {
		if (!superAdmin) {
			@SuppressWarnings("unchecked")
			List<EPUserApp> userRoles = dataAccessService.getList(EPUserApp.class,
					" where id = " + userId + " and role.id = " + ACCOUNT_ADMIN_ROLE_ID + " and app.id = " + appId, null, null);
			return (userRoles.size() > 0);
		}
		return true;
	}

	private void validateOnboardingWidget(OnboardingWidget onboardingWidget, FieldsValidator fieldsValidator) {
		@SuppressWarnings("unchecked")
		List<Widget> widgets = dataAccessService.getList(Widget.class,
				" where url = '" + onboardingWidget.url + "'" + " or name = '" + onboardingWidget.name + "'", null, null);
		boolean dublicatedUrl = false;
		boolean dublicatedName = false;
		for (Widget widget : widgets) {
			if (onboardingWidget.id != null && onboardingWidget.id.equals(widget.getId())) {
				// widget should not be compared with itself
				continue;
			}
			if (!dublicatedUrl && widget.getUrl().equals(onboardingWidget.url)) {
				dublicatedUrl = true;
				if (dublicatedName) {
					break;
				}
			}
			if (!dublicatedName && widget.getName().equalsIgnoreCase(onboardingWidget.name) && widget.getAppId().equals(onboardingWidget.appId)) {
				dublicatedName = true;
				if (dublicatedUrl) {
					break;
				}
			}
		}
		if (dublicatedUrl || dublicatedName) {
			if (dublicatedUrl) {
				fieldsValidator.addProblematicFieldName(urlField);
			}
			if (dublicatedName) {
				fieldsValidator.addProblematicFieldName(nameField);
			}
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_CONFLICT);
			fieldsValidator.errorCode = DUBLICATED_FIELD_VALUE_ECOMP_ERROR;
		}
	}

	private void applyOnboardingWidget(OnboardingWidget onboardingWidget, FieldsValidator fieldsValidator) {
		boolean result = false;
		Session localSession = null;
		Transaction transaction = null;
		try {
			localSession = sessionFactory.openSession();
			transaction = localSession.beginTransaction();
			Widget widget;
			if (onboardingWidget.id == null) {
				widget = new Widget();
			} else {
				widget = (Widget) localSession.get(Widget.class, onboardingWidget.id);
			}
			widget.setAppId(onboardingWidget.appId);
			widget.setName(onboardingWidget.name);
			widget.setWidth(onboardingWidget.width);
			widget.setHeight(onboardingWidget.height);
			widget.setUrl(onboardingWidget.url);
			localSession.saveOrUpdate(widget);
			transaction.commit();
			result = true;
		} catch (Exception e) {
			EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
			EcompPortalUtils.rollbackTransaction(transaction, "applyOnboardingWidget rollback, exception = " + e);
		} finally {
			EcompPortalUtils.closeLocalSession(localSession, "applyOnboardingWidget");
		}
		if (!result) {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private FieldsValidator updateOrSaveWidget(boolean superAdmin, Long userId, OnboardingWidget onboardingWidget) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		if (!this.isUserAdminOfAppForWidget(superAdmin, userId, onboardingWidget.appId)) {
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_FORBIDDEN);
			return fieldsValidator;
		}
		synchronized (syncRests) {
			// onboardingWidget.id is null for POST and not null for PUT
			if (onboardingWidget.id == null) {
				this.validateOnboardingWidget(onboardingWidget, fieldsValidator);
			} else {
				Widget widget = (Widget) dataAccessService.getDomainObject(Widget.class, onboardingWidget.id, null);
				if (widget == null || widget.getId() == null) {
					// Widget not found
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_NOT_FOUND);
					return fieldsValidator;
				}
				this.validateOnboardingWidget(onboardingWidget, fieldsValidator);
			}
			if (fieldsValidator.httpStatusCode.intValue() == HttpServletResponse.SC_OK) {
				this.applyOnboardingWidget(onboardingWidget, fieldsValidator);
			}
		}
		return fieldsValidator;
	}

	@Override
	public FieldsValidator setOnboardingWidget(EPUser user, OnboardingWidget onboardingWidget) {
		if (onboardingWidget.name.length() == 0 || onboardingWidget.url.length() == 0 || onboardingWidget.appId == null
				|| onboardingWidget.appId.equals(LONG_ECOMP_APP_ID) || onboardingWidget.width.intValue() <= 0 || onboardingWidget.height.intValue() <= 0) {
			if (onboardingWidget.appId.equals(LONG_ECOMP_APP_ID)) {
				// logger.error("Alarm!!! Security breach attempt on user " + user.getFullName() + ", userId = " + user.getUserId());
			}
			FieldsValidator fieldsValidator = new FieldsValidator();
			fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_BAD_REQUEST);
			return fieldsValidator;
		}
		return this.updateOrSaveWidget(adminRolesService.isSuperAdmin(user), user.getId(), onboardingWidget);
	}

	@Override
	public FieldsValidator deleteOnboardingWidget(EPUser user, Long onboardingWidgetId) {
		FieldsValidator fieldsValidator = new FieldsValidator();
		synchronized (syncRests) {
			Widget widget = (Widget) dataAccessService.getDomainObject(Widget.class, onboardingWidgetId, null);
			if (widget != null && widget.getId() != null) { // widget exists
				if (!this.isUserAdminOfAppForWidget(adminRolesService.isSuperAdmin(user), user.getId(), widget.getAppId())) {
					fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_FORBIDDEN);
				} else {
					boolean result = false;
					Session localSession = null;
					Transaction transaction = null;
					try {
						localSession = sessionFactory.openSession();
						transaction = localSession.beginTransaction();
						localSession.delete(localSession.get(Widget.class, onboardingWidgetId));
						transaction.commit();
						result = true;
					} catch (Exception e) {
						EPLogUtil.logEcompError(EPAppMessagesEnum.BeDaoSystemError);
						EcompPortalUtils.rollbackTransaction(transaction, "deleteOnboardingWidget rollback, exception = " + e);
					} finally {
						EcompPortalUtils.closeLocalSession(localSession, "deleteOnboardingWidget");
					}
					if (!result) {
						fieldsValidator.httpStatusCode = new Long(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
				}
			}
		}
		return fieldsValidator;
	}

}

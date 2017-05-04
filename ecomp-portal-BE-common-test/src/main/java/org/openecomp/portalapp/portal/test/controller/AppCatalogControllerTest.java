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
package org.openecomp.portalapp.portal.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openecomp.portalapp.portal.controller.AppCatalogController;
import org.openecomp.portalapp.portal.domain.EPApp;
import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.ecomp.model.AppCatalogItem;
import org.openecomp.portalapp.portal.service.AdminRolesService;
import org.openecomp.portalapp.portal.service.AdminRolesServiceImpl;
import org.openecomp.portalapp.portal.service.EPAppCommonServiceImpl;
import org.openecomp.portalapp.portal.service.EPAppService;
import org.openecomp.portalapp.portal.service.PersUserAppService;
import org.openecomp.portalapp.portal.service.PersUserAppServiceImpl;
import org.openecomp.portalapp.portal.test.core.MockEPUser;
import org.openecomp.portalapp.portal.test.framework.MockitoTestSuite;
import org.openecomp.portalapp.portal.transport.AppCatalogPersonalization;
import org.openecomp.portalapp.portal.transport.FieldsValidator;
import org.openecomp.portalapp.portal.transport.FieldsValidator.FieldName;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.util.SystemProperties;

public class AppCatalogControllerTest extends MockitoTestSuite {

	@Mock
	AdminRolesService adminRolesService = new AdminRolesServiceImpl();

	@Mock
	EPAppService appService = new EPAppCommonServiceImpl();

	@InjectMocks
	AppCatalogController appCatalogController = new AppCatalogController();

	PersUserAppService persUserAppService = Mockito.spy(new PersUserAppServiceImpl());

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	MockitoTestSuite mockitoTestSuite = new MockitoTestSuite();

	HttpServletRequest mockedRequest = mockitoTestSuite.getMockedRequest();
	HttpServletResponse mockedResponse = mockitoTestSuite.getMockedResponse();

	@Mock
	EPUserUtils ePUserUtils = new EPUserUtils();

	@Mock
	EPUser epuser;

	NullPointerException nullPointerException = new NullPointerException();

	MockEPUser mockUser = new MockEPUser();

	public AppCatalogItem mockAppCatalogItem() {
		AppCatalogItem appCatalogItem = new AppCatalogItem();
		appCatalogItem.setId((long) 1);
		appCatalogItem.setName("Ecomp Portal");
		appCatalogItem.setImageUrl("Test_URL");
		appCatalogItem.setDescription("Testing");
		appCatalogItem.setNotes("Test");
		appCatalogItem.setUrl("test");
		appCatalogItem.setAlternateUrl("test");
		appCatalogItem.setRestricted(false);
		appCatalogItem.setOpen(false);
		appCatalogItem.setAccess(true);
		appCatalogItem.setSelect(true);
		appCatalogItem.setPending(false);

		return appCatalogItem;
	}

	@Test
	public void getAppCatalogTestIfUserNotAdmin() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<AppCatalogItem> actualAppCatalogList = null;

		List<AppCatalogItem> expectedAppCatalog = new ArrayList<>();

		AppCatalogItem appCatalogItem = mockAppCatalogItem();
		expectedAppCatalog.add(appCatalogItem);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);
		Mockito.when(appService.getUserAppCatalog(user)).thenReturn(expectedAppCatalog);
		actualAppCatalogList = appCatalogController.getAppCatalog(mockedRequest, mockedResponse);

		assertTrue(actualAppCatalogList.contains(appCatalogItem));

	}

	@Test
	public void getAppCatalogTestIfUserIsAdmin() throws IOException {
		EPUser user = mockUser.mockEPUser();
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<AppCatalogItem> actualAppCatalogList = null;

		List<AppCatalogItem> expectedAppCatalog = new ArrayList<>();

		AppCatalogItem appCatalogItem = mockAppCatalogItem();

		expectedAppCatalog.add(appCatalogItem);
		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(true);
		Mockito.when(appService.getAdminAppCatalog(user)).thenReturn(expectedAppCatalog);
		actualAppCatalogList = appCatalogController.getAppCatalog(mockedRequest, mockedResponse);

		assertTrue(actualAppCatalogList.contains(appCatalogItem));

	}

	@Test
	public void getAppCatalogTestIfUserisNull() throws IOException {
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(null);
		List<AppCatalogItem> actualAppCatalogList = new ArrayList<>();
		;
		actualAppCatalogList = appCatalogController.getAppCatalog(mockedRequest, mockedResponse);
		assertNull(actualAppCatalogList);

	}

	@Test
	public void getAppCatalogTestIfUserThrowsExceptionTest() throws IOException {
		EPUser user = new EPUser();
		user.setFirstName("test");
		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		List<AppCatalogItem> actualAppCatalogList = new ArrayList<>();
		;

		Mockito.when(adminRolesService.isSuperAdmin(user)).thenReturn(false);

		Mockito.when(appCatalogController.getAppCatalog(mockedRequest, mockedResponse)).thenThrow(nullPointerException);

		actualAppCatalogList = appCatalogController.getAppCatalog(mockedRequest, mockedResponse);
		assertNull(actualAppCatalogList);

	}

	@Test
	public void putAppCatalogSelectionTestWhenAppIsNull() throws IOException {

		AppCatalogPersonalization persRequest = new AppCatalogPersonalization();
		persRequest.setAppId((long) 1);
		persRequest.setPending(false);
		persRequest.setSelect(false);

		EPUser user = mockUser.mockEPUser();

		FieldsValidator expectedFieldValidator = new FieldsValidator();

		FieldsValidator actualFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		;

		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);

		EPApp app = null;

		Mockito.when(appService.getApp(persRequest.getAppId())).thenReturn(app);

		HttpSession session = mockedRequest.getSession();
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);

		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		actualFieldValidator = appCatalogController.putAppCatalogSelection(mockedRequest, persRequest, mockedResponse);
		assertEquals(expectedFieldValidator, actualFieldValidator);

	}

	@Test
	public void putAppCatalogSelectionTest() throws IOException {

		AppCatalogPersonalization persRequest = new AppCatalogPersonalization();
		persRequest.setAppId((long) 1);
		persRequest.setPending(false);
		persRequest.setSelect(false);

		EPUser user = mockUser.mockEPUser();

		FieldsValidator expectedFieldValidator = new FieldsValidator();

		FieldsValidator actualFieldValidator = new FieldsValidator();
		List<FieldName> fields = new ArrayList<>();
		;

		expectedFieldValidator.setHttpStatusCode((long) 200);
		expectedFieldValidator.setFields(fields);
		expectedFieldValidator.setErrorCode(null);

		EPApp app = new EPApp();

		app.setName("Test");
		app.setImageUrl("test");
		app.setDescription("test");
		app.setNotes("test");
		app.setUrl("test");
		app.setId((long) 1);
		app.setAppRestEndpoint("test");
		app.setAlternateUrl("test");
		app.setName("test");
		app.setMlAppName("test");
		app.setMlAppAdminId("test");
		app.setUsername("test");
		app.setAppPassword("test");
		app.setOpen(false);
		app.setEnabled(false);
		app.setUebKey("test");
		app.setUebSecret("test");
		app.setUebTopicName("test");
		app.setAppType(1);

		Mockito.when(appService.getApp(persRequest.getAppId())).thenReturn(app);

		HttpSession session = mockedRequest.getSession();
		session.setAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME), user);

		Mockito.when(EPUserUtils.getUserSession(mockedRequest)).thenReturn(user);
		Mockito.doNothing().when(persUserAppService).setPersUserAppValue(user, app, persRequest.getSelect(),
				persRequest.getPending());

		actualFieldValidator = appCatalogController.putAppCatalogSelection(mockedRequest, persRequest, mockedResponse);

		assertEquals(expectedFieldValidator, actualFieldValidator);

	}

}
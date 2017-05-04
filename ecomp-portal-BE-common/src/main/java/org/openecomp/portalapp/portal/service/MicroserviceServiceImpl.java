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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.openecomp.portalapp.portal.domain.BasicAuthCredentials;
import org.openecomp.portalapp.portal.domain.EPUserAppRolesRequestDetail;
import org.openecomp.portalapp.portal.domain.MicroserviceData;
import org.openecomp.portalapp.portal.domain.MicroserviceParameter;
import org.openecomp.portalapp.portal.domain.WidgetCatalog;
import org.openecomp.portalapp.portal.domain.WidgetServiceHeaders;
import org.openecomp.portalapp.portal.ecomp.model.PortalRestResponse;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.transport.CommonWidgetMeta;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("microserviceService")
@EnableAspectJAutoProxy
@EPMetricsLog
public class MicroserviceServiceImpl implements MicroserviceService {

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MicroserviceServiceImpl.class);


	@Autowired
	private DataAccessService dataAccessService;

	@Autowired
	private SessionFactory sessionFactory;

	public Long saveMicroservice(MicroserviceData newService) throws Exception {
		if (newService.getPassword() != null)
			newService.setPassword(encryptedPassword(newService.getPassword()));
		getDataAccessService().saveDomainObject(newService, null);
		return newService.getId();
	}

	public void saveServiceParameters(long serviceId, List<MicroserviceParameter> list) throws Exception {
		for (int i = 0; i < list.size(); i++) {
			MicroserviceParameter para = list.get(i);
			para.setServiceId(serviceId);
			getDataAccessService().saveDomainObject(para, null);
		}
	}

	@Override
	public MicroserviceData getMicroserviceDataById(long id) {
		MicroserviceData data = null;
		try {
			data = (MicroserviceData) dataAccessService
					.getList(MicroserviceData.class, " where id = '" + id + "'", null, null).get(0);
			data.setParameterList(getServiceParameters(id));
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getMicroserviceDataById failed", e);
			throw e;
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MicroserviceData> getMicroserviceData() throws Exception {
		List<MicroserviceData> list = (List<MicroserviceData>) dataAccessService.getList(MicroserviceData.class, null);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getPassword() != null)
				list.get(i).setPassword(decryptedPassword(list.get(i).getPassword()));
			list.get(i).setParameterList(getServiceParameters(list.get(i).getId()));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<MicroserviceParameter> getServiceParameters(long serviceId) {
		List<MicroserviceParameter> list = (List<MicroserviceParameter>) dataAccessService
				.getList(MicroserviceParameter.class, " where service_id = '" + serviceId + "'", null, null);
		return list;
	}

	@Override
	public void deleteMicroservice(long serviceId) throws Exception {

		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("serviceId", Long.toString(serviceId));

			dataAccessService.executeNamedQuery("deleteMicroserviceParameter", params, null);
			dataAccessService.executeNamedQuery("deleteMicroservice", params, null);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(EELFLoggerDelegate.errorLogger, "deleteMicroservice failed", e);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateMicroservice(long serviceId, MicroserviceData newService) throws Exception {
		try {
			newService.setId(serviceId);
			if (newService.getPassword() != null)
				newService.setPassword(encryptedPassword(newService.getPassword()));
			getDataAccessService().saveDomainObject(newService, null);
			List<MicroserviceParameter> oldService = getServiceParameters(serviceId);
			boolean foundParam;
			for (int i = 0; i < oldService.size(); i++) {
				foundParam = false;
				for (int n = 0; n < newService.getParameterList().size(); n++) {
					if (newService.getParameterList().get(n).getId() == oldService.get(i).getId()) {
						foundParam = true;
						break;
					}
				}
				if (foundParam == false) {
					MicroserviceParameter pd = oldService.get(i);
					Session localSession = sessionFactory.openSession();
					localSession.delete(pd);
					localSession.flush();
					localSession.clear();
				}

			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "updateMicroservice failed", e);
			throw e;
		}
		saveServiceParameters(serviceId, newService.getParameterList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MicroserviceParameter> getParametersById(long serviceId) {
		List<Criterion> restrictionsList = new ArrayList<Criterion>();
		Criterion contextIdCrit = Restrictions.eq("serviceId", serviceId);
		restrictionsList.add(contextIdCrit);
		List<MicroserviceParameter> list = (List<MicroserviceParameter>) dataAccessService
				.getList(MicroserviceParameter.class, null, restrictionsList, null);
		logger.debug(EELFLoggerDelegate.debugLogger,
				"getParametersById: microservice parameters list size: " + list.size());
		return list;
	}

	private String decryptedPassword(String encryptedPwd) throws Exception {
		String result = "";
		if (encryptedPwd != null & encryptedPwd.length() > 0) {
			try {
				result = CipherUtil.decrypt(encryptedPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "decryptedPassword failed", e);
				throw e;
			}
		}
		return result;
	}

	private String encryptedPassword(String decryptedPwd) throws Exception {
		String result = "";
		if (decryptedPwd != null & decryptedPwd.length() > 0) {
			try {
				result = CipherUtil.encrypt(decryptedPwd,
						SystemProperties.getProperty(SystemProperties.Decryption_Key));
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "encryptedPassword failed", e);
				throw e;
			}
		}
		return result;
	}

	public DataAccessService getDataAccessService() {
		return dataAccessService;
	}

}
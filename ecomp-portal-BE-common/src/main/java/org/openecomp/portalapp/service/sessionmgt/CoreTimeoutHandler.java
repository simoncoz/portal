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
package org.openecomp.portalapp.service.sessionmgt;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.utils.EcompPortalUtils;
import org.openecomp.portalsdk.core.domain.sessionmgt.TimeoutVO;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.onboarding.util.PortalApiConstants;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class CoreTimeoutHandler{
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(CoreTimeoutHandler.class);
	
	public static final Map<String, HttpSession> sessionMap = new Hashtable<String,HttpSession>(); 
	public static final Integer repeatInterval = 15 * 60; // 15 minutes
	ObjectMapper mapper = new ObjectMapper(); 
	
	
	public static void sessionCreated(String portalJSessionId, String jSessionId, HttpSession session) {
		
		storeMaxInactiveTime(session);
		
		// this key is a combination of portal jsession id and app session id
		session.setAttribute(PortalApiConstants.PORTAL_JSESSION_ID,jSessionKey(jSessionId, portalJSessionId));
		sessionMap.put((String)session.getAttribute(PortalApiConstants.PORTAL_JSESSION_ID), session);
		
	}
	
	protected static void storeMaxInactiveTime(HttpSession session) {
		
		if(session.getAttribute(PortalApiConstants.GLOBAL_SESSION_MAX_IDLE_TIME) == null)
			session.setAttribute(PortalApiConstants.GLOBAL_SESSION_MAX_IDLE_TIME,session.getMaxInactiveInterval());
	}
	
	public static void sessionDestroyed(HttpSession session) {
		
		try{
		sessionMap.remove((String)session.getAttribute(PortalApiConstants.PORTAL_JSESSION_ID));
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "************************ Session Management: Error while destroying session for " + session.getId() + " Details: " + EcompPortalUtils.getStackTrace(e));
		}
		
	}
	
	public String gatherSessionExtenstions() {
		
		Map<String,TimeoutVO> sessionTimeoutMap = new Hashtable<String,TimeoutVO>();
		String jsonMap = "";
		
		
		for(String jSessionKey: sessionMap.keySet()) {
			
			try{
				// get the expirytime in seconds
				HttpSession session = sessionMap.get(jSessionKey);
				
				Long lastAccessedTimeMilliSec = session.getLastAccessedTime();
				Long maxIntervalMilliSec = session.getMaxInactiveInterval() * 1000L;
				//Long currentTimeMilliSec = Calendar.getInstance().getTimeInMillis() ;
				//(maxIntervalMilliSec - (currentTimeMilliSec - lastAccessedTimeMilliSec) + ;
				Calendar instance = Calendar.getInstance();
				instance.setTimeInMillis(session.getLastAccessedTime());
				logger.info(EELFLoggerDelegate.errorLogger, "************************ Session Management: Last Accessed time for "+ jSessionKey + ": " + instance.getTime());
				
				Long sessionTimOutMilliSec = maxIntervalMilliSec + lastAccessedTimeMilliSec;
				
				sessionTimeoutMap.put( portalJSessionId(jSessionKey), new TimeoutVO(jSessionId(jSessionKey),sessionTimOutMilliSec));
				
				
				jsonMap = mapper.writeValueAsString(sessionTimeoutMap);
			} catch(Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "************************ Session Management: Error during JsonSessionTimout conversion. Details: " + EcompPortalUtils.getStackTrace(e));
			}
		}
		
		return jsonMap;
		
	}
	
	
	public void updateSessionExtensions(String sessionTimeoutMapStr) throws Exception {
		
		//Map<String,Object> sessionTimeoutMap = mapper.readValue(sessionTimeoutMapStr, Map.class);
		Map<String,TimeoutVO> sessionTimeoutMap;
		try{
			TypeReference<Hashtable<String,TimeoutVO>> typeRef
			 = new TypeReference<Hashtable<String,TimeoutVO>>() {};
	
			 sessionTimeoutMap  = mapper.readValue(sessionTimeoutMapStr, typeRef);
		}catch(Exception e){
			logger.error(EELFLoggerDelegate.errorLogger, "************************ Session Management: Error while to parse update session extension in portal", e);
			return;
		}
		for(String jPortalSessionId: sessionTimeoutMap.keySet()) {
			try {
				
				TimeoutVO extendedTimeoutVO = mapper.readValue(mapper.writeValueAsString(sessionTimeoutMap.get(jPortalSessionId)),TimeoutVO.class);
				HttpSession session = sessionMap.get(jSessionKey(extendedTimeoutVO.getjSessionId(), jPortalSessionId));
				
				if(session == null) {
					continue;
				}
				
				Long lastAccessedTimeMilliSec = session.getLastAccessedTime();
				Long maxIntervalMilliSec = session.getMaxInactiveInterval() * 1000L;
				Long sessionTimOutMilliSec = maxIntervalMilliSec + lastAccessedTimeMilliSec;
				
				Long maxTimeoutTimeMilliSec = extendedTimeoutVO.getSessionTimOutMilliSec();
				if(maxTimeoutTimeMilliSec>sessionTimOutMilliSec) {
					logger.debug(EELFLoggerDelegate.debugLogger, "************************ Session Management: " + " updated session max idle time");
					session.setMaxInactiveInterval((int)(maxTimeoutTimeMilliSec-lastAccessedTimeMilliSec)/1000);
				}
			} catch (Exception e) {
				logger.error(EELFLoggerDelegate.errorLogger, "************************ Session Management: " + EcompPortalUtils.getStackTrace(e));
			}
			
		}
			
	}

	protected  static String jSessionKey(String jSessionId, String portalJSessionId) {
		return portalJSessionId + "-" + jSessionId;
	}
	
	protected  String portalJSessionId(String jSessionKey) {
		return jSessionKey.split("-")[0];
	}
	
	protected  String jSessionId(String jSessionKey) {
		return jSessionKey.split("-")[1];
	}

}
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
package org.openecomp.portalapp.portal.logging.aop;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.exception.SessionExpiredException;
import org.openecomp.portalsdk.core.logging.format.AlarmSeverityEnum;
import org.openecomp.portalsdk.core.logging.format.AuditLogFormatter;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.portalsdk.core.util.SystemProperties.SecurityEventTypeEnum;
import org.openecomp.portalsdk.core.web.support.UserUtils;
import org.slf4j.MDC;

import com.att.eelf.configuration.Configuration;

@org.springframework.context.annotation.Configuration
public class EPEELFLoggerAdvice {

	private EELFLoggerDelegate adviceLogger = EELFLoggerDelegate.getLogger(EPEELFLoggerAdvice.class);

	/**
	 * DateTime Format according to the ECOMP Application Logging Guidelines.
	 */
	private static final SimpleDateFormat ecompLogDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	/**
	 * @return Current date and time in the format specified by the ECOMP
	 *         Application Logging Guidelines.
	 */
	public static String getCurrentDateTimeUTC() {
		String currentDateTime = ecompLogDateFormat.format(new Date());
		return currentDateTime;
	}

	/**
	 * Sets logging context with values from HttpServletRequest object.
	 * 
	 * @param req
	 *            HttpServletRequest
	 * @param securityEventType
	 *            SecurityEventTypeEnum
	 */
	public void loadServletRequestBasedDefaults(HttpServletRequest req, SecurityEventTypeEnum securityEventType) {
		try {
			setHttpRequestBasedDefaultsIntoGlobalLoggingContext(req, securityEventType, req.getServletPath());
		} catch (Exception e) {
			adviceLogger.error(EELFLoggerDelegate.errorLogger, "loadServletRequestBasedDefaults failed", e);
		}
	}

	/**
	 * 
	 * @param securityEventType
	 * @param args
	 * @param passOnArgs
	 * @return
	 */
	public Object[] before(SecurityEventTypeEnum securityEventType, Object[] args, Object[] passOnArgs) {
		String className = "";
		if (passOnArgs[0] != null) {
			className = passOnArgs[0].toString();
		}

		String methodName = "";
		if (passOnArgs[1] != null) {
			methodName = passOnArgs[1].toString();
		}

		// Initialize Request defaults only for controller methods.
		MDC.put(className + methodName + EPCommonSystemProperties.METRICSLOG_BEGIN_TIMESTAMP, getCurrentDateTimeUTC());
		MDC.put(EPCommonSystemProperties.TARGET_ENTITY, EPCommonSystemProperties.ECOMP_PORTAL_BE);
		MDC.put(EPCommonSystemProperties.TARGET_SERVICE_NAME, methodName);
		if (securityEventType != null) {
			MDC.put(className + methodName + EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,
					getCurrentDateTimeUTC());
			HttpServletRequest req = null;
			if (args[0] != null && args[0] instanceof HttpServletRequest) {
				req = (HttpServletRequest) args[0];
				this.setHttpRequestBasedDefaultsIntoGlobalLoggingContext(req, securityEventType, methodName);
			}
		}

		EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(className);
		logger.debug(EELFLoggerDelegate.debugLogger, "EPEELFLoggerAdvice#before: entering {}", methodName);
		return new Object[] { "" };
	}

	/**
	 * 
	 * @param securityEventType
	 * @param statusCode
	 * @param responseCode
	 * @param args
	 * @param returnArgs
	 * @param passOnArgs
	 */
	public void after(SecurityEventTypeEnum securityEventType, String statusCode, String responseCode, Object[] args,
			Object[] returnArgs, Object[] passOnArgs) {
		String className = "";
		if (passOnArgs[0] != null)
			className = passOnArgs[0].toString();

		// Method Name
		String methodName = "";
		if (passOnArgs[1] != null)
			methodName = passOnArgs[1].toString();

		if (MDC.get(EPCommonSystemProperties.TARGET_SERVICE_NAME) == null
				|| MDC.get(EPCommonSystemProperties.TARGET_SERVICE_NAME) == "")
			MDC.put(EPCommonSystemProperties.TARGET_SERVICE_NAME, methodName);

		if (MDC.get(EPCommonSystemProperties.TARGET_ENTITY) == null
				|| MDC.get(EPCommonSystemProperties.TARGET_ENTITY) == "")
			MDC.put(EPCommonSystemProperties.TARGET_ENTITY, EPCommonSystemProperties.ECOMP_PORTAL_BE);

		MDC.put(EPCommonSystemProperties.METRICSLOG_BEGIN_TIMESTAMP,
				MDC.get(className + methodName + EPCommonSystemProperties.METRICSLOG_BEGIN_TIMESTAMP));
		MDC.put(EPCommonSystemProperties.METRICSLOG_END_TIMESTAMP, getCurrentDateTimeUTC());
		this.calculateDateTimeDifference(MDC.get(EPCommonSystemProperties.METRICSLOG_BEGIN_TIMESTAMP),
				MDC.get(EPCommonSystemProperties.METRICSLOG_END_TIMESTAMP));

		// Making sure to reload the INCOMING request MDC defaults if they have
		// been wiped out by either Outgoing or LDAP Phone book search
		// operations.
		if (securityEventType != null && args[0] != null && args[0] instanceof HttpServletRequest
				&& securityEventType == SecurityEventTypeEnum.INCOMING_REST_MESSAGE
				&& (MDC.get(EPCommonSystemProperties.FULL_URL) == null
						|| MDC.get(EPCommonSystemProperties.FULL_URL) == "")) {
			HttpServletRequest req = (HttpServletRequest) args[0];
			this.setHttpRequestBasedDefaultsIntoGlobalLoggingContext(req, securityEventType, methodName);
		}

		// Use external API response code in case if it resulted in an error.
		String externalAPIResponseCode = MDC.get(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE);
		if (externalAPIResponseCode == null || externalAPIResponseCode == ""
				|| externalAPIResponseCode.trim().equalsIgnoreCase("200")) {
			MDC.put(EPCommonSystemProperties.RESPONSE_CODE, responseCode);
			MDC.put(EPCommonSystemProperties.STATUS_CODE, statusCode);
		} else {
			MDC.put(EPCommonSystemProperties.RESPONSE_CODE, externalAPIResponseCode);
			MDC.put(EPCommonSystemProperties.STATUS_CODE, "ERROR");
		}

		EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(className);
		logger.debug(EELFLoggerDelegate.debugLogger, "EPEELFLoggerAdvice#after: finished {}", methodName);

		// Log security message, if necessary
		if (securityEventType != null) {
			MDC.put(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP,
					MDC.get(className + methodName + EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP));
			MDC.put(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP, getCurrentDateTimeUTC());
			this.calculateDateTimeDifference(MDC.get(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP),
					MDC.get(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP));

			this.logSecurityMessage(logger, securityEventType, methodName);

			// Outgoing & LDAP messages are part of Incoming requests so,
			// keep "RequestId", "PartnerName", "ServiceName", "LoginId" &
			// "ResponseCode" etc. in memory and remove it only when
			// finished processing the parent incoming message.
			if (securityEventType != SecurityEventTypeEnum.OUTGOING_REST_MESSAGE
					&& securityEventType != SecurityEventTypeEnum.LDAP_PHONEBOOK_USER_SEARCH) {
				MDC.remove(Configuration.MDC_KEY_REQUEST_ID);
				MDC.remove(EPCommonSystemProperties.PARTNER_NAME);
				MDC.remove(Configuration.MDC_SERVICE_NAME);
				MDC.remove(EPCommonSystemProperties.MDC_LOGIN_ID);
				MDC.remove(EPCommonSystemProperties.EXTERNAL_API_RESPONSE_CODE);
			}

			// clear when finishes audit logging
			MDC.remove(EPCommonSystemProperties.FULL_URL);
			MDC.remove(EPCommonSystemProperties.PROTOCOL);
			MDC.remove(EPCommonSystemProperties.STATUS_CODE);
			MDC.remove(className + methodName + EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
			MDC.remove(EPCommonSystemProperties.AUDITLOG_BEGIN_TIMESTAMP);
			MDC.remove(EPCommonSystemProperties.AUDITLOG_END_TIMESTAMP);
			MDC.remove(EPCommonSystemProperties.RESPONSE_CODE);
		}
		MDC.remove(className + methodName + EPCommonSystemProperties.METRICSLOG_BEGIN_TIMESTAMP);
		MDC.remove(EPCommonSystemProperties.METRICSLOG_BEGIN_TIMESTAMP);
		MDC.remove(EPCommonSystemProperties.METRICSLOG_END_TIMESTAMP);
		MDC.remove(EPCommonSystemProperties.MDC_TIMER);
		MDC.remove(EPCommonSystemProperties.TARGET_ENTITY);
		MDC.remove(EPCommonSystemProperties.TARGET_SERVICE_NAME);
	}

	/**
	 * 
	 * @param logger
	 * @param securityEventType
	 * @param restMethod
	 */
	private void logSecurityMessage(EELFLoggerDelegate logger, SecurityEventTypeEnum securityEventType,
			String restMethod) {
		StringBuilder additionalInfoAppender = new StringBuilder();
		String auditMessage = "";

		if (securityEventType == SecurityEventTypeEnum.OUTGOING_REST_MESSAGE) {
			additionalInfoAppender.append(String.format("%s '%s' request was initiated.", restMethod,
					MDC.get(EPCommonSystemProperties.TARGET_SERVICE_NAME)));
		} else if (securityEventType == SecurityEventTypeEnum.LDAP_PHONEBOOK_USER_SEARCH) {
			additionalInfoAppender.append("LDAP Phonebook search operation is performed.");
		} else {
			additionalInfoAppender.append(String.format("%s request was received.", restMethod));

			if (securityEventType == SecurityEventTypeEnum.FE_LOGIN_ATTEMPT) {
				String loginId = "";
				String additionalMessage = " Successfully authenticated.";
				loginId = MDC.get(EPCommonSystemProperties.MDC_LOGIN_ID);
				if (loginId == null || loginId == "" || loginId == EPCommonSystemProperties.UNKNOWN) {
					additionalMessage = " No cookies are found.";
				}
				additionalInfoAppender.append(additionalMessage);
			} else if (securityEventType == SecurityEventTypeEnum.FE_LOGOUT) {
				additionalInfoAppender.append(" User has been successfully logged out.");
			}
		}

		String fullURL = MDC.get(EPCommonSystemProperties.FULL_URL);
		if (fullURL != null && fullURL != "") {
			additionalInfoAppender.append(" Request-URL:" + MDC.get(EPCommonSystemProperties.FULL_URL));
		}

		auditMessage = AuditLogFormatter.getInstance().createMessage(MDC.get(EPCommonSystemProperties.PROTOCOL),
				securityEventType.name(), MDC.get(EPCommonSystemProperties.MDC_LOGIN_ID),
				additionalInfoAppender.toString());

		logger.info(EELFLoggerDelegate.auditLogger, auditMessage);
	}

	/**
	 * 
	 * @param req
	 * @param securityEventType
	 * @param restMethod
	 */
	private void setHttpRequestBasedDefaultsIntoGlobalLoggingContext(HttpServletRequest req,
			SecurityEventTypeEnum securityEventType, String restMethod) {
		/**
		 * No need to load the request based defaults for the following security
		 * messages since either they are initiated by the Portal BE or not Http
		 * request based.
		 */
		if (req != null) {
			if (securityEventType != SecurityEventTypeEnum.OUTGOING_REST_MESSAGE
					&& securityEventType != SecurityEventTypeEnum.LDAP_PHONEBOOK_USER_SEARCH
					&& securityEventType != SecurityEventTypeEnum.INCOMING_UEB_MESSAGE) {
				// Load the RequestID (aka TrasactionId) into MDC context.
				String requestId = UserUtils.getRequestId(req);
				if (requestId == "" || requestId == null) {
					requestId = UUID.randomUUID().toString();
				}
				MDC.put(Configuration.MDC_KEY_REQUEST_ID, requestId);

				// Load user agent into MDC context, if available.
				String accessingClient = "Unknown";
				accessingClient = req.getHeader(SystemProperties.USERAGENT_NAME);
				if (accessingClient != null && accessingClient != "" && (accessingClient.contains("Mozilla")
						|| accessingClient.contains("Chrome") || accessingClient.contains("Safari"))) {
					accessingClient = EPCommonSystemProperties.ECOMP_PORTAL_FE;
				}
				MDC.put(EPCommonSystemProperties.PARTNER_NAME, accessingClient);

				// Load loginId into MDC context.
				EPUser user = null;
				try {
					user = EPUserUtils.getUserSession(req);
				} catch (SessionExpiredException se) {
					adviceLogger.debug(EELFLoggerDelegate.debugLogger,
							"setHttpRequestBasedDefaultsIntoGlobalLoggingContext: No user found in session");
				}

				MDC.put(EPCommonSystemProperties.MDC_LOGIN_ID, (user != null ? user.getOrgUserId() : "NoUser"));

				// Rest URL & Protocol
				String restURL = "";
				MDC.put(EPCommonSystemProperties.FULL_URL, EPCommonSystemProperties.UNKNOWN);
				MDC.put(EPCommonSystemProperties.PROTOCOL, EPCommonSystemProperties.HTTP);
				restURL = UserUtils.getFullURL(req);
				if (restURL != null && restURL != "") {
					MDC.put(EPCommonSystemProperties.FULL_URL, restURL);
					if (restURL.toLowerCase().contains("https")) {
						MDC.put(EPCommonSystemProperties.PROTOCOL, EPCommonSystemProperties.HTTPS);
					}
				}

				// Rest Path
				MDC.put(Configuration.MDC_SERVICE_NAME, restMethod);
				String restPath = req.getServletPath();
				if (restPath != null && restPath != "") {
					MDC.put(Configuration.MDC_SERVICE_NAME, restPath);
				}

				// Client IPAddress i.e. IPAddress of the remote host who is
				// making this request.
				String clientIPAddress = "";
				clientIPAddress = req.getHeader("X-FORWARDED-FOR");
				if (clientIPAddress == null) {
					clientIPAddress = req.getRemoteAddr();
				}
				MDC.put(EPCommonSystemProperties.CLIENT_IP_ADDRESS, clientIPAddress);
			} else if (securityEventType == SecurityEventTypeEnum.LDAP_PHONEBOOK_USER_SEARCH) {
				MDC.put(EPCommonSystemProperties.TARGET_ENTITY, "Phonebook");
				MDC.put(EPCommonSystemProperties.TARGET_SERVICE_NAME, "search");
			}
		} else {
			MDC.put(Configuration.MDC_SERVICE_NAME, restMethod);
			MDC.put(EPCommonSystemProperties.PARTNER_NAME, EPCommonSystemProperties.ECOMP_PORTAL_FE);
		}

		MDC.put(Configuration.MDC_SERVICE_INSTANCE_ID, "");
		MDC.put(Configuration.MDC_ALERT_SEVERITY, AlarmSeverityEnum.INFORMATIONAL.toString());
		try {
			MDC.put(Configuration.MDC_SERVER_FQDN, InetAddress.getLocalHost().getHostName());
			MDC.put(Configuration.MDC_SERVER_IP_ADDRESS, InetAddress.getLocalHost().getHostAddress());
			MDC.put(Configuration.MDC_INSTANCE_UUID, SystemProperties.getProperty(SystemProperties.INSTANCE_UUID));
		} catch (Exception e) {
			adviceLogger.error(EELFLoggerDelegate.errorLogger,
					"setHttpRequestBasedDefaultsIntoGlobalLoggingContext failed", e);
		}
	}

	/**
	 * 
	 * @param beginDateTime
	 * @param endDateTime
	 */
	private void calculateDateTimeDifference(String beginDateTime, String endDateTime) {
		if (beginDateTime != null && endDateTime != null) {
			try {
				Date beginDate = ecompLogDateFormat.parse(beginDateTime);
				Date endDate = ecompLogDateFormat.parse(endDateTime);
				String timeDifference = String.format("%d", endDate.getTime() - beginDate.getTime());
				MDC.put(SystemProperties.MDC_TIMER, timeDifference);
			} catch (Exception e) {
				adviceLogger.error(EELFLoggerDelegate.errorLogger, "calculateDateTimeDifference failed", e);
			}
		}
	}

	public String getInternalResponseCode() {
		return MDC.get(EPCommonSystemProperties.RESPONSE_CODE);
	}
}
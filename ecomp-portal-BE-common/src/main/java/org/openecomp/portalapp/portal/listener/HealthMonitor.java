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
package org.openecomp.portalapp.portal.listener;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.logging.format.EPAppMessagesEnum;
import org.openecomp.portalapp.portal.logging.logic.EPLogUtil;
import org.openecomp.portalapp.portal.ueb.EPUebHelper;
import org.openecomp.portalapp.portal.utils.EPCommonSystemProperties;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
@EPMetricsLog
public class HealthMonitor {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private EPUebHelper epUebHelper;

	private static boolean databaseUp;
	private static boolean uebUp;
	private static boolean frontEndUp;
	private static boolean backEndUp;
	private static boolean dbClusterStatusOk;
	private static boolean dbPermissionsOk;
	public static boolean isSuspended = false;

	Thread healthMonitorThread;

	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(HealthMonitor.class);

	public HealthMonitor() {

	}

	public static boolean isDatabaseUp() {
		return databaseUp;
	}

	public static boolean isClusterStatusOk() {
		return dbClusterStatusOk;
	}

	public static boolean isDatabasePermissionsOk() {
		return dbPermissionsOk;
	}

	public static boolean isUebUp() {
		return uebUp;
	}

	public static boolean isFrontEndUp() {
		return frontEndUp;
	}

	public static boolean isBackEndUp() {
		return backEndUp;
	}

	private void monitorEPHealth() throws InterruptedException {

		int numIntervalsDatabaseHasBeenDown = 0;
		int numIntervalsClusterNotHealthy = 0;
		int numIntervalsDatabasePermissionsIncorrect = 0;
		int numIntervalsUebHasBeenDown = 0;

		logger.debug(EELFLoggerDelegate.debugLogger, "==> Health Monitor thread started");

		long sleepInterval = (Long
				.valueOf(SystemProperties.getProperty(EPCommonSystemProperties.HEALTH_POLL_INTERVAL_SECONDS)) * 1000);
		long numIntervalsBetweenAlerts = Long
				.valueOf(SystemProperties.getProperty(EPCommonSystemProperties.HEALTHFAIL_ALERT_EVERY_X_INTERVALS));
		logger.debug(EELFLoggerDelegate.debugLogger,
				"Polling health every " + sleepInterval + " milliseconds. Alerting every "
						+ (sleepInterval * numIntervalsBetweenAlerts) / 1000 + " seconds when component remains down.");

		while (true) {
			//
			// Get DB status. If down, signal alert once every X intervals.
			//
			databaseUp = this.checkIfDatabaseUp();
			if (databaseUp == false) {

				if ((numIntervalsDatabaseHasBeenDown % numIntervalsBetweenAlerts) == 0) {
					// Write a Log entry that will generate an alert
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeHealthCheckMySqlError);
					logger.debug(EELFLoggerDelegate.debugLogger,
							"Database down, logging to error log to trigger alert.");
					numIntervalsDatabaseHasBeenDown++;
				} else {
					numIntervalsDatabaseHasBeenDown = 0;
				}
			}

			dbClusterStatusOk = this.checkClusterStatus();
			if (dbClusterStatusOk == false) {

				if ((numIntervalsClusterNotHealthy % numIntervalsBetweenAlerts) == 0) {
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeHealthCheckMySqlError);
					logger.debug(EELFLoggerDelegate.debugLogger,
							"Cluster nodes appear to be down, logging to error log to trigger alert.");
					numIntervalsClusterNotHealthy++;
				} else {
					numIntervalsClusterNotHealthy = 0;
				}
			}

			dbPermissionsOk = this.checkDatabaseAndPermissions();
			if (dbPermissionsOk == false) {

				if ((numIntervalsDatabasePermissionsIncorrect % numIntervalsBetweenAlerts) == 0) {
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeHealthCheckMySqlError);
					logger.debug(EELFLoggerDelegate.debugLogger,
							"Database permissions don't seem correct, logging to error log to trigger alert.");
					numIntervalsDatabasePermissionsIncorrect++;
				} else {
					numIntervalsDatabasePermissionsIncorrect = 0;
				}
			}

			//
			// Get UEB status. Publish a bogus message to EP inbox, if 200 OK
			// returned, status is Up.
			// If down, signal alert once every X intervals.
			// EP will ignore this bogus message.
			//
			uebUp = this.checkIfUebUp();
			if (uebUp == false) {

				if ((numIntervalsUebHasBeenDown % numIntervalsBetweenAlerts) == 0) {
					// Write a Log entry that will generate an alert
					EPLogUtil.logEcompError(logger, EPAppMessagesEnum.BeHealthCheckUebClusterError);
					logger.debug(EELFLoggerDelegate.debugLogger, "UEB down, logging to error log to trigger alert");
					numIntervalsUebHasBeenDown++;
				} else {
					numIntervalsUebHasBeenDown = 0;
				}
			}

			// The front end should be up because the API is called through
			// proxy front end server.
			frontEndUp = true;

			// If the rest API called, the backend is always up
			backEndUp = true;

			//
			// future nice to have...get Partner status
			//
			// For all apps exposing a rest url, query one of the rest
			// urls(/roles?) and manage a list
			// of app name/status. We might not return back a non 200 OK in
			// health check, but we
			// could return information in the json content of a health check.
			//

			//
			// Get DB status. If down, signal alert once every X intervals.
			//
			if (Thread.interrupted()) {
				logger.info(EELFLoggerDelegate.errorLogger, "==> UebMainHandler exiting");
				break;
			}

			try {
				Thread.sleep(sleepInterval);
			} catch (InterruptedException e) {
				logger.error(EELFLoggerDelegate.errorLogger, "monitorEPHealth interrupted", e);
				Thread.currentThread().interrupt();
			}
		}
	}

	@PostConstruct
	public void initHealthMonitor() {

		healthMonitorThread = new Thread("EP HealthMonitor thread") {
			public void run() {
				try {
					monitorEPHealth();
				} catch (InterruptedException e) {
					logger.debug(EELFLoggerDelegate.debugLogger, "healthMonitorThread interrupted", e);
				} catch (Exception e) {
					logger.error(EELFLoggerDelegate.errorLogger, "healthMonitorThread failed", e);
				}
			}
		};
		if (healthMonitorThread != null) {
			healthMonitorThread.start();
		}
	}

	@PreDestroy
	public void closeHealthMonitor() {
		this.healthMonitorThread.interrupt();
	}

	private boolean checkIfDatabaseUp() {

		boolean isUp = false;

		Session localSession = null;

		try {
			localSession = sessionFactory.openSession();

			if (localSession != null) {

				String sql = "select app_name from fn_app where app_id=1";
				Query query = localSession.createSQLQuery(sql);
				@SuppressWarnings("unchecked")
				List<String> queryList = query.list();
				if (queryList != null) {
					isUp = true;
				}
				localSession.close();
			}
		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, "checkIfDatabaseUp failed", e);
			isUp = false;
		}

		return isUp;
	}

	private boolean checkClusterStatus() {

		boolean isUp = false;

		Session localSession = null;

		try {
			localSession = sessionFactory.openSession();
			if (localSession != null) {
				// If all nodes are unhealthy in a cluster, this will throw an
				// exception
				String sql = "select * from mysql.user";
				Query query = localSession.createSQLQuery(sql);
				@SuppressWarnings("unchecked")
				List<String> queryList = query.list();
				if (queryList != null) {
					isUp = true;
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "checkClusterStatus failed", e);
			if ((e.getCause() != null) && (e.getCause().getMessage() != null)) {
				logger.error(EELFLoggerDelegate.errorLogger,
						"checkClusterStatus() exception msg = " + e.getCause().getMessage());
			}
			isUp = false;
		} finally {
			if (localSession != null) {
				localSession.close();
			}
		}

		return isUp;

	}

	private boolean checkDatabaseAndPermissions() {

		boolean isUp = false;

		Session localSession = null;

		try {
			localSession = sessionFactory.openSession();
			if (localSession != null) {
				String sql = "SHOW GRANTS FOR CURRENT_USER";
				Query query = localSession.createSQLQuery(sql);
				@SuppressWarnings("unchecked")
				List<String> grantsList = query.list();
				for (String str : grantsList) {
					if ((str.toUpperCase().contains("ALL"))
							|| (str.toUpperCase().contains("DELETE") && str.toUpperCase().contains("SELECT")
									&& str.toUpperCase().contains("UPDATE") && str.toUpperCase().contains("INSERT"))) {
						isUp = true;
						break;
					}
				}
				if (isUp == false) {
					logger.error(EELFLoggerDelegate.errorLogger,
							"checkDatabaseAndPermissions() returning false.  SHOW GRANTS FOR CURRENT_USER being dumped:");
					for (String str : grantsList) {
						logger.error(EELFLoggerDelegate.errorLogger, "grants output item = [" + str + "]");
					}
				}
			}
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "checkDatabaseAndPermissions failed", e);
			if ((e.getCause() != null) && (e.getCause().getMessage() != null)) {
				logger.error(EELFLoggerDelegate.errorLogger,
						"checkDatabaseAndPermissions() exception msg = " + e.getCause().getMessage());
			}
			isUp = false;
		} finally {
			if (localSession != null) {
				localSession.close();
			}
		}

		return isUp;

	}

	private boolean checkIfUebUp() {
		boolean uebUp = false;
		try {
			boolean isAvailable = epUebHelper.checkAvailability();
			boolean messageCanBeSent = epUebHelper.MessageCanBeSentToTopic();
			uebUp = (isAvailable && messageCanBeSent);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "checkIfUebUp failed", e);
		}

		return uebUp;

	}

}
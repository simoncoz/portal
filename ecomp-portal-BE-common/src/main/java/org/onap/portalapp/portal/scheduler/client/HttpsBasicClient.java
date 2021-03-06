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

package org.onap.portalapp.portal.scheduler.client;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.eclipse.jetty.util.security.Password;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.onap.portalapp.portal.scheduler.SchedulerProperties;
import org.onap.portalapp.portal.scheduler.util.CustomJacksonJaxBJsonProvider;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;

 /**
  *  General SSL client using the VID tomcat keystore. It doesn't use client certificates.
  */
 
public class HttpsBasicClient{
	
	/** The logger. */
	static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(HttpsBasicClient.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	/**
	 * Retrieve an SSL client.
	 *
	 * @return Client The SSL client
	 * @throws Exception the exception
	 */
	public static Client getClient() throws Exception {
		String methodName = "getClient";
		ClientConfig config = new ClientConfig();
		//config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		//config.getClasses().add(org.onap.aai.util.CustomJacksonJaxBJsonProvider.class);
	
		SSLContext ctx = null;
		
		try {
			
			config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
			
			String truststore_path = SchedulerProperties.getProperty(SchedulerProperties.VID_TRUSTSTORE_FILENAME);
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + " " + methodName + " truststore_path=" + truststore_path);
			String truststore_password = SchedulerProperties.getProperty(SchedulerProperties.VID_TRUSTSTORE_PASSWD_X);
			
			
			String decrypted_truststore_password = Password.deobfuscate(truststore_password);
			//logger.debug(dateFormat.format(new Date()) + " " + methodName + " decrypted_truststore_password=" + decrypted_truststore_password);
			
			File tr = new File (truststore_path);
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + " " + methodName + " absolute truststore path=" + tr.getAbsolutePath());
			
			//String keystore_path = certFilePath + AAIProperties.FILESEPARTOR + SystemProperties.getProperty(AAIProperties.AAI_KEYSTORE_FILENAME);
			//String keystore_password = SystemProperties.getProperty(AAIProperties.AAI_KEYSTORE_PASSWD_X);
			//String decrypted_keystore_password = EncryptedPropValue.decryptTriple(keystore_password);
			
		    System.setProperty("javax.net.ssl.trustStore", truststore_path);
		    System.setProperty("javax.net.ssl.trustStorePassword", decrypted_truststore_password);
			HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){
			    public boolean verify(String string,SSLSession ssls) {
			        return true;
			    }
			});
	
			//May need to make the algorithm a parameter. MSO requires TLSv1.1	or TLSv1.2
			ctx = SSLContext.getInstance("TLSv1.2");
			
			/* 
			KeyManagerFactory kmf = null;
			try {
				kmf = KeyManagerFactory.getInstance("SunX509");
				FileInputStream fin = new FileInputStream(keystore_path);
				KeyStore ks = KeyStore.getInstance("PKCS12");
				char[] pwd = decrypted_keystore_password.toCharArray();
				ks.load(fin, pwd);
				kmf.init(ks, pwd);
			} catch (Exception e) {
				System.out.println("Error setting up kmf: exiting");
				e.printStackTrace();
				System.exit(1);
			}

			ctx.init(kmf.getKeyManagers(), null, null);
			*/
			ctx.init(null, null, null);
			//config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, 
			//							new HTTPSProperties( , ctx));
			
			return ClientBuilder.newBuilder()
				.sslContext(ctx)
				.hostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify( String s, SSLSession sslSession ) {
						return true;
					}
				}).withConfig(config)
				.build()
				.register(CustomJacksonJaxBJsonProvider.class);
			
		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, "Error setting up config: exiting");
			//System.out.println("Error setting up config: exiting");
			e.printStackTrace();
			return null;
		}
			
		//Client client = ClientBuilder.newClient(config);
		// uncomment this line to get more logging for the request/response
		// client.addFilter(new LoggingFilter(System.out));
		
		//return client;
	}
}  

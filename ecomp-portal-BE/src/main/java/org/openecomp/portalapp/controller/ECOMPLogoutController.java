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
package org.openecomp.portalapp.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalapp.portal.domain.EPUser;
import org.openecomp.portalapp.portal.logging.aop.EPAuditLog;
import org.openecomp.portalapp.portal.logging.aop.EPMetricsLog;
import org.openecomp.portalapp.portal.utils.EPSystemProperties;
import org.openecomp.portalapp.util.EPUserUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
@org.springframework.context.annotation.Configuration
@EnableAspectJAutoProxy
public class ECOMPLogoutController extends EPUnRestrictedBaseController{
	
	private EPUser user;
	private static final String EP_SERVICE = "EPService";
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ECOMPLogoutController.class);

	@EPAuditLog
	@RequestMapping(value = {"/logout.htm" }, method = RequestMethod.GET)
	public ModelAndView logOut(HttpServletRequest request,
	            						HttpServletResponse response) throws Exception {
	
		ModelAndView modelView = null;
				
		chatRoomLogout(request);
		logger.debug(EELFLoggerDelegate.debugLogger, "ECOMPLogoutController.handleRequestInternal - Logout request received.");
		
		modelView = new ModelAndView("redirect:login.htm");
		
		/**
		if (UserUtils.isClientMobileDevice(request)){
		modelView.setViewName(modelView.getViewName().concat("?viewType=mobile"));
		}
		*/
		String cookieDoamin = EPSystemProperties.getProperty(EPSystemProperties.COOKIE_DOMAIN);
		Cookie epCookie = new Cookie(EP_SERVICE, "");
		epCookie.setMaxAge(0);
		epCookie.setDomain(cookieDoamin);
		epCookie.setPath("/");
		
		Cookie appHeaderCookie = new Cookie("show_app_header", "");
		appHeaderCookie.setMaxAge(0);
		appHeaderCookie.setDomain(cookieDoamin);
		appHeaderCookie.setPath("/");
		
		Cookie appTabCookie = new Cookie("cookieTabs", "");
		appTabCookie.setMaxAge(0);
		appTabCookie.setDomain(cookieDoamin);
		appTabCookie.setPath("/");
		
		Cookie appVisInvisTabCookie = new Cookie("visInVisCookieTabs", "");
		appVisInvisTabCookie.setMaxAge(0);
		appVisInvisTabCookie.setDomain(cookieDoamin);
		appVisInvisTabCookie.setPath("/");
		
	    response.addCookie(epCookie);	
	    response.addCookie(appHeaderCookie);
	    response.addCookie(appTabCookie);
	    response.addCookie(appVisInvisTabCookie);
		request.getSession().invalidate();
		
		logger.debug(EELFLoggerDelegate.debugLogger, "ECOMPLogoutController.handleRequestInternal - Successfully processed the logout request.");
		
		return modelView;
	}
	
	@EPMetricsLog
	public void chatRoomLogout(HttpServletRequest request){
		request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest(); 
		setUser(EPUserUtils.getUserSession(request));
	}
	
	public EPUser getUser() {
		return user;
	}
	
	public void setUser(EPUser user) {
		this.user = user;
	}
}

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
app.controller('MSO_Ctrl_KPI', function($scope, $http, $log,  $modal, KpiDashboardService) {
	$scope.activeToplevelTabId = 'MSO';
	$scope.activeTabId = 'KPI';	
	var TabIdforState = 'MSO';
	$scope.toplevelgTabs1 = KpiDashboardService.getToplevelgTabs1();
	$scope.toplevelgTabs2 = KpiDashboardService.getToplevelgTabs2();
	$scope.toplevelgTabs3 = KpiDashboardService.getToplevelgTabs3();
	$scope.gTabs = KpiDashboardService.getGenericTabs(TabIdforState);
});

app.controller('MSO_Ctrl_UserDefinedKPI', function($scope, $http, $log,  $modal, KpiDashboardService) {
	$scope.activeToplevelTabId = 'MSO';
	$scope.activeTabId = 'User Defined KPI';	
	var TabIdforState = 'MSO';
	$scope.toplevelgTabs1 = KpiDashboardService.getToplevelgTabs1();
	$scope.toplevelgTabs2 = KpiDashboardService.getToplevelgTabs2();
	$scope.toplevelgTabs3 = KpiDashboardService.getToplevelgTabs3();
	$scope.gTabs = KpiDashboardService.getGenericTabs(TabIdforState);
});

app.controller('MSO_Ctrl_Metrics', function($scope, $http, $log,  $modal, KpiDashboardService) {
	$scope.activeToplevelTabId = 'MSO';
	$scope.activeTabId = 'Metrics';	
	var TabIdforState = 'MSO';
	$scope.toplevelgTabs1 = KpiDashboardService.getToplevelgTabs1();
	$scope.toplevelgTabs2 = KpiDashboardService.getToplevelgTabs2();
	$scope.toplevelgTabs3 = KpiDashboardService.getToplevelgTabs3();
	$scope.gTabs = KpiDashboardService.getGenericTabs(TabIdforState);
});

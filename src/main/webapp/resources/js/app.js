'use strict';

var heavyWorkApp = angular.module('heavyWorkApp', ['ngRoute']);

heavyWorkApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'resources/html/partials/form.html',
		controller : 'FormController'
	}).when('/result', {
		templateUrl : 'resources/html/partials/result.html',
		controller : 'ResultController'
	}).otherwise({
		redirectTo : '/'
	});
} ]);

heavyWorkApp.controller('FormController', ['$scope', '$http', '$location', function($scope, $http, $location) {
	
	$scope.start = function(countTo) {
		$http.post('counter/start', { countTo: countTo })
			.success(function() {
				$location.path('/result');
			});
	};
	
}]);

heavyWorkApp.controller('ResultController', ['$scope', '$http', '$location', '$timeout', function($scope, $http, $location, $timeout) {

	$scope.running = true;
	// Start polling status every second
	(function tick() {
	    $http.get('counter/status').success(function (data) {
	        $scope.data = data;
	        if (data === "Running") {
	        	$timeout(tick, 1000);
	        } else {
	        	$scope.running = false;
	        }
	    });
	})();
}]);

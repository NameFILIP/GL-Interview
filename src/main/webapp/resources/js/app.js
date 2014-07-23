'use strict';

var heavyWorkApp = angular.module('heavyWorkApp', ['ngRoute']);


// Define routes
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

// First Page Controller
heavyWorkApp.controller('FormController', ['$scope', '$http', '$location', function($scope, $http, $location) {
	
	$scope.start = function(countTo) {
		$http.post('counter/start', { countTo: countTo })
			.success(function() {
				$location.path('/result');
			});
	};
	
}]);

// Result Page Controller
heavyWorkApp.controller('ResultController', ['$scope', '$http', '$timeout', function($scope, $http, $timeout) {
	
	$scope.data = { "false" : "0" };
	$scope.running = true;
	
	// Start polling status every second
	(function tick() {
	    $http.get('counter/status').success(function (data) {
	        $scope.data = data;
	        if (data['false']) {
	        	
	        	// call tick() function every second
	        	var promise = $timeout(tick, 1000);
	        	
	        	// if user leaves the page, then the task is cancelled
	        	$scope.$on('$locationChangeStart', function() {
	        		$timeout.cancel(promise);
	        		$http.get('counter/cancel');
	        	});
	        } else {
	        	// the task is done
	        	$scope.running = false;
	        }
	    });
	})();
	
	
}]);

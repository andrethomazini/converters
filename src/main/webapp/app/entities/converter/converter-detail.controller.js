(function() {
    'use strict';

    angular
        .module('converterApp')
        .controller('ConverterDetailController', ConverterDetailController);

    ConverterDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Converter'];

    function ConverterDetailController($scope, $rootScope, $stateParams, previousState, entity, Converter) {
        var vm = this;

        vm.converter = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('converterApp:converterUpdate', function(event, result) {
            vm.converter = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

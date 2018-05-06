(function() {
    'use strict';

    angular
        .module('converterApp')
        .controller('ConverterDialogController', ConverterDialogController);

    ConverterDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Converter', '$resource'];

    function ConverterDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Converter, $resource) {
        var vm = this;

        vm.converter = entity;
        vm.clear = clear;
        vm.save = save;
        vm.convertWithTika = convertWithTika;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.converter.id !== null) {
                Converter.update(vm.converter, onSaveSuccess, onSaveError);
            } else {
                Converter.save(vm.converter, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('converterApp:converterUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function convertWithTika() {
            var resourceUrl =  'api/converter/tika';

            return $resource(resourceUrl, {}, {
                'query': { method: 'GET', isArray: true}
            });

        }

        function convertWithTikaSuccess() {
            $scope.$emit('Convertido com sucesso - Tika');
        }

        function convertWithTikaError() {
            $scope.$emit('Problemas durante a conversão');
        }


    }
})();

(function() {
    'use strict';

    angular
        .module('converterApp')
        .controller('ConverterDeleteController',ConverterDeleteController);

    ConverterDeleteController.$inject = ['$uibModalInstance', 'entity', 'Converter'];

    function ConverterDeleteController($uibModalInstance, entity, Converter) {
        var vm = this;

        vm.converter = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Converter.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

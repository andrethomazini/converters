(function() {
    'use strict';

    angular
        .module('converterApp')
        .controller('ConverterController', ConverterController);

    ConverterController.$inject = ['Converter', 'ConverterSearch'];

    function ConverterController(Converter, ConverterSearch) {

        var vm = this;

        vm.converters = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Converter.query(function(result) {
                vm.converters = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ConverterSearch.query({query: vm.searchQuery}, function(result) {
                vm.converters = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();

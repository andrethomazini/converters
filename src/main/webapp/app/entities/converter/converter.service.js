(function() {
    'use strict';
    angular
        .module('converterApp')
        .factory('Converter', Converter);

    Converter.$inject = ['$resource'];

    function Converter ($resource) {
        var resourceUrl =  'api/converters/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

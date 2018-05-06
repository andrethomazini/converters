(function() {
    'use strict';

    angular
        .module('converterApp')
        .factory('ConverterSearch', ConverterSearch);

    ConverterSearch.$inject = ['$resource'];

    function ConverterSearch($resource) {
        var resourceUrl =  'api/_search/converters/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

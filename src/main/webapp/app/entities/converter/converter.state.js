(function() {
    'use strict';

    angular
        .module('converterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('converter', {
            parent: 'entity',
            url: '/converter',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Converters'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/converter/converters.html',
                    controller: 'ConverterController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('converter-detail', {
            parent: 'converter',
            url: '/converter/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Converter'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/converter/converter-detail.html',
                    controller: 'ConverterDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Converter', function($stateParams, Converter) {
                    return Converter.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'converter',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('converter-detail.edit', {
            parent: 'converter-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/converter/converter-dialog.html',
                    controller: 'ConverterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Converter', function(Converter) {
                            return Converter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('converter.new', {
            parent: 'converter',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/converter/converter-dialog.html',
                    controller: 'ConverterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('converter', null, { reload: 'converter' });
                }, function() {
                    $state.go('converter');
                });
            }]
        })
        .state('converter.edit', {
            parent: 'converter',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/converter/converter-dialog.html',
                    controller: 'ConverterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Converter', function(Converter) {
                            return Converter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('converter', null, { reload: 'converter' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('converter.delete', {
            parent: 'converter',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/converter/converter-delete-dialog.html',
                    controller: 'ConverterDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Converter', function(Converter) {
                            return Converter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('converter', null, { reload: 'converter' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

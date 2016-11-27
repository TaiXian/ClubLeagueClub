var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $http) {
    let endpoint = "http://leagueclubleague.azurewebsites.net";
//    let endpoint = "http://localhost:8000";
    $scope.updating = false;
    $http.get(endpoint + "/all")
        .then(function(response) {
            $scope.myWelcome = response.data;
        });
    $scope.update = function(summoner){
        $scope.updating = true;
        let updatePoint = endpoint + "/update"+ "?name=" + summoner.Name;
        $http.get(updatePoint)
        .then(function(response) {
            $scope.myWelcome = $scope.myWelcome.filter(function(originals) {
                return originals.Name !== summoner.Name;
            });
            $scope.myWelcome = response.data;
            $scope.updating=false;
        });
    }
    $scope.addToDelete = function(summoner){
        $scope.summoner_ID = summoner.Summoner_ID;
    }
    $scope.put = function(){
        $scope.adding = true;
        let putPoint = endpoint + "/put"+ "?name=" + $scope.name +
        "&summoner=" + $scope.summoner +
        "&password=" + $scope.addPassword;
        $http.get(putPoint)
            .then(function(response) {
                $scope.myWelcome.push(response.data);
                $scope.adding=false;
            });
    }
    $scope.delete = function(){
        $scope.deleting = true;
        let deletePoint = endpoint + "/remove"+ "?ID=" + $scope.summoner_ID +
        "&password=" + $scope.deletePassword;
        $http.get(deletePoint).then(function() {
                $http.get(endpoint + "/all").then(function(response) {
                                $scope.myWelcome = response.data;
                            });
                $scope.deleting = false;
            });
    }
});
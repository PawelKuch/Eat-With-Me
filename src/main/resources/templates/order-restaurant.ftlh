<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">

    <script src="https://code.jquery.com/jquery-3.6.4.min.js" ></script>
    <script src="static/js/order-restaurant.js"></script>
    <link rel="stylesheet" href="css/order-restaurant.css">
    <title>Restauracje</title>
</head>
<body>
<div id="main-container" class="d-flex flex-row">
    <#include "side-navbar.ftlh">

    <div id="add-restaurant-container" class="d-flex flex-column align-items-center w-100 mx-1 my-1">
        <div id="display-restaurant-container" class="row w-100 justify-content-center" style="border: 3px solid green;">
            <#list restaurantList as restaurant>
                <div class="restaurant-tile col-3 my-1 mx-1 d-flex flex-column justify-content-center" data-restaurant-id="${restaurant.restaurantId}">
                    <div class="main-info h-100">
                    <h5>${restaurant.name}</h5>
                    <span id="restaurant-address-header-${restaurant.restaurantId}">${restaurant.address}</span>
                    <div class="img-sample-${restaurant.restaurantId}" data-restaurant-id="${restaurant.restaurantId}" style="width: 40%; height: 60%; border: 1px solid black;"><img src="/logos/${restaurant.restaurantId}" alt="${restaurant.name} logo" width="100%" height="100%"></div>
                    </div>
                    <div class="restaurant-details-${restaurant.restaurantId} mb-1 mx-1 h-100 d-flex flex-row justify-content-center align-items-start d-none">
                        <div>
                            Nazwa: <b>${restaurant.name}</b><br/>
                            Nr tel.: ${restaurant.phone}<br/>
                            Email: ${restaurant.email}<br/>
                            Adres: ${restaurant.getAddress()}<br/>
                            ${restaurant.getTags()}
                        </div>
                        <div class="w-75 h-25 d-flex"><a href="restaurant-menu/${restaurant.restaurantId}" class="text-body">SPRAWDŹ MENU</a></div>
                    </div>
                    <div class="btn btn-primary details-button-${restaurant.restaurantId} mt-1 mb-1">Szczegóły lokalu</div>
                    <a href="/create-order/${restaurant.restaurantId}" class="mb-1"><div class="w-100 btn btn-primary bg-primary mt-1">Złóż zamówienie</div></a>
                </div>
            </#list>
        </div>

    </div>
</div>




<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>
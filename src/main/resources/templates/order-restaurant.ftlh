<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">

    <link rel="stylesheet" href="css/order-restaurant.css">
    <title>Restauracje</title>
</head>
<body>
<div id="main-container" class="d-flex flex-row">
    <#include "side-navbar.ftlh">

    <div id="add-restaurant-container" class="d-flex flex-column align-items-center w-100 mx-1 my-1">
        <div id="display-restaurant-container" class="row w-100 justify-content-center" style="border: 3px solid green;">
            <#list restaurantList as restaurant>
                <div class="restaurant-tile col-3 my-1 mx-1 d-flex flex-column justify-content-center" data-restaurant-id="${restaurant.restaurantId}" style="border: 1px solid black; min-height: 300px;">
                    <div class="main-info h-100">
                    <h5>${restaurant.name}</h5>
                    <span id="restaurant-address-header-${restaurant.restaurantId}">${restaurant.address}</span>
                    <div class="img-sample-${restaurant.restaurantId}" data-restaurant-id="${restaurant.restaurantId}" style="width: 120px; height: 120px; border: 1px solid black;"></div>
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
                    <a href="/order-menu/${restaurant.restaurantId}" class="mb-1"><div class="w-100 btn btn-primary bg-primary mt-1">Złóż zamówienie</div></a>
                </div>
            </#list>
        </div>

    </div>
</div>


<script>
    document.addEventListener("DOMContentLoaded", function (){


        document.querySelectorAll(".restaurant-tile").forEach(restaurant => {
            let restaurantId = restaurant.dataset.restaurantId;
            let restaurantDetails = document.querySelector(".restaurant-details-" + restaurantId);
            let restaurantImg = document.querySelector(".img-sample-" + restaurantId);
            let restaurantAddressHeader = document.querySelector("#restaurant-address-header-" + restaurantId);
            let detailsButton = document.querySelector(".details-button-" + restaurantId);
            detailsButton.addEventListener("click", function (){
                if(restaurantDetails.classList.contains("d-none")){
                    restaurantDetails.classList.remove("d-none");
                    restaurantImg.classList.add("d-none");
                    restaurantAddressHeader.classList.add("d-none");
                }else {
                    restaurantDetails.classList.add("d-none");
                    restaurantImg.classList.remove("d-none");
                    restaurantAddressHeader.classList.remove("d-none");
                }
            });
        })
    });
</script>



<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>
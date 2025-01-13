<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">

    <link rel="stylesheet" href="css/restaurant.css">
    <title>Restauracje</title>
</head>
<body>
<div id="main-container" class="d-flex flex-row">
    <#include "side-navbar.ftlh">

    <div id="add-restaurant-container" class="d-flex flex-column align-items-center w-100 mx-1 my-1">
        <div id="display-restaurant-container" class="row w-100 justify-content-center" style="border: 3px solid green;">
            <#list restaurantList as restaurant>
                <div class="restaurant-tile col-3 my-1 mx-1 d-flex flex-column justify-content-center" data-restaurant-id="${restaurant.restaurantId}" style="border: 1px solid black; min-height: 250px;">
                    <h5>${restaurant.name}</h5>
                    <span id="restaurant-address-header-${restaurant.restaurantId}">${restaurant.address}</span>
                    <div class="img-sample-${restaurant.restaurantId}" data-restaurant-id="${restaurant.restaurantId}" style="width: 120px; height: 120px; border: 1px solid black;"> ${restaurant.image}</div>
                    <div class="restaurant-details-${restaurant.restaurantId} my-1 mx-1 d-flex flex-row justify-content-center d-none">
                        <div class="h-100">
                            Nazwa: ${restaurant.name}<br/>
                            Nr tel.: ${restaurant.phone}<br/>
                            Email: ${restaurant.email}<br/>
                            Adres: ${restaurant.getAddress()}<br/>
                            ${restaurant.getTags()}
                        </div>
                        <div class="w-75 h-25 d-flex"><a href="restaurant-menu/${restaurant.restaurantId}" class="text-body">SPRAWDŹ MENU</a></div>
                    </div>
                </div>
            </#list>
        </div>
        <button data-bs-toggle="modal" data-bs-target="#create-restaurant-modal" class="btn btn-dark">Dodaj restaurację</button>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function (){


       document.querySelectorAll(".restaurant-tile").forEach(restaurant => {
           let restaurantId = restaurant.dataset.restaurantId;
           let restaurantDetails = document.querySelector(".restaurant-details-" + restaurantId);
           let restaurantImg = document.querySelector(".img-sample-" + restaurantId);
           let restaurantAddressHeader = document.querySelector("#restaurant-address-header-" + restaurantId);
           restaurant.addEventListener("click", function (){
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
<div class="modal fade" id="create-restaurant-modal" tabindex="-1" aria-labelledby="create-restaurant-modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <div id="modal-title-div">
                    <h1 class="modal-title fs-5">Dodaj restaurację</h1>
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form method="post" action="/restaurant">
                    <div class="input-group">
                        <span class="input-group-text mb-2 bg-dark text-white" style="width: 125px;">nazwa</span>
                        <input class="mb-2 form-control" id="name" name="name">
                    </div>
                    <div class="input-group">
                        <span class="input-group-text mb-2 bg-dark text-white" style="width: 125px;">nr tel.</span>
                        <input class="mb-2 form-control" id="phone" name="phone">
                    </div>
                    <div class="input-group">
                        <span class="input-group-text mb-2 bg-dark text-white" style="width: 125px;">Email </span>
                        <input class="mb-2 form-control" id="email" name="email">
                    </div>
                    <div class="input-group">
                        <span class="input-group-text mb-2 bg-dark text-white" style="width: 125px;">opis</span>
                        <input class="mb-2 form-control" id="address" name="address">
                    </div>
                    <textarea name="tags" cols="60" rows="5"; placeholder="#tags"></textarea><br/>
                    <button class="btn btn-outline-dark" type="submit">Dodaj</button>
                </form>
            </div>
            <div class="modal-footer">
                <button id="submit-form-button" class="btn btn-outline-dark mb-3" type="submit">Dodaj</button>
                <button type="button" class="btn btn-secondary mb-3" data-bs-dismiss="modal">Zamknij</button>
            </div>
        </div>
    </div>
</div>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>
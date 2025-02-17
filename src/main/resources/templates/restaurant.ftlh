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

    <script src="/static/js/side-navbar.js"></script>
    <link rel="stylesheet" href="/static/css/restaurant.css">
    <script src="/static/js/restaurant.js"></script>
    <title>Restauracje</title>
</head>
<body>
<div id="main-container" class="d-flex flex-row w-100">
    <#include "side-navbar.ftlh">

    <div id="restaurant-container" class="d-flex px-2 list-group w-100">
        <button data-bs-toggle="modal" data-bs-target="#create-restaurant-modal" class="btn btn-dark col-2 mt-3">Dodaj restaurację</button>
            <#list restaurantList as restaurant>
                <a href="/restaurants/${restaurant.restaurantId}" class="text-decoration-none"><div class="restaurant-tile list-group-item d-flex flex-row align-items-center mt-3" data-restaurant-id="${restaurant.restaurantId}">
                    <div class="col-2"><img src="/restaurants/logos/${restaurant.restaurantId}" alt="${restaurant.name} logo" width="50px" height="50px"></div>
                    <div class="col-2">${restaurant.name}</div>
                    <div class="col-4">${restaurant.address}</div>
                    <div class="col-4 mx-3">${restaurant.tags}</div>

                </div>
                </a>
            </#list>
    </div>
</div>

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
                <form id="restaurant-form" method="post" action="/restaurants" enctype="multipart/form-data">
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
                    <div class="input-group">
                        <input class="mb-2 form-control" type="file" id="jpg-file" name="image" accept="image/png, image/jpeg" >
                    </div>
                    <textarea name="tags" cols="60" rows="5" placeholder="#tags"></textarea><br/>
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
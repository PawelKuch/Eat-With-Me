<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">

    <script src="https://code.jquery.com/jquery-3.6.4.min.js" ></script>

    <title>Dodaj nową restaurację</title>


</head>
<body>
<div id="main-container" class="d-flex flex-row">
    <#include "side-navbar.ftlh">
    <div id="restaurant-details-container" class="d-flex flex-column w-100">
        <div id="logo-container" class="d-flex justify-content-center">
            <img src="/logos/${restaurant.restaurantId}" alt="${restaurant.name}" width=400px" height="400px">
        </div>
        <div id="details-container" class="d-flex flex-column justify-content-center mt-3">
            <div class="fw-bold fs-4 m-auto"> ${restaurant.name}</div>
            <div class="list-group px-2 w-100">
                <div class="list-group-item d-flex">
                    <div class="col-4 d-flex">Tagi</div>
                    <div id="valueDiv-1" class="col-7 d-flex">${restaurant.tags}</div>
                    <div id="inputDiv-1" class="col-7 d-none">
                        <form id="form-1" method="post" action="/update-restaurant/${restaurant.restaurantId}">
                            <textarea name="tags" id="textarea-1" rows="1" cols="70">${restaurant.tags}</textarea>
                        </form>
                        <button data-button-id="1" class="submit-btn btn btn-dark ms-1">OK</button>
                        <button data-button-id="1" class="cancel-btn btn btn-dark ms-1">X</button>
                    </div>
                    <div class="col-1">
                        <button id="change-btn-1" data-button-id="1" class="change-btn btn btn-dark justify-content-center"> <i class="bi bi-pencil-fill"></i> </button>
                    </div>
                </div>

                <div class="list-group-item d-flex">
                    <div class="col-4">Email</div>
                    <div id="valueDiv-2" class="col-7">${restaurant.email}</div>
                    <div id="inputDiv-2" class="col-7 d-none">
                        <form id="form-2" method="post" action="/update-restaurant/${restaurant.restaurantId}">
                            <textarea name="email" id="textarea-2" rows="1" cols="70">${restaurant.email}</textarea>
                        </form>
                        <button data-button-id="2" class="submit-btn btn btn-dark ms-1">OK</button>
                        <button data-button-id="2" class="cancel-btn btn btn-dark ms-1">X</button>
                    </div>
                    <div class="col-1">
                        <button id="change-btn-2" data-button-id="2" class="change-btn btn btn-dark justify-content-center"> <i class="bi bi-pencil-fill"></i> </button>
                    </div>
                </div>

                <div class="list-group-item d-flex">
                    <div class="col-4">Adres</div>
                    <div id="valueDiv-3" class="col-7">${restaurant.address}</div>
                    <div id="inputDiv-3" class="col-7 d-none">
                        <form id="form-3" method="post" action="/update-restaurant/${restaurant.restaurantId}">
                            <textarea name="address" id="textarea-3" rows="1" cols="70">${restaurant.address}</textarea>
                        </form>
                        <button data-button-id="3" class="submit-btn btn btn-dark ms-1">OK</button>
                        <button data-button-id="3" class="cancel-btn btn btn-dark ms-1">X</button>
                    </div>
                    <div class="col-1">
                        <button id="change-btn-3" data-button-id="3" class="change-btn btn btn-dark justify-content-center"> <i class="bi bi-pencil-fill"></i> </button>
                    </div>
                </div>

                <div class="list-group-item d-flex">
                    <div class="col-4">Nr tel.</div>
                    <div id="valueDiv-4" class="col-7">${restaurant.phone}</div>
                    <div id="inputDiv-4" class="col-7 d-none">
                        <form id="form-4" method="post" action="/update-restaurant/${restaurant.restaurantId}">
                            <textarea name="phone" id="textarea-4" rows="1" cols="70">${restaurant.phone}</textarea>
                        </form>
                        <button data-button-id="4" class="submit-btn btn btn-dark ms-1">OK</button>
                        <button data-button-id="4" class="cancel-btn btn btn-dark ms-1">X</button>
                    </div>
                    <div class="col-1">
                        <button id="change-btn-4" data-button-id="4" class="change-btn btn btn-dark justify-content-center"> <i class="bi bi-pencil-fill"></i> </button>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
<script src="/static/js/restaurant-details.js"></script>
</body>
</html>
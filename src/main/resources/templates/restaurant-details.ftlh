<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
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
                    <div class="col-7 d-flex">${restaurant.tags}</div>
                    <div class="col-1">
                        <button class="btn btn-dark justify-content-center"> <i class="bi bi-pencil-fill"></i> </button>
                    </div>
                </div>

                <div class="list-group-item d-flex">
                    <div class="col-4">Email</div>
                    <div class="col-7">${restaurant.email}</div>
                    <div class="col-1">
                        <button class="btn btn-dark justify-content-center"> <i class="bi bi-pencil-fill"></i> </button>
                    </div>
                </div>

                <div class="list-group-item d-flex">
                    <div class="col-4">Adres</div>
                    <div class="col-7">${restaurant.address}</div>
                    <div class="col-1">
                        <button class="btn btn-dark justify-content-center"> <i class="bi bi-pencil-fill"></i> </button>
                    </div>
                </div>

                <div class="list-group-item d-flex">
                    <div class="col-4">Nr tel.</div>
                    <div class="col-7">${restaurant.phone}</div>
                    <div class="col-1">
                        <button class="btn btn-dark justify-content-center"> <i class="bi bi-pencil-fill"></i> </button>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <title>MENU</title>
</head>
<body>
<div id="main-container" class="d-flex flex-row">
    <#include "side-navbar.ftlh">

    <div id="add-menuItem-container" style="width: 50%">
        <form action="/restaurant-menu" method="post">
            <div class="input-group">
                <input type="hidden" id="restaurantId" name="restaurantId" value="${restaurant.restaurantId}">
            </div>
            <div class="input-group">
                <span class="input-group-text mb-2 bg-dark text-white" style="width: 125px;">nazwa</span>
                <input class="mb-2 form-control" id="name" name="name">
            </div>
            <div class="input-group">
                <span class="input-group-text mb-2 bg-dark text-white" style="width: 125px;">kategoria</span>
                <select id="category" name="category" class="form-control mb-2">
                    <option value="breakfast">śniadania</option>
                    <option value="apetizers">przystawki</option>
                    <option value="soups">zupy</option>
                    <option value="main_courses">dania główne</option>
                    <option value="salads">sałatki</option>
                    <option value="desserts">desery</option>
                    <option value="snacks">przekąski</option>
                    <option value="beverages">napoje</option>
                </select>
            </div>
            <div class="input-group">
                <span class="input-group-text mb-2 bg-dark text-white" style="width: 125px;">cena</span>
                <input class="mb-2 form-control" type="number" id="price" name="price">
            </div>
            <div class="input-group">
                <span class="input-group-text mb-2 bg-dark text-white" style="width: 125px;">opis</span>
                <input class="mb-2 form-control" id="description" name="description">
            </div>
            <button class="btn btn-outline-dark" type="submit">dodaj</button>
        </form>
    </div>
</div>
<div id="display-menuItem-container" style="width: 50%;">

</div>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>


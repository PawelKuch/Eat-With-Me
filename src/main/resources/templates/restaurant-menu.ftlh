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


    <div class="d-flex flex-column align-items-center w-100">
        <#if restaurantMenuItemList?has_content>
            <div id="display-menuItem-container" class="row w-100 justify-content-center" style="border: 1px solid green;">
                <#list restaurantMenuItemList as menuItem>
                    <div class="restaurant-tile row my-1 mx-1 d-flex justify-content-center">
                        <h5 class="col-1">${menuItem.name}</h5>
                        <div class="menuItem-description col-3">${menuItem.description}</div>
                        <div class="menuItem-price col-1">${menuItem.price} zł</div>
                    </div>
                </#list>
            </div>
        <#else>
            <h5> Brak danych do wyświetlenia</h5>
        </#if>
    <button data-bs-toggle="modal" data-bs-target="#create-menuItem-modal" class="btn btn-dark w-25">dodaj danie</button>
    </div>
</div>
    <div class="modal fade" id="create-menuItem-modal" tabindex="-1" aria-labelledby="create-menuItem-modal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <div id="modal-title-div">
                        <h1 class="modal-title fs-5">Dodaj danie</h1>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
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


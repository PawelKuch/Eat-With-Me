<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">

    <script src="https://code.jquery.com/jquery-3.6.4.min.js" ></script>
    <script src="/static/js/side-navbar.js"></script>
    <title>Dodaj nową restaurację</title>


</head>
<body>
<div id="main-container" class="d-flex flex-row">
    <#include "side-navbar.ftlh">
    <div id="restaurant-details-container" class="d-flex flex-column w-100">
        <div id="logo-container" class="d-flex justify-content-center">
            <img src="/restaurants/logos/${restaurant.restaurantId}" alt="${restaurant.name}" width=400px" height="300px">
        </div>
        <div class="d-flex justify-content-start mb-3">
            <button data-restaurant-id="${restaurant.restaurantId}" id="change-restaurant-image-btn" class="btn btn-dark mb-3 ms-4 ps-3" data-bs-toggle="modal" data-bs-target="#change-logo-modal">Zmień logo</button>
            <button data-restaurant-id="${restaurant.restaurantId}" id="delete-restaurant-btn" class="btn btn-dark mb-3 ms-4 ps-3">Usuń</button>
        </div>
        <div id="details-container" class="d-flex flex-column justify-content-center mt-3">
            <div class="fw-bold fs-4 m-auto"> ${restaurant.name}</div>
            <div class="row justify-content-center mt-3">
                <div class="col-5">
                    <div class="card p-3 mb-3">
                        <div class="d-flex flex-row w-100">
                            <div class="w-50"><h5><i class="bi bi-tags"></i> Tags</h5></div>
                            <div class="d-flex justify-content-end w-50"><button id="change-btn-1" data-button-id="1" class="change-btn btn btn-dark justify-content-center"> <i class="bi bi-pencil-fill"></i> </button></div>
                        </div>
                        <div id="valueDiv-1" class="col-7 card-text">${restaurant.tags}</div>
                        <div id="inputDiv-1" class="col-7 d-none">
                            <form id="form-1" method="post" action="/restaurants/${restaurant.restaurantId}">
                                <textarea class="tags" name="tags" id="textarea-1" rows="1" cols="70">${restaurant.tags}</textarea>
                            </form>
                            <button data-restaurant-id="${restaurant.restaurantId}" data-button-id="1" class="submit-btn btn btn-dark ms-1">OK</button>
                            <button data-button-id="1" class="cancel-btn btn btn-dark ms-1">X</button>
                        </div>
                    </div>
                </div>

                <div class="col-5">
                    <div class="card p-3 mb-3">
                        <div class="d-flex flex-row w-100">
                            <div class="w-50"><h5><i class="bi bi-envelope"></i> Email</h5></div>
                            <div class="d-flex justify-content-end w-50"><button id="change-btn-2" data-button-id="2" class="change-btn btn btn-dark justify-content-center"> <i class="bi bi-pencil-fill"></i> </button></div>
                        </div>
                        <div id="valueDiv-2" class="col-7 card-text">${restaurant.email}</div>
                        <div id="inputDiv-2" class="col-7 d-none">
                            <form id="form-2" method="post" action="/restaurants/${restaurant.restaurantId}">
                                <textarea class="email" name="email" id="textarea-2" rows="1" cols="70">${restaurant.email}</textarea>
                            </form>
                            <button data-restaurant-id="${restaurant.restaurantId}" data-button-id="2" class="submit-btn btn btn-dark ms-1">OK</button>
                            <button data-button-id="2" class="cancel-btn btn btn-dark ms-1">X</button>
                        </div>
                    </div>
                </div>

                <div class="col-5">
                    <div class="card p-3 mb-3">
                        <div class="d-flex flex-row w-100">
                            <div class="w-50"><h5><i class="bi bi-geo-alt"> Adres.</i></h5></div>
                            <div class="d-flex justify-content-end w-50"><button id="change-btn-3" data-button-id="3" class="change-btn btn btn-dark justify-content-center"> <i class="bi bi-pencil-fill"></i> </button></div>
                        </div>
                        <div id="valueDiv-3" class="col-7 card-text">${restaurant.address}</div>
                        <div id="inputDiv-3" class="col-7 d-none">
                            <form id="form-3" method="post" action="/restaurants/${restaurant.restaurantId}">
                                <textarea class="address" name="address" id="textarea-3" rows="1" cols="70">${restaurant.address}</textarea>
                            </form>
                            <button data-restaurant-id="${restaurant.restaurantId}" data-button-id="3" class="submit-btn btn btn-dark ms-1">OK</button>
                            <button data-button-id="3" class="cancel-btn btn btn-dark ms-1">X</button>
                        </div>
                    </div>
                </div>

                <div class="col-5">
                    <div class="card p-3 mb-3">
                        <div class="d-flex flex-row w-100">
                            <div class="w-50"><h5><i class="bi bi-telephone"></i> Nr tel.</h5></div>
                            <div class="d-flex justify-content-end w-50"><button id="change-btn-4" data-button-id="4" class="change-btn btn btn-dark justify-content-center"> <i class="bi bi-pencil-fill"></i> </button></div>
                        </div>
                        <div id="valueDiv-4" class="col-7 card-text">${restaurant.phone}</div>
                        <div id="inputDiv-4" class="col-7 d-none">
                            <form id="form-4" method="post" action="/restaurants/${restaurant.restaurantId}">
                                <textarea class="phone" name="phone" id="textarea-4" rows="1" cols="70">${restaurant.phone}</textarea>
                            </form>
                            <button data-restaurant-id="${restaurant.restaurantId}" data-button-id="4" class="submit-btn btn btn-dark ms-1">OK</button>
                            <button data-button-id="4" class="cancel-btn btn btn-dark ms-1">X</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div data-restaurant-id="${restaurant.restaurantId}" id="display-menuItem-container" class="row d-flex justify-content-center mt-3">
            <div class="d-flex justify-content-center"><h3>MENU</h3></div>
            <div class="d-flex justify-content-start mb-3 ms-5 ps-3"><button class="btn btn-dark" data-bs-toggle="modal" data-bs-target="#add-menu-item-modal">Dodaj danie</button></div>
            <div class="list-group px-3">
                <#list restaurantMenuItemList as menuItem>
                    <div class="menu-item-tile-${menuItem.menuItemId} my-1 d-flex flex-row justify-content-center align-items-center list-group-item menu-item-${menuItem.menuItemId}">
                        <h5 class="col-4">${menuItem.name}</h5>
                        <div class="col-4">${menuItem.description}</div>
                        <div class="col-2" >${menuItem.price} zł</div>
                        <div class="col-2">
                            <button data-menu-item-id="${menuItem.menuItemId}" class="btn btn-dark change-menu-item-btn"><i class="bi bi-pencil-fill"></i></button>
                            <button data-menu-item-id="${menuItem.menuItemId}" class="delete-btn btn btn-dark"><i class="bi bi-trash3-fill"></i></button>
                        </div>
                    </div>

                    <form class="my-1 d-none flex-row justify-content-center align-items-center list-group-item update-menu-item-form-${menuItem.menuItemId}">
                        <div class="col-4 name"><textarea class="name-${menuItem.menuItemId}" cols="35" rows="1" name="name">${menuItem.name}</textarea></div>
                        <div class="col-4 description"><textarea class="description-${menuItem.menuItemId}" cols="50" rows="1" name="description">${menuItem.description}</textarea></div>
                        <div class="col-2 price"><textarea class="price-${menuItem.menuItemId}" cols="8" rows="1" name="price">${menuItem.price}</textarea></div>

                        <div class="col-2">
                            <div data-menu-item-id="${menuItem.menuItemId}" class="btn btn-dark confirm-update-btn">OK</div>
                            <div class="btn btn-dark discard-button-${menuItem.menuItemId}">X</div>
                        </div>
                    </form>

                </#list>
            </div>
        </div>
    </div>
</div>

<div id="modals-container">
    <div class="modal fade" id="add-menu-item-modal" tabindex="-1" aria-labelledby="add-menu-item-modal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <div id="modal-title-div">
                        <h1 class="modal-title fs-5">DODAJ DANIE</h1>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form action="/restaurants/${restaurant.restaurantId}/menuItems" method="post">
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
                    <button id="submit-sign-in-form-button" class="btn btn-dark mb-3">Zaloguj</button>
                    <button type="button" class="btn btn-secondary mb-3" data-bs-dismiss="modal">Zamknij</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="change-logo-modal" tabindex="-1" aria-labelledby="change-logo-modal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <div id="modal-title-div">
                        <h1 class="modal-title fs-5">Zmień logo</h1>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="file" id="image">
                </div>
                <div class="modal-footer">
                    <button data-restaurant-id="${restaurant.restaurantId}" id="confirm-new-logo" class="btn btn-dark mb-3">Zatwierdź</button>
                    <button type="button" class="btn btn-secondary mb-3" data-bs-dismiss="modal">Zamknij</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
<script src="/static/js/restaurant-details.js"></script>
</body>
</html>
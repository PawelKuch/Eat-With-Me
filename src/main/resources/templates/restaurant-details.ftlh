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
    <div class="h-100 w-100">
        <div id="restaurant-details-container" class="d-flex flex-column w-100 px-5" style="height: 100vh;">
            <div class="d-flex flex-row border-bottom border-dark mt-3 mb-3">
                <div id="logo-container" class="d-flex justify-content-end w-25">
                    <img src="/restaurants/${restaurant.restaurantId}/logo" alt="${restaurant.name}" style="clip-path: circle(31%); width: 300px;">
                </div>
                <div class="d-flex flex-column px-2 pt-4">
                    <div class="fs-4 fw-bold">${restaurant.name}</div>
                    <div class="my-2">
                        <i class="bi bi-geo-alt"></i> ${restaurant.address}
                    </div>
                    <div class="d-flex flex-colum my-2">
                        <div>
                            <i class="bi bi-envelope-at"></i> ${restaurant.email}
                        </div>
                        <div class="ms-3">
                            <i class="bi bi-telephone"></i> ${restaurant.phone}
                        </div>
                    </div>
                    <div class="my-2">
                        <i class="bi bi-tags"></i> ${restaurant.tags}
                    </div>
                </div>
            </div>

            <div data-restaurant-id="${restaurant.restaurantId}" id="display-menuItem-container" class="row d-flex justify-content-center mt-3">
                <div class="list-group-flush px-3">
                    <#list restaurantMenuItemList as menuItem>
                        <div class="menu-item-tile-${menuItem.menuItemId} my-1 d-flex flex-row justify-content-center align-items-center list-group-item menu-item-${menuItem.menuItemId}">
                            <h5 class="col-4">${menuItem.name}</h5>
                            <div class="col-4">${menuItem.description}</div>
                            <div class="col-2" >${menuItem.price} zł</div>
                            <div class="col-2">
                                <button data-menu-item-id="${menuItem.menuItemId}" class="btn btn-outline-primary change-menu-item-btn"><i class="bi bi-pencil-fill"></i></button>
                                <button data-menu-item-id="${menuItem.menuItemId}" class="delete-btn btn btn-danger"><i class="bi bi-trash3-fill"></i></button>
                            </div>
                        </div>

                        <form class="my-1 d-none flex-row justify-content-center align-items-center list-group-item update-menu-item-form-${menuItem.menuItemId}">
                            <div class="col-4 name"><textarea class="name-${menuItem.menuItemId}" cols="35" rows="1" name="name">${menuItem.name}</textarea></div>
                            <div class="col-4 description"><textarea class="description-${menuItem.menuItemId}" cols="50" rows="1" name="description">${menuItem.description}</textarea></div>
                            <div class="col-2 price"><textarea class="price-${menuItem.menuItemId}" cols="8" rows="1" name="price">${menuItem.price}</textarea></div>

                            <div class="col-2">
                                <div data-menu-item-id="${menuItem.menuItemId}" class="btn btn-primary confirm-update-btn"><i class="bi bi-check2"></i></div>
                                <div class="btn btn-outline-primary discard-button-${menuItem.menuItemId}"><i class="bi bi-backspace-fill"></i></div>
                            </div>
                        </form>
                    </#list>
                </div>
            </div>
        </div>
        <div class="d-flex flex-row justify-content-between w-100 border-top border-dark pt-4 pb-4">
            <div class="d-flex justify-content-start ms-3"><button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#add-menu-item-modal"> <i class="bi bi-plus-lg me-1"></i> Dodaj danie</button></div>
            <div class="d-flex justify-content-start"><button data-restaurant-id="${restaurant.restaurantId}" id="delete-restaurant-btn" class="btn btn-danger"><i class="bi bi-trash3"></i> Usuń restaurację</button></div>
            <#if currentUser?has_content>
                <div class="d-flex justify-content-start"><a href="/orders/new/${restaurant.restaurantId}" class="text-decoration-none"><button class="btn btn-primary"><i class="bi bi-bookmark"></i> Stwórz zamówienie</button></a></div>
            </#if>
            <div class="d-flex justify-content-start me-3"><button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#update-restaurant-modal"><i class="bi bi-pencil-fill"></i> Edytuj szczegóły restauracji</button></div>
        </div>
    </div>
</div>

<div id="modals-container">
    <div class="modal fade" id="update-restaurant-modal" tabindex="-1" aria-labelledby="update-restaurant-modal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <div id="modal-title-div">
                        <h1 class="modal-title fs-5">Edytowanie szczegółów</h1>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form class="d-flex flex-row border-bottom border-dark pb-3">
                        <input type="file" id="image">
                        <div id="confirm-new-logo" class="btn btn-primary">Zmień logo</div>
                    </form>
                    <form class="d-flex flex-column pt-3">
                        <div class="input-group">
                            <span class="input-group-text w-25 bg-white border border-primary">Nazwa</span>
                            <input id="restaurant-name" class="form-control border border-primary" placeholder="${restaurant.name}" value="${restaurant.name}">
                        </div>

                        <div class="input-group my-1 d-flex flex-column">
                            <span class="input-group-text bg-white border border-primary">Adres</span>
                            <textarea class="form-control w-100" id="address" rows="2" cols="50">${restaurant.address}</textarea>
                        </div>

                        <div class="input-group my-1">
                            <span class="input-group-text w-25 bg-white border border-primary">Email</span>
                            <input class="form-control border border-primary" id="email" placeholder="${restaurant.email}" value="${restaurant.email}">
                        </div>

                        <div class="input-group my-1">
                            <span class="input-group-text w-25 bg-white border border-primary">Tel.</span>
                            <input class="form-control border border-primary" id="phone" placeholder="${restaurant.phone}" value="${restaurant.phone}">
                        </div>

                        <div class="input-group my-1 d-flex flex-column">
                            <span class="input-group-text bg-white border border-primary">Tagi</span>
                            <textarea class="form-control w-100" id="tags" rows="2" cols="50">${restaurant.tags}</textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button data-restaurant-id="${restaurant.restaurantId}" id="update-restaurant-btn" class="btn btn-primary mb-3">Zatwierdź</button>
                    <button type="button" class="btn btn-outline-primary mb-3" data-bs-dismiss="modal">Zamknij</button>
                </div>
            </div>
        </div>
    </div>


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
                    <form id="add-menu-item-form" action="/restaurants/${restaurant.restaurantId}/menuItems" method="post">
                        <div class="input-group">
                            <span class="input-group-text mb-2 bg-white text-dark border border-primary" style="width: 125px;">nazwa</span>
                            <input class="mb-2 form-control border border-primary" id="name" name="name">
                        </div>
                        <div class="input-group">
                            <span class="input-group-text mb-2 bg-white text-dark border border-primary" style="width: 125px;">kategoria</span>
                            <select id="category" name="category" class="form-control mb-2 border border-primary">
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
                            <span class="input-group-text mb-2 bg-white text-dark border border-primary" style="width: 125px;">cena</span>
                            <input class="mb-2 form-control border border-primary" type="number" id="price" name="price">
                        </div>
                        <div class="input-group">
                            <span class="input-group-text mb-2 bg-white text-dark border border-primary" style="width: 125px;">opis</span>
                            <input class="mb-2 form-control border border-primary" id="description" name="description">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button id="add-menu-item-btn" class="btn btn-primary mb-3">Dodaj</button>
                    <button type="button" class="btn btn-outline-primary mb-3" data-bs-dismiss="modal">Odrzuć</button>
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
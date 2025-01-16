<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">

    <link rel="stylesheet" href="/static/css/order-menu.css">
    <title>Menu zamówienia</title>
</head>
<body>

    <div id="main-container" class="d-flex flex-row">
        <#include "side-navbar.ftlh">
        <div class="w-100">
            <div id="info-panel" class="sticky-top d-flex align-items-center justify-content-center bg-dark text-white row">
                <span class="col-3 d-flex justify-content-center">21:33:00</span>
                <span class="col-6 d-flex justify-content-center"><b>${restaurant.name}</b></span>
                <span class="col-3 d-flex justify-content-center">
                    <i class="bi bi-cart4"></i>
                    <i id="cart-circle" style="position: absolute; top: 25px; right: 170px; font-size: 10px;" class="bi bi-circle-fill text-danger"></i>
                </span>
            </div>
            <div class="d-flex flex-column align-items-center w-100">
                <#if menuItemList?has_content>
                    <div id="display-menuItem-container" class="row w-100 justify-content-center" style="border: 1px solid green;">
                        <#list menuItemList as menuItem>
                            <div class="menuItem-tile-${menuItem.menuItemId} row my-1 mx-1 d-flex justify-content-center">
                                <h5 class="col-3 mx-3">${menuItem.name}</h5>
                                <div class="menuItem-description col-3">${menuItem.description}</div>
                                <div class="menuItem-price col-1">${menuItem.price} zł</div>
                                <div data-menu-item-id="${menuItem.menuItemId}" class="d-flex flex-row col-1 menuItem-actions">
                                    <button data-bs-toggle="modal" data-bs-target="#add-item-to-order-modal-${menuItem.menuItemId}" class="mx-1 btn btn-dark text-white update-menuItem-${menuItem.menuItemId}"><i class="bi bi-basket3-fill"></i></button>
                                </div>
                            </div>

                            <div class="modal fade" id="add-item-to-order-modal-${menuItem.menuItemId}" tabindex="-1" aria-labelledby="add-item-to-order-modal">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <div id="modal-title-div">
                                                <h1 class="modal-title fs-5">Dodaj do zamówienia</h1>
                                            </div>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <form method="post" action="/order-menu/1">
                                                <div class="input-group">
                                                    <h5>${menuItem.name}</h5>
                                                </div>
                                                <div class="input-group">
                                                    <span class="input-group-text mb-2 bg-dark text-white" style="width: 125px;">
                                                        Dodatkowy <br/> opis
                                                    </span>
                                                    <textarea class="mb-2 form-control" cols="60" rows="5" id="description" name="description">Dodaj opcjonalnie</textarea>
                                                </div>
                                                <div class="input-group">
                                                    <span class="input-group-text mb-2 bg-dark text-white" style="width: 125px;">Ilość </span>
                                                    <input class="mb-2 form-control" type="number" id="quantity" name="quantity">
                                                </div>
                                                <div>
                                                    <div class="row">
                                                        <div class="col-6 d-flex justify-content-start">Wartość</div>
                                                        <div class="col-6 d-flex justify-content-end">${menuItem.price} zł</div>

                                                    </div>

                                                </div>
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
                        </#list>
                    </div>
                <#else>
                    <h5> Brak danych do wyświetlenia</h5>
                </#if>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>





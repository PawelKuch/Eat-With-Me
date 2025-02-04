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
    <script src="/static/js/order-menu.js"></script>

    <link rel="stylesheet" href="/static/css/order-menu.css">
    <title>Menu zamówienia</title>
</head>
<body>

    <div id="main-container" class="d-flex flex-row w-100">
        <#include "side-navbar.ftlh">
        <div class="align-items-start w-100">
            <div id="container" class="row w-100">
                <div class="col-7 d-flex flex-column">
                    <#if menuItemList?has_content>
                        <div id="display-menuItem-container" class="list-group justify-content-center">
                            <#if menuItemList?has_content>
                                <div class="group-list row justify-content-center fw-bold fs-5">
                                    <span class="col-3">Danie</span>
                                    <span class="col-3">Opis</span>
                                    <span class="col-1">Cena</span>
                                    <span class="col-1">Czynność</span>
                                </div>
                            <#list menuItemList as menuItem>
                                <div class="menuItem-tile-${menuItem.menuItemId} list-group-item my-1 mx-1 d-flex justify-content-center">
                                    <h5 class="col-3 mx-3">${menuItem.name}</h5>
                                    <div class="menuItem-description col-3">${menuItem.description}</div>
                                    <div class="menuItem-price col-1">${menuItem.price} zł</div>
                                    <div data-menu-item-id="${menuItem.menuItemId}" class="col-1 menuItem-actions">
                                        <#if currentUser?has_content><button data-bs-toggle="modal" data-bs-target="#add-item-to-order-modal-${menuItem.menuItemId}" class="mx-1 btn btn-dark text-white"><i class="bi bi-basket3-fill"></i></button><#else><div class="mx-1 w-100 d-flex justify-content-end"> - </div></#if>
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
                                                <form class="order-form-${menuItem.menuItemId}" method="post" action="/order-menu/add-menu-item-to-order/${restaurantId}/${order.orderId}">
                                                    <div class="input-group">
                                                        <h5>${menuItem.name}</h5>
                                                        <input name="menuItemId" type="hidden" value="${menuItem.menuItemId}">
                                                    </div>
                                                        <#if currentUser?has_content> <input type="hidden" name="userId" value="${currentUser.userId}"></#if>
                                                    <div class="input-group">
                                                        <span id="description-span" class="input-group-text mb-2 bg-dark text-white">
                                                            Dodatkowy <br/> opis
                                                        </span>
                                                        <textarea class="mb-2 form-control" cols="60" rows="5" id="description" name="description" placeholder="Dodaj opcjonalnie"></textarea>
                                                    </div>
                                                    <div>
                                                        <div class="row">
                                                            <div class="col-6 d-flex justify-content-start fw-bold">Cena</div>
                                                            <div class="col-6 d-flex justify-content-end fw-bold">${menuItem.price} zł</div>
                                                        </div>
                                                        <input type="hidden" name="price" value="${menuItem.price}">
                                                    </div>

                                                </form>
                                            </div>
                                            <div class="modal-footer">
                                                <button data-item-id="${menuItem.menuItemId}" class="btn btn-outline-dark mb-3 submit-form-btn" type="submit">Dodaj</button>
                                                <button type="button" class="btn btn-secondary mb-3" data-bs-dismiss="modal">Zamknij</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#list>
                            </#if>
                        </div>

                    <#else>
                        <h5> Brak danych do wyświetlenia</h5>
                    </#if>
                </div>

                <div id="orders-containers" class="col-5 d-flex flex-column border-start border-dark">
                    <div class="d-flex justify-content-center"><img src="/logos/${restaurant.restaurantId}" alt="restaurant logo ${restaurant.restaurantId}" width="200px" height="200px"></div>
                    <#if order.orderItemList?has_content>
                    <div class="row fw-bold border-bottom p-2">
                        <div class="col-3">Danie</div>
                        <div class="col-1">Cena</div>
                        <div class="col-3 d-flex justify-content-center">Opis</div>
                        <div class="col-3">Pracownik</div>
                        <div class="col-2">Czynność</div>
                    </div>
                    <div class="list-group-flush row d-flex">
                        <#list order.orderItemList as orderItem>
                            <div class="list-group-item col-3">${orderItem.menuItem.name}</div>
                            <div class="list-group-item col-1">${orderItem.menuItem.price}</div>
                            <div class="list-group-item col-3">${orderItem.description}</div>
                            <div class="list-group-item col-3">${orderItem.user.firstName} ${orderItem.user.lastName}</div>
                            <div class="list-group-item col-2">
                                <#if currentUser?has_content && (orderItem.user.userId == currentUser.userId)>
                                    <a href="/delete-order-item/${restaurant.restaurantId}/${order.orderId}/${orderItem.orderItemId}" class="btn btn-dark justify-content-center me-1">
                                        <i class="bi bi-trash-fill"></i>
                                    </a>
                                <#else>
                                    <div class="me-1"> - </div>
                                </#if>
                            </div>
                        </#list>
                        <div class="col-8 fw-bold">Wartość całkowita</div>
                        <div class="col-4 fw-bold d-flex justify-content-end">${order.value} zł</div>
                    </div>
                    <#else>
                        <div class="alert alert-warning text-center mt-3">Brak pozycji w zamówieniu</div>
                    </#if>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>





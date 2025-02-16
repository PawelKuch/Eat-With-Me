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
    <div class="container">
            <div id="orders-containers" class="d-flex flex-column">
                <div class="d-flex justify-content-center"><img src="/restaurants/logos/${restaurant.restaurantId}" alt="restaurant logo ${restaurant.restaurantId}" width="200px" height="200px"></div>
                <h5>Podsumowanie zamówienia</h5>
                <#if order.orderItemList?has_content>
                    <div class="row fw-bold border-bottom py-2">
                        <div class="col-4">Danie</div>
                        <div class="col-4">Opis</div>
                        <div class="col-3">Użytkownik</div>
                        <div class="col-1 d-flex justify-content-end">Cena</div>
                    </div>
                    <div class="list-group-flush d-flex flex-column row">
                        <#list order.orderItemList as orderItem>
                            <div class="list-group-item row d-flex flex-row">
                                <div class="col-4">${orderItem.menuItem.name}</div>
                                <div class="col-4">${orderItem.description}</div>
                                <div class="col-3">${orderItem.user.firstName} ${orderItem.user.lastName}</div>
                                <div class="col-1 d-flex justify-content-end">${orderItem.menuItem.price}</div>
                            </div>
                        </#list>
                        <div class="d-flex flex-row mt-2">
                            <div class="col-11 fw-bold">Wartość całkowita</div>
                            <div class="col-1 fw-bold d-flex justify-content-end">${order.value} zł</div>
                        </div>
                    </div>
                    <#if !order.isClosed>
                    <form id="extra-payment-discount-form" action="/orders/${order.orderId}/summary" method="post">
                        <div class="d-flex flex-column align-items-center">
                            <div class="input-group w-25 mb-3">
                                <span class="input-group-text">Rabat [zł]</span>
                                <input id="cash-discount" class="form-control w-25" type="number" name="cashDiscount" value="0.0">
                            </div>

                            <div class="input-group w-25 mb-3">
                                <span class="input-group-text">Rabat [%]</span>
                                <input id="percentage-discount" class="form-control w-25" type="number" name="percentageDiscount" value="0">
                            </div>

                            <div class="input-group w-25 mb-3">
                                <span class="input-group-text">Dopłata [zł]</span>
                                <input id="extra-payment" class="form-control w-25" type="number" name="extraPayment" value="0.0">
                            </div>

                            <button class="btn btn-dark" type="submit">Zatwierdź i zamknij zamówienie</button>
                        </div>
                    </form>
                    <#else>
                        <h5>Zamówienie zamknięte</h5>
                    </#if>
                <#else>
                    <div class="alert alert-warning text-center mt-3">Brak pozycji w zamówieniu</div>
                </#if>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>





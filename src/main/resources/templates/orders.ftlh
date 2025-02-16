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
    <link rel="stylesheet" href="/static/css/order-menu.css">
    <title>Menu zamówienia</title>
</head>
<body>

<div id="main-container" class="d-flex flex-row">
    <#include "side-navbar.ftlh">
    <div id="container" class="w-100">
        <#if orderList?has_content>
            <div class="d-flex justify-content-start">
                <a href="/orders/restaurants"><button class="btn btn-dark">Dodaj</button></a>
            </div>
            <div id="orders-containers" class="row d-flex justify-content-center align-items-center">
                <div class="row col-12">
                    <div class="col-2 fw-bold"> Restauracja </div>
                    <div class="col-3 fw-bold"> Data zamknięcia</div>
                    <div class="col-2 fw-bold"> Wartość</div>
                    <div class="col-2 fw-bold"> Szczegóły</div>
                    <div class="col-1 fw-bold"> Status</div>
                    <div class="col-2 fw-bold"> Właściciel</div>
                </div>
                <div class="row">
                    <#list orderList as order>
                        <div class="col-2">
                            ${order.restaurantData.name}
                        </div>
                        <div class="col-3">
                            ${order.closingDateTime?datetime?string("dd-MM-yyyy HH:mm")}
                        </div>
                        <div class="col-2">
                            ${order.value}
                        </div>
                        <div class="col-2">
                            <a href="/orders/${order.orderId}/orderItems">Szczegóły</a>
                        </div>
                        <div class="col-1">
                            <#if order.isClosed>Zamknięte<#else>Otwarte</#if>
                        </div>
                        <div class="col-2">
                            <a href="#" id="user-details">${order.owner.firstName} ${order.owner.lastName}</a>
                        </div>
                    </#list>
                </div>
            </div>
        <#else>
            <div class="d-flex justify-content-center flex-column align-items-center h-100">
                <span class="alert alert-warning text-dark">Brak zamówień do wyświetlenia</span>
                <#if currentUser?has_content>
                    <a href="/orders/restaurants" class="text-decoration-none mt-2"><button class="btn btn-dark">Dodaj zamówienie</button></a>
                </#if>
            </div>
        </#if>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>





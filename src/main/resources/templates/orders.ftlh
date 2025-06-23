<!doctype html>
<html lang="pl">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet" />

    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="/static/js/side-navbar.js"></script>
    <link rel="stylesheet" href="/static/css/order-menu.css" />
    <title>Menu zamówienia</title>
</head>
<body>

<div id="main-container" class="d-flex flex-row min-vh-100">
    <#include "side-navbar.ftlh">

    <div id="container" class="flex-grow-1 p-4">
        <#if orderList?has_content>
            <div class="mb-3">
                <a href="/orders/restaurants" class="btn btn-primary">
                    <i class="bi bi-plus-lg me-1"></i> Dodaj
                </a>
            </div>

            <div id="orders-containers">
                <table class="table table-striped table-hover align-middle">
                    <thead class="table-primary">
                    <tr>
                        <th>Restauracja</th>
                        <th>Data zamknięcia</th>
                        <th>Wartość</th>
                        <th>Szczegóły</th>
                        <th>Status</th>
                        <th>Właściciel</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list orderList as order>
                        <tr>
                            <td>${order.restaurantName}</td>
                            <td>${order.closingDate?datetime?string("dd-MM-yyyy HH:mm")}</td>
                            <td>${order.orderValue}</td>
                            <td>
                                <#if order.closed>
                                    <button class="btn btn-primary"> <a href="/orders/${order.orderId}/summary" class="text-white"><i class="bi bi-info-circle"></i></a></button>
                                <#else>
                                    <#if currentUser?has_content>
                                        <button class="btn btn-primary"><a href="/orders/${order.orderId}/orderItems" class="text-white"><i class="bi bi-info-circle"></i>
                                        </a></button>
                                    <#else>
                                        <button class="btn btn-primary"> <a href="/orders/${order.orderId}/summary" class="text-white"><i class="bi bi-info-circle"></i></a></button>
                                    </#if>
                                </#if>
                            </td>
                            <td>
                                <#if order.isClosed()>Zamknięte<#else>Otwarte</#if>
                            </td>
                            <td>
                                <a href="#" id="user-details">${order.ownerFirstName} ${order.ownerLastName}</a>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        <#else>
            <div class="d-flex flex-column justify-content-center align-items-center">
                <div class="alert alert-warning text-center w-100">
                    Brak zamówień do wyświetlenia
                </div>
                <#if currentUser?has_content>
                    <a href="/orders/restaurants" class="btn btn-outline-primary mt-3">Dodaj zamówienie</a>
                </#if>
            </div>
        </#if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

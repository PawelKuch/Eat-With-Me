<!doctype html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">

    <script src="https://code.jquery.com/jquery-3.6.4.min.js" ></script>
    <script src="/static/js/order-summary.js"></script>

    <title>Menu zamówienia</title>
</head>
<body>

<div id="main-container" class="d-flex flex-row w-100">
    <#include "side-navbar.ftlh">
    <div class="container">
        <div id="orders-containers" class="d-flex flex-column">
            <div class="d-flex justify-content-center"><img src="/restaurants/${restaurant.restaurantId}/logo" alt="restaurant logo ${restaurant.restaurantId}" width="200px" height="200px"></div>
            <h5>Podsumowanie zamówienia</h5>
            <#if order.orderItemList?has_content>
                <div class="row fw-bold border-bottom py-2">
                    <div class="col-4">Danie</div>
                    <div class="col-3">Opis</div>
                    <div class="col-2">Użytkownik</div>
                    <div class="col-1 d-flex justify-content-end">Cena</div>
                </div>
                <div class="list-group-flush d-flex flex-column row">
                    <#list order.orderItemList as orderItem>
                        <div class="list-group-item row d-flex flex-row">
                            <div class="col-4">${orderItem.menuItem.name}</div>
                            <div class="col-3">${orderItem.description}</div>
                            <div class="col-2">${orderItem.user.firstName} ${orderItem.user.lastName}</div>
                            <div class="col-1 d-flex justify-content-end">${orderItem.menuItem.price} zł</div>
                        </div>
                    </#list>
                    <#if order.extraPaymentValue?number gt 0.00>
                        <div class="list-group-item row d-flex flex-row col-12">
                            <div class="col-11 d-flex">
                                <div class="col-4">Dostawa</div>
                                <div class="col-6"></div>
                                <div class="col-1 d-flex justify-content-end pe-2">${order.extraPaymentValue} zł</div>
                            </div>
                        </div>
                    </#if>
                    <#if order.cashDiscount?number gt 0.00>
                        <div class="list-group-item row d-flex flex-row col-12">
                            <div class="col-11 d-flex">
                                <div class="col-4">Rabat kwotowy</div>
                                <div class="col-6"></div>
                                <div class="col-1 d-flex justify-content-end pe-2">${order.cashDiscount} zł</div>
                            </div>
                        </div>
                    </#if>
                    <#if order.percentageDiscount?number gt 0.00>
                        <div class="list-group-item row d-flex flex-row col-12">
                            <div class="col-11 d-flex">
                                <div class="col-4">Rabat procentowy</div>
                                <div class="col-2 d-flex justify-content-center">${order.percentageDiscount}%</div>
                                <div class="col-4"></div>
                                <div class="col-1 d-flex justify-content-end pe-2">${order.percentageDiscountCashValue} zł</div>
                            </div>
                        </div>
                    </#if>
                </div>
            <#else>
                <div class="alert alert-warning text-center mt-3">Brak pozycji w zamówieniu</div>
            </#if>
        </div>


        <button class="btn btn-primary" data-bs-target="#order-details-modal" data-bs-toggle="modal">Szczegóły</button>

        <div class="modal fade w-100" id="order-details-modal" tabindex="-1" aria-labelledby="order-details-modal">
            <div class="modal-dialog modal-xl w-100">
                <div class="modal-content">
                    <div class="modal-header">
                        <div id="modal-title-div">
                            <h1 class="modal-title fs-5">SZCZEGÓŁY</h1>
                        </div>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body w-100">
                        <div class="d-flex justify-content-center flex-column">
                            <div class="list-group-flush">
                                <div class="list-group-item d-flex flex-row">
                                    <div class="d-flex col-10">Liczba użytkowników w zamówieniu</div>
                                    <div class="d-flex col-2 align-items-center">${userAmountForOrder} <br/></div>
                                </div>
                                <div class="list-group-item d-flex flex-row">
                                    <div class="d-flex col-8 align-items-center">Rabat procentowy</div>
                                    <div class="d-flex col-4 justify-content-center">${order.percentageDiscount}% <br/> (${order.percentageDiscountCashValue} zł) </div>
                                </div>
                                <div id="costs-summary" class="mt-5">
                                    <div class="list-group-flush">
                                        <div class="list-group-item d-flex flex-row">
                                            <div class="d-flex col-2">Użytkownik</div>
                                            <div class="d-flex col-2 justify-content-center">Koszt w zamówieniu</div>
                                            <div class="d-flex col-2 justify-content-center">Udział w rabacie kwotowym</div>
                                            <div class="d-flex col-2 justify-content-center">Dopłata</div>
                                            <div class="d-flex col-2 justify-content-center">Cena po rabatach</div>
                                            <div class="d-flex col-2 justify-content-center">Cena końcowa</div>
                                        </div>
                                        <#list order.userOrderPaymentList as uop>
                                            <div class="list-group-item d-flex flex-row">
                                                <div class="d-flex col-2">${uop.user.firstName} ${uop.user.lastName}</div>
                                                <div class="d-flex col-2 justify-content-center">
                                                    <#if uop.extraPaymentValue?number gt 0.00 || uop.discountValueInCash?number gt 0.00 || uop.discountInPercentageInCash?number gt 0.00>
                                                        <s>${uop.menuItemsValue} zł </s>
                                                    <#else>
                                                        ${uop.menuItemsValue} zł
                                                    </#if>
                                                </div>

                                                <div class="d-flex col-2 justify-content-center">${uop.discountValueInCash} zł</div>
                                                <div class="d-flex col-2 justify-content-center">${uop.extraPaymentValue} zł</div>
                                                <div class="d-flex col-2 justify-content-center">${uop.amountToPayWithoutExtraPayment} zł</div>
                                                <div class="d-flex col-2 justify-content-center">${uop.amountToPay} zł</div>

                                            </div>
                                        </#list>
                                        <div class="list-group-item d-flex flex-row">
                                            <div class="d-flex col-2">RAZEM</div>
                                            <div class="d-flex col-2 justify-content-center">
                                                <#if order.extraPaymentValue?number gt 0.00 || order.cashDiscount?number gt 0.00 || order.percentageDiscountCashValue?number gt 0.00>
                                                    <s>${order.netValue} zł </s>
                                                <#else>
                                                    ${order.netValue} zł
                                                </#if>
                                            </div>
                                            <div class="d-flex col-6"></div>
                                            <div class="d-flex col-2 justify-content-center">${order.value} zł</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary mb-3" data-bs-dismiss="modal">Zamknij</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="summary" class="mt-5 d-flex justify-content-center">
            <div class="list-group-flush w-75">
                <div class="list-group-item d-flex flex-row">
                    <div class="d-flex col-4">Użytkownik</div>
                    <div class="d-flex col-3 justify-content-center">Koszt</div>
                    <div class="d-flex col-2 justify-content-center">Dopłata</div>
                    <div class="d-flex col-3 justify-content-center">Do zapłaty</div>
                </div>
                <#list userOrderPaymentList as uop>
                    <div class="list-group-item d-flex flex-row">
                        <div class="d-flex col-4">${uop.user.firstName} ${uop.user.lastName}</div>
                        <div class="d-flex col-3 justify-content-center">${uop.menuItemsValue} zł</div>
                        <div class="d-flex col-2 justify-content-center">${uop.extraPaymentValue} zł</div>
                        <div class="d-flex col-3 justify-content-center">${uop.amountToPay} zł</div>
                    </div>
                </#list>
            </div>
        </div>
        <br/>

        ______________________________________
        <#if !order.isClosed>
            <div class="d-flex fw-bold mt-3">Ustaw dodatkowe rabaty lub opłaty</div>

            <form action="/orders/${order.orderId}/summary" method="post" class="mt-2">
                <div class="d-flex flex-row">
                    <div class="input-group w-25 mb-3 mx-2">
                        <span class="input-group-text">Rabat [zł]</span>
                        <input id="cash-discount" class="form-control w-25" type="number" name="cashDiscount" value="${order.cashDiscount}">
                    </div>
                </div>


                <div class="d-flex flex-row">
                    <div class="input-group w-25 mb-3 mx-2">
                        <span class="input-group-text">Rabat [%]</span>
                        <input id="percentage-discount" data-order-discount="<#if order.percentageDiscount?number lt 0.01>0.0<#else>${order.percentageDiscount}</#if>"  class="form-control w-25" type="number" name="percentageDiscount" value="${order.percentageDiscount}">
                    </div>
                </div>


                <div class="d-flex flex-row">
                    <div class="input-group mb-3 mx-2 w-25">
                        <span class="input-group-text">Dostawa [zł]</span>
                        <input id="extra-payment" class="form-control w-25" name="extraPayment" value="${order.extraPaymentValue}">
                    </div>
                </div>

                <div class="d-flex mx-2 mb-3 w-25">
                    <button class="btn btn-outline-primary form-control" type="submit">Zatwierdź zmiany</button>
                </div>
            </form>
        <#else>
            <h5>Zamówienie zamknięte</h5>
        </#if>
    </div>
</div>







<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
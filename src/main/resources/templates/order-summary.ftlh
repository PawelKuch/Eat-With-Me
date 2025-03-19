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
    <script src="/static/js/order-summary.js"></script>

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
                            <div class="col-1 d-flex justify-content-end">${orderItem.menuItem.price}</div>
                        </div>
                    </#list>
                    <#list order.extraPaymentList as extraPayment>
                        <div class="list-group-item row d-flex flex-row col-12">
                            <div id="extra-payment-details-div-${extraPayment.extraPaymentId}" class="col-11 d-flex">
                                <div class="col-4">${extraPayment.extraPaymentProduct}</div>
                                <div class="col-6"></div>
                                <div class="col-1 d-flex justify-content-end pe-2">${extraPayment.price}</div>
                            </div>
                            <form id="update-extra-payment-form-${extraPayment.extraPaymentId}" class="col-11 d-none flex-row">
                                <textarea class="col-4" cols="50" rows="1" id="new-extra-payment-product-${extraPayment.extraPaymentId}">${extraPayment.extraPaymentProduct}</textarea>
                                <div class="col-6"></div>
                                <textarea cols="10" rows="1" id="new-extra-payment-price-${extraPayment.extraPaymentId}">${extraPayment.price}</textarea>
                            </form>
                            <div class="col-1 d-flex flex-row">
                                <div id="update-and-delete-extra-payment-div-${extraPayment.extraPaymentId}" class="d-flex flex-row">
                                    <button class="btn btn-dark update-extra-payment-btn" data-extra-payment-id="${extraPayment.extraPaymentId}" data-order-id="${order.orderId}"><i class="bi bi-pencil-fill"></i></button>
                                    <button class="btn btn-dark ms-1 delete-extra-payment-btn" data-extra-payment-id="${extraPayment.extraPaymentId}" data-order-id="${order.orderId}"><i class="bi bi-trash3-fill"></i></button>
                                </div>
                                <div id="confirm-and-discard-new-extra-payment-div-${extraPayment.extraPaymentId}" class="d-none flex-row">
                                    <button id="confirm-new-extra-payment-btn-${extraPayment.extraPaymentId}" class="btn btn-dark" data-extra-payment-id="${extraPayment.extraPaymentId}" data-order-id="${order.orderId}">OK</button>
                                    <button id="discard-new-extra-payment-btn-${extraPayment.extraPaymentId}" class="btn btn-dark ms-1" data-extra-payment-id="${extraPayment.extraPaymentId}" data-order-id="${order.orderId}">X</button>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            <#else>
                <div class="alert alert-warning text-center mt-3">Brak pozycji w zamówieniu</div>
            </#if>
        </div>
        <div id="costs-summary" class="mt-5">
            <div class="list-group-flush">
                <div class="list-group-item d-flex flex-row">
                    <div class="d-flex col-3">Użytkownik</div>
                    <div class="d-flex col-2 justify-content-center">Koszt w zamówieniu</div>
                    <div class="d-flex col-3 justify-content-center">Udział w rabacie kwotowym</div>
                    <div class="d-flex col-2 justify-content-center">Cena po rabatach</div>
                    <div class="d-flex col-2 justify-content-center">Cena końcowa</div>
                </div>
                <#list order.userOrderPaymentList as uop>
                    <div class="list-group-item d-flex flex-row">
                        <div class="d-flex col-3">${uop.user.firstName} ${uop.user.lastName}</div>
                        <div class="d-flex col-2 justify-content-center">${uop.menuItemsValue} zł</div>
                        <div class="d-flex col-3 justify-content-center">${uop.discountValueInCash} zł</div>
                        <div class="d-flex col-2 justify-content-center">${uop.amountToPayWithoutExtraPayment} zł</div>
                        <div class="d-flex col-2 justify-content-center">${uop.amountToPay} zł</div>

                    </div>
                </#list>
                <div class="list-group-item d-flex flex-row">
                    <div class="d-flex col-3">RAZEM</div>
                    <div class="d-flex col-2 justify-content-center">${order.netValue} zł</div>
                    <div class="d-flex col-5"></div>
                    <div class="d-flex col-2 justify-content-center">${order.value}zł</div>
                </div>
            </div>

        </div>

        Obecna liczba użytkowników na zamówieniu: ${userAmountForOrder} <br/>
        Rabat procentowy:${order.percentageDiscount} <br/>
        Wartość procentowego rabatu: ${order.percentageDiscountCashValue} zł <br/>
        Rabat kwotowy: ${order.cashDiscount} <br/>
        Dopłata: ${order.extraPaymentValue}

        <br/>

        ______________________________________
        <#list userOrderPaymentList as payment>
            <br/>${payment.user.firstName} ${payment.user.lastName} || do zapłaty: ${payment.amountToPay} || <#if payment.extraPaymentValue?has_content> dopłata: ${payment.extraPaymentValue} </#if> zapłacone: ${payment.isPaid?string}
            <br/>rabat w %: ${payment.discountInPercentage} || rabat w gotówce: ${payment.discountValueInCash} || Całkowita wartość rabatu: ${payment.generalDiscountValue} zł
        </#list>
        <#if !order.isClosed>
            <div class="d-flex fw-bold mt-3">Ustaw dodatkowe rabaty lub opłaty</div>


            <form action="/orders/${order.orderId}/summary" method="post" class="mt-2">
                <input name="isPercentage" value="true" type="hidden">
                <div class="d-flex flex-row">
                    <div class="input-group w-25 mb-3 mx-2">
                        <span class="input-group-text">Rabat [%]</span>
                        <input id="percentage-discount" data-order-discount="<#if order.percentageDiscount?number lt 0.01>0.0<#else>${order.percentageDiscount}</#if>"  class="form-control w-25" type="number" name="percentageDiscount">
                    </div>
                    <div class="mx-2">
                        <button class="btn btn-outline-primary" type="submit">Zatwierdź rabat</button>
                    </div>
                </div>
            </form>


            <form action="/orders/${order.orderId}/summary" method="post">
                <input name="isPercentage" value="false" type="hidden">
                <div class="d-flex flex-row">
                    <div class="input-group w-25 mb-3 mx-2">
                        <span class="input-group-text">Rabat [zł]</span>
                        <input id="cash-discount" class="form-control w-25" type="number" name="cashDiscount" value="0.0">
                    </div>
                    <div class="mx-2">
                        <button type="submit" class="btn btn-outline-primary">Zatwierdź rabat</button>
                    </div>
                </div>
            </form>

            <form action="/orders/${order.orderId}/summary" method="post">
                <div class="d-flex flex-row">
                    <div class="input-group mb-3 mx-2 w-25">
                        <span class="input-group-text">Dodatkowa opłata</span>
                        <input id="extra-payment-product" class="form-control w-25" name="extraPaymentProduct" placeholder="np. dostawa">
                    </div>
                    <div class="input-group mb-3 mx-2 w-25">
                        <span class="input-group-text">Kwota [zł]</span>
                        <input id="extra-payment" class="form-control w-25" name="extraPayment" value="0.0">
                    </div>
                    <div class="d-flex mx-2 mb-3 w-25">
                        <button class="btn btn-outline-primary form-control" type="submit">Zatwierdź dopłatę</button>
                    </div>
                </div>
            </form>
        <#else>
            <h5>Zamówienie zamknięte</h5>
        </#if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>





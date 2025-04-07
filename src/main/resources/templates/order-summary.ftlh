<!doctype html>
<html lang="pl">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="/static/js/order-summary.js"></script>

    <title>Menu zamówienia</title>
</head>
<body>
<div id="main-container" class="d-flex flex-row w-100">
    <#include "side-navbar.ftlh">
    <div class="container py-4">
        <div id="orders-containers">
            <div class="text-center mb-4">
                <img src="/restaurants/${restaurant.restaurantId}/logo" alt="restaurant logo" class="img-fluid rounded" width="200" height="200">
            </div>
            <h5 class="text-center">Podsumowanie zamówienia</h5>

            <#if order.orderItemList?has_content>
                <div class="row fw-bold border-bottom pb-2">
                    <div class="col-4">Danie</div>
                    <div class="col-3">Opis</div>
                    <div class="col-2">Użytkownik</div>
                    <div class="col-1 text-end">Cena</div>
                </div>
                <div class="list-group-flush d-flex flex-column">
                    <#list order.orderItemList as orderItem>
                        <div class="list-group-item d-flex flex-row row py-2">
                            <div class="col-4">${orderItem.menuItem.name}</div>
                            <div class="col-3">${orderItem.description}</div>
                            <div class="col-2">${orderItem.user.firstName} ${orderItem.user.lastName}</div>
                            <div class="col-1 text-end">${orderItem.menuItem.price} zł</div>
                        </div>
                    </#list>
                    <#if order.extraPaymentValue?number gt 0.00>
                        <div class="list-group-item d-flex flex-row row py-2">
                            <div class="col-4">Dostawa</div>
                            <div class="col-5"></div>
                            <div class="col-1 text-end">${order.extraPaymentValue} zł</div>
                        </div>
                    </#if>
                    <#if order.cashDiscount?number gt 0.00>
                        <div class="list-group-item d-flex flex-row row py-2">
                            <div class="col-4">Rabat kwotowy</div>
                            <div class="col-5"></div>
                            <div class="col-1 text-end">${order.cashDiscount} zł</div>
                        </div>
                    </#if>
                    <#if order.percentageDiscount?number gt 0.00>
                        <div class="list-group-item d-flex flex-row row py-2">
                            <div class="col-4">Rabat procentowy</div>
                            <div class="col-3">${order.percentageDiscount}%</div>
                            <div class="col-2"></div>
                            <div class="col-1 text-end">${order.percentageDiscountCashValue} zł</div>
                        </div>
                    </#if>
                    <div class="list-group-item d-flex flex-row row py-2">
                        <div class="col-4"><b>Wartość końcowa zamówienia</b></div>
                        <div class="col-5"></div>
                        <div class="col-1 text-end">
                            <#if order.extraPaymentValue?number gt 0.00 || order.cashDiscount?number gt 0.00 || order.percentageDiscountCashValue?number gt 0.00>
                                <s>${order.netValue} zł </s> <b>${order.value}</b>
                            <#else>
                                <b>${order.netValue} zł</b>
                            </#if>
                        </div>


                    </div>
                </div>
            <#else>
                <div class="alert alert-warning text-center mt-3">Brak pozycji w zamówieniu</div>
            </#if>
        </div>

        <div class="text-center mt-4">
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#order-details-modal">Szczegóły</button>
        </div>


        <div class="modal fade" id="order-details-modal" tabindex="-1">
            <div class="modal-dialog modal-xl">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">SZCZEGÓŁY</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Zamknij"></button>
                    </div>
                    <div class="modal-body">
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
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Zamknij</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="summary" class="mt-5">
            <div class="list-group-item d-flex">
                <div class="col-4 fw-bold">Użytkownik</div>
                <div class="col-3 text-center fw-bold">Koszt</div>
                <div class="col-2 text-center fw-bold">Dopłata/Dowóz</div>
                <div class="col-3 text-center fw-bold">Do zapłaty</div>
            </div>
            <#list userOrderPaymentList as uop>
                <div class="list-group-item d-flex">
                    <div class="col-4">${uop.user.firstName} ${uop.user.lastName}</div>
                    <div class="col-3 text-center">${uop.menuItemsValue} zł</div>
                    <div class="col-2 text-center">${uop.extraPaymentValue} zł</div>
                    <div class="col-3 text-center">${uop.amountToPay} zł</div>
                </div>
            </#list>
        </div>


        <#if !order.isClosed>
            <div class="mt-5">
                <h5>Ustaw dodatkowe rabaty lub opłaty</h5>
                <form action="/orders/${order.orderId}/summary" method="post">
                    <div class="row mb-3">
                        <div class="col-md-4">
                            <label class="form-label">Rabat [zł]</label>
                            <input id="cash-discount" name="cashDiscount" type="number" class="form-control" value="${order.cashDiscount}">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Rabat [%]</label>
                            <input id="percentage-discount" name="percentageDiscount" type="number" class="form-control" value="${order.percentageDiscount}">
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Dostawa [zł]</label>
                            <input id="extra-payment" name="extraPayment" type="number" class="form-control" value="${order.extraPaymentValue}">
                        </div>
                    </div>
                    <button type="submit" class="btn btn-success">Zapisz</button>
                </form>
            </div>
        </#if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

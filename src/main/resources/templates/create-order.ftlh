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
    <link rel="stylesheet" href="/static/css/restaurant.css">
    <script src="/static/js/create-order.js"></script>
    <title>Restauracje</title>
</head>
<body>
<div id="main-container" class="d-flex flex-row w-100">
    <#include "side-navbar.ftlh">


    <div id="create-order-container" class="d-flex flex-column w-100 mt-3 align-items-center justify-content-center">

        <div class="order-details d-flex flex-column justify-content-center bg-dark w-50 rounded-3 px-3 py-3">
            <div class="text-white"><h2>Ustal szczegóły nowego zamówienia</h2></div>
            <form id="create-order-form" method="post" action="/orders">
                <div class="input-group">
                    <span class="input-group-text w-50 mb-2">Data zamknięcia</span>
                    <input id="closing-date" class="form-control mb-2" name="closingDate" type="datetime-local" placeholder="data zamknięcia" required>
                </div>
                <div class="input-group">
                    <span class="input-group-text w-50 mb-2">Minimalna kwota</span>
                    <input id="min-value" class="form-control mb-2" name="minValue" placeholder="minimalna kwota zamówienia" required>
                </div>
                <div class="input-group d-flex flex-column mb-2">
                    <span class="input-group-text">Dodatkowe informacje</span>
                    <textarea rows="6" name="description">Brak dodatkowych informacji</textarea>
                </div>

                <input name="restaurantId" type="hidden" value="${restaurantId}">
                <div id="create-order-btn" class="btn btn-outline-secondary text-white"> Utwórz </div>
            </form>
        </div>
        <div id="closing-date-alert-div" class="alert alert-danger mt-3">Data zamknięcia nie może być wcześniejsza od aktualnej!</div>
    </div>
</div>



<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>
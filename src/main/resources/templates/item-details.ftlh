<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <title>Szczegóły restauracji</title>
</head>
<body>
<div id="main-container" class="d-flex flex-row">
    <#include "side-navbar.ftlh">
    <div id="item-details-container" class="d-flex">
        <table class="table table-dark">
            <thead>
            <tr>
                <th>Nazwa</th>
                <th>Kategoria</th>
                <th>Cena</th>
                <th>Opis</th>
                <th>Restauracja</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>${item.name}</td>
                <td>${item.category}</td>
                <td>${item.price}</td>
                <td>${item.description}</td>
                <td>${item.restaurant.name}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
</body>
</html>
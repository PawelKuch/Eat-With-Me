$(document).ready(function() {

    let $submitFormBtn = $('#submit-form-button');
    let $restaurantForm = $('#restaurant-form');
    $submitFormBtn.on('click', function (){
        $restaurantForm.submit();
    })
});
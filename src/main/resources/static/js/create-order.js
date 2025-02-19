$(document).ready(function (){
    let createOrderForm = $('#create-order-form');
    let $createOrderBtn = $('#create-order-btn');
    let $alertDiv = $('#closing-date-alert-div');

    $alertDiv.hide();

    $createOrderBtn.on('click', function (){
        let closingDateValue = $('#closing-date').val();
       let currentDate = new Date();
       if(!closingDateValue){
           $alertDiv.text('Nie wprowadzono daty!').show();
           return 0;
       }
       let closingDate = new Date(closingDateValue);
       if (currentDate >= closingDate) {
           $alertDiv.text('Wprowadzona data zamknięcia nie może być wcześniejsza niż data aktualna!').show();
           return 0;
       }

       let minValue = $('#min-value').val().trim();
       if(!minValue){
           $alertDiv.text('Nie wprowadzono minimalnej wartości zamówienia!').show();
           return 0;
       }else if(minValue < 1.0) {
           $alertDiv.text('Wartość zamówienia musi być większa od zera!').show();
           return 0;
       }

       createOrderForm.submit();
    });
});
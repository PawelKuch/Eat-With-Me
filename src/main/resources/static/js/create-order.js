$(document).ready(function (){
    let createOrderForm = $('#create-order-form');
    let $createOrderBtn = $('#create-order-btn');
    let $alertDiv = $('#closing-date-alert-div');

    $alertDiv.hide();

    $createOrderBtn.on('click', function (){
       let closingDate = new Date($('#closing-date').val());
       let currentDate = new Date();
       if(currentDate >= closingDate){
           $alertDiv.show();
       }else {
           createOrderForm.submit();
       }
    });
});
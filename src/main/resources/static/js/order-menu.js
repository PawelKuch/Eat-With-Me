$(document).ready(function () {
   let $submitFormBtn = $("#submit-form-button");
   let $orderForm = $("#order-form");
   $submitFormBtn.on("click", function () {
       $orderForm.submit();
   });
});
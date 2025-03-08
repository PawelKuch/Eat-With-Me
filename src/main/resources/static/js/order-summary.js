$(document).ready(function (){
    let $updatePercentageDiscountBtn = $('#update-percentage-discount-btn');
    let $updatePercentageDiscountDiv = $('#update-percentage-discount-div');
    let $confirmNewPercentageDiscountBtn = $('#confirm-new-percentage-discount-btn');

    let discardPercentageDiscountChangesBtn = $('#discard-percentage-discount-changes-btn');

    $updatePercentageDiscountDiv.addClass('d-none');

    $updatePercentageDiscountBtn.on('click', function (){
        if($updatePercentageDiscountDiv.hasClass('d-none')){
            $updatePercentageDiscountDiv.removeClass('d-none');
            $(this).hide();
        } else {
            $updatePercentageDiscountDiv.addClass('d-none');
        }
    });

    discardPercentageDiscountChangesBtn.on('click', function (){
       $updatePercentageDiscountBtn.show();
       $updatePercentageDiscountDiv.addClass('d-none');
    });

    console.log("script works");

    $confirmNewPercentageDiscountBtn.on('click',function (){
        console.log("confirm button has been clicked")
        let orderId = $updatePercentageDiscountDiv.data('order-id');
        let newPercentageDiscountValue = $('#percentage-discount').val();
        $.ajax({
           url: '/orders/' + orderId + '/summary',
           type: 'PUT',
           contentType: 'application/json',
           data: JSON.stringify({newPercentageDiscountValue: newPercentageDiscountValue}),
           success: function (response) {
               location.reload();
           },
           error: function (response){
               console.log(response);
           }
        });
    });

    let $updateCashDiscountBtn = $('#update-cash-discount-btn');
    let $updateCashDiscountDiv = $('#update-cash-discount-div');
    let $confirmNewCashDiscountBtn = $('#confirm-new-cash-discount-btn');
    let $discardCashDiscountBtn = $('#discard-cash-discount-changes-btn');
    $updateCashDiscountDiv.addClass('d-none');

    $updateCashDiscountBtn.on('click', function (){
       $updateCashDiscountDiv.removeClass('d-none');
       $(this).hide();
    });

    $discardCashDiscountBtn.on('click', function (){
       $updateCashDiscountDiv.addClass('d-none');
       $updateCashDiscountBtn.show();
    });

    console.log("script's still working");
    $confirmNewCashDiscountBtn.on('click', function (){
        let orderId = $updateCashDiscountDiv.data('order-id');
        console.log("script's still working after cnfrim new discoun clicked");
        let newCashDiscount = $('#cash-discount').val();
        console.log("orderId: " + orderId);
        $.ajax({
           url: '/orders/' + orderId + '/summary',
           type: 'PUT',
           contentType: 'application/json',
           data: JSON.stringify({newCashDiscountValue: newCashDiscount}),
           success: function (response) {
               console.log(response);
               console.log("cash discount updated");
               location.reload();
           },
           error: function (){
               console.log('cash discount has not been updated!');
           }
        });
    });

    let $updateExtraPaymentBtn = $('#update-extra-payment-btn');
    let $deleteExtraPaymentBtn = $('#delete-extra-payment-btn');

    $deleteExtraPaymentBtn.on('click', function (){
       let extraPaymentId = $(this).data('extra-payment-id');
       let orderId = $(this).data('order-id');

       $.ajax({
          url: '/orders/' + orderId + '/summary',
          type: 'DELETE',
          contentType: 'application/json',
          data: JSON.stringify({extraPaymentId: extraPaymentId}),
          success: function (response){
              console.log(response);
              location.reload();
          },
          error: function (response){
              console.log(response);
          }
       });
    });


});
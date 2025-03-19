$(document).ready(function (){
    let $updatePercentageDiscountBtn = $('#update-percentage-discount-btn');
    let $updatePercentageDiscountDiv = $('#update-percentage-discount-div');
    let $confirmNewPercentageDiscountBtn = $('#confirm-new-percentage-discount-btn');

    let discardPercentageDiscountChangesBtn = $('#discard-percentage-discount-changes-btn');

    let $percentageDiscountInput = $('#percentage-discount');
    let percentageOrderDiscount = $percentageDiscountInput.data("order-discount");


    $percentageDiscountInput.attr('placeholder', percentageOrderDiscount).val(percentageOrderDiscount);


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

    $('.delete-extra-payment-btn').each(function (){
        $(this).on('click', function (){
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

    $('.update-extra-payment-btn').each(function (){
        let extraPaymentId = $(this).data('extra-payment-id');
        let $updateAndDeleteExtraPaymentBtnDiv = $('#update-and-delete-extra-payment-div-' + extraPaymentId);
        let $updateExtraPaymentForm = $('#update-extra-payment-form-' + extraPaymentId);
        let $extraPaymentDetailsDiv = $('#extra-payment-details-div-' + extraPaymentId);
        let $confirmAndDiscardNewExtraPaymentDiv = $('#confirm-and-discard-new-extra-payment-div-' + extraPaymentId);
        let defaultExtraPaymentProductValue = $('#new-extra-payment-product-' + extraPaymentId).val();
        let defaultExtraPaymentPrice = $('#new-extra-payment-price-' + extraPaymentId).val();
        let $discardNewExtraPaymentBtn = $('#discard-new-extra-payment-btn-' + extraPaymentId);
        let $confirmNewExtraPaymentBtn = $('#confirm-new-extra-payment-btn-' + extraPaymentId);
        console.log(defaultExtraPaymentProductValue);
        console.log(defaultExtraPaymentPrice);


        $(this).on('click', function (){
            console.log('button ' + extraPaymentId + ' clicked');

            if($updateAndDeleteExtraPaymentBtnDiv.hasClass('d-flex')) {
                $updateExtraPaymentForm.removeClass('d-none');
                $updateExtraPaymentForm.addClass('d-flex');
                $extraPaymentDetailsDiv.removeClass('d-flex');
                $extraPaymentDetailsDiv.addClass('d-none');
                $updateAndDeleteExtraPaymentBtnDiv.addClass('d-none');
                $updateAndDeleteExtraPaymentBtnDiv.removeClass('d-flex');
                $confirmAndDiscardNewExtraPaymentDiv.removeClass('d-none');
                $confirmAndDiscardNewExtraPaymentDiv.addClass('d-flex');
            }
        });

        $discardNewExtraPaymentBtn.on('click', function (){
           $updateExtraPaymentForm.removeClass('d-flex');
           $updateExtraPaymentForm.addClass('d-none');
            $extraPaymentDetailsDiv.removeClass('d-none');
            $extraPaymentDetailsDiv.addClass('d-flex');
            $updateAndDeleteExtraPaymentBtnDiv.removeClass('d-none');
            $updateAndDeleteExtraPaymentBtnDiv.addClass('d-flex');
            $confirmAndDiscardNewExtraPaymentDiv.toggleClass('d-none');
            $confirmAndDiscardNewExtraPaymentDiv.toggleClass('d-flex');
        });

        $confirmNewExtraPaymentBtn.on('click', function (){
            let newExtraPaymentProduct = $('#new-extra-payment-product-' + extraPaymentId).val();
            let newExtraPaymentPrice = $('#new-extra-payment-price-' + extraPaymentId).val();
            let orderId = $(this).data('order-id');

            $.ajax({
                url: '/orders/' + orderId + '/summary',
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify({extraPaymentId: extraPaymentId, newExtraPaymentProduct: newExtraPaymentProduct, newExtraPaymentPrice: newExtraPaymentPrice}),
                success: function (response) {
                    console.log("Udate extrapayment succed. Response: " + response);
                    location.reload();
                },
                error: function (response) {
                    console.log("Update extraPayment failed. Response: " + response);
                }
            });
        });
    });
});
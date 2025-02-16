$(document).ready(function () {
    $('.submit-form-btn').each(function () {
        let $submitBtn = $(this);
        let id = $submitBtn.data("itemId");
        let $form = $('.order-form-' + id);
        $submitBtn.on('click', function () {
            $form.submit();
        });
    });

    $('.delete-btn').each(function (){
        let $deleteBtn = $(this);
        let orderItemId = $deleteBtn.data('order-item-id');

        $deleteBtn.on('click', function (){
            $.ajax({
                url: '/orders/' + orderItemId + '/orderItems',
                type: 'DELETE',
                success: function (response, status) {
                    console.log(status);
                    location.reload();
                },
                error: function () {
                    console.log('nie usunięto');
                }
            }) ;
        });
    });
});

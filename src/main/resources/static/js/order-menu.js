$(document).ready(function () {
    $('.submit-form-btn').each(function () {
        let $submitBtn = $(this);
        let id = $submitBtn.data("itemId");
        let $form = $('.order-form-' + id);
        $submitBtn.on('click', function () {
            $form.submit();
        });
    });
});

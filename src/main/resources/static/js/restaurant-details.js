$(document).ready(function (){
   $('.change-btn').each(function (){
      let btnId = $(this).data('button-id');
      let $inputDiv = $('#inputDiv-' + btnId);
      let $valueDiv = $('#valueDiv-' + btnId);
      $(this).on('click', function (){
         $valueDiv.toggleClass('d-none');
         $inputDiv.toggleClass('d-flex');
         $inputDiv.toggleClass('d-none');
         $(this).addClass('d-none');
      });
   });

   $('.cancel-btn').each(function (){
      let btnId = $(this).data('button-id');
      let $changeBtn = $('#change-btn-' + btnId);
      let $textarea = $('#textarea-'+btnId);
      let $inputDiv = $('#inputDiv-' + btnId);
      let $valueDiv = $('#valueDiv-' + btnId);
      let defaultTextAreaValue = $textarea.val();

      $(this).on('click', function (){
         $textarea.val(defaultTextAreaValue);
         $valueDiv.toggleClass('d-none');
         $inputDiv.toggleClass('d-flex');
         $inputDiv.toggleClass('d-none');
         if($changeBtn.hasClass('d-none')){
             $changeBtn.removeClass('d-none');
         }
      });
   });

   $('.submit-btn').each(function (){
      let btnId = $(this).data('button-id');
      let $form = $('#form-' + btnId);
      $(this).on('click', function (){
         $form.submit();
      });
   });
});

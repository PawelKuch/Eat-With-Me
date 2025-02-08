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

   //menu panel
   $('.change-menu-item-btn').each(function (){
      let btnId = $(this).data('menu-item-id');
      let $updateMenuItemForm = $('.update-menu-item-form-' + btnId);
      let $discardBtn = $('.discard-button-' + btnId);
      let $menuItem = $('.menu-item-' + btnId);

      //input fields from menu item
      let $nameTextarea = $('.name-' + btnId);
      let $descriptionTextarea = $('.description-' + btnId);
      let $priceTextarea = $('.price-' + btnId);

      //default values from menu item fields
      let name = $nameTextarea.val();
      let description = $descriptionTextarea.val();
      let price = $priceTextarea.val();

      $(this).on('click', function (){
         $(".menu-item-" + btnId).each(function (){
            $(this).addClass('d-none');
         });
         $updateMenuItemForm.removeClass('d-none');
         $updateMenuItemForm.addClass('d-flex');

      });
      $discardBtn.on('click', function (){
         $updateMenuItemForm.addClass('d-none');
         $updateMenuItemForm.removeClass('d-flex');
         $menuItem.removeClass('d-none');
         $nameTextarea.val(name);
         $descriptionTextarea.val(description);
         $priceTextarea.val(price);
      });
   });
});

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
      let restaurantId = $(this).data('restaurant-id');
      let $form = $('#form-' + btnId);
      $(this).on('click', function (){

         let restaurant = {
            restaurantId: restaurantId,
            tags: $('.tags').val(),
            email: $('.email').val(),
            address: $('.address').val(),
            phone: $('.phone').val()
         };

         $.ajax({
            url: "/restaurants/" + restaurantId,
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(restaurant),
            success: function (response) {
               console.log(response);
               location.reload();
            },
            error: function(xhr, status) {
               console.log(status);
            }
         });

         //$form.submit();
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

   //CRUD menuItems
   let restaurantId = $('#display-menuItem-container').data('restaurant-id');
   $('.delete-btn').each(function (){
      let $deleteBtn = $(this);
      let menuItemId = $deleteBtn.data('menu-item-id');
      let $menuItemTile = $('.menu-item-tile-' + menuItemId);
      $deleteBtn.on('click', function (){
         $.ajax({
            url: '/restaurants/' + restaurantId + '/menuItems/' + menuItemId,
            type: 'DELETE',
            success: function (response) {
               console.log(response);
               $menuItemTile.remove();
            },
            error: function (xhr, status) {
               console.log(status);
            }
         });
      });
   });

   $('.confirm-update-btn').each(function (){
      let $confirmMenuItemUpdateBtn = $(this);
      let menuItemId = $confirmMenuItemUpdateBtn.data('menu-item-id');

      $confirmMenuItemUpdateBtn.on('click', function (){
         let menuItem = {
            menuItemId: menuItemId,
            name: $('.name-' + menuItemId).val(),
            description: $('.description-' + menuItemId).val(),
            price: $('.price-' + menuItemId).val()
         }

         $.ajax({
            url: '/restaurants/' + restaurantId + "/menuItems/" + menuItemId,
            type: "PUT",
            contentType: 'application/json',
            data: JSON.stringify(menuItem),
            success: function (response) {
               console.log("menuItem updated. Response: ", response);
               location.reload();
            },
            error: function (xhr, status){
              console.log('menuItem has not been updated. Status: ', status);
            }
         });
      });
   });

   // delete restaurant
   $('#delete-restaurant-btn').each(function (){
      let $deleteBtn = $(this);
      let restaurantId = $deleteBtn.data('restaurant-id');

      $deleteBtn.on('click', function (){
         $.ajax({
            url: '/restaurants/' + restaurantId,
            type: 'DELETE',
            success: function (response){
               console.log(response, ' restaurant has been deleted');
               window.location.href = '/restaurants';
            },
            error: function (response){
               console.log(response)
            }
         });
      });

   });
});

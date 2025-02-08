$(document).ready(function (){
   let $submitSignInBtn = $('#submit-sign-in-form-button');
   let $signInForm = $('#sign-in-form');

   $submitSignInBtn.on('click', function (){
      $signInForm.submit();
   });
});
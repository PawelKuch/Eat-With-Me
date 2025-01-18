$(document).ready(function() {
    $(".restaurant-tile").each(function (){
        let $restaurant = $(this);
        let restaurantId = $restaurant.data("restaurantId");
        let restaurantDetails = $(".restaurant-details-" + restaurantId);
        let restaurantImg = $(".img-sample-" + restaurantId);
        let restaurantAddressHeader = $("#restaurant-address-header-") + restaurantId;
        let detailsButton = $(".details-button-" + restaurantId)
        detailsButton.on("click", function (){
            if(restaurantDetails.hasClass("d-none")){
                restaurantDetails.removeClass("d-none");
                restaurantImg.addClass("d-none");
                restaurantAddressHeader.addClass("d-none")
            } else {
                restaurantDetails.addClass("d-none");
                restaurantImg.removeClass("d-none");
                restaurantAddressHeader.removeClass("d-none");
            }
        });
    });
});
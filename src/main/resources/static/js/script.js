$(document).ready(function (){

    $('.openbtn').on('click', function(){
        $('#mySidebar').css('width', '250px');
        $('#main').css('margin-left', '250px');
        $('.closebtn')
    });

    $('.openbtn2').on('click', function(){
        $('#mySidebar').css('width', '250px');
        $('#main').css('margin-left', '250px');
        $('.closebtn').toggle();
        $('.openbtn2').toggle();
        //$('.collapsed-menu-item').css('display', 'none');
        //$('.full-menu-item').css('display', 'block');
        $('.collapsed-menu-item').fadeOut(100, function(){
            $(this).css('display', 'none');
            $('.full-menu-item').fadeIn(100, function (){
                $(this).css('display', 'block');
            });
        });
    });

    $('.closebtn').on('click', function(){
        $('#mySidebar').css('width', '50px');
        $('#main').css('margin-left', '50px');
        $('.closebtn').toggle();
        $('.openbtn2').toggle();
        //$('.collapsed-menu-item').css('display', 'block');
        //$('.full-menu-item').css('display', 'none');
        $('.full-menu-item').fadeOut(100, function(){
            $(this).css('display', 'none');
            $('.collapsed-menu-item').fadeIn(100, function (){
                $(this).css('display', 'block');
            });
        });
    });
});
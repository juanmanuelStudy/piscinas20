$(document).ready(function() {
    // Iterar sobre cada imagen y cargarla usando AJAX
    $('.foto').each(function() {
        var nombreArchivo = $(this).attr('src').split('/').pop(); // Obtener el nombre del archivo de la URL
        $.get('/fotos/' + nombreArchivo, function(data) {
            // Asignar la imagen obtenida al elemento <img>
            $(this).attr('src', URL.createObjectURL(data));
        }.bind(this), 'blob');
    });

    // Inicializar el carrusel de fotos con Slick
    $('#fotoCarousel').slick({
        centerMode: true, // Centra las imágenes
        centerPadding: '50px', // Ajusta el espacio entre las imágenes
        dots: false,
        infinite: true,
        speed: 300,
        slidesToShow: 5,
        slidesToScroll: 1,
        prevArrow: '<button type="button" class="slick-prev">&#10094;</button>', // Personaliza el botón previo
        nextArrow: '<button type="button" class="slick-next">&#10095;</button>'  // Personaliza el botón siguiente
    });

    // Hacer que si pulso en una fotoMini se cargue la misma en fotoCarousel
    $('.fotos').click(function() {
        var src = $(this).attr('src');
        $('.foto').attr('src', src);
    });

    // Cargar la imagen en el carrusel, la primera por defecto, con margen máximo del 50%
    $('#fotoCarousel img').attr('src', $('#fotoMini img').first().attr('src'));
});

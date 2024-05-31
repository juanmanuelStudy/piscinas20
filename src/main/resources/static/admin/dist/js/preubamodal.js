
var itemsHelper = {
    calcularImporte: function(id, precio, cantidad){
        $("#total_importe_" + id).html(parseInt(precio) * parseInt(cantidad));
        this.calcularGranTotal();
    },
    hasProducto: function(id){
        var resultado = false;
        $('input[name="item_id[]"]').each(function(){
            if(parseInt(id) == parseInt($(this).val()) ){
                resultado = true;
            }
        });
        return resultado;
    },
    incrementaCantidad: function(id,precio){
        var cantidad = $("#cantidad_" + id).val() ? parseInt($("#cantidad_" + id).val()) : 0;
        $("#cantidad_" + id).val(++cantidad);
        this.calcularImporte(id,precio, cantidad);
    },
    eliminarLineaFactura: function(id){
        $("#row_" + id).remove();
        this.calcularGranTotal();
    },
    calcularGranTotal: function(){
        var total = 0;
        $('span[id^="total_importe_"]').each(function(){
            total += parseInt($(this).html());
        });
        $('#gran_total').html(total);
    }
}
$(document).ready(function() {
    // Función para cargar los productos en el modal
    var productosCargados = false; // Variable para controlar si los productos ya se han cargado

    function cargarProductos() {
        // Verificar si los productos ya se han cargado previamente
        if (!productosCargados) {
            $.ajax({
                url: "/facturas/cargar-productos",
                type: "GET",
                dataType: "json",
                success: function(data) {
                    // Limpiar el contenido anterior
                    $("#productosBody").empty();

                    // Iterar sobre los datos y agregarlos al modal
                    $.each(data, function(index, producto) {
                        // Crea una tabla con los productos que los detalles de nombre y precio utiliza DOM
                        var productoItem = "<tr>";
                        productoItem += "<td>" + producto.id + "</td>";
                        productoItem += "<td>" + producto.nombre + "</td>";
                        productoItem += "<td>" + producto.precio + "</td>";
                        productoItem += "<td>" + producto.cantidad + "</td>";
                        productoItem += "<td><button type='button' class='btn btn-primary agregar-producto' data-id='" + producto.id + "' data-nombre='" + producto.nombre + "' data-precio='" + producto.precio + "' data-descuento='" + producto.descuento + "' data-pedido-id='" + npedido + "'>Agregar</button></td>";
                        productoItem += "</tr>";
                        $("#productosBody").append(productoItem);
                    });


                    // Marcar que los productos han sido cargados
                    productosCargados = true;
                },
                error: function() {
                    alert("Error al cargar los productos.");
                }
            });
        }
    }


    // Función para agregar un producto a la tabla desde el modal
    function agregarProducto(id, nombre, precio, descuento, cantidad = 1) {
        if (itemsHelper.hasProducto(id)) {
            itemsHelper.incrementaCantidad(id, precio, descuento);
        } else {
            var linea = $("#plantillaItemsFactura").html();

            linea = linea.replace(/{ID}/g, id);
            linea = linea.replace(/{NOMBRE}/g, nombre);
            linea = linea.replace(/{PRECIO}/g, precio);
            linea = linea.replace(/{DESCUENTO}/g, descuento);
            linea = linea.replace(/{CANTIDAD}/g, cantidad);

            // Agregar la línea a la tabla
            $("#cargarItemProductos tbody").append(linea);

            // Calcular importe y actualizar totales
            itemsHelper.calcularImporte(id, precio, 1, descuento);
        }
    }

    // Delegar eventos de clic en el botón "Agregar Producto"
    $(document).on('click', '.agregar-producto', function() {
        var id = $(this).data('id');
        var nombre = $(this).data('nombre');
        var precio = $(this).data('precio');
        var descuento = $(this).data('descuento');
        agregarProducto(id, nombre, precio, descuento);



    });

   // Cargar los productos cuando se abre el modal
    $('#productoModal').on('shown.bs.modal', function() {
        cargarProductos();
    });

    // Manejo del envío del formulario para eliminar la plantilla de la tabla
    $("form").submit(function() {
        $("#plantillaItemsFactura").remove();
        return;
    });
});

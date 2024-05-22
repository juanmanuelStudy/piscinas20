
window.onload = function() {
	// Inicializar DataTables para la tabla de productos
	$('#tableProducto').DataTable({
		scrollY: '200px', // Ajusta la altura según tus necesidades
		scrollCollapse: true,
		paging: false
	});
	// Función para cargar los productos en el modal
	var productosCargados = false; // Variable para controlar si los productos ya se han cargado

	function cargarProductos() {
		// Verificar si los productos ya se han cargado previamente
		if (!productosCargados) {
			$.ajax({
				url: "/facturas/cargar-productos",
				type: "GET",
				dataType: "json",
				success: function (data) {
					// Limpiar el contenido anterior
					$("#productosBody").empty();

					// Iterar sobre los datos y agregarlos al modal
					$.each(data, function (index, producto) {
						console.log(producto);
						// Crea una tabla con los productos que los detalles de nombre y precio utiliza DOM
						var productoItem = "<tr>";
						productoItem += "<td>" + producto.id + "</td>";
						productoItem += "<td>" + producto.nombre + "</td>";
						productoItem += "<td>" + producto.precio + "</td>";
						productoItem += "<td>" + producto.cantidad + "</td>";
						productoItem += "<td><button type='button' class='btn btn-primary' onclick='agregarProducto(" + producto.id + ", \"" + producto.nombre + "\", " + producto.precio + ", " + producto.descuento + ")'>Agregar</button></td>";
						productoItem += "</tr>";
						$("#productosBody").append(productoItem);
					});

					// Inicializar DataTables para la tabla de productos
					$('#tableProducto').DataTable({
						scrollY: '200px', // Ajusta la altura según tus necesidades
						scrollCollapse: true,
						paging: false
					});

					// Marcar que los productos han sido cargados
					productosCargados = true;
				},
				error: function () {
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

		// Cerrar el modal después de agregar el producto
		$('#productoModal').modal('hide');
	}

	$(document).ready(function () {
		// Cargar los productos cuando se abre el modal
		$('#productoModal').on('shown.bs.modal', function () {
			cargarProductos();
		});

		// Manejo del envío del formulario para eliminar la plantilla de la tabla
		$("form").submit(function () {
			$("#plantillaItemsFactura").remove();
			return;
		});
	});


	var itemsHelper = {
		calcularImporte: function (id, precio, cantidad, descuento) {
			//hace los calculos y muestra en pantallla

			var des = parseFloat(precio.toFixed(2)) * parseInt(descuento) / 100;
			var pre = parseFloat(precio.toFixed(2)) - des;
			var total = pre.toFixed(2) * parseInt(cantidad);

			$("#total_importe_" + id).html(total.toFixed(2));
			this.calcularGranTotal();
			this.calcularBaseIva();
			this.calcularTotalIva();
			this.copiarValorTotal();//copia el valor del total a una variable total de la tabla factura
			this.copiarValorIva();//copia el valor del iva a una variable iva de la tabla factura

		},
		hasProducto: function (id) {

			var resultado = false;

			$('input[name="item_id[]"]').each(function () {
				if (parseInt(id) == parseInt($(this).val())) {
					resultado = true;
				}
			});

			return resultado;
		},
		incrementaCantidad: function (id, precio) {
			var cantidad = $("#cantidad_" + id).val() ? parseInt($("#cantidad_" + id).val()) : 0;
			$("#cantidad_" + id).val(++cantidad);
			this.calcularImporte(id, precio, cantidad, descuento);
		},
		eliminarLineaFactura: function (id) {
			$("#row_" + id).remove();
			this.calcularGranTotal();
			this.calcularBaseIva();
			this.calcularTotalIva();
		},
		calcularGranTotal: function () {
			var total = 0;

			$('span[id^="total_importe_"]').each(function () {
				total += parseFloat($(this).html());
			});

			$('#gran_total').html(total);
			$('#gran_total_').val(total);
			console.log("total"+$('#gran_total_').val(total));
		},
		calcularBaseIva: function () {
			var total = 0;
			//todo calcular el iva modificar el valor de la base iva
			var div = 0 / 100;
			$('span[id^="total_importe_"]').each(function () {
				total += parseFloat($(this).html());
			});

			$('#base_ival').html((total * parseFloat(div)).toFixed(2));
		},
		calcularTotalIva: function () {
			var total = 0;


			var valor = document.getElementById("base_ival").innerHTML;
			var anticipo = document.getElementById("anticipo").value;
			var valorAn = parseFloat(valor) - parseFloat(anticipo);


			if (anticipo != null) {
				$('span[id^="total_importe_"]').each(function () {
					total += parseFloat($(this).html());
				});

				$('#gran_total_iva').html((total + parseFloat(valorAn)).toFixed(2));
				$('#anticipo2').html((parseFloat(anticipo)).toFixed(2));

			} else {
				$('span[id^="total_importe_"]').each(function () {
					total += parseFloat($(this).html());
				});


				$('#gran_total_iva').html((total + parseFloat(valor)).toFixed(2));
				$('#anticipo2').html((parseFloat(anticipo)).toFixed(2));

			}
		}, copiarValorTotal: function () {

			var totalpasar = 0;
			$('span[id^="gran_total_iva"]').each(function () {
				totalpasar = parseFloat($(this).html());
			});
			$('#total_iva').val(parseFloat(totalpasar));


		},
		copiarValorIva: function () {

			var totalpasarIva = 0;
			$('span[id^="base_ival"]').each(function () {
				totalpasarIva = parseFloat($(this).html());
			});
			$('#ivaprecio').val(parseFloat(totalpasarIva));
		}


	}

}
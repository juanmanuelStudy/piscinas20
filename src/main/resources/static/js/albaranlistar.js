document.addEventListener('DOMContentLoaded', function() {
    // Verificar si hay valores guardados en el almacenamiento local
    var clienteSeleccionado = localStorage.getItem('clienteSeleccionado');
    var tipoSeleccionado = localStorage.getItem('tipoSeleccionado');

    if (clienteSeleccionado && tipoSeleccionado) {
        console.log(clienteSeleccionado);
        console.log(tipoSeleccionado);
        // Si hay valores guardados, establecerlos en los campos del formulario
        var clienteHidden = document.getElementById('clienteHidden');
        var tipoHidden = document.getElementById('tipoHidden');

        if (clienteHidden && tipoHidden) {
            clienteHidden.value = clienteSeleccionado;
            tipoHidden.value = tipoSeleccionado;
        }
    }
    console.log(clienteSeleccionado);
    console.log(tipoSeleccionado);

    // Al enviar el formulario, guardar los valores seleccionados
    var formBusquedaAlbaran = document.getElementById('formBusquedaAlbaran');

    if (formBusquedaAlbaran) {
        formBusquedaAlbaran.addEventListener('submit', function(e) {
            // Obtiene los valores seleccionados
            var cliente = document.getElementById('cliente');
            var tipo = document.getElementById('tipo');

            if (cliente && tipo) {
                var clienteSeleccionado = cliente.value;
                var tipoSeleccionado = tipo.value;

                // Guarda los valores en el almacenamiento local
                localStorage.setItem('clienteSeleccionado', clienteSeleccionado);
                localStorage.setItem('tipoSeleccionado', tipoSeleccionado);
            }
        });
    }
});

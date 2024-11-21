window.onload = function() {
    // Verificar si hay valores guardados en el almacenamiento local
    var clienteSeleccionado = localStorage.getItem('clienteSeleccionado');
    var estadoSeleccionado = localStorage.getItem('estadoSeleccionado');

    if (clienteSeleccionado && estadoSeleccionado) {
        console.log(clienteSeleccionado);
        console.log(estadoSeleccionado);
        // Si hay valores guardados, establecerlos en los campos del formulario
        document.getElementById('clienteHidden').value = clienteSeleccionado;
        document.getElementById('estadoHidden').value = estadoSeleccionado;
    }
    console.log(clienteSeleccionado);
    console.log(estadoSeleccionado);

    // Al enviar el formulario, guardar los valores seleccionados
    document.getElementById('formBusqueda').addEventListener('submit', function (e) {
        // Obtiene los valores seleccionados
        var clienteSeleccionado = document.getElementById('cliente').value;
        var estadoSeleccionado = document.getElementById('estado').value;

        // Guarda los valores en el almacenamiento local
        localStorage.setItem('clienteSeleccionado', clienteSeleccionado);
        localStorage.setItem('estadoSeleccionado', estadoSeleccionado);

    });


    // Aplicar clase de color a cada fila según su estado
    $('#table tbody tr').each(function () {
        const estado = $(this).find('td:eq(2)').text(); // Obtener el texto de la columna 'Estado'

        // Aplicar clase según el estado
        if (estado === 'PENDIENTE') {
            $(this).addClass('estado-Pendiente');
        } else if (estado === 'REALIZANDO') {
            $(this).addClass('estado-Realizando');
        } else if (estado === 'TERMINADO') {
            $(this).addClass('estado-Terminado');
        }
    });

}

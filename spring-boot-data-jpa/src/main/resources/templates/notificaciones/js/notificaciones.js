// ../notificaciones/js/notificaciones.js

$(document).ready(function(){
    $.get("/notifications/notifications", function(data){
        var notifications = data;
        if(notifications.length > 0) {
            var popupContent = "<ul>";
            notifications.forEach(function(notification){
                popupContent += "<li> " + notification.mensaje + "</li>";
            });
            popupContent += "</ul>";
            alert("Tienes nuevas notificaciones:\n\n" + popupContent);
        } else {
            alert("No tienes nuevas notificaciones.");
        }
    });
});

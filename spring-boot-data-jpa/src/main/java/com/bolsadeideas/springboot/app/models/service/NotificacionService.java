package com.bolsadeideas.springboot.app.models.service;

import com.bolsadeideas.springboot.app.models.dao.IProductoDao;
import com.bolsadeideas.springboot.app.models.dao.NotificacionRepository;
import com.bolsadeideas.springboot.app.models.entity.Notificacion;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private IProductoDao productoService;



    public Long getNotificationCount() {
        return notificacionRepository.count();
    }

    public void guardar(Notificacion notificacion) {
        notificacionRepository.save(notificacion);
    }

    public List<Notificacion> getNotifications() {
        return notificacionRepository.findAll();
    }

    public Long totalNotificaciones() {
        Long total = notificacionRepository.count();
        Notificacion notificacion = new Notificacion();
        //alamcenar en base de datos el total de notificaciones
        notificacion.setTotal(total);
        return total;
    }

    public void clearNotifications() {
        notificacionRepository.deleteAll();
    }

    public void addNotification(String notification) {
        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(notification);
        notificacion.setLeida(false);

        guardar(notificacion);
    }

    public void verificarStock() {
        List<Producto> productosBajoStock = productoService.findByCantidadLessThan(100.0);

        // Recorrer los productos con stock bajo y crear notificaciones si es necesario
        for (Producto producto : productosBajoStock) {
            Notificacion notificacion = new Notificacion();
            notificacion.setMensaje("El producto " + producto.getNombre() + " tiene un stock de " + producto.getCantidad() + " bajo por debajo de 100gr. Recuerda reponer.");
            notificacion.setMaterialAfectado(producto);
            notificacion.setLeida(false);
            guardar(notificacion);
        }
    }
}
// Compare this snippet from src/main/java/com/bolsadeideas/springboot/app/models/service/NotificacionService.java:
package com.bolsadeideas.springboot.app.models.service;

import com.bolsadeideas.springboot.app.models.dao.IProductoDao;
import com.bolsadeideas.springboot.app.models.dao.NotificacionRepository;
import com.bolsadeideas.springboot.app.models.entity.Notificacion;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NotificacionService  {

    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private IProductoDao productoService;
    private List<String> notifications = new ArrayList<>();


    public void guardar(Notificacion notificacion) {
        notificacionRepository.save(notificacion);


}
    public List<Notificacion> getNotifications() {
        return notificacionRepository.findAll();
    }

    public Long totalNotificaciones() {
        return  notificacionRepository.count();
    }

    public void clearNotifications() {
        notificacionRepository.deleteAll();
    }


    public void addNotification(String notification) {
        notifications.add(notification);
    }


    public void verificarStock() {
        List<Producto> productosBajoStock = productoService.findByCantidadLessThan(100.0);
        //obtner las notificaciones
        List <Notificacion> idnotificacion = getNotifications();
        //recorrer las notificaciones
        for (Notificacion notificacion : idnotificacion) {
            //si la notificacion no ha sido leida
            if (!notificacion.isLeida()) {
                //agregar la notificacion a la lista de notificaciones
                addNotification(notificacion.getMensaje());
            }
        }
        //recorre la lista de productos con stock bajo
        for (Producto producto : productosBajoStock) {
            //crea un mensaje de notificacion
            Notificacion notificacion = new Notificacion();
            notificacion.setMensaje("El producto " + producto.getNombre() +" tiene un stock de "+producto.getCantidad()+"bajo por debajo de 100gr. Recuerda reponer. ");
            notificacion.setMaterialAfectado(producto);
            notificacion.setLeida(false);
            guardar(notificacion);
        }
    }


}


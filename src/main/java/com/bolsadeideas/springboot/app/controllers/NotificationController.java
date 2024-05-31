package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.service.NotificacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificacionService notificacionService;

    private final Logger log = LoggerFactory.getLogger(getClass());



    @GetMapping("/count")
    public ResponseEntity<Long> getNotificationCount() {
        Long count = notificacionService.getNotificationCount();
        return ResponseEntity.ok(count);
    }



    @GetMapping("/notifications")
    public String notifications(Model model) {
        // Limpiar las notificaciones previas y verificar el stock
        notificacionService.clearNotifications();
        notificacionService.verificarStock();

        // Pasar las notificaciones al modelo
        model.addAttribute("notifications", notificacionService.getNotifications());

        return "notificaciones/notifications";
    }

    @GetMapping("/listarTotal")
    public String listarTotal(Model model) {
        log.info("Ejecutando listarTotal");

        // Obtener el total de notificaciones y pasarlo al modelo
        Long total = notificacionService.totalNotificaciones();
        model.addAttribute("total", total);

        log.info("Total de notificaciones: " + total);
        return "admin/shared/header";
    }

    @GetMapping("/clearNotifications")
    public String clearNotifications() {
        notificacionService.clearNotifications();
        return "redirect:/notifications/notifications";
    }

    @GetMapping("/addNotification")
    public String addNotification() {
        notificacionService.addNotification("New notification");
        return "redirect:/notifications/notifications";
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        log.info("Inicializando la aplicación y verificando total de notificaciones.");
        Long total = notificacionService.totalNotificaciones();
        log.info("Total de notificaciones al inicio: " + total);
    }
}

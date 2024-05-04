package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.dao.IProductoDao;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.service.NotificacionService;
import com.bolsadeideas.springboot.app.models.service.ProductoServiceImpl;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Controller

@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private IProductoDao productoService;

    private final Logger log = LoggerFactory.getLogger(getClass());
    @GetMapping("/notifications")
    public String notifications(Model model) {
        //resetear los mensajes
        //notificacionService.clearNotifications();
        //verificar stock
        notificacionService.verificarStock();
        model.addAttribute("notifications", notificacionService.getNotifications());

        return "notificaciones/notifications";
    }

    @GetMapping("/listatTotal")
    public String listar(Model model) {
      log.info("Ejecutando listatTotal");
        // Obtener el total de notificaciones y pasarlo al modelo
        Long totalNotificaciones = notificacionService.totalNotificaciones();
        model.addAttribute("total", totalNotificaciones);
        log.info("Total de notificaciones: " + totalNotificaciones);

        return "/admin/shared/header";
    }

    @GetMapping("/clearNotifications")
    public String clearNotifications() {
        notificacionService.clearNotifications();
        return "redirect:/notifications";
    }

    @GetMapping("/addNotification")
    public String addNotification() {
        notificacionService.addNotification("New notification");
        return "redirect:/notifications";
    }




}


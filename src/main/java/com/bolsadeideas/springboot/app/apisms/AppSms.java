package com.bolsadeideas.springboot.app.apisms;

import com.bolsadeideas.springboot.app.models.entity.Pedido;
import com.bolsadeideas.springboot.app.models.service.PedidoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class AppSms {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String url = "https://api.infobip.com/sms/2/text/advanced";
    private final String authorizationToken = "App efc3679e93e05d97f6b5c9e092314160-c41b87f9-028f-4b4b-af79-254186d92089";

    private PedidoService pedidoService;

    // Method to send the SMS message
    // Método en AppSms para enviar el mensaje y devolver la respuesta
    public ResponseEntity<String> sendMessage(String toPhoneNumber, String messageText) {
        try {
            String fromPhoneNumber = "34957870425";

            // Crear el contenido del mensaje
            Map<String, Object> messageContent = new HashMap<>();
            messageContent.put("to", toPhoneNumber);
            messageContent.put("from", fromPhoneNumber);
            messageContent.put("text", messageText);

            // Estructura del mensaje
            Map<String, Object> message = new HashMap<>();
            message.put("destinations", new Map[]{messageContent});
            message.put("from", fromPhoneNumber);
            message.put("text", messageText);

            // Cuerpo final de la solicitud
            Map<String, Object> finalBody = new HashMap<>();
            finalBody.put("messages", new Map[]{message});

            // Convertir el cuerpo a JSON
            String jsonBody = new ObjectMapper().writeValueAsString(finalBody);

            // Configurar los encabezados
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", authorizationToken);

            // Preparar la entidad HTTP con los encabezados y el cuerpo JSON
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            // Realizar la solicitud POST
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            return response; // Retorna la respuesta para obtener el estado
        } catch (JsonProcessingException e) {
            System.out.println("Error al convertir el mapa a JSON: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocurrió un error en la solicitud: " + e.getMessage());
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el envío"); // En caso de error
    }

}

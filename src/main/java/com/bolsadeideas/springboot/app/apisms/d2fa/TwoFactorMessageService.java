package com.bolsadeideas.springboot.app.apisms.d2fa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class TwoFactorMessageService {

    private final Random random = new Random();
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Genera un código de verificación de 6 dígitos.
     */
    public String generateVerificationCode() {
        int code = 100000 + random.nextInt(900000);  // Código de 6 dígitos
        return String.valueOf(code);
    }

    /**
     * Envía el código de verificación al correo del usuario.
     *
     * @param email El correo electrónico del usuario.
     * @param code  El código de verificación.
     * @throws MessagingException Si ocurre un error al enviar el correo.
     */
    public void sendVerificationCode(String email, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        //  helper.setFrom("tecnicohome@gmx.es");
        helper.setTo(email);
        helper.setSubject("Código de Verificación de Dos Factores");
        helper.setText("<p>Tu código de verificación es: <strong>" + code + "</strong></p>", true);

        mailSender.send(message);
    }
}


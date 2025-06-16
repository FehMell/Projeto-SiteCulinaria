package com.example.siteculinaria.service;

import org.springframework.core.io.FileSystemResource;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmailContato(String nome, String email, String mensagem) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom("mell39654@gmail.com");
        helper.setTo("mell39654@gmail.com");
        helper.setReplyTo(email);
        helper.setSubject("Nova mensagem de contato do site");

        String corpoHtml = "<div style='font-family: Arial, sans-serif; font-size: 16px; color: #333;'>"
                + "<div style='text-align: center; margin-bottom: 20px;'>"
                + "<img src='cid:logoEmail' alt='Logo Email' style='width: 120px; height: auto;' />"
                + "</div>"
                + "<h2 style='color: #b45309; font-weight: bold;'>Mensagem do formulário de contato</h2>"
                + "<p><strong style='color: #b45309;'>Nome:</strong> " + nome + "</p>"
                + "<p><strong style='color: #b45309;'>Email:</strong> " + email + "</p>"
                + "<p><strong style='color: #b45309;'>Mensagem:</strong></p>"
                + "<p style='font-size: 18px;'>" + mensagem + "</p>"
                + "</div>";

        helper.setText(corpoHtml, true);

        FileSystemResource imagem = new FileSystemResource("uploads/logoemail.png");
        helper.addInline("logoEmail", imagem);

        mailSender.send(mimeMessage);
    }
}

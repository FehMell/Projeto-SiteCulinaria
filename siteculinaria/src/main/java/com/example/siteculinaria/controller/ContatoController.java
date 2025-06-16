package com.example.siteculinaria.controller;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.siteculinaria.service.EmailService;

@Controller
public class ContatoController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/contato")
    public String mostrarFormulario() {
        return "contato";
    }

    @PostMapping("/contato")
    public String enviarMensagem(@RequestParam String nome,
            @RequestParam String email,
            @RequestParam String mensagem,
            Model model) {
        try {
            emailService.enviarEmailContato(nome, email, mensagem);
            model.addAttribute("sucesso", "Mensagem enviada com sucesso!");
        } catch (MessagingException e) {
            e.printStackTrace();
            model.addAttribute("erro", "Falha ao enviar a mensagem. Tente novamente.");
        }

        return "contato";
    }
}

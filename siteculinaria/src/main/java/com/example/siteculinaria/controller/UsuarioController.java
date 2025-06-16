package com.example.siteculinaria.controller;

import com.example.siteculinaria.models.Usuario;
import com.example.siteculinaria.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios";
    }

    @GetMapping("/nova")
    public String formularioNovoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "nova_usuario";
    }

    @PostMapping
    public String salvarUsuario(@ModelAttribute Usuario usuario,
            @RequestParam("imagem") MultipartFile imagem) {
        try {
            if (imagem != null && !imagem.isEmpty()) {
                String nomeArquivo = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();
                Path caminhoArquivo = Paths.get("uploads").resolve(nomeArquivo);
                Files.createDirectories(caminhoArquivo.getParent());
                Files.copy(imagem.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);
                usuario.setNomeImagem(nomeArquivo);
            } else if (usuario.getId() != null) {

                Usuario usuarioExistente = usuarioRepository.findById(usuario.getId()).orElse(null);
                if (usuarioExistente != null) {
                    usuario.setNomeImagem(usuarioExistente.getNomeImagem());
                }
            }
            usuarioRepository.save(usuario);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido: " + id));
        model.addAttribute("usuario", usuario);
        return "nova_usuario";
    }

    @GetMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/usuarios";
    }

    @GetMapping("/api/total-chefs")
    @ResponseBody
    public Long getTotalChefs() {
        return usuarioRepository.count();
    }

}
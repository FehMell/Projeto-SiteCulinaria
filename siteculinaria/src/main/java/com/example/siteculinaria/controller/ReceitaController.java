package com.example.siteculinaria.controller;

import com.example.siteculinaria.models.Receita;
import com.example.siteculinaria.repository.ReceitaRepository;
import com.example.siteculinaria.repository.CategoriaRepository;

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
@RequestMapping("/receitas")
public class ReceitaController {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public String listarReceitas(Model model) {
        model.addAttribute("receitas", receitaRepository.findAll());
        return "receitas";
    }

    @GetMapping("/nova")
    public String formularioNovaReceita(Model model) {
        model.addAttribute("receita", new Receita());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "nova_receita";
    }

    @PostMapping
    public String salvarReceita(@ModelAttribute Receita receita,
            @RequestParam("imagem") MultipartFile imagem) {

        try {
            if (imagem != null && !imagem.isEmpty()) {
                String nomeArquivo = System.currentTimeMillis() + "_" + imagem.getOriginalFilename();
                Path caminhoArquivo = Paths.get("uploads").resolve(nomeArquivo);
                Files.copy(imagem.getInputStream(), caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);
                receita.setNomeImagem(nomeArquivo);
            } else if (receita.getId() != null) {
                // Mantém a imagem antiga ao editar e não enviar nova
                Receita receitaExistente = receitaRepository.findById(receita.getId()).orElse(null);
                if (receitaExistente != null) {
                    receita.setNomeImagem(receitaExistente.getNomeImagem());
                }
            }
            receitaRepository.save(receita);

        } catch (IOException e) {
            e.printStackTrace();
            // trate o erro como preferir
        }
        return "redirect:/receitas";
    }

    @GetMapping("/editar/{id}")
    public String editarReceita(@PathVariable Long id, Model model) {
        Receita receita = receitaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Receita inválida: " + id));
        model.addAttribute("receita", receita);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "nova_receita";
    }

    @GetMapping("/deletar/{id}")
    public String deletarReceita(@PathVariable Long id) {
        receitaRepository.deleteById(id);
        return "redirect:/receitas";
    }

    @GetMapping("/api/total-receitas")
    @ResponseBody
    public Long getTotalReceitas() {
        return receitaRepository.count();
    }
}
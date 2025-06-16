package com.example.siteculinaria.config;

import com.example.siteculinaria.models.Categoria;
import com.example.siteculinaria.repository.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;

    public DataLoader(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoriaRepository.count() == 0) {  
            Categoria doces = new Categoria();
            doces.setNome("Doces");
            categoriaRepository.save(doces);

            Categoria salgados = new Categoria();
            salgados.setNome("Salgados");
            categoriaRepository.save(salgados);

            Categoria bebidas = new Categoria();
            bebidas.setNome("Bebidas");
            categoriaRepository.save(bebidas);

            System.out.println("Categorias iniciais criadas.");
        }
    }
}

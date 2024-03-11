package com.algaworks.algamoney.api.service;

import com.algaworks.algamoney.api.model.Categoria;
import com.algaworks.algamoney.api.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    private CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listar(){
        return categoriaRepository.findAll();
    }

    public Categoria criar(Categoria categoria){
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return categoriaSalva;
    }

    public Categoria buscarPeloCodigo(Long codigo){
        return categoriaRepository.findById(codigo).orElse(null);
    }
}

package com.algaworks.algamoney.api.service;

import com.algaworks.algamoney.api.event.RecursoCriadoEvent;
import com.algaworks.algamoney.api.model.Categoria;
import com.algaworks.algamoney.api.repository.CategoriaRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class CategoriaService {
    private CategoriaRepository categoriaRepository;

    private ApplicationEventPublisher publisher;

    public CategoriaService(CategoriaRepository categoriaRepository, ApplicationEventPublisher publisher) {
        this.categoriaRepository = categoriaRepository;
        this.publisher = publisher;
    }

    public ResponseEntity<Categoria> buscarPeloCodigo(Long codigo) {
        Categoria categoria = categoriaRepository.findById(codigo).orElse(null);
        if (categoria == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(categoria);
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public ResponseEntity<Categoria> criar(Categoria categoria, HttpServletResponse response) {
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, categoria.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
    }
}

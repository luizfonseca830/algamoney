package com.algaworks.algamoney.api.service;

import com.algaworks.algamoney.api.event.RecursoCriadoEvent;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class PessoaService {

    private PessoaRepository pessoaRepository;
    private ApplicationEventPublisher publisher;

    public PessoaService(PessoaRepository pessoaRepository, ApplicationEventPublisher publisher) {
        this.pessoaRepository = pessoaRepository;
        this.publisher = publisher;
    }

    public List<Pessoa> listar() {
        return pessoaRepository.findAll();
    }

    public ResponseEntity<Pessoa> buscarPelocodigo(Long codigo) {
        Pessoa pessoa = pessoaRepository.findById(codigo).orElse(null);
        if (pessoa == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(pessoa);
    }

    public ResponseEntity<Pessoa> criar(Pessoa pessoa, HttpServletResponse response) {
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoa.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    public void remover(Long codigo){
        pessoaRepository.deleteById(codigo);
    }
}

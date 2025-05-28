package com.example.microservice.service;

import com.example.microservice.dto.ClienteDTO;
import com.example.microservice.model.Cliente;
import com.example.microservice.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    @Transactional
    public Cliente createFromDTO(ClienteDTO dto) {
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con este email");
        }
        if (dto.getNumeroDocumento() != null && clienteRepository.existsByNumeroDocumento(dto.getNumeroDocumento())) {
            throw new RuntimeException("Ya existe un cliente con este número de documento");
        }
        
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setNumeroDocumento(dto.getNumeroDocumento());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente update(Long id, Cliente cliente) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        
        Cliente clienteExistente = clienteRepository.findById(id).get();
        if (!clienteExistente.getEmail().equals(cliente.getEmail()) && 
            clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con este email");
        }
        
        if (cliente.getNumeroDocumento() != null && 
            !cliente.getNumeroDocumento().equals(clienteExistente.getNumeroDocumento()) && 
            clienteRepository.existsByNumeroDocumento(cliente.getNumeroDocumento())) {
            throw new RuntimeException("Ya existe un cliente con este número de documento");
        }
        
        cliente.setId(id);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> findByNumeroDocumento(String numeroDocumento) {
        return clienteRepository.findByNumeroDocumento(numeroDocumento);
    }
} 
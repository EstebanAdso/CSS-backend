package com.example.compuservicessoft.services;

import com.example.compuservicessoft.entities.Categoria;
import com.example.compuservicessoft.respositories.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaServices {

    private final CategoriaRepository categoriaRepository;

    // Método para listar categorías excluyendo las no deseadas
    public List<Categoria> findAllExcludingCategories(List<Long> excludedCategoryIds) {
        return categoriaRepository.findByIdNotIn(excludedCategoryIds);
    }

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }

    // Búsqueda de categorías por nombre parcial (ej: "disco" retorna "Disco SSD", "Disco NVMe")
    public List<Categoria> searchByName(String name, List<Long> excludedIds) {
        return categoriaRepository.searchByName(excludedIds, name.trim());
    }
}
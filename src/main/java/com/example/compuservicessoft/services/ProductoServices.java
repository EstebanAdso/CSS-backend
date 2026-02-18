package com.example.compuservicessoft.services;

import com.example.compuservicessoft.entities.Producto;
import com.example.compuservicessoft.respositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServices {

    private final ProductoRepository productoRepository;

    // Todos los productos paginados, excluyendo categorías ocultas
    public Page<Producto> findAll(List<Long> excludedIds, Pageable pageable) {
        return productoRepository.findAllExcluding(excludedIds, pageable);
    }

    // Productos de una categoría específica paginados, excluyendo categorías ocultas
    public Page<Producto> findByCategoryId(Long categoryId, List<Long> excludedIds, Pageable pageable) {
        return productoRepository.findByCategoryIdExcluding(categoryId, excludedIds, pageable);
    }

    // Producto por ID
    public Optional<Producto> findById(long id) {
        return productoRepository.findById(id);
    }

    // Búsqueda inteligente: divide el término en palabras y busca cada una en el nombre
    public Page<Producto> searchByName(String term, List<Long> excludedIds, Pageable pageable) {
        String[] words = term.trim().toLowerCase().split("\\s+");
        String w1 = words.length > 0 ? words[0] : null;
        String w2 = words.length > 1 ? words[1] : null;
        String w3 = words.length > 2 ? words[2] : null;
        String w4 = words.length > 3 ? words[3] : null;
        return productoRepository.searchByKeywords(excludedIds, w1, w2, w3, w4, pageable);
    }
}
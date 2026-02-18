package com.example.compuservicessoft.controller;

import com.example.compuservicessoft.config.CategoriasConfig;
import com.example.compuservicessoft.entities.Producto;
import com.example.compuservicessoft.services.ProductoServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoServices productoServices;

    // 1. Todos los productos paginados (excluye categorías ocultas)
    // GET /api/products?page=0&size=12&sort=id,asc
    @GetMapping
    public ResponseEntity<Page<Producto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        Sort.Direction direction = (sort.length > 1 && sort[1].equalsIgnoreCase("desc")) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        log.info("Listando todos los productos - Página: {}", page);
        return ResponseEntity.ok(productoServices.findAll(CategoriasConfig.EXCLUDED_CATEGORY_IDS, pageable));
    }

    // 2. Producto por ID
    // GET /api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        log.info("Buscando producto con ID: {}", id);
        return productoServices.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Búsqueda inteligente por nombre (palabras en cualquier orden)
    // GET /api/products/search?q=256+patriot&page=0&size=12
    @GetMapping("/search")
    public ResponseEntity<Page<Producto>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        if (q == null || q.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Pageable pageable = PageRequest.of(page, size);
        log.info("Búsqueda de productos por nombre: '{}'", q);
        return ResponseEntity.ok(productoServices.searchByName(q, CategoriasConfig.EXCLUDED_CATEGORY_IDS, pageable));
    }
}
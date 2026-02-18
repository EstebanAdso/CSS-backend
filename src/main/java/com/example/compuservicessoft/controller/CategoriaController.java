package com.example.compuservicessoft.controller;

import com.example.compuservicessoft.entities.Categoria;
import com.example.compuservicessoft.entities.Producto;
import com.example.compuservicessoft.services.CategoriaServices;
import com.example.compuservicessoft.services.ProductoServices;
import com.example.compuservicessoft.config.CategoriasConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaServices categoriaServices;
    private final ProductoServices productoServices;

    // 5. Listado de todas las categorías (excluye las ocultas)
    // GET /api/categories
    @GetMapping
    public ResponseEntity<List<Categoria>> getAll() {
        return ResponseEntity.ok(categoriaServices.findAllExcludingCategories(CategoriasConfig.EXCLUDED_CATEGORY_IDS));
    }

    // 6. Categoría por ID
    // GET /api/categories/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getById(@PathVariable Long id) {
        return categoriaServices.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Búsqueda de categorías por nombre (ej: "disco" retorna "Disco SSD", "Disco NVMe")
    // GET /api/categories/search?q=disco
    @GetMapping("/search")
    public ResponseEntity<List<Categoria>> search(@RequestParam String q) {
        if (q == null || q.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(categoriaServices.searchByName(q, CategoriasConfig.EXCLUDED_CATEGORY_IDS));
    }

    // 2. Productos de una categoría específica paginados
    // GET /api/categories/{id}/products?page=0&size=12
    @GetMapping("/{id}/products")
    public ResponseEntity<Page<Producto>> getProductsByCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productoServices.findByCategoryId(id, CategoriasConfig.EXCLUDED_CATEGORY_IDS, pageable));
    }
}
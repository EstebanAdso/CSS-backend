package com.example.compuservicessoft.respositories;

import com.example.compuservicessoft.entities.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Todos los productos excluyendo categorías ocultas — JOIN FETCH evita el problema N+1
    @Query("SELECT p FROM Producto p JOIN FETCH p.categoria c WHERE c.id NOT IN :excludedIds")
    Page<Producto> findAllExcluding(@Param("excludedIds") List<Long> excludedIds, Pageable pageable);

    // Productos de una categoría específica — JOIN FETCH trae la categoría en la misma query
    @Query("SELECT p FROM Producto p JOIN FETCH p.categoria c WHERE c.id = :categoryId AND c.id NOT IN :excludedIds")
    Page<Producto> findByCategoryIdExcluding(
            @Param("categoryId") Long categoryId,
            @Param("excludedIds") List<Long> excludedIds,
            Pageable pageable
    );

    // Búsqueda inteligente: cada palabra debe aparecer en el nombre (JOIN FETCH incluido)
    @Query("SELECT p FROM Producto p JOIN FETCH p.categoria c WHERE c.id NOT IN :excludedIds " +
           "AND (:w1 IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :w1, '%'))) " +
           "AND (:w2 IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :w2, '%'))) " +
           "AND (:w3 IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :w3, '%'))) " +
           "AND (:w4 IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :w4, '%')))")
    Page<Producto> searchByKeywords(
            @Param("excludedIds") List<Long> excludedIds,
            @Param("w1") String w1,
            @Param("w2") String w2,
            @Param("w3") String w3,
            @Param("w4") String w4,
            Pageable pageable
    );
}
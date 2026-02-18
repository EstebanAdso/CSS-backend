package com.example.compuservicessoft.config;

import java.util.Arrays;
import java.util.List;

// Fuente única de verdad para las categorías ocultas en el frontend
public class CategoriasConfig {
    public static final List<Long> EXCLUDED_CATEGORY_IDS =
            Arrays.asList(7L, 9L, 13L, 24L, 25L, 26L, 29L, 30L, 32L, 33L);

    private CategoriasConfig() {}
}

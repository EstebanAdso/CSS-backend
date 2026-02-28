# CompuServicesSoft — API REST Backend

Backend de la aplicación **CompuServicesSoft**, desarrollado con **Spring Boot 3.3.3** y **Java 17**. Expone una API REST para gestionar productos y categorías de una tienda de tecnología.

---

## Stack tecnológico

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 3.3.3 |
| Spring Data JPA | (incluido en Boot) |
| MySQL | 8+ |
| Lombok | (incluido en Boot) |
| Maven | Wrapper incluido |

---

## Configuración del entorno

La aplicación usa variables de entorno para la conexión a la base de datos. Crea un archivo `.env` o configúralas en tu sistema antes de ejecutar:

```env
DB_HOST
DB_PORT
DB_NAME
DB_USER
DB_PASSWORD
PORT
API_KEY
```

El servidor corre por defecto en el puerto `8084`.

---

## Ejecutar el proyecto

```bash
./mvnw spring-boot:run
```

---

## Estructura del proyecto

```
src/main/java/com/example/compuservicessoft/
│
├── controller/
│   ├── CategoriaController.java   # Endpoints de categorías y productos por categoría
│   └── ProductoController.java    # Endpoints de productos (listado, búsqueda, detalle)
│
├── services/
│   ├── CategoriaServices.java     # Lógica de negocio para categorías
│   └── ProductoServices.java      # Lógica de negocio para productos
│
├── respositories/
│   ├── CategoriaRepository.java   # Queries JPA para categorías
│   └── ProductoRepository.java    # Queries JPA para productos (incluye búsqueda inteligente)
│
└── entities/
    ├── Categoria.java             # Entidad: id, nombre, descripcion, descripcionGarantia
    └── Producto.java              # Entidad: id, nombre, cantidad, precioVendido,
                                   #          imagen, descripcion, categoria (FK)
```

### Relación entre entidades

```
Categoria (1) ──────< Producto (N)
  - id                  - id
  - nombre              - nombre
  - descripcion         - cantidad
  - descripcionGarantia - precioVendido
                        - imagen
                        - descripcion
                        - categoria_id (FK)
```

---

## Categorías excluidas del frontend

Las siguientes categorías están ocultas en todos los endpoints públicos (no aparecen en listados ni en productos):

> IDs excluidos: `7, 9, 13, 24, 25, 26, 29, 30, 32, 33`

Esta lista se define una sola vez en `CategoriaController.EXCLUDED_CATEGORY_IDS` y es reutilizada por `ProductoController`.

---

## API Reference

### Base URL

```
http://localhost:8084
```

---

### Productos — `/api/products`

---

#### 1. Obtener todos los productos paginados

```
GET /api/products
```

**Query params:**

| Parámetro | Tipo | Default | Descripción |
|---|---|---|---|
| `page` | int | `0` | Número de página (base 0) |
| `size` | int | `12` | Cantidad de items por página |
| `sort` | string[] | `id,asc` | Campo y dirección de orden |

**Ejemplo:**
```
GET /api/products?page=0&size=12&sort=nombre,asc
```

**Respuesta `200 OK`:**
```json
{
  "content": [
    {
      "id": 1,
      "nombre": "Patriot SSD 256GB",
      "cantidad": 10,
      "precioVendido": 45.99,
      "total": 459.90,
      "imagen": "ssd-patriot.jpg",
      "descripcion": "SSD de alto rendimiento...",
      "categoria": {
        "id": 3,
        "nombre": "Almacenamiento",
        "descripcion": "...",
        "descripcionGarantia": "..."
      }
    }
  ],
  "totalElements": 120,
  "totalPages": 10,
  "size": 12,
  "number": 0
}
```

---

#### 2. Obtener producto por ID

```
GET /api/products/{id}
```

**Path params:**

| Parámetro | Tipo | Descripción |
|---|---|---|
| `id` | Long | ID del producto |

**Ejemplo:**
```
GET /api/products/1
```

**Respuesta `200 OK`:**
```json
{
  "id": 1,
  "nombre": "Patriot SSD 256GB",
  "cantidad": 10,
  "precioVendido": 45.99,
  "total": 459.90,
  "imagen": "ssd-patriot.jpg",
  "descripcion": "SSD de alto rendimiento...",
  "categoria": {
    "id": 3,
    "nombre": "Almacenamiento"
  }
}
```

**Respuesta `404 Not Found`** si el ID no existe.

---

#### 3. Búsqueda inteligente de productos por nombre

```
GET /api/products/search
```

La búsqueda es **inteligente**: divide el término en palabras individuales y verifica que **cada palabra** aparezca en el nombre del producto, sin importar el orden. Por ejemplo, buscar `"256 patriot"` encuentra `"Patriot SSD 256GB"`.

**Query params:**

| Parámetro | Tipo | Default | Descripción |
|---|---|---|---|
| `q` | string | requerido | Término de búsqueda (hasta 4 palabras) |
| `page` | int | `0` | Número de página |
| `size` | int | `12` | Items por página |

**Ejemplos:**
```
GET /api/products/search?q=patriot
GET /api/products/search?q=256+patriot
GET /api/products/search?q=ssd+256+patriot&page=0&size=6
```

**Respuesta `200 OK`:** misma estructura paginada que el endpoint de listado.

**Respuesta `400 Bad Request`** si `q` está vacío.

---

### Categorías — `/api/categories`

---

#### 4. Buscar categorías por nombre

```
GET /api/categories/search
```

Retorna las categorías cuyo nombre coincida con el término buscado. Ideal para móvil — el usuario escribe "disco" y obtiene "Disco SSD", "Disco NVMe", etc. La búsqueda es **insensible a mayúsculas**.

**Query params:**

| Parámetro | Tipo | Default | Descripción |
|---|---|---|---|
| `q` | string | requerido | Nombre parcial de la categoría |

**Ejemplos:**
```
GET /api/categories/search?q=disco
GET /api/categories/search?q=procesador
GET /api/categories/search?q=monitor
```

**Respuesta `200 OK`:**
```json
[
  { "id": 5, "nombre": "Disco SSD", "descripcion": "...", "descripcionGarantia": "..." },
  { "id": 6, "nombre": "Disco NVMe", "descripcion": "...", "descripcionGarantia": "..." }
]
```

**Respuesta `400 Bad Request`** si `q` está vacío.

---

#### 5. Obtener todas las categorías

```
GET /api/categories
```

Retorna el listado de categorías visibles (excluye las categorías ocultas configuradas).

**Ejemplo:**
```
GET /api/categories
```

**Respuesta `200 OK`:**
```json
[
  {
    "id": 1,
    "nombre": "Laptops",
    "descripcion": "Computadoras portátiles...",
    "descripcionGarantia": "Garantía de 1 año..."
  },
  {
    "id": 2,
    "nombre": "Monitores",
    "descripcion": "Monitores Full HD y 4K...",
    "descripcionGarantia": "Garantía de 6 meses..."
  }
]
```

---

#### 5. Obtener categoría por ID

```
GET /api/categories/{id}
```

**Path params:**

| Parámetro | Tipo | Descripción |
|---|---|---|
| `id` | Long | ID de la categoría |

**Ejemplo:**
```
GET /api/categories/1
```

**Respuesta `200 OK`:**
```json
{
  "id": 1,
  "nombre": "Laptops",
  "descripcion": "Computadoras portátiles...",
  "descripcionGarantia": "Garantía de 1 año..."
}
```

**Respuesta `404 Not Found`** si el ID no existe.

---

#### 6. Obtener productos de una categoría específica (paginado)

```
GET /api/categories/{id}/products
```

**Path params:**

| Parámetro | Tipo | Descripción |
|---|---|---|
| `id` | Long | ID de la categoría |

**Query params:**

| Parámetro | Tipo | Default | Descripción |
|---|---|---|---|
| `page` | int | `0` | Número de página |
| `size` | int | `12` | Items por página |

**Ejemplo:**
```
GET /api/categories/3/products?page=0&size=8
```

**Respuesta `200 OK`:** misma estructura paginada que el endpoint de listado de productos.

---

## Estructura de respuesta paginada

Todos los endpoints que retornan listas paginadas siguen esta estructura estándar de Spring Data:

```json
{
  "content": [ ... ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 12,
    "sort": { "sorted": true, "unsorted": false }
  },
  "totalElements": 120,
  "totalPages": 10,
  "last": false,
  "first": true,
  "numberOfElements": 12,
  "empty": false
}
```

---

## Notas de desarrollo

- **Sin duplicación**: la lista de categorías excluidas se define una sola vez en `CategoriaController.EXCLUDED_CATEGORY_IDS`.
- **Búsqueda por palabras**: soporta hasta 4 palabras clave. Si el término tiene más, solo se usan las primeras 4.
- **Ordenamiento flexible**: el endpoint `GET /api/products` acepta cualquier campo de la entidad `Producto` como criterio de orden vía `sort=campo,direccion`.

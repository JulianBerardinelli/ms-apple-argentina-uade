# Endpoints, seguridad y pruebas

## Reglas de seguridad

Configuradas en `SecurityConfig.java`.

| Ruta | Acceso |
|---|---|
| `/api/auth/**` | Publico |
| `/actuator/health` | Publico |
| `/swagger-ui/**`, `/swagger-ui.html`, `/v3/api-docs/**` | Publico |
| `/api/usuarios/**` | Solo admin, `hasRole("ADMIN")` |
| `/api/productos/**`, `/api/carritos/**`, `/api/items-carrito/**` | Usuario autenticado |
| Cualquier otra ruta | Usuario autenticado |

Nota importante: aunque la tabla explicita productos, carritos e items, la regla final `.anyRequest().authenticated()` tambien protege categorias, fotos, ordenes y detalles.

## Auth

| Metodo | Endpoint | Descripcion |
|---|---|---|
| POST | `/api/auth/register` | Registra usuario y devuelve JWT |
| POST | `/api/auth/login` | Autentica usuario y devuelve JWT |

## Usuarios

Base path: `/api/usuarios`. Requiere `ROLE_ADMIN`.

| Metodo | Endpoint | Controller |
|---|---|---|
| GET | `/api/usuarios` | `UsuarioController.getAllUsuarios` |
| GET | `/api/usuarios/{id}` | `UsuarioController.getUsuarioById` |
| POST | `/api/usuarios` | `UsuarioController.createUsuario` |
| PUT | `/api/usuarios/{id}` | `UsuarioController.updateUsuario` |
| DELETE | `/api/usuarios/{id}` | `UsuarioController.deleteUsuario` |

## Categorias

Base path: `/api/categorias`. Requiere autenticacion por la regla final.

| Metodo | Endpoint | Controller |
|---|---|---|
| GET | `/api/categorias` | `CategoriaController.getAllCategorias` |
| GET | `/api/categorias/{id}` | `CategoriaController.getCategoriaById` |
| POST | `/api/categorias` | `CategoriaController.createCategoria` |
| PUT | `/api/categorias/{id}` | `CategoriaController.updateCategoria` |
| DELETE | `/api/categorias/{id}` | `CategoriaController.deleteCategoria` |

## Productos

Base path: `/api/productos`. Requiere autenticacion.

| Metodo | Endpoint | Controller |
|---|---|---|
| GET | `/api/productos` | `ProductoController.getAllProductos` |
| GET | `/api/productos/{id}` | `ProductoController.getProductoById` |
| POST | `/api/productos` | `ProductoController.createProducto` |
| PUT | `/api/productos/{id}` | `ProductoController.updateProducto` |
| DELETE | `/api/productos/{id}` | `ProductoController.deleteProducto` |

## Fotos

Base path: `/api/fotos`. Requiere autenticacion por la regla final.

| Metodo | Endpoint | Controller |
|---|---|---|
| GET | `/api/fotos` | `FotoProductoController.getAllFotos` |
| GET | `/api/fotos/{id}` | `FotoProductoController.getFotoById` |
| GET | `/api/fotos/producto/{productoId}` | `FotoProductoController.getFotosByProducto` |
| POST | `/api/fotos` | `FotoProductoController.createFoto` |
| DELETE | `/api/fotos/{id}` | `FotoProductoController.deleteFoto` |

## Carritos e items

Base paths: `/api/carritos` y `/api/items-carrito`. Requiere autenticacion.

| Metodo | Endpoint | Controller |
|---|---|---|
| GET | `/api/carritos` | `CarritoController.getAllCarritos` |
| GET | `/api/carritos/{id}` | `CarritoController.getCarritoById` |
| GET | `/api/carritos/usuario/{usuarioId}` | `CarritoController.getCarritosByUsuario` |
| POST | `/api/carritos` | `CarritoController.createCarrito` |
| DELETE | `/api/carritos/{id}` | `CarritoController.deleteCarrito` |
| POST | `/api/carritos/{id}/checkout` | `CarritoController.checkout` |
| GET | `/api/items-carrito/{id}` | `ItemCarritoController.getItemById` |
| GET | `/api/items-carrito/carrito/{carritoId}` | `ItemCarritoController.getItemsByCarrito` |
| POST | `/api/items-carrito` | `ItemCarritoController.createItem` |
| DELETE | `/api/items-carrito/{id}` | `ItemCarritoController.deleteItem` |

## Ordenes y detalles

Base paths: `/api/ordenes` y `/api/detalles-orden`. Requiere autenticacion por la regla final.

| Metodo | Endpoint | Controller |
|---|---|---|
| GET | `/api/ordenes` | `OrdenCompraController.getAllOrdenes` |
| GET | `/api/ordenes/{id}` | `OrdenCompraController.getOrdenById` |
| GET | `/api/ordenes/usuario/{usuarioId}` | `OrdenCompraController.getOrdenesByUsuario` |
| GET | `/api/detalles-orden/{id}` | `DetalleOrdenController.getDetalleById` |
| GET | `/api/detalles-orden/orden/{ordenId}` | `DetalleOrdenController.getDetallesByOrden` |

## Como probar manualmente

1. Levantar la app:

```powershell
.\mvnw.cmd spring-boot:run
```

2. Login:

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@apple-ar.com",
  "password": "password"
}
```

3. Copiar el token y llamar un endpoint protegido:

```http
GET http://localhost:8080/api/productos
Authorization: Bearer <token>
```

4. Explorar Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

## Postman

La coleccion esta en:

```text
postman/ms-apple-argentina-uade.postman_collection.json
```

Incluye flujos de:

- Auth register/login.
- Usuarios con admin.
- Categorias, productos y fotos.
- Carritos, items y checkout.
- Ordenes y detalles.
- Casos negativos como 401, 404 y login invalido.

## Tests automatizados

Comando:

```powershell
.\mvnw.cmd test
```

Tipos de tests:

| Tipo | Archivos |
|---|---|
| Contexto Spring | `ECommerceApplicationTests.java` |
| Integracion JWT + endpoint protegido | `AuthJwtIntegrationTest.java` |
| Controllers delegando a services | `controller/ControllersTest.java` |
| Services con Mockito | `service/*ServiceTest.java` |
| JWT unitario | `security/JwtServiceTest.java` |
| Handler global de errores | `exception/GlobalExceptionHandlerTest.java` |
| DTO comun | `dto/common/ApiResponseTest.java` |

Los tests de contexto e integracion usan H2 en modo MySQL mediante properties dentro del test:

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MYSQL
spring.datasource.driver-class-name=org.h2.Driver
```

## Cobertura

JaCoCo esta configurado en `pom.xml`. Luego de correr tests, el reporte HTML queda en:

```text
target/site/jacoco/index.html
```


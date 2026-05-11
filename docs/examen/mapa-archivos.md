# Mapa de archivos para responder preguntas

Este archivo es el indice rapido para saber a donde ir si durante el examen te preguntan por una parte concreta.

## Configuracion y arranque

| Pregunta | Archivo |
|---|---|
| Cual es la clase main | `src/main/java/com/apple/tpo/e_commerce/ECommerceApplication.java` |
| Que version de Java y Spring usamos | `pom.xml` |
| Que dependencias tiene el proyecto | `pom.xml` |
| Como se configura MySQL local | `src/main/resources/application.properties` |
| Como se configura MySQL dentro de Docker | `src/main/resources/application-docker.properties` |
| Como se levanta backend + DB | `docker-compose.yml` |
| Como se construye la imagen | `Dockerfile` |
| Como se cargan datos iniciales | `src/main/resources/data_seed_startup.sql` |
| Donde esta la guia original del proyecto | `README.md` |
| Donde esta la consigna del TP | `TPO_API-1C-20261.pdf` |
| Donde esta el material de demo | `video.mp4`, `Video_segunda_entrega.rar` |

## Seguridad y autenticacion

| Pregunta | Archivo |
|---|---|
| Que endpoints son publicos o protegidos | `src/main/java/com/apple/tpo/e_commerce/config/SecurityConfig.java` |
| Como se agrega el filtro JWT al chain | `SecurityConfig.java` |
| Como se genera y valida el token | `src/main/java/com/apple/tpo/e_commerce/security/JwtService.java` |
| Como se lee `Authorization: Bearer ...` | `src/main/java/com/apple/tpo/e_commerce/security/JwtAuthFilter.java` |
| Como se carga el usuario desde email | `src/main/java/com/apple/tpo/e_commerce/service/CustomUserDetailsService.java` |
| Como se responden 401 personalizados | `src/main/java/com/apple/tpo/e_commerce/security/CustomAuthenticationEntryPoint.java` |
| Como se responden 403 personalizados | `src/main/java/com/apple/tpo/e_commerce/security/CustomAccessDeniedHandler.java` |
| Donde estan los roles | `src/main/java/com/apple/tpo/e_commerce/model/Role.java` |
| Como Swagger sabe usar Bearer JWT | `src/main/java/com/apple/tpo/e_commerce/config/OpenApiJwtConfig.java` |

## Auth

| Pregunta | Archivo |
|---|---|
| Endpoint de registro y login | `src/main/java/com/apple/tpo/e_commerce/controller/AuthController.java` |
| Reglas de registro: email unico, password encode, ROLE_USER | `src/main/java/com/apple/tpo/e_commerce/service/AuthService.java` |
| Request de login | `src/main/java/com/apple/tpo/e_commerce/dto/auth/LoginRequest.java` |
| Request de registro | `src/main/java/com/apple/tpo/e_commerce/dto/auth/RegisterRequest.java` |
| Response con token | `src/main/java/com/apple/tpo/e_commerce/dto/auth/AuthResponse.java` |

## Dominio y negocio

| Pregunta | Archivo |
|---|---|
| Usuarios CRUD | `controller/UsuarioController.java`, `service/UsuarioService.java`, `model/Usuario.java` |
| Productos CRUD | `controller/ProductoController.java`, `service/ProductoService.java`, `model/Producto.java` |
| Categorias CRUD | `controller/CategoriaController.java`, `service/CategoriaService.java`, `model/Categoria.java` |
| Fotos de producto | `controller/FotoProductoController.java`, `service/FotoProductoService.java`, `model/FotoProducto.java` |
| Carritos | `controller/CarritoController.java`, `service/CarritoService.java`, `model/Carrito.java` |
| Items de carrito | `controller/ItemCarritoController.java`, `service/ItemCarritoService.java`, `model/ItemCarrito.java` |
| Ordenes de compra | `controller/OrdenCompraController.java`, `service/OrdenCompraService.java`, `model/OrdenCompra.java` |
| Detalles de orden | `controller/DetalleOrdenController.java`, `service/DetalleOrdenService.java`, `model/DetalleOrden.java` |
| Checkout completo | `src/main/java/com/apple/tpo/e_commerce/service/CarritoService.java`, metodo `checkout` |
| Pedido viejo/deprecado | `model/Pedido.java`, `service/PedidoService.java` |

## Persistencia

| Pregunta | Archivo |
|---|---|
| Repositories JPA | `src/main/java/com/apple/tpo/e_commerce/respository/` |
| Query por email | `UsuarioRepository.java` |
| Query carritos por usuario | `CarritoRepository.java` |
| Query items por carrito | `ItemCarritoRepository.java` |
| Query ordenes por usuario | `OrdenCompraRepository.java` |
| Query detalles por orden | `DetalleOrdenRepository.java` |
| Query fotos por producto | `FotoProductoRepository.java` |

Nota: el paquete se llama `respository`, con una letra invertida. No rompe porque todo el codigo usa el mismo package, pero si te preguntan, es un typo de nombre, no un patron tecnico.

## DTOs, mapeo y respuestas

| Pregunta | Archivo |
|---|---|
| DTOs de entrada y salida | `src/main/java/com/apple/tpo/e_commerce/dto/` |
| Mapeo entity -> response | `src/main/java/com/apple/tpo/e_commerce/mapper/DtoMapper.java` |
| Formato generico de respuesta OK/error | `src/main/java/com/apple/tpo/e_commerce/dto/common/ApiResponse.java` |
| Manejo central de excepciones | `src/main/java/com/apple/tpo/e_commerce/exception/GlobalExceptionHandler.java` |
| Excepcion 404 | `ResourceNotFoundException.java` |
| Excepcion 400 negocio | `BusinessException.java` |
| Excepcion 409 conflicto | `ConflictException.java` |
| Excepcion 401 credenciales | `InvalidCredentialsException.java` |

## Tests y herramientas

| Pregunta | Archivo |
|---|---|
| Test de contexto Spring | `src/test/java/com/apple/tpo/e_commerce/ECommerceApplicationTests.java` |
| Test de login JWT con endpoint protegido | `src/test/java/com/apple/tpo/e_commerce/AuthJwtIntegrationTest.java` |
| Tests de controllers | `src/test/java/com/apple/tpo/e_commerce/controller/ControllersTest.java` |
| Tests de services | `src/test/java/com/apple/tpo/e_commerce/service/` |
| Tests de seguridad JWT | `src/test/java/com/apple/tpo/e_commerce/security/JwtServiceTest.java` |
| Tests del handler de errores | `src/test/java/com/apple/tpo/e_commerce/exception/GlobalExceptionHandlerTest.java` |
| Coleccion Postman | `postman/ms-apple-argentina-uade.postman_collection.json` |
| Reporte JaCoCo generado | `target/site/jacoco/index.html` |

# Preguntas tipicas de examen

## Que construyeron?

Construimos una API REST backend para un e-commerce de productos Apple. Permite registrar y loguear usuarios, listar y administrar productos/categorias/fotos, crear carritos, agregar items y hacer checkout para generar ordenes de compra con detalles.

## Que tecnologias usaron y por que?

Usamos Java 17 y Spring Boot para crear rapido una API robusta. Spring Web expone endpoints REST. Spring Data JPA abstrae el acceso a MySQL. Hibernate mapea clases Java a tablas. Spring Security protege endpoints. JWT permite autenticacion stateless. Lombok reduce getters, setters y constructores. Swagger/OpenAPI documenta endpoints. Docker Compose levanta backend y MySQL. JUnit, Mockito, MockMvc y H2 permiten probar sin depender de una base externa.

## Como esta organizada la app?

Esta organizada por capas:

- `controller`: endpoints HTTP.
- `service`: logica de negocio.
- `respository`: persistencia con JPA.
- `model`: entidades de base de datos.
- `dto`: requests y responses.
- `mapper`: conversion entity -> DTO.
- `security`: JWT y handlers de seguridad.
- `exception`: manejo central de errores.
- `config`: seguridad y Swagger.

## Como funciona Spring Boot en este proyecto?

La clase `ECommerceApplication` tiene `@SpringBootApplication`, que activa auto-configuracion, component scanning y configuracion de Spring. Al ejecutar `SpringApplication.run`, Spring detecta beans como controllers, services, repositories, filtros y configuraciones.

## Como se conecta a la base?

La conexion esta en `application.properties`. Usa MySQL en `localhost:3306/ecommerce_db4`, usuario root y password root. En Docker se activa el perfil `docker`, que usa host `mysql-db` desde `application-docker.properties`. JPA/Hibernate actualiza el schema con `ddl-auto=update`.

## Como se cargan datos iniciales?

Con `data_seed_startup.sql`, configurado por `spring.sql.init.data-locations`. El script usa `INSERT IGNORE`, entonces se puede ejecutar muchas veces sin duplicar registros existentes.

## Como funciona el registro?

`AuthController.register` recibe `RegisterRequest`. `AuthService.register` valida que el email no exista, hashea password con BCrypt, crea un usuario con `ROLE_USER`, lo guarda y genera un JWT con `JwtService`.

## Como funciona el login?

`AuthService.login` usa `AuthenticationManager` con email y password. Spring Security llama a `CustomUserDetailsService`, que busca el usuario por email. Si la password coincide con el hash BCrypt, se genera un JWT y se devuelve en `AuthResponse`.

## Que contiene el JWT?

El JWT usa como subject el email del usuario. Tambien tiene fecha de emision y expiracion. Se firma con HS256 usando la clave Base64 de `app.jwt.secret`.

## Como se protegen los endpoints?

`SecurityConfig` define la cadena de seguridad. Deshabilita CSRF, usa sesiones stateless, permite `/api/auth/**`, Swagger y health, exige admin para `/api/usuarios/**`, exige autenticacion para productos/carritos/items y deja el resto autenticado por `.anyRequest().authenticated()`. El filtro `JwtAuthFilter` valida el Bearer token antes del controller.

## Como se manejan roles?

Los roles estan en el enum `Role`: `ROLE_USER` y `ROLE_ADMIN`. Spring usa authorities. En `SecurityConfig`, `hasRole("ADMIN")` espera una authority llamada `ROLE_ADMIN`, que coincide con el enum.

## Como evitan devolver passwords?

No se devuelve la entity `Usuario` directamente. Se convierte a `UsuarioResponse`, que no tiene campo `password`. El mapeo esta en `DtoMapper.toUsuarioResponse`.

## Como funciona un CRUD comun?

Ejemplo productos:

1. `ProductoController` recibe la request.
2. Delega a `ProductoService`.
3. El service busca o guarda con `ProductoRepository`.
4. Si no encuentra un id, lanza `ResourceNotFoundException`.
5. Convierte entity a `ProductoResponse`.
6. Devuelve JSON.

## Como funciona checkout?

`CarritoService.checkout` busca el carrito, valida que este `ACTIVO`, valida que tenga items, valida stock suficiente, crea una `OrdenCompra`, crea un `DetalleOrden` por cada item, descuenta stock de cada producto, calcula total, cambia el carrito a `CHECKOUT` y devuelve `OrdenCompraResponse`.

## Cuando se descuenta stock?

Se descuenta durante checkout, no al agregar un item al carrito. Esto esta en `CarritoService.checkout`, dentro del loop de items.

## Que pasa si no hay stock?

`CarritoService.checkout` lanza `BusinessException` con mensaje de stock insuficiente. `GlobalExceptionHandler` lo convierte en HTTP 400.

## Que pasa si un recurso no existe?

Los services lanzan `ResourceNotFoundException`. `GlobalExceptionHandler` devuelve HTTP 404 con `ApiResponse.error`.

## Que formato tienen los errores?

Usan `ApiResponse<T>` con `timestamp`, `status`, `success`, `message` y `data`. Para seguridad hay handlers especiales que devuelven JSON para 401 y 403.

## Por que usan repositories?

Porque Spring Data JPA genera implementaciones automaticamente. Al extender `JpaRepository<Entity, Long>` obtenemos CRUD, paginacion basica y queries derivadas por nombre, como `findByEmail`, `findByUsuarioId` o `findByCarritoId`.

## Que es `@Transactional`?

Marca operaciones de service para que se ejecuten dentro de una transaccion. Es importante en checkout porque hay varias escrituras relacionadas: orden, detalles, stock y estado del carrito.

## Que pruebas tienen?

Tienen tests unitarios de services con Mockito, tests de controllers que verifican delegacion, tests de JWT, tests del handler global de errores, test de contexto Spring y un test de integracion con MockMvc que prueba login y acceso a un endpoint protegido con Bearer token.

## Como lo levantan con Docker?

`docker-compose.yml` levanta dos servicios: `mysql-db` con MySQL y `backend` construido desde el `Dockerfile`. El backend depende del healthcheck de MySQL y usa `SPRING_PROFILES_ACTIVE=docker`, por eso toma la URL `jdbc:mysql://mysql-db:3306/ecommerce_db4`.

## Para que sirve Swagger?

Swagger UI permite explorar y probar endpoints desde el navegador. `OpenApiJwtConfig` define el esquema `bearerAuth` para documentar que los endpoints protegidos necesitan JWT.

## Para que sirve Postman?

La coleccion Postman contiene requests ya preparados para probar auth, usuarios, catalogo, carritos, checkout, ordenes, detalles y casos negativos. Sirve como evidencia de prueba manual y demo.

## Puntos honestos a mencionar si preguntan mejoras

Estos no invalidan el proyecto; muestran que entendes el codigo.

| Punto | Mejora posible |
|---|---|
| Estados de carrito y orden son `String` | Convertir a enums |
| Paquete `respository` tiene typo | Renombrar a `repository` |
| `UsuarioRequest` y otros DTOs tienen poca validacion | Agregar `@NotBlank`, `@Email`, rangos de precio/stock |
| Secret JWT y credenciales estan en properties | Mover a variables de entorno |
| Stock se valida en checkout | Opcional: validar tambien al agregar item para feedback temprano |
| Controllers mezclan `@Autowired` y constructor injection | Unificar constructor injection |
| `Pedido` y `PedidoService` estan deprecados | Eliminarlos en un refactor |


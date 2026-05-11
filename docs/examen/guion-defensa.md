# Guion de defensa oral

Este archivo sirve para practicar una explicacion corrida. La clave es no memorizar palabra por palabra, sino recordar el orden: problema, stack, arquitectura, seguridad, datos, flujo fuerte, pruebas.

## Version 2 minutos

El proyecto es una API REST backend para un e-commerce de productos Apple. Esta hecha con Java 17 y Spring Boot. Usamos Spring Web para exponer endpoints, Spring Data JPA con Hibernate para persistir entidades en MySQL, Spring Security con JWT para proteger la API, Swagger para documentar y probar endpoints, Docker Compose para levantar backend y base de datos, y tests con JUnit, Mockito, MockMvc y H2.

La arquitectura esta separada en capas. Los controllers reciben requests HTTP, los services tienen la logica de negocio, los repositories acceden a la base con `JpaRepository`, las entities representan tablas y los DTOs definen lo que entra y sale por la API. Para no exponer datos sensibles, como password, convertimos entities a responses con `DtoMapper`.

La parte mas importante del negocio es checkout. Un usuario crea un carrito, agrega items y al hacer checkout el service valida que el carrito este activo, que tenga items y que haya stock suficiente. Despues crea una orden de compra, crea detalles por cada item, descuenta stock, calcula el total y cambia el carrito a estado `CHECKOUT`.

## Version 5 minutos

Arrancaria diciendo que es un backend API REST para administrar un e-commerce Apple. El usuario puede registrarse, loguearse, consultar catalogo, crear carrito, agregar productos y convertir ese carrito en una orden de compra.

El stack principal esta en `pom.xml`: Java 17, Spring Boot 4.0.5, Spring Web, Spring Data JPA, Spring Security, Validation, Springdoc OpenAPI, JJWT, MySQL, H2, Lombok, JUnit, Mockito, Spring Security Test y JaCoCo.

La aplicacion arranca en `ECommerceApplication`, con `@SpringBootApplication`. La configuracion de base esta en `application.properties`; localmente apunta a MySQL en `localhost:3306/ecommerce_db4`. En Docker se usa `application-docker.properties`, donde el host es `mysql-db`, que es el nombre del servicio dentro de `docker-compose.yml`.

La arquitectura esta separada por paquetes. `controller` expone endpoints REST; `service` contiene reglas de negocio y transacciones; `respository` extiende `JpaRepository`; `model` define entities JPA; `dto` define request y response; `mapper` transforma entities a DTOs; `security` contiene JWT y handlers; `exception` centraliza errores.

La seguridad se configura en `SecurityConfig`. La API es stateless, se deshabilita CSRF, se permite `/api/auth/**`, Swagger y health. Los usuarios requieren `ROLE_ADMIN`; productos, carritos e items requieren autenticacion; y el resto queda autenticado por `.anyRequest().authenticated()`. El filtro `JwtAuthFilter` lee `Authorization: Bearer <token>`, extrae el email, carga el usuario con `CustomUserDetailsService`, valida el token con `JwtService` y setea el `SecurityContext`.

El login y registro estan en `AuthController` y `AuthService`. En registro se valida email unico, se hashea password con BCrypt, se asigna `ROLE_USER`, se guarda usuario y se genera JWT. En login se usa `AuthenticationManager`, se verifica password y se devuelve otro JWT.

El modelo de datos tiene usuarios, categorias, productos, fotos, carritos, items, ordenes y detalles. Las relaciones principales son: usuario tiene carritos y ordenes; categoria tiene productos; carrito tiene items; producto aparece en items y detalles; una orden se genera desde un carrito y tiene detalles.

El flujo de negocio mas importante es checkout en `CarritoService.checkout`. Ahi se valida estado `ACTIVO`, carrito no vacio y stock suficiente. Luego se crea la orden, se crean detalles, se descuenta stock y se marca el carrito como `CHECKOUT`. Esta clase usa `@Transactional`, que es importante porque son varias escrituras relacionadas.

Los errores se manejan con excepciones propias: `ResourceNotFoundException` para 404, `BusinessException` para 400, `ConflictException` para 409 e `InvalidCredentialsException` para 401. `GlobalExceptionHandler` las transforma en respuestas JSON con `ApiResponse`.

Para probar, tenemos una coleccion Postman, Swagger UI, tests unitarios de services, tests de controllers, tests de JWT y un test de integracion que hace login y llama un endpoint protegido con Bearer token usando MockMvc.

## Version 10 minutos

1. Presentar el objetivo:
   API REST backend para e-commerce Apple con usuarios, catalogo, carrito, checkout y ordenes.

2. Presentar stack:
   Java 17, Spring Boot, Spring Web, JPA/Hibernate, MySQL, Spring Security JWT, Lombok, Swagger, Docker, Maven, JUnit/Mockito/MockMvc/H2/JaCoCo.

3. Explicar arranque:
   `ECommerceApplication` arranca Spring. Spring detecta controllers, services, repositories, filtros y configs.

4. Explicar arquitectura:
   Controller recibe HTTP, service aplica reglas, repository persiste, model representa tablas, DTO define contratos, mapper convierte, exception handler unifica errores.

5. Explicar seguridad:
   `SecurityConfig` define rutas publicas y protegidas. `JwtAuthFilter` valida Bearer token. `JwtService` genera y valida JWT. `CustomUserDetailsService` busca usuario por email. Roles: `ROLE_USER`, `ROLE_ADMIN`.

6. Explicar auth:
   Register valida email, hashea password, guarda usuario con rol default, genera JWT. Login usa `AuthenticationManager`, carga usuario y genera JWT.

7. Explicar modelo:
   Usuario, Categoria, Producto, FotoProducto, Carrito, ItemCarrito, OrdenCompra, DetalleOrden. Mostrar `modelo-datos.md`.

8. Explicar flujo checkout:
   Buscar carrito, validar activo, validar items, validar stock, crear orden, crear detalles, descontar stock, calcular total, marcar carrito como checkout.

9. Explicar errores:
   Services lanzan excepciones. `GlobalExceptionHandler` responde JSON consistente. 401/403 tienen handlers de seguridad personalizados.

10. Explicar pruebas:
    Unit tests con Mockito, integracion con MockMvc/H2, Postman para demo manual, Swagger para exploracion, JaCoCo para cobertura.

## Cierre recomendado

Si queres cerrar fuerte, deci:

```text
Lo mas importante del proyecto es que no es solo CRUD: tiene una arquitectura por capas, seguridad JWT stateless, persistencia relacional con JPA y un flujo de checkout transaccional que valida reglas de negocio y modifica varias entidades de forma consistente.
```


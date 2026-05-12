# Posibles preguntas de examen y respuestas sobre nuestro repo

Este archivo imagina por donde puede venir el examen segun las notas del profesor. Esta armado para responder con conceptos y, enseguida, mostrar donde esta en nuestro TPO.

## 1. Explique tecnicamente el flujo de login en su TPO

Respuesta:

El flujo empieza en `AuthController.login`, que recibe un `LoginRequest` con email y password. Ese request esta validado con `@Valid`, `@Email` y `@NotBlank`. El controller delega en `AuthService.login`.

En el service se llama a `AuthenticationManager.authenticate`, pasando un `UsernamePasswordAuthenticationToken`. Spring Security usa `CustomUserDetailsService` para buscar el usuario por email en `UsuarioRepository`. Despues compara la password ingresada contra la password hasheada con BCrypt.

Si autentica, `AuthService` busca el usuario, construye un `UserDetails` y llama a `JwtService.generateToken`. El token vuelve en `AuthResponse` junto con email y role.

Donde mostrar:

- `AuthController.java`
- `AuthService.java`
- `CustomUserDetailsService.java`
- `JwtService.java`
- `SecurityConfig.java`

## 2. Su API es stateless? Como lo demuestra en codigo?

Respuesta:

Si. La API no guarda sesion HTTP del usuario. En `SecurityConfig` se configura:

```java
session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
```

Eso significa que cada request protegida debe traer su token JWT en el header `Authorization`. El filtro `JwtAuthFilter` valida ese token antes de llegar al controller.

Donde mostrar:

- `SecurityConfig.securityFilterChain`
- `JwtAuthFilter.doFilterInternal`

## 3. Que diferencia hay entre autenticacion y autorizacion en su proyecto?

Respuesta:

Autenticacion es verificar quien es el usuario. En nuestro TPO ocurre en login, con email y password.

Autorizacion es verificar que puede hacer ese usuario. En nuestro TPO ocurre cuando intenta acceder a endpoints. Por ejemplo, `/api/usuarios/**` requiere `ROLE_ADMIN`.

Donde mostrar:

- Autenticacion: `AuthService.login`
- Autorizacion: `SecurityConfig`, regla `.requestMatchers("/api/usuarios/**").hasRole("ADMIN")`

## 4. Como se protege un endpoint con JWT?

Respuesta:

Primero `SecurityConfig` define que rutas requieren autenticacion. Luego `JwtAuthFilter` intercepta cada request antes del controller. Si encuentra un header `Authorization: Bearer <token>`, extrae el token, obtiene el email con `JwtService.extractUsername`, carga el usuario con `CustomUserDetailsService`, valida el token y carga la autenticacion en `SecurityContextHolder`.

Si el token falta o es invalido, Spring responde 401 para rutas protegidas.

Donde mostrar:

- `JwtAuthFilter.java`
- `JwtService.java`
- `CustomAuthenticationEntryPoint.java`

## 5. Cuando validan un token, consultan la base de datos?

Respuesta:

Si, en nuestra implementacion se consulta la base para cargar el usuario. `JwtAuthFilter` extrae el email del token y llama a `userDetailsService.loadUserByUsername(email)`. Ese service usa `UsuarioRepository.findByEmail`.

Pero no se guarda una sesion en base ni en memoria. La consulta sirve para cargar authorities y confirmar que el usuario existe. La identidad principal viaja en el JWT firmado.

Donde mostrar:

- `JwtAuthFilter.java`
- `CustomUserDetailsService.java`
- `UsuarioRepository.java`

## 6. Por que usan DTOs y no devuelven entities?

Respuesta:

Porque las entities son el modelo interno y pueden tener campos sensibles o relaciones que no queremos exponer. Por ejemplo, `Usuario` tiene `password`, pero `UsuarioResponse` no.

El DTO funciona como un filtro de informacion. `DtoMapper.toUsuarioResponse` decide que campos salen al cliente.

Donde mostrar:

- `Usuario.java`
- `UsuarioResponse.java`
- `DtoMapper.toUsuarioResponse`

## 7. Que pasaria si devuelven `Usuario` directamente desde el Controller?

Respuesta:

Podriamos filtrar la password hasheada, roles y relaciones internas como carritos, ordenes o productos. Ademas podrian aparecer ciclos de serializacion por relaciones bidireccionales.

Aunque la password este hasheada, no corresponde exponerla. El cliente solo necesita datos seguros.

## 8. Explique el checkout como caso de negocio

Respuesta:

El checkout esta en `CarritoService.checkout`. Es el caso de negocio mas importante del repo.

Pasos:

1. Busca el carrito.
2. Verifica que este `ACTIVO`.
3. Verifica que tenga items.
4. Valida stock suficiente.
5. Crea una `OrdenCompra`.
6. Por cada item, descuenta stock y crea `DetalleOrden`.
7. Calcula total.
8. Guarda la orden.
9. Cambia carrito a `CHECKOUT`.
10. Devuelve `OrdenCompraResponse`.

Donde mostrar:

- `CarritoController.

`
- `CarritoService.checkout`
- `OrdenCompra.java`
- `DetalleOrden.java`

## 9. Por que checkout debe estar en Service y no en Controller?

Respuesta:

Porque checkout no es solo HTTP; es logica de negocio. Tiene validaciones, calculo de total, modificacion de stock, creacion de orden y cambio de estado. Si estuviera en Controller, mezclariamos transporte HTTP con reglas de dominio.

Ademas el Service tiene `@Transactional`, necesario para que las operaciones de base funcionen como unidad.

Donde mostrar:

- `CarritoService.java`, anotacion `@Transactional`

## 10. Que hace `@Transactional` y por que importa?

Respuesta:

`@Transactional` hace que las operaciones del service se ejecuten dentro de una transaccion. En checkout importa porque se escriben varias tablas: productos, ordenes, detalles y carrito.

Si algo falla en medio del proceso, la transaccion evita que queden datos a medias, como stock descontado sin orden generada.

## 11. Donde validan stock y por que en backend?

Respuesta:

El stock se valida en `CarritoService.checkout`, antes de descontarlo:

- Si el producto tiene menos stock que la cantidad del item, se lanza `BusinessException`.

Se valida en backend porque el frontend no es confiable: un usuario puede modificar la request o llamar la API desde Postman.

Mejora posible:

- Validar tambien al agregar item para dar respuesta temprana.
- Mantener siempre la validacion final en checkout.

## 12. Como se manejan errores?

Respuesta:

Los services lanzan excepciones personalizadas. `GlobalExceptionHandler` las captura y las transforma en respuestas JSON con `ApiResponse`.

Mapeo:

- `ResourceNotFoundException`: 404.
- `BusinessException`: 400.
- `ConflictException`: 409.
- `InvalidCredentialsException`: 401.
- `MethodArgumentNotValidException`: 400.

Donde mostrar:

- `GlobalExceptionHandler.java`
- `ApiResponse.java`

## 13. Por que no usan try-catch en cada Controller?

Respuesta:

Porque repetiria codigo y mezclaria responsabilidades. Con `GlobalExceptionHandler`, los errores se manejan de forma centralizada y todos los endpoints tienen respuestas consistentes.

## 14. Que es Repository y por que algunos estan vacios?

Respuesta:

Repository es la capa de acceso a datos. En Spring Data JPA, al extender `JpaRepository`, Spring genera automaticamente metodos CRUD como `findAll`, `findById`, `save`, `deleteById`.

Por eso `ProductoRepository` puede estar vacio. Si necesitamos consultas especificas, agregamos metodos por convencion de nombre, como `findByEmail` o `findByUsuarioId`.

Donde mostrar:

- `ProductoRepository.java`
- `UsuarioRepository.java`
- `CarritoRepository.java`

## 15. Que ORM usan?

Respuesta:

Usamos Hibernate a traves de Spring Data JPA. JPA es la especificacion y Hibernate es la implementacion que mapea entities Java a tablas de MySQL.

Donde mostrar:

- Dependencia `spring-boot-starter-data-jpa` en `pom.xml`
- Entities en `model/`
- Repositories en `respository/`

## 16. Como se crean las tablas?

Respuesta:

Las tablas se crean o actualizan a partir de entities JPA y la configuracion:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Las entities tienen anotaciones como:

- `@Entity`
- `@Table`
- `@Id`
- `@GeneratedValue`
- `@ManyToOne`
- `@OneToMany`
- `@JoinColumn`

Donde mostrar:

- `Usuario.java`
- `Producto.java`
- `Carrito.java`
- `OrdenCompra.java`
- `application.properties`

## 17. Como se relacionan las entidades principales?

Respuesta:

- `Usuario` tiene productos creados, carritos y ordenes.
- `Categoria` tiene productos.
- `Producto` tiene fotos.
- `Carrito` tiene items.
- `ItemCarrito` apunta a producto y carrito.
- `OrdenCompra` apunta a usuario y carrito.
- `DetalleOrden` apunta a orden y producto.

Donde mostrar:

- `docs/examen/modelo-datos.md`
- `docs/examen/diagramas/modelo-er.mmd`

## 18. Que pasa si borran una categoria con productos?

Respuesta:

Como no hay cascade configurado en `Categoria.productos`, JPA no deberia borrar productos automaticamente. La base puede impedir el borrado por integridad referencial si hay productos asociados.

Desde negocio, no conviene borrar categorias usadas sin decidir una regla: reasignar productos, desactivar categoria o impedir borrado.

## 19. Por que `FotoProducto` tiene `producto_id`?

Respuesta:

Porque la relacion es un producto con muchas fotos. La clave foranea conviene en el lado muchos, o sea en `fotos_producto`.

Donde mostrar:

- `FotoProducto.java`: `@ManyToOne`, `@JoinColumn(name = "producto_id")`
- `Producto.java`: `@OneToMany(mappedBy = "producto")`

## 20. Que dependencias importantes hay en `pom.xml`?

Respuesta:

- `spring-boot-starter-web`: API REST.
- `spring-boot-starter-data-jpa`: persistencia.
- `spring-boot-starter-security`: seguridad.
- `spring-boot-starter-validation`: validaciones.
- `springdoc-openapi-starter-webmvc-ui`: Swagger.
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson`: JWT.
- `mysql-connector-j`: MySQL.
- `h2`: tests.
- `lombok`: menos boilerplate.
- `spring-boot-starter-test`: tests.
- `spring-security-test`: tests de seguridad.
- `jacoco-maven-plugin`: cobertura.

## 21. Que hace Lombok en el proyecto?

Respuesta:

Reduce boilerplate. Por ejemplo:

- `@Data` genera getters y setters.
- `@RequiredArgsConstructor` genera constructor con campos `final`.
- `@AllArgsConstructor` genera constructor completo.

Donde mostrar:

- DTOs en `src/main/java/.../dto`
- `AuthController`
- `AuthService`
- `ApiResponse`

## 22. Como se configura Docker?

Respuesta:

El `Dockerfile` construye la app en dos etapas:

1. Usa Maven con JDK 17 para compilar y empaquetar.
2. Usa JRE 17 para ejecutar el `.jar`.

`docker-compose.yml` levanta dos servicios:

- `mysql-db`: base MySQL.
- `backend`: app Spring Boot.

El backend usa `SPRING_PROFILES_ACTIVE=docker`, por eso toma `application-docker.properties` con host `mysql-db`.

## 23. Imagen Docker vs contenedor

Respuesta:

Una imagen es una plantilla estatica con la app y sus dependencias. Un contenedor es una instancia en ejecucion de esa imagen.

En Cyber Monday, tener una imagen permite levantar muchas instancias iguales del backend rapidamente.

## 24. Por que Docker ayuda con escalabilidad horizontal?

Respuesta:

Porque permite crear multiples contenedores identicos desde la misma imagen. Si la API es stateless, esos contenedores pueden atender requests indistintamente. Un balanceador reparte el trafico.

Relacion con nuestro TPO:

- Docker replica backend.
- JWT/stateless permite no depender de sesiones locales.
- MySQL queda como persistencia compartida.

## 25. Que revisaria si no levanta la base?

Respuesta:

Revisaria:

- `application.properties`: URL, usuario, password, driver.
- `application-docker.properties`: host `mysql-db`.
- `docker-compose.yml`: servicio MySQL, variables, puerto y healthcheck.
- `pom.xml`: dependencia MySQL.
- Logs del backend.
- Si el puerto 3306 esta ocupado.

## 26. Que tests tienen?

Respuesta:

Tenemos:

- Tests de services con Mockito.
- Tests de controllers que verifican delegacion.
- Test de JWT.
- Test de `GlobalExceptionHandler`.
- Test de contexto Spring.
- Test de integracion con MockMvc para login y endpoint protegido.

Donde mostrar:

- `src/test/java/com/apple/tpo/e_commerce/`

## 27. Que prueba demuestra JWT funcionando?

Respuesta:

`AuthJwtIntegrationTest` hace login, obtiene un token y luego llama un endpoint protegido con:

```http
Authorization: Bearer <token>
```

Tambien prueba que un endpoint protegido sin token devuelve 401.

## 28. Que mejora tecnica propondria?

Respuesta:

Propondria:

- Convertir estados `ACTIVO`, `CHECKOUT`, `COMPLETADA` a enums.
- Agregar mas validaciones en DTOs, como stock y precio positivos.
- Mover `app.jwt.secret` y credenciales a variables de entorno.
- Corregir typo del paquete `respository` a `repository`.
- Unificar inyeccion por constructor.
- Agregar control de concurrencia para stock si escalaramos el marketplace.

## 29. Pregunta trampa: JWT evita consultar la base siempre?

Respuesta:

JWT evita guardar y consultar una sesion del usuario. Pero en nuestro TPO, al validar token, igualmente consultamos la base para cargar el usuario y authorities.

Entonces la respuesta precisa es:

> No tenemos sesiones server-side. El JWT trae identidad firmada. Pero nuestra implementacion consulta la base para cargar el usuario actual y sus permisos.

## 30. Pregunta trampa: si el password esta con BCrypt, ya no necesito HTTPS?

Respuesta:

No. BCrypt protege la password guardada en la base. HTTPS protege la informacion en transito.

Sin HTTPS, un atacante podria capturar el token o incluso las credenciales durante login. Por eso en produccion se necesitan ambos: BCrypt para reposo y HTTPS para transito.

## 31. Pregunta trampa: si valido stock en frontend, alcanza?

Respuesta:

No. El frontend es manipulable. La validacion que protege la integridad debe estar en backend, cerca de la base y dentro del caso de negocio.

En nuestro repo, checkout valida stock en `CarritoService`.

## 32. Pregunta trampa: por que no llamar Repository desde Controller?

Respuesta:

Porque se saltea la capa donde viven las reglas de negocio. En un CRUD simple podria funcionar, pero en un marketplace real aparecen stock, promociones, pagos, usuarios, auditoria y transacciones. Todo eso debe estar en Service.

## 33. Mini respuesta para cerrar si te piden explicar todo el TPO

Respuesta:

> Nuestro TPO es una API REST de e-commerce Apple hecha con Spring Boot. Esta separada en controllers, services, repositories, models y DTOs. Usa MySQL con JPA/Hibernate para persistencia, Spring Security con JWT para autenticacion stateless, DTOs para no exponer entidades internas, excepciones centralizadas con `GlobalExceptionHandler`, Docker Compose para levantar backend y base, y tests con JUnit, Mockito, MockMvc y H2. El flujo mas importante es checkout, porque valida negocio, descuenta stock, genera orden y detalles dentro de una transaccion.


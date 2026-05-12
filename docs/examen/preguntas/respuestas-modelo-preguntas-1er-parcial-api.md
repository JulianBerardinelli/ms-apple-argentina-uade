# Respuestas al modelo de preguntas - 1er Parcial API

Fuente base: `ModeloPreguntas-1erParcial-API.pdf`.

Este archivo responde los temas del documento del profesor y los aterriza en nuestro TPO: una API REST de e-commerce Apple hecha con Spring Boot, Spring Security JWT, Spring Data JPA, MySQL, Docker y tests.

## API y REST

### Que es una API?

Una API, Application Programming Interface, es un contrato que permite que un software use funcionalidades de otro sin conocer su implementacion interna. Define como pedir algo, que datos mandar y que respuesta esperar.

En nuestro repo, la API es el conjunto de endpoints bajo `/api/...`, por ejemplo:

- `POST /api/auth/login`
- `GET /api/productos`
- `POST /api/carritos/{id}/checkout`

Donde verlo:

- `src/main/java/com/apple/tpo/e_commerce/controller/`

### Que es una API REST?

Una API REST es una API web que usa HTTP como protocolo y recursos identificados por URLs. Normalmente usa metodos HTTP:

- `GET`: consultar.
- `POST`: crear o ejecutar una accion.
- `PUT`: actualizar.
- `DELETE`: borrar.

En nuestro TPO:

- `GET /api/productos` lista productos.
- `POST /api/productos` crea un producto.
- `PUT /api/productos/{id}` actualiza.
- `DELETE /api/productos/{id}` elimina.

Donde verlo:

- `ProductoController.java`
- `CategoriaController.java`
- `UsuarioController.java`
- `CarritoController.java`

### Por que una API se parece al ejemplo del cliente y el mozo?

El cliente no entra a la cocina ni sabe como se prepara el plato. Le pide al mozo algo del menu. El mozo lleva el pedido a la cocina y devuelve el resultado.

En software:

- Cliente: Postman, Swagger o frontend.
- Mozo: Controller.
- Cocina: Service + Repository + Base de datos.
- Plato: JSON de respuesta.

Ejemplo:

`GET /api/productos` entra por `ProductoController`, delega en `ProductoService`, consulta `ProductoRepository` y devuelve un JSON con productos.

### Diferencia entre API y endpoint

La API es el conjunto completo de reglas y endpoints. Un endpoint es una URL concreta dentro de esa API.

Ejemplo:

- API: todo nuestro backend de e-commerce.
- Endpoint: `POST /api/auth/login`.

### Ejemplo de API que no sea web

Una API puede ser una libreria local. Por ejemplo, el `PasswordEncoder` de Spring Security expone metodos como `encode` y `matches`; nuestro codigo lo usa sin conocer toda su implementacion interna.

Donde verlo:

- `AuthService.java`
- `UsuarioService.java`
- `SecurityConfig.java`

### Relacion entre API REST y token de login

REST es stateless: el servidor no guarda una sesion por cada usuario logueado. Por eso, despues del login el backend devuelve un token JWT. El cliente debe enviarlo en cada request protegida.

En nuestro TPO:

1. `POST /api/auth/login` devuelve `AuthResponse` con `token`.
2. El cliente llama endpoints protegidos con:

```http
Authorization: Bearer <token>
```

Donde verlo:

- `AuthController.java`
- `AuthService.java`
- `JwtService.java`
- `JwtAuthFilter.java`
- `SecurityConfig.java`

### Que significa stateless?

Significa que cada request debe traer toda la informacion necesaria para ser procesada. El backend no depende de una sesion guardada en memoria.

Ejemplo:

Si llamo `GET /api/productos`, el servidor no recuerda que hice login antes. Lo sabe porque en esa request mando el JWT.

Relacion con token:

El token reemplaza la necesidad de una sesion interna. Contiene identidad firmada, en nuestro caso el email como subject.

### Como puede Mercado Libre comprar a nombre de un usuario si el backend no guarda sesion?

Porque el cliente envia un token en cada request. El backend valida la firma del token, extrae la identidad del usuario y procesa la operacion. No necesita guardar la sesion en memoria.

En nuestro repo, `JwtAuthFilter` extrae el email del token y carga el usuario con `CustomUserDetailsService`.

### Por que stateless ayuda a escalabilidad horizontal?

Porque cualquier instancia del backend puede atender cualquier request. Si la sesion estuviera guardada en memoria en un servidor especifico, el usuario tendria que volver siempre a ese mismo servidor. Con JWT, cualquier nodo que conozca la clave puede validar el token.

Ejemplo Cyber Monday:

Si pasamos de 10.000 a 100.000 compras por dia, se pueden levantar mas instancias identicas del backend y balancear peticiones entre ellas. Como no hay sesion local, no importa que request atienda cada instancia.

### Por que REST suele ser mas ligero que SOAP?

REST suele usar HTTP simple y JSON, que es liviano y facil de leer. SOAP usa XML con una estructura mas pesada y estricta. SOAP puede servir en sistemas empresariales muy formales, pero para una API de e-commerce REST es mas simple y eficiente.

### Marketplace vs homebanking: que autenticacion o arquitectura usaria?

Marketplace:

- REST stateless + JWT.
- Escala bien.
- Es adecuado para catalogo, carrito y compras.

Homebanking:

- Seria mas estricto.
- Usaria HTTPS obligatorio, MFA, tokens de vida corta, auditoria, posible estado transaccional controlado y confirmaciones.
- Para transferencias o pagos sensibles podria combinar REST con mecanismos que preserven estado de transaccion, idempotencia y trazabilidad.

Idea clave:

En homebanking la seguridad e integridad pesan mas que la simplicidad. En marketplace se prioriza escala, rapidez y disponibilidad, sin descuidar seguridad.

### Por que JSON y stateless son criticos con 100.000 usuarios?

JSON reduce el peso de mensajes y simplifica parsing. Stateless permite balancear requests entre muchas instancias. Juntos reducen carga de red, memoria y CPU, y evitan depender de sesiones en un nodo unico.

### Por que JWT es como el QR de UADE?

Porque el QR contiene informacion que permite identificar y autorizar el acceso. El guardia no necesita preguntarle a toda la universidad en cada ingreso si sos vos; valida el QR.

JWT:

- Tiene subject, en nuestro caso email.
- Tiene fecha de emision.
- Tiene expiracion.
- Esta firmado con una clave secreta.

No se puede falsificar facilmente porque si alguien cambia el contenido, la firma deja de coincidir.

Donde verlo:

- `JwtService.generateToken`
- `JwtService.extractUsername`
- `JwtService.isTokenValid`

## Spring Boot y Maven

### Que es un gestor de paquetes?

Es una herramienta que descarga, organiza y versiona dependencias. Evita bajar `.jar` manualmente y ayuda a que todos los desarrolladores compilen igual.

### Que es Maven?

Maven es el gestor de dependencias y build del proyecto. Lee `pom.xml`, descarga librerias, compila, corre tests y empaqueta el `.jar`.

Donde verlo:

- `pom.xml`
- `mvnw.cmd`
- `mvnw`

### Para que sirve `pom.xml`?

Define:

- Version de Java: 17.
- Version de Spring Boot: 4.0.5.
- Dependencias: Web, JPA, Security, Validation, MySQL, H2, Lombok, tests, JJWT, Swagger.
- Plugins: Spring Boot Maven Plugin y JaCoCo.

### Como se agregan dependencias?

Se agrega un bloque `<dependency>` en `pom.xml`. Maven descarga esa libreria y sus dependencias transitivas.

Ejemplo de nuestro repo:

- `spring-boot-starter-web`: controllers REST.
- `spring-boot-starter-data-jpa`: repositories y ORM.
- `spring-boot-starter-security`: autenticacion/autorizacion.
- `jjwt-api`: JWT.
- `lombok`: menos boilerplate.

### Ventajas de Spring Boot para backend

Spring Boot ayuda con:

- Auto-configuracion.
- Servidor embebido.
- Inyeccion de dependencias.
- Controllers REST.
- Seguridad.
- Persistencia.
- Validacion.
- Tests.
- Configuracion por `application.properties`.

Clase de arranque:

- `ECommerceApplication.java`

### Que dependencias minimizan boilerplate?

Lombok:

- `@Data`: getters, setters, `toString`, etc.
- `@RequiredArgsConstructor`: constructor para campos `final`.
- `@AllArgsConstructor`: constructor con todos los campos.

Spring Data JPA:

- Evita escribir DAOs manuales.
- `JpaRepository` ya trae CRUD.

Spring Boot:

- Evita mucha configuracion manual.

Sin Lombok y JPA aumentaria el boilerplate en:

- DTOs y models con getters/setters.
- Repositories con CRUD manual.
- Services con construccion manual de dependencias y consultas.

### Donde se configura la base?

Local:

- `src/main/resources/application.properties`

Docker:

- `src/main/resources/application-docker.properties`

Datos importantes:

- URL JDBC.
- Username/password.
- Driver MySQL.
- `spring.jpa.hibernate.ddl-auto=update`.
- Seed SQL.

### Servidor web vs Tomcat

Un servidor web recibe peticiones HTTP y devuelve respuestas. Tomcat es un servidor de aplicaciones/servlet container que puede ejecutar aplicaciones Java web.

En Spring Boot, Tomcat viene embebido por defecto con `spring-boot-starter-web`. Por eso no instalamos Tomcat aparte.

### Maven vs descargar JARs manualmente

Maven:

- Controla versiones.
- Descarga dependencias transitivas.
- Permite builds repetibles.
- Corre tests y empaqueta.
- Facilita CI/CD.

JAR manual:

- Propenso a errores.
- Dificil de versionar.
- Dificil de replicar en otro equipo.

## Capas

### Por que separar en capas?

Para mantener el codigo ordenado, testeable y mantenible. Cada capa tiene una responsabilidad.

En nuestro repo:

- Controller: recibe HTTP.
- Service: reglas de negocio.
- Repository: acceso a datos.
- Model: entidades persistentes.
- DTO: contrato externo.
- Mapper: conversion.

### Responsabilidades

Controller:

- Define endpoints.
- Lee `@RequestBody` y `@PathVariable`.
- Devuelve respuestas HTTP.

Service:

- Contiene logica de negocio.
- Valida reglas.
- Maneja transacciones.

Repository:

- Habla con la base.
- Extiende `JpaRepository`.

Model:

- Define entidades JPA y relaciones.

### Por que el Controller no debe tener logica?

Porque se vuelve dificil de mantener y testear. El Controller deberia ser delgado: recibir la request y delegar. Si metemos negocio ahi, duplicamos reglas y mezclamos HTTP con dominio.

Ejemplo correcto:

- `CarritoController.checkout` delega en `CarritoService.checkout`.

### Como se exponen endpoints?

Con anotaciones:

- `@RestController`
- `@RequestMapping`
- `@GetMapping`
- `@PostMapping`
- `@PutMapping`
- `@DeleteMapping`

Spring Boot levanta Tomcat embebido y Spring MVC registra esas rutas.

### Como se invoca un endpoint?

Con una request HTTP:

```http
GET http://localhost:8080/api/productos
Authorization: Bearer <token>
```

Formato:

```text
protocolo://host:puerto/ruta
```

### Que es logica de negocio?

Son reglas propias del dominio. Por ejemplo:

- No hacer checkout si el carrito no esta activo.
- No hacer checkout si no hay items.
- No comprar si no hay stock.
- Descontar stock y generar orden.

Donde verlo:

- `CarritoService.checkout`
- `ItemCarritoService.createItem`
- `AuthService.register`

### Para que sirve `@Transactional`?

Agrupa operaciones de base de datos en una transaccion. Si algo falla, se puede hacer rollback para evitar inconsistencias.

Ejemplo:

En checkout se crea orden, detalles, se descuenta stock y se cambia estado del carrito. Debe ser una unidad logica.

Donde verlo:

- `CarritoService.java`

### En Service se trabaja con entities?

Si. El Service puede trabajar con entities porque esta dentro del backend y aplica reglas sobre el dominio. Pero al Controller conviene devolver DTOs, no entities directas.

### Donde validar stock no negativo?

En el backend, preferentemente en Service o con validaciones de DTO/entity. El frontend puede validar para experiencia de usuario, pero no alcanza porque se puede saltear.

En nuestro repo hoy se valida stock suficiente en checkout, pero una mejora seria validar stock/precio no negativos al crear o actualizar producto en `ProductoService` y/o con annotations en `ProductoRequest`.

### Que es DTO y por que usarlo?

DTO es Data Transfer Object. Define que datos entran y salen de la API.

Ventajas:

- No exponer password.
- No exponer relaciones internas.
- Evitar ciclos JSON.
- Dar contratos claros.

Donde verlo:

- `dto/`
- `DtoMapper.java`

### Entre que capas van DTOs?

En este repo:

- Controller recibe DTO request.
- Controller llama Service con DTO request.
- Service devuelve DTO response.
- Repository trabaja con entities.

### Por que Repository puede estar vacio?

Porque Spring Data JPA genera implementaciones automaticamente al extender `JpaRepository`.

Ejemplo:

- `ProductoRepository extends JpaRepository<Producto, Long>`

### Como hacer consultas especificas?

Con query methods por nombre:

- `findByEmail`
- `findByUsuarioId`
- `findByCarritoId`

O con `@Query` si la consulta es mas compleja.

## Persistencia

### Que es persistencia?

Guardar datos de forma duradera para que sobrevivan al fin de la ejecucion. En nuestro caso, guardar usuarios, productos, carritos y ordenes en MySQL.

### Que es ORM?

Object Relational Mapping. Permite mapear clases Java a tablas relacionales.

En Spring Boot usamos Hibernate como proveedor JPA.

Ventajas:

- Menos SQL manual.
- Relaciones con anotaciones.
- CRUD con repositories.
- Codigo mas orientado a objetos.

### Como se crean tablas con Spring Boot?

Con entities anotadas:

- `@Entity`
- `@Table`
- `@Id`
- `@GeneratedValue`
- `@ManyToOne`
- `@OneToMany`
- `@OneToOne`
- `@JoinColumn`

Y configuracion:

```properties
spring.jpa.hibernate.ddl-auto=update
```

### Que pasa si borro una categoria con productos asociados?

Si no hay cascade configurado, JPA no deberia borrar automaticamente productos. La base puede rechazar el delete por clave foranea, o quedar dependiendo de la configuracion del schema. Conceptualmente, no conviene borrar una categoria usada por productos sin una regla clara.

En nuestro repo no hay cascade en `Categoria.productos`, por lo tanto no esta disenado para borrar en cascada.

### Donde conviene poner la clave foranea de muchas imagenes de producto?

En la tabla del lado muchos: `fotos_producto` tiene `producto_id`.

En nuestro repo:

- `FotoProducto @ManyToOne Producto`
- `Producto @OneToMany(mappedBy = "producto")`

## Excepciones

### Que es una excepcion?

Es un evento que interrumpe el flujo normal del programa. En Java se usa para indicar errores o situaciones excepcionales.

### Que es una excepcion personalizada?

Una clase propia que extiende `RuntimeException` para expresar errores del dominio.

En nuestro repo:

- `ResourceNotFoundException`
- `BusinessException`
- `ConflictException`
- `InvalidCredentialsException`

### Flujo de excepcion en nuestro TPO

1. El Service detecta un problema.
2. Lanza una excepcion.
3. Spring la captura.
4. `GlobalExceptionHandler` tiene un `@ExceptionHandler`.
5. Devuelve `ResponseEntity<ApiResponse<...>>` con status y mensaje.

Ejemplo:

Si no existe un producto, `ProductoService` lanza `ResourceNotFoundException` y se responde 404.

### Por que no poner try-catch gigantes en Controller?

Porque mezcla responsabilidades. El Controller deberia atender HTTP; el manejo comun de errores debe estar centralizado. Con `GlobalExceptionHandler`, todos los endpoints responden errores de forma consistente.

### Validar stock en frontend o backend?

En backend. El frontend ayuda a la experiencia, pero no garantiza integridad. Un usuario podria llamar la API desde Postman o modificar la request. La regla fuerte debe estar en el Service.

En nuestro repo:

- `CarritoService.checkout` valida stock antes de descontar.

## Seguridad

### Que provee Spring Security?

Provee autenticacion, autorizacion, filtros, manejo de sesiones/stateless, password encoding, protection chain y handlers de error.

En nuestro repo:

- `SecurityConfig`
- `JwtAuthFilter`
- `CustomAuthenticationEntryPoint`
- `CustomAccessDeniedHandler`

### Autenticacion vs autorizacion

Autenticacion:

- Saber quien sos.
- Ejemplo: login con email/password.

Autorizacion:

- Saber que permisos tenes.
- Ejemplo: solo `ROLE_ADMIN` puede entrar a `/api/usuarios/**`.

### Como se protegen endpoints?

En `SecurityConfig.securityFilterChain`:

- `/api/auth/**`: publico.
- `/api/usuarios/**`: admin.
- `/api/productos/**`, `/api/carritos/**`, `/api/items-carrito/**`: autenticado.
- Todo lo demas: autenticado.

### Como sabe que algoritmo usar para password?

Por el bean:

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

Donde verlo:

- `SecurityConfig.java`

### Como se genera el token?

`JwtService.generateToken` usa:

- Username del `UserDetails`, en nuestro caso email.
- Fecha de emision.
- Fecha de expiracion.
- Clave secreta `app.jwt.secret`.
- Algoritmo HS256.

### Donde guardar la llave secreta?

En un proyecto real, en variables de entorno o un gestor de secretos. En nuestro TPO esta en `application.properties`, lo cual sirve para desarrollo/entrega academica, pero no seria ideal en produccion.

### Que enviar a endpoints protegidos?

Header:

```http
Authorization: Bearer <token>
```

### Al validar token, consulta base de datos?

En nuestro repo, si. `JwtAuthFilter` extrae el email del token y luego llama a `userDetailsService.loadUserByUsername(email)`, que consulta `UsuarioRepository.findByEmail`.

Matiz para explicar:

JWT permite no tener una tabla de sesiones. Pero nuestra implementacion igual consulta la base para cargar authorities y verificar que el usuario exista. Es una decision valida para mantener permisos actualizados.

### Por que se puede confiar en JWT?

Porque esta firmado. Si alguien cambia el contenido, la firma no valida. Ademas tiene expiracion.

### Por que BCrypt?

Porque no guarda password en texto plano. BCrypt agrega salt y costo computacional. Si alguien roba la base, ve hashes, no passwords reales.

## Despliegue y Docker

### Que es deployment?

Es poner la aplicacion disponible para usuarios en un ambiente ejecutable: servidor, contenedor, cloud, variables, base de datos, red y monitoreo.

### Servidor fisico vs VM vs contenedor

Servidor fisico:

- Menos flexible.
- Escalar requiere hardware.

VM:

- Aisla mejor.
- Pero cada VM trae sistema operativo completo.

Contenedor:

- Comparte kernel del host.
- Empaqueta app y dependencias.
- Consume menos recursos que VM.

### Que es Dockerfile?

Archivo que define como construir una imagen. Nuestro Dockerfile:

- Usa Maven + JDK para build.
- Copia `pom.xml` y `src`.
- Ejecuta `mvn -DskipTests package`.
- Usa imagen JRE para runtime.
- Expone puerto 8080.
- Ejecuta `java -jar`.

Instrucciones usadas:

- `FROM`
- `WORKDIR`
- `COPY`
- `RUN`
- `EXPOSE`
- `ENV`
- `ENTRYPOINT`

### Imagen vs contenedor

Imagen:

- Plantilla estatica.
- Contiene app y dependencias.

Contenedor:

- Instancia en ejecucion de una imagen.

### Docker Compose

Permite levantar varios servicios juntos. Nuestro `docker-compose.yml` levanta:

- `mysql-db`
- `backend`

El backend espera el healthcheck de MySQL y usa perfil `docker`.

### Docker y Cyber Monday

Con una imagen Docker del backend, se pueden levantar multiples contenedores identicos rapidamente y repartir carga con un balanceador. Esto facilita escalar horizontalmente ante alta demanda.

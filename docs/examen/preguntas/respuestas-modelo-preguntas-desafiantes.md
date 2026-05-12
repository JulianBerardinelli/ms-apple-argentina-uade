# Respuestas al modelo de preguntas desafiantes

Fuente base: `ModeloPreguntasDesafiantes.pdf`.

Este archivo responde las preguntas de estilo mas exigente y las relaciona con nuestro repo.

## API

### Statelessness y JWT: por que devolver token y no manejar sesion interna?

Una API REST stateless no guarda en memoria del servidor el estado de cada usuario logueado. Por eso, despues del login el backend devuelve un JWT al cliente. Ese token viaja en cada request protegida y permite identificar al usuario sin depender de una sesion guardada en el servidor.

Si el backend manejara sesiones internas, cada usuario logueado ocuparia memoria y ademas cada request tendria que llegar al servidor que conoce esa sesion, o habria que centralizar sesiones en otro sistema. Con JWT, cualquier instancia que tenga la clave de firma puede validar el token.

Relacion con nuestro repo:

- `AuthService.login` genera el token.
- `JwtService.generateToken` lo firma.
- `JwtAuthFilter` lo valida en cada request.
- `SecurityConfig` define que rutas requieren autenticacion.

Respuesta oral corta:

> REST es stateless, entonces el servidor no recuerda sesiones. Devuelve un JWT para que el cliente demuestre su identidad en cada request. Asi evitamos sesiones en memoria y facilitamos escalar horizontalmente.

### Stateless y escalabilidad horizontal

La escalabilidad horizontal consiste en agregar mas instancias del backend para repartir carga. Si el sistema es stateless, cualquier instancia puede atender cualquier request porque la identidad viaja en el token.

En un escenario con muchas peticiones:

- Instancia A puede atender login.
- Instancia B puede atender productos.
- Instancia C puede atender checkout.

Mientras todas validen el JWT con la misma clave, no necesitan compartir memoria de sesion.

En nuestro TPO:

- La app es stateless por `SessionCreationPolicy.STATELESS`.
- No hay sesiones HTTP.
- El token viaja por `Authorization: Bearer`.

Donde verlo:

- `SecurityConfig.securityFilterChain`

### Cyber Monday: JSON + stateless

Con 100.000 usuarios concurrentes, dos factores ayudan:

1. JSON es liviano comparado con formatos mas pesados como XML/SOAP.
2. Stateless evita guardar sesion por usuario en memoria del backend.

Eso reduce:

- Memoria por usuario.
- Acoplamiento entre usuario e instancia.
- Trafico innecesario.
- Complejidad del balanceo.

En nuestro repo, el backend responde JSON desde controllers y DTOs. El cliente se autentica con JWT y puede pegarle a cualquier instancia si el despliegue escala.

## Spring Boot

### Error `DataSource health indicator: UNKNOWN`: que revisar?

Revisaria estas secciones:

1. `application.properties`
   - URL JDBC correcta.
   - Usuario/password.
   - Driver MySQL.
   - Base `ecommerce_db4`.

2. `application-docker.properties`
   - Si corre en Docker, el host no debe ser `localhost`, debe ser `mysql-db`.

3. `docker-compose.yml`
   - Servicio `mysql-db`.
   - Variables `MYSQL_ROOT_PASSWORD` y `MYSQL_DATABASE`.
   - Puerto `3306`.
   - Healthcheck.
   - `SPRING_PROFILES_ACTIVE=docker` en backend.

4. `pom.xml`
   - Dependencia `mysql-connector-j`.
   - Dependencia actuator si el error viene de health.

5. Estado real de MySQL
   - Contenedor levantado.
   - Puerto disponible.
   - Credenciales correctas.

En nuestro repo:

- Local: `jdbc:mysql://localhost:3306/ecommerce_db4...`
- Docker: `jdbc:mysql://mysql-db:3306/ecommerce_db4...`

### Que componente expone endpoints?

Los endpoints los exponen los Controllers, ubicados en la capa `controller`.

Anotaciones principales:

- `@RestController`
- `@RequestMapping`
- `@GetMapping`
- `@PostMapping`
- `@PutMapping`
- `@DeleteMapping`
- `@PathVariable`
- `@RequestBody`

Spring Boot arranca Tomcat embebido y Spring MVC registra las rutas.

Ejemplos:

- `ProductoController`
- `AuthController`
- `CarritoController`

## Capas

### Riesgo de devolver `Usuario` entity con password y rol

Si el Controller devuelve directamente la entity `Usuario`, se puede filtrar informacion sensible:

- Password hasheada.
- Rol del usuario.
- Relaciones internas.
- Estructura de la base.

Aunque el password este hasheado, no conviene exponerlo. Un atacante podria usarlo para ataques offline.

DTO como firewall:

- `UsuarioResponse` no tiene `password`.
- `DtoMapper.toUsuarioResponse` solo copia campos seguros.

Donde verlo:

- `Usuario.java`
- `UsuarioResponse.java`
- `DtoMapper.java`

Respuesta oral:

> El DTO actua como una frontera: la entity es el modelo interno y el DTO es el contrato publico. No todo lo interno debe salir por la API.

### Compra con stock, factura y cobro: por que `@Transactional` en Service?

Una compra tiene pasos que deben comportarse como unidad:

1. Descontar stock.
2. Generar comprobante/orden.
3. Cobrar.

Si el cobro falla despues de descontar stock, los datos quedan inconsistentes. Con `@Transactional`, si ocurre una excepcion durante el proceso, la transaccion puede revertirse.

Por que en Service:

- El Service representa el caso de uso completo.
- El Controller no debe coordinar reglas de negocio.
- Tres llamadas separadas desde Controller no comparten una transaccion clara.

En nuestro repo:

`CarritoService.checkout` hace algo parecido:

- Valida carrito.
- Valida stock.
- Crea orden.
- Crea detalles.
- Descuenta stock.
- Cambia estado del carrito.

La clase tiene `@Transactional`.

### Consulta SQL compleja en Controller con entities

Problemas:

- Mezcla HTTP con persistencia.
- Dificulta tests unitarios.
- Duplica logica si otro endpoint necesita lo mismo.
- Acopla el Controller a detalles de base.
- Hace mas dificil cambiar la consulta.

Forma correcta:

- Controller delega.
- Service define caso de uso.
- Repository encapsula consulta.
- DTO define salida.

En nuestro repo:

- `OrdenCompraController` no consulta SQL.
- `OrdenCompraService` usa `OrdenCompraRepository.findByUsuarioId`.

### Saltarse Service y llamar Repository desde Controller

Al principio puede parecer rapido, pero genera riesgos:

- Las reglas quedan distribuidas en controllers.
- Checkout y stock serian dificiles de mantener.
- Se pierde una ubicacion central para transacciones.
- Tests se vuelven mas fragiles.
- Cambiar reglas de negocio rompe endpoints.

En un marketplace, cuando aparecen promociones, cupones, stock reservado, pagos o auditoria, el Service es indispensable.

## Persistencia

### `@OneToMany` y `FetchType.LAZY` en alta concurrencia

`LAZY` significa que una relacion se carga solo cuando se necesita. Si una entidad tiene muchos hijos y usamos carga ansiosa, cada consulta puede traer demasiada informacion.

En Cyber Monday, si cada pedido trae automaticamente todos sus items, productos, fotos y relaciones, se saturan:

- Memoria RAM.
- CPU.
- Base de datos.
- Red.

En nuestro repo hay relaciones como:

- `Producto` -> `fotos`
- `Carrito` -> `items`
- `OrdenCompra` -> `detalles`

JPA por defecto suele usar LAZY en `@OneToMany`, lo cual ayuda. Pero hay que tener cuidado con los DTOs: `DtoMapper` puede disparar carga de relaciones si convierte listas completas.

Respuesta madura:

> LAZY evita traer grafos completos de objetos cuando no hacen falta. En alta concurrencia, eso reduce consumo de memoria y consultas innecesarias.

## Excepciones

### Por que es mala practica llenar Controllers con try-catch?

Porque el Controller debe encargarse de HTTP, no de todas las variantes de error del negocio y la base. Si cada Controller tiene try-catch gigante:

- Hay codigo repetido.
- Cada endpoint puede responder distinto.
- Es dificil mantener.
- Es dificil testear.
- Se viola separacion de responsabilidades.

En nuestro repo:

- Los Services lanzan excepciones.
- `GlobalExceptionHandler` centraliza respuestas.
- `ApiResponse` mantiene formato consistente.

Donde verlo:

- `GlobalExceptionHandler.java`
- `ApiResponse.java`
- `BusinessException.java`
- `ResourceNotFoundException.java`

### Email duplicado: null o excepcion personalizada?

Conviene que el Service lance una excepcion personalizada o semantica. En nuestro repo usamos `ConflictException` cuando el email ya existe.

Por que no `null`:

- `null` no explica el problema.
- Puede causar `NullPointerException`.
- Obliga al Controller a adivinar.
- No comunica bien el HTTP status.

En nuestro repo:

```java
if (usuarioRepository.existsByEmail(request.getEmail())) {
    throw new ConflictException("El email ya esta registrado");
}
```

`GlobalExceptionHandler` responde 409 Conflict.

### Validar stock en Frontend o Backend?

En backend. El frontend puede mostrar advertencias, pero no puede garantizar integridad porque:

- Se puede saltear usando Postman.
- Puede haber concurrencia.
- El stock real esta en la base.
- El cliente no es confiable.

En nuestro repo:

- `CarritoService.checkout` valida stock contra `Producto.stock`.

Mejora posible:

- Validar tambien al crear item para feedback temprano.
- Mantener la validacion fuerte en checkout.

## Seguridad

### Autenticacion vs autorizacion en nuestro TPO

Autenticacion:

- Determina quien es el usuario.
- Ocurre en login.
- Usa `AuthenticationManager`, `CustomUserDetailsService` y BCrypt.

Autorizacion:

- Determina que puede hacer.
- Ocurre cuando pide un endpoint.
- Usa roles/authorities y reglas de `SecurityConfig`.

Ejemplo:

- Login correcto: autenticado.
- Entrar a `/api/usuarios`: autorizado solo si tiene `ROLE_ADMIN`.

### Como JWT permite confiar sin base de sesiones?

El JWT esta firmado. El backend valida:

- Que la firma sea correcta.
- Que no este expirado.
- Que el subject coincida con el usuario.

No necesita una tabla de sesiones porque el token trae la identidad firmada. En nuestro repo igual se consulta la base para cargar el usuario y sus authorities, pero no se guarda una sesion del usuario.

Donde verlo:

- `JwtService.extractUsername`
- `JwtService.isTokenValid`
- `JwtAuthFilter`
- `CustomUserDetailsService`

### Riesgos de exponer entities

Riesgos:

- Fuga de password hasheado.
- Fuga de roles.
- Fuga de relaciones internas.
- Ciclos JSON.
- Acoplar API al schema.
- Exponer campos que despues no se pueden cambiar sin romper clientes.

DTOs previenen eso porque publican solo lo necesario.

### MITM, BCrypt y HTTPS

Si un atacante intercepta trafico:

- BCrypt protege passwords almacenadas en la base, no protege el trafico.
- Si el login viaja sin HTTPS, el atacante podria capturar email/password o token.
- HTTPS cifra la comunicacion entre cliente y backend.

Por eso:

- BCrypt es obligatorio para proteger passwords en reposo.
- HTTPS es obligatorio para proteger credenciales y JWT en transito.

JWT y HTTPS:

Un token interceptado puede usarse hasta que expire. Por eso en produccion se exige HTTPS y tokens con expiracion razonable.

### JWT como QR de UADE

El QR acredita identidad y permiso de acceso. El JWT tambien:

- Identifica al usuario.
- Tiene expiracion.
- Esta firmado.
- Se valida al entrar a un recurso.

La analogia sirve porque ambos son credenciales portables: quien los presenta puede acceder si son validos.

## Docker

### Docker y "en mi maquina funciona"

Docker empaqueta la aplicacion y su ambiente de ejecucion en una imagen. Eso reduce diferencias entre desarrollo y produccion.

En nuestro repo:

El `Dockerfile` define:

- Imagen Maven con JDK 17 para compilar.
- Imagen JRE 17 para ejecutar.
- Copia del `.jar`.
- Puerto 8080.
- Comando `java -jar`.

`docker-compose.yml` define:

- Backend.
- MySQL.
- Variables.
- Red interna.
- Healthcheck.

### Por que contenedores son mas eficientes que VMs?

VM:

- Cada instancia tiene sistema operativo completo.
- Mas consumo de RAM y disco.
- Arranque mas lento.

Contenedor:

- Comparte kernel del host.
- Aisla procesos.
- Arranca rapido.
- Consume menos recursos.

Esto permite ejecutar mas instancias de una API en la misma infraestructura.

### Docker y escalamiento horizontal

Una imagen Docker es estatica y reproducible. Desde esa imagen se pueden levantar muchos contenedores identicos.

Cyber Monday:

Si pasamos de 10.000 a 100.000 peticiones, se levantan mas contenedores backend desde la misma imagen y se balancea la carga. Como la API es stateless, cualquier contenedor puede atender cualquier request con JWT.

Relacion clave:

- Docker facilita replicar instancias.
- Stateless permite que esas instancias sean intercambiables.
- JWT evita sesiones pegadas a un nodo.


# 🛍️ E-Commerce Apple Argentina — Backend API

API REST desarrollada con **Spring Boot** para un sistema de e-commerce de productos Apple.  
Trabajo Práctico Obligatorio — Aplicaciones Interactivas — Primer Cuatrimestre 2026 — UADE.

---

## 👥 Equipo

**Grupo Nº 7**

| Integrante | Legajo |
|------------|--------|
| Aguado, Augusto | 1195486 |
| Berardinelli, Julián | 1174139 |
| Juliano, Pedro Gastón | 1203000 |
| Mansilla, Marcelo | 1202504 |
| Sosa, Christian | 1202494 |

---

## 🛠️ Tecnologías utilizadas

- **Java 17**
- **Spring Boot 4.x**
- **Spring Data JPA / Hibernate**
- **Spring Security + JWT**
- **MySQL 8.x**
- **Lombok**
- **Maven**

---

## ⚙️ Configuración de la Base de Datos

El archivo de configuración se encuentra en:

```
src/main/resources/application.properties
```

---

### Opción A — macOS con Docker + MySQL Workbench (configuración actual)

Esta es la configuración que usamos durante el desarrollo del proyecto.

#### 1. Levantar el contenedor de MySQL con Docker

```bash
docker run --name mysql-ecommerce \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=ecommerce_db4 \
  -p 3306:3306 \
  -d mysql:8.0
```

> Esto crea un contenedor MySQL con:
>
> - Usuario: `root`
> - Contraseña: `root`
> - Base: `ecommerce_db4`
> - Puerto: `3306` (mapeado al host)

#### 1.b Alternativa recomendada: Docker Compose

También podés levantar **MySQL y el backend** con el archivo `docker-compose.yml` del proyecto (incluye servicio `backend`):

```bash
docker compose up -d --build
```

Esto crea/levanta los servicios `mysql-db` y `backend` y mantiene los datos en un volumen persistente.

#### 2. Verificar que el contenedor esté corriendo

```bash
docker ps
```

Deberías ver `mysql-ecommerce` en la lista con estado `Up`.

#### 3. Conectar MySQL Workbench

Abrí MySQL Workbench y creá una nueva conexión con estos datos:

| Campo           | Valor              |
| --------------- | ------------------ |
| Connection Name | `ecommerce-docker` |
| Hostname        | `127.0.0.1`        |
| Port            | `3306`             |
| Username        | `root`             |
| Password        | `root`             |

#### 4. Configuración en `application.properties` (ya configurado así)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db4?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> ✅ La base de datos `ecommerce_db4` se crea automáticamente gracias al parámetro `createDatabaseIfNotExist=true`.

---

### 🪟 Opción B — Windows con XAMPP

Si usás Windows con XAMPP, seguí estos pasos:

#### 1. Iniciar XAMPP

Abrí el **XAMPP Control Panel** y hacé clic en **Start** en el módulo **MySQL**.

> XAMPP levanta MySQL en el puerto `3306` con usuario `root` y **sin contraseña** por defecto.

#### 2. Crear la base de datos (opcional, lo hace Spring automáticamente)

Podés abrir **phpMyAdmin** (`http://localhost/phpmyadmin`) y crear la base de datos manualmente:

```sql
CREATE DATABASE ecommerce_db4;
```

O dejar que Spring Boot la cree sola al iniciar la app.

#### 3. Modificar `application.properties`

Reemplazá la sección de datasource con:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db4?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> ⚠️ La diferencia clave con respecto a macOS/Docker es que en XAMPP la contraseña es vacía (`password=`).  
> Si configuraste una contraseña para root en XAMPP, ponela ahí.

---

## 🚀 Pasos para levantar el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/e-commerce.git
cd e-commerce
```

### 2. Configurar la base de datos

Elegí la opción A (Docker/Compose) o B (XAMPP) según tu entorno.

Si usás Docker/Compose, el backend toma la configuración de `application-docker.properties` mediante el perfil `docker` (no hace falta editar `application.properties`).

### 3. Levantar la aplicación

```bash
./mvnw spring-boot:run
```

O desde tu IDE (IntelliJ / Eclipse / VS Code) corré la clase `ECommerceApplication.java`.

### 4. Verificar que levantó correctamente

Abrí en el navegador o en Postman:

```
GET http://localhost:8080/api/productos
```

Deberías recibir un array JSON (vacío si no cargaste el seed todavía).

---

## 🔐 Seguridad (Spring Security + JWT)

La API protege endpoints con JWT Bearer Token.

### Endpoints de autenticación

- `POST /api/auth/register` -> registra usuario y devuelve token.
- `POST /api/auth/login` -> login y devuelve token.

### Uso del token

En endpoints protegidos enviá:

```http
Authorization: Bearer <tu_token_jwt>
```

### Reglas de acceso

- `/api/auth/**` -> público
- `/api/usuarios/**` -> solo `ROLE_ADMIN`
- `/api/productos/**`, `/api/carritos/**`, `/api/items-carrito/**` -> requiere autenticación

### Cómo probar endpoints protegidos (JWT)
La demo recomendada es: login -> token -> request con `Authorization`.

1. Login (obtiene el JWT):
```http
POST /api/auth/login
Content-Type: application/json
{
  "email": "admin@apple-ar.com",
  "password": "password"
}
```

2. Endpoint protegido (enviar token):
```http
Authorization: Bearer <tu_token_jwt>
```

Ejemplos:
- `GET /api/productos` (requiere autenticación)
- `GET /api/usuarios` (requiere `ROLE_ADMIN`)

---

## 🌱 Cargar datos de prueba (Seed)

El proyecto carga datos de prueba automáticamente al iniciar usando:
- `src/main/resources/data_seed_startup.sql`

> En Docker/Compose, si querés volver a sembrar desde cero, frená el compose y eliminá el volumen:
> `docker compose down -v` y luego `docker compose up -d --build`.

El seed incluye:

- 3 usuarios de prueba
- 6 categorías (Mac, iPhone, iPad, Apple Watch, AirPods, Accesorios)
- 20 productos del catálogo Apple 2025
- Fotos de productos, carritos, items y una orden de compra completada

Credenciales seed (password en texto plano: `password`):

- Admin: `admin@apple-ar.com`
- Usuario: `carlos@email.com`
- Usuario: `lucia@email.com`

---

## 🧪 Postman (colección completa)

Se incluye una colección con endpoints y pruebas automáticas:

```text
postman/ms-apple-argentina-uade.postman_collection.json
```

Incluye:

- flujo de auth (register/login),
- pruebas de catálogo, carrito, checkout, órdenes y detalles,
- tests negativos comunes (401/404),
- guardado automático de `token` e IDs para encadenar requests.

Importación:

1. Abrir Postman -> **Import**
2. Seleccionar `postman/ms-apple-argentina-uade.postman_collection.json`
3. Verificar variable `baseUrl = http://localhost:8080`

---

## 📚 Swagger / OpenAPI

La documentación interactiva de endpoints queda disponible en:

- `http://localhost:8080/swagger-ui/index.html`

JSON OpenAPI:

- `http://localhost:8080/v3/api-docs`

Swagger quedó habilitado sin token para facilitar pruebas y exploración de la API.

> Importante: varios endpoints requieren JWT. Si probás desde Swagger y recibís `401/403`, tenés que enviar:
> `Authorization: Bearer <tu_token_jwt>`

---

## 📌 Endpoints principales

| Método | URL                           | Descripción                         |
| ------ | ----------------------------- | ----------------------------------- |
| GET    | `/api/productos`              | Listar todos los productos          |
| GET    | `/api/productos/{id}`         | Obtener producto por ID             |
| POST   | `/api/productos`              | Crear producto                      |
| PUT    | `/api/productos/{id}`         | Actualizar producto                 |
| DELETE | `/api/productos/{id}`         | Eliminar producto                   |
| GET    | `/api/categorias`             | Listar categorías                   |
| POST   | `/api/auth/register`          | Registro de usuario                 |
| POST   | `/api/auth/login`             | Login y token JWT                   |
| POST   | `/api/carritos`               | Crear carrito                       |
| POST   | `/api/carritos/{id}/checkout` | Realizar checkout (descuenta stock) |
| GET    | `/api/ordenes/usuario/{id}`   | Ver órdenes de un usuario           |

---

## 🐳 Docker Compose (backend + MySQL) - Recomendado para entrega

Levantar todo (backend + DB):
```bash
docker compose up -d --build
```

Endpoints:
- Backend: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui/index.html`
- Health: `http://localhost:8080/actuator/health`

Re-seed (volver a cargar datos) si hace falta:
```bash
docker compose down -v
docker compose up -d --build
```


## 📊 Reporte de cobertura de tests (JaCoCo)

Generar tests + reporte:

```bash
./mvnw test
```

En Windows PowerShell:

```powershell
.\mvnw.cmd test
```

El reporte HTML se genera en:

```text
target/site/jacoco/index.html
```

Para revisarlo, abrí ese archivo en el navegador.  
Ahí vas a ver cobertura por **líneas, ramas, métodos y clases**.
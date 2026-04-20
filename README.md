# 🛍️ E-Commerce Apple Argentina — Backend API (PROYECTO DE PRUEBA)

API REST desarrollada con **Spring Boot** para un sistema de e-commerce de productos Apple (PROYECTO DE PRUEBA).  
Trabajo Práctico Obligatorio — Aplicaciones Interactivas — UADE 1C 2026.

---

## 🛠️ Tecnologías utilizadas

- **Java 17**
- **Spring Boot 4.x**
- **Spring Data JPA / Hibernate**
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

### 🍎 Opción A — macOS con Docker + MySQL Workbench (configuración actual)

Esta es la configuración que usamos durante el desarrollo del proyecto.

#### 1. Levantar el contenedor de MySQL con Docker

```bash
docker run --name mysql-ecommerce \
  -e MYSQL_ROOT_PASSWORD=root \
  -p 3306:3306 \
  -d mysql:8.0
```

> Esto crea un contenedor MySQL con:
>
> - Usuario: `root`
> - Contraseña: `root`
> - Puerto: `3306` (mapeado al host)

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

Elegí la opción A (Docker) o B (XAMPP) según tu entorno y editá `application.properties`.

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

## 🌱 Cargar datos de prueba (Seed)

Una vez que la app levantó al menos una vez (para que Hibernate cree las tablas):

1. **Detener la app**
2. Abrir el archivo `src/main/resources/data_seed.sql` en MySQL Workbench o phpMyAdmin
3. Ejecutar el script completo
4. **Volver a levantar la app**

El seed incluye:

- 3 usuarios de prueba
- 6 categorías (Mac, iPhone, iPad, Apple Watch, AirPods, Accesorios)
- 20 productos del catálogo Apple 2025
- Fotos de productos, carritos, items y una orden de compra completada

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
| POST   | `/api/carritos`               | Crear carrito                       |
| POST   | `/api/carritos/{id}/checkout` | Realizar checkout (descuenta stock) |
| GET    | `/api/ordenes/usuario/{id}`   | Ver órdenes de un usuario           |

Para la lista completa de endpoints ver la documentación del proyecto.

---

## 👥 Equipo

Trabajo Práctico Obligatorio — Aplicaciones Interactivas  
Primer Cuatrimestre 2026 — UADE

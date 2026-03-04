# Coworking Access Control API

API REST para el control de ingreso y salida de personas en múltiples sedes de coworking.
Incluye autenticación JWT con expiración de 6 horas, control de roles (ADMIN y OPERADOR),
validación de capacidad por sede, facturación automática por tiempo de permanencia y
generación de cupones de fidelidad.

---

## Repositorio complementario

Este proyecto trabaja en conjunto con un microservicio de notificaciones.
Para contar con todas las funcionalidades, debes clonar y ejecutar también:

```
https://github.com/DIEGUS15/notification_service.git
```

Sin este servicio la API funciona, pero las notificaciones de cupones de fidelidad
no se enviarán. Ambos proyectos deben correr simultáneamente:

- API principal: puerto `8080`
- Microservicio de notificaciones: puerto `8081`

---

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.5
- Spring Security con JWT
- PostgreSQL
- Springdoc OpenAPI (Swagger UI)
- Maven

---

## Requisitos previos

Antes de ejecutar el proyecto, asegurate de tener instalado:

- Java 21
- Maven
- PostgreSQL

---

## Paso 1 — Crear la base de datos

Abre tu cliente de PostgreSQL (pgAdmin, DBeaver o psql) y ejecuta:

```sql
CREATE DATABASE coworkings;
```

La aplicación crea las tablas automáticamente al iniciar por primera vez.

---

## Paso 2 — Clonar el repositorio

```bash
git clone https://github.com/DIEGUS15/coworkings.git
cd coworkings
```

---

## Paso 3 — Configurar las credenciales de la base de datos

Por defecto la aplicación usa estas credenciales:

| Parametro  | Valor por defecto                              |
|------------|------------------------------------------------|
| URL        | jdbc:postgresql://localhost:5432/coworkings    |
| Usuario    | postgres                                       |
| Contrasena | Ab12345678.                                    |

Si tus credenciales de PostgreSQL son diferentes, puedes sobreescribirlas
definiendo estas variables de entorno antes de ejecutar:

```
DB_URL=jdbc:postgresql://localhost:5432/coworkings
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contrasena
```

---

## Paso 4 — Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

En Windows:

```bash
mvnw.cmd spring-boot:run
```

La aplicación estara disponible en: `http://localhost:8080`

---

## Documentacion interactiva — Swagger UI

Con la aplicacion corriendo, accede desde el navegador a:

```
http://localhost:8080/swagger-ui/index.html
```

Desde ahi puedes ver y probar todos los endpoints sin necesidad de Postman.

Para autenticarte en Swagger:

1. Ejecuta el endpoint `POST /api/auth/login` con las credenciales de admin.
2. Copia el valor del campo `token` de la respuesta.
3. Haz clic en el boton **Authorize** en la parte superior derecha.
4. Ingresa el token en el formato: `Bearer <token>`
5. Confirma y todos los endpoints quedaran autenticados.

---

## Usuario administrador por defecto

Al iniciar la aplicacion por primera vez se crea automaticamente un usuario ADMIN:

| Campo      | Valor           |
|------------|-----------------|
| Email      | admin@mail.com  |
| Contrasena | admin           |
| Rol        | ADMIN           |

---

## Coleccion Postman

Junto con este repositorio se entrega una coleccion de Postman documentada
con todos los endpoints organizados por carpetas, ejemplos de body y
descripcion de cada operacion. Importala en Postman para probar la API
de forma rapida y ordenada.

---

## Endpoints disponibles

### Autenticacion

| Metodo | Endpoint              | Acceso  | Descripcion                     |
|--------|-----------------------|---------|---------------------------------|
| POST   | /api/auth/login       | Publico | Obtener token JWT               |
| POST   | /api/auth/register    | ADMIN   | Crear usuario con rol OPERADOR  |

### Sedes

| Metodo | Endpoint              | Acceso | Descripcion                  |
|--------|-----------------------|--------|------------------------------|
| POST   | /api/sedes            | ADMIN  | Crear sede                   |
| GET    | /api/sedes            | ADMIN  | Listar todas las sedes       |
| GET    | /api/sedes/{id}       | ADMIN  | Obtener sede por ID          |
| PUT    | /api/sedes/{id}       | ADMIN  | Actualizar sede              |
| PATCH  | /api/sedes/{id}/toggle| ADMIN  | Activar o desactivar sede    |

### Ingreso y Salida

| Metodo | Endpoint                          | Acceso   | Descripcion                         |
|--------|-----------------------------------|----------|-------------------------------------|
| POST   | /api/registros/ingreso            | OPERADOR | Registrar ingreso de una persona    |
| POST   | /api/registros/salida             | OPERADOR | Registrar salida y calcular cobro   |
| GET    | /api/registros/sedes/{id}/activos | OPERADOR | Ver personas dentro de la sede      |

### Indicadores

| Metodo | Endpoint                                     | Acceso            |
|--------|----------------------------------------------|-------------------|
| GET    | /api/indicadores/top-personas                | ADMIN             |
| GET    | /api/indicadores/top-personas/sedes/{id}     | ADMIN, OPERADOR   |
| GET    | /api/indicadores/top-personas/mis-sedes      | OPERADOR          |
| GET    | /api/indicadores/primeras-visitas            | ADMIN             |
| GET    | /api/indicadores/primeras-visitas/mis-sedes  | OPERADOR          |
| GET    | /api/indicadores/ingresos-economicos/sedes/{id} | OPERADOR       |
| GET    | /api/indicadores/top-operadores-semana       | ADMIN             |
| GET    | /api/indicadores/top-sedes-semana            | ADMIN             |

### Cupones de fidelidad

| Metodo | Endpoint                   | Acceso          | Descripcion              |
|--------|----------------------------|-----------------|--------------------------|
| GET    | /api/cupones/{codigo}      | ADMIN, OPERADOR | Consultar cupon          |
| POST   | /api/cupones/{codigo}/redimir | ADMIN, OPERADOR | Redimir cupon         |


##NOTA
Cuando un usuario acumule 20 horas de haber ingresado a alguna sede, automaticamente
se genera un cupón, el código de este cupón estará disponible y podrá ser visualizados
a través de los logs que se generan en la terminal del proyecto.

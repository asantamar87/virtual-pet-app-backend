# 🐾 Virtual Pet API - IT Academy Case Study

API REST para la gestión de mascotas virtuales, desarrollada con **Spring Boot 3**, siguiendo principios de **Arquitectura Limpia** y seguridad **JWT**.

## 🚀 Tecnologías Utilizadas
* **Java 17** & **Spring Boot 3**
* **Spring Security** con **JWT** (Json Web Token)
* **JPA / Hibernate** con base de datos H2 (en memoria)
* **MapStruct** para mapeo de DTOs
* **Lombok** para reducir código repetitivo
* **SLF4J** para Logging profesional
* **Spring Cache** para optimización de rendimiento

## 🏗️ Arquitectura
El proyecto sigue una estructura de capas desacoplada:
1. **Controller**: Endpoints REST con validación de entrada.
2. **Service**: Lógica de negocio, gestión de caché y logs.
3. **Repository**: Acceso a datos mediante Spring Data JPA.
4. **Security**: Filtros JWT, UserPrincipal (Adaptador) y configuración de CORS para React.

## 🛠️ Instalación y Ejecución
1. Clonar el repositorio.
2. Asegurarse de tener instalado **JDK 17** y **Maven**.
3. Ejecutar `./mvnw spring-boot:run`.
4. La API estará disponible en `http://localhost:8080`.

## 🔑 Endpoints Principales
### Autenticación (Público)
* `POST /api/auth/register` - Registro de usuario.
* `POST /api/auth/login` - Obtención de token JWT.

### Mascotas (Privado - Requiere Bearer Token)
* `GET /api/pets` - Lista las mascotas del usuario (o todas si es ADMIN).
* `POST /api/pets` - Crea una nueva mascota.
* `PATCH /api/pets/{id}/feed` - Alimenta a la mascota (+energía).
* `PATCH /api/pets/{id}/play` - Juega con la mascota (+felicidad, -energía).

## 📈 Características del Nivell 2
- **Logging**: Implementado con SLF4J en controladores, servicios y seguridad para trazabilidad total.
- **Caching**: Implementado con `@EnableCaching` para optimizar las consultas repetitivas de mascotas.
- **CORS**: Configurado para permitir conexiones desde `http://localhost:3000` (React).
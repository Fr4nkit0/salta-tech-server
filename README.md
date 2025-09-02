# 🚀 OtherCode Server

Sistema de gestión empresarial multi-organizacional con autenticación JWT y control de acceso basado en roles.

## 📋 Descripción

OtherCode Server es una aplicación Spring Boot que proporciona una API REST para la gestión de organizaciones, sucursales, clientes y usuarios con un sistema de autenticación y autorización robusto basado en JWT.

### 🎯 Características Principales

- **Multi-organizacional**: Soporte para múltiples organizaciones con aislamiento de datos
- **Multi-sucursal**: Gestión de sucursales por organización
- **Autenticación JWT**: Sistema de autenticación seguro con tokens JWT
- **Control de Acceso (RBAC)**: Roles y permisos granulares por organización
- **Auditoría**: Trazabilidad completa de cambios 
- **API REST**: Endpoints RESTful bien documentados
- **Base de Datos PostgreSQL**: Persistencia robusta con Flyway para migraciones

## 🏗️ Arquitectura

El proyecto sigue una arquitectura hexagonal (Clean Architecture) con las siguientes capas:

```
src/main/java/com/othercode/
├── auth/             # Autenticación y autorización
├── branch/           # Gestión de sucursales
├── customer/         # Gestión de clientes
├── organization/     # Gestión de organizaciones
├── user/             # Gestión de usuarios
├── address/          # Gestión de direcciones
└── common/           # Componentes compartidos
```

## 🛠️ Tecnologías

- **Java 22** - Lenguaje de programación
- **Spring Boot 3.5.0** - Framework principal
- **Spring Security** - Seguridad y autenticación
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL** - Base de datos
- **Flyway** - Migraciones de base de datos
- **JWT** - Tokens de autenticación
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias

## 📋 Prerrequisitos

### Software Requerido

- **Java 22** o superior
- **Maven 3.6+** (o usar el wrapper incluido)
- **PostgreSQL 12+**
- **Docker** (opcional, para desarrollo)

### Verificar Java

```bash
java -version
# Debe mostrar: openjdk version "22.x.x"
```

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/OtherCde/project-othercode-server.git
cd project-othercode-server
```

### 2. Configurar Base de Datos

#### Opción A: Instalar PostgreSQL con Docker (si no tienes PostgreSQL instalado)
```bash
# Crear archivo .env con las variables de entorno
POSTGRES_USER=postgres
POSTGRES_PASSWORD=root
# Levantar PostgreSQL
docker-compose up -d
```
#### Opción B: PostgreSQL Local

1. Crear base de datos: `othercode_db`
2. Configurar usuario y contraseña

### 3. Configurar Variables de Entorno Sin IntelliJ IDEA

Crear archivo `.env.sh` en la raíz del proyecto:

```bash
# Base de Datos
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=othercode_db
export DB_USERNAME=postgres
export DB_PASSWORD=root
# Servidor
export SERVER_PORT=8080
# JWT
export JWT_SECRET_KEY="jJ4Gs6z0vWZlJMQW6QzK6AzyvSvhk1C2H1Xt1b+bh9GqB0HnEwNfX1pWxW4W2PjN"
export JWT_ACCESS_EXPIRATION_MINUTES=15
export JWT_REFRESH_EXPIRATION_DAYS=1
echo "Variables de entorno cargadas correctamente"
```
Ejecutar en la terminal donde se encuentra el proyecto:

```bash
 source .env.sh
 ```
### 4. Ejecutar la Aplicación

```bash
# Usando Maven wrapper
./mvnw spring-boot:run

# O usando Maven instalado
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080/api/v1`


## 🔐 Sistema de Autenticación

### Convención de URLS

Las URLS deben seguir el formato:
```
/api/v1/{organizationSlug}/...
```

Ejemplo:
```
GET /api/v1/flashtech/auth/authenticate
```

### Flujo de Autenticación

1. **Login**: `POST /api/v1/{organizationSlug}/auth/authenticate`
2. **Refresh**: `POST /api/v1/{organizationSlug}/auth/refresh`
3. **Logout**: `POST /api/v1/{organizationSlug}/auth/logout`


### Formato de Autenticación

```json
{
  "email": "usuario@empresa.com",
  "password": "contraseña"
}
```

## 🔧 Configuración por Perfiles

### Desarrollo (`application-dev.yml`)

- Logs detallados (DEBUG)
- SQL visible en consola
- DDL auto: update
- Configuración local

### Producción (`application-prod.yml`)

- Logs mínimos (INFO)
- SQL oculto
- DDL auto: validate
- Variables de entorno requeridas

## 🧪 Testing

```bash
# Ejecutar tests
./mvnw test

# Ejecutar tests con cobertura
./mvnw test jacoco:report
```

## 📦 Build y Deployment

### Build del JAR

```bash
./mvnw clean package
```

### Ejecutar JAR

```bash
java -jar target/othercode-0.0.1-SNAPSHOT.jar
```

### Variables de Entorno para Produccion

```bash
export DB_HOST=your-db-host
export DB_PORT=5432
export DB_NAME=othercode_db
export DB_USERNAME=your-username
export DB_PASSWORD=your-secure-password
export JWT_SECRET_KEY=your-secure-jwt-secret
export JWT_ACCESS_EXPIRATION_MINUTES=15
export JWT_REFRESH_EXPIRATION_DAYS=1
export SERVER_PORT=8080
```

## 🔍 Logs y Monitoreo

### Niveles de Log

- **DEBUG**: Información detallada para desarrollo
- **INFO**: Información general de la aplicación
- **WARN**: Advertencias
- **ERROR**: Errores que requieren atención

### Logs Habilitados

- SQL queries (desarrollo)
- Autenticación y autorización
- Operaciones de negocio
- Errores y excepciones

## 🚨 Manejo de Errores

El sistema incluye un manejo global de excepciones con:

- **ResourceNotFoundException**: Recurso no encontrado
- **NoDataFoundException**: Sin datos disponibles
- **CustomerNotFoundException**: Cliente no encontrado
- **OrganizationNotFoundException**: Organización no encontrada
- **TokenNotFoundException**: Token JWT no encontrado

## 🔒 Seguridad

### Características de Seguridad

- **JWT Tokens**: Autenticación stateless
- **Password Encoding**: Contraseñas encriptadas con BCrypt
- **CORS**: Configuración de Cross-Origin
- **CSRF**: Protección CSRF habilitada
- **Auditoría**: Trazabilidad de cambios

### Validaciones

- Validación de entrada con Bean Validation
- Sanitización de datos
- Validación de permisos por organización y sucursal

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 📞 Soporte

Para soporte técnico o preguntas:

- Crear un issue en GitHub
- Contactar al equipo de desarrollo

## 🔄 Changelog

### v0.0.1-SNAPSHOT
- Implementación inicial del sistema
- Autenticación JWT
- Gestión de clientes y sucursales
- Sistema de roles y permisos
- Base de datos PostgreSQL con Flyway

---

**Desarrollado con ❤️ por el equipo de OtherCode** 
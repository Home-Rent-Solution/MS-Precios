MS-Precios

Microservicio encargado de gestionar los precios y temporadas asociados a las propiedades del sistema Home-Rent-Solution.

## Funcionalidades

* Registrar precios
* Consultar precios
* Actualizar precios
* Eliminar precios
* Buscar precios por temporada
* Buscar precios por propiedad
* Obtener precios ordenados por multiplicador
------------------------------------------------

## Endpoints Principales

### Obtener todos los precios

GET /api/v1/precios

### Obtener precio por ID

GET /api/v1/precios/{id}

### Crear precio

POST /api/v1/precios

### Actualizar precio

PUT /api/v1/precios/{id}

### Eliminar precio

DELETE /api/v1/precios/{id}

### Buscar precios por temporada

GET /api/v1/precios/temporada/{temporada}

### Buscar precios por propiedad

GET /api/v1/precios/propiedad/{id}

### Buscar precios ordenados por temporada

GET /api/v1/precios/temporada/ordenado/{temporada}
-----------------------------------------------------------

## Integraciones

Este microservicio utiliza OpenFeign para comunicarse con:

* MS-Propiedades
* MS-Reservas
* MS-Pagos
----------------------------------------------------------

## Tecnologías

* Java 25
* Spring Boot
* Spring Data JPA
* MySQL
* OpenFeign
* OpenAPI / Swagger
* Maven

# Hotel Reservation System

Sistema de reserva de habitaciones de hotel desarrollado con **Java 19**, **JavaFX 21** (interfaz gráfica), **Hibernate 6** (persistencia JPA) y **H2** (base de datos embebida). Proyecto modular con Java Platform Module System (JPMS).

## Descripción

Aplicación de escritorio que permite a los clientes registrados buscar habitaciones disponibles en una cadena hotelera, realizar reservas con pago anticipado del 20%, y unirse a listas de espera cuando no hay disponibilidad.

### Casos de uso principales

- **Buscar habitaciones disponibles** — Selección de hotel y rango de fechas para consultar tipos de habitación libres con sus precios.
- **Reservar habitación** — Formaliza la reserva realizando un pago adelantado del 20% del importe total.
- **Unirse a lista de espera** — Si no hay habitaciones del tipo deseado, el cliente puede registrarse en la lista de espera.

### Reglas de negocio relevantes

- Los precios dependen del **hotel**, el **tipo de habitación** y la **fecha** (`DailyPrice`).
- El precio adicional por día extra (`AdditionalDayPrice`) varía por hotel y tipo, pero no por fecha.
- La reserva se hace sobre un **tipo de habitación**, no sobre una habitación concreta; la asignación de habitación específica se produce en el check-in.

## Arquitectura

El proyecto sigue una arquitectura en capas de tres niveles:

```
┌─────────────────────────────────────┐
│         UI (JavaFX + FXML)          │  ← Presentación
│  ReserveRoom / ReserveRoomController│
├─────────────────────────────────────┤
│        Business Logic Layer         │  ← Lógica de negocio
│   BlFacade / BlFacadeImplementation │
├─────────────────────────────────────┤
│        Data Access Layer            │  ← Persistencia
│          DataAccess (Hibernate)     │
├─────────────────────────────────────┤
│        Domain Model (JPA)           │  ← Entidades de dominio
│  Hotel, Room, Customer, Reservation…│
└─────────────────────────────────────┘
```

### Módulos Java (JPMS)

El proyecto usa `module-info.java` con el nombre de módulo `eus.ehu.hotelreservation`.

### Modelo de dominio

| Entidad | Descripción |
|---|---|
| `Hotel` | Hotel con nombre, categoría, dirección, teléfono y email |
| `Room` | Habitación individual dentro de un hotel |
| `RoomType` | Tipo de habitación (SIMPLE, DOUBLE, TRIPLE, SUITE) |
| `HotelRoomType` | Asociación hotel–tipo de habitación con precios |
| `Rate` | Tarifa diaria asociada a hotel, tipo y fecha |
| `Customer` | Cliente identificado por DNI, con tarjeta de crédito |
| `Reservation` | Reserva con fechas, importes pagados/deudores y habitación asignada |
| `WaitingListEntry` | Entrada en lista de espera para un tipo de habitación |

## Tecnologías

| Componente | Versión |
|---|---|
| Java (source/target) | 19 |
| JavaFX (controls, fxml) | 21.0.2 |
| Hibernate ORM | 6.1.2.Final |
| Jakarta Persistence API | 3.1.0 |
| H2 Database | 2.2.220 |
| Log4j2 | 2.17.1 |
| BootstrapFX | 0.4.0 |
| JUnit 5 | 5.9.1 |
| AssertJ | 3.21.0 |

## Configuración

El archivo `config.properties` controla el comportamiento de la base de datos:

```properties
db.openmode=initialize    # "initialize" para recrear el esquema al arrancar
db.local=true             # acceso local a la base de datos
```

La base de datos H2 se almacena en `~/hotelreservation` y se configura en `hibernate.cfg.xml` con `hbm2ddl.auto=create` (recrea el esquema en cada inicio).

## Estructura del proyecto

```
src/main/java/
├── module-info.java                        # Definición del módulo JPMS
└── eus/ehu/hotelreservation/
    ├── businessLogic/
    │   ├── BlFacade.java                   # Interfaz de la lógica de negocio
    │   └── BlFacadeImplementation.java     # Implementación (singleton)
    ├── configuration/
    │   ├── Config.java                     # Carga de config.properties
    │   └── UtilDate.java                   # Utilidades de fecha
    ├── dataAccess/
    │   └── DataAccess.java                 # Capa de acceso a datos (Hibernate)
    ├── domain/
    │   ├── Customer.java                   # Entidad Cliente
    │   ├── Hotel.java                      # Entidad Hotel
    │   ├── HotelRoomType.java              # Asociación Hotel–Tipo
    │   ├── Rate.java                       # Tarifa diaria
    │   ├── Reservation.java                # Entidad Reserva
    │   ├── Room.java                       # Entidad Habitación
    │   ├── RoomStatus.java                 # Enum estado de habitación
    │   ├── RoomType.java                   # Enum tipo de habitación
    │   └── WaitingListEntry.java           # Entidad Lista de espera
    ├── model/
    │   └── AvailableRoomModel.java         # Modelo JavaFX para la tabla
    └── ui/
        ├── ReserveRoom.java                # Punto de entrada (Application)
        └── ReserveRoomController.java      # Controlador FXML

src/main/resources/
├── hibernate.cfg.xml                       # Configuración Hibernate
├── log4j2.xml                              # Configuración Log4j2
└── eus/ehu/hotelreservation/ui/
    └── reserve-room.fxml                   # Layout de la interfaz

doc/
├── domain-model.puml                       # Diagrama de dominio (PlantUML)
├── usecase-diagram.puml                    # Diagrama de casos de uso
├── reserve-room-sequence.puml              # Diagrama de secuencia
├── modelling-notes.md                      # Notas de modelado
└── usecase-notes.md                        # Notas de casos de uso
```

## Requisitos previos

- **JDK 19+** (el `pom.xml` compila con `source=19` y `target=19`)
- **Maven 3.8+**
- **JavaFX SDK** (incluido como dependencia Maven)

## Compilación y ejecución

```bash
# Compilar el proyecto
mvn compile

# Ejecutar la aplicación JavaFX
mvn javafx:run

# Ejecutar tests
mvn test

# Empaquetar
mvn package
```

## Diagramas UML

La carpeta `doc/` contiene los siguientes diagramas en formato PlantUML:

- **`domain-model.puml`** — Modelo de dominio completo con todas las entidades y relaciones
- **`usecase-diagram.puml`** — Diagrama de casos de uso del sistema
- **`reserve-room-sequence.puml`** — Diagrama de secuencia del flujo de reserva

## Notas de diseño

- El usuario actual se carga automáticamente al iniciar la aplicación (cliente de prueba con DNI `12345678A`).
- La fachada de lógica de negocio (`BlFacadeImplementation`) es un **singleton** que gestiona la sesión del usuario actual.
- El controlador FXML recibe la fachada mediante inyección a través del `ControllerFactory` de `FXMLLoader`.
- El estilo visual utiliza **BootstrapFX** para emular la apariencia de Bootstrap en la interfaz JavaFX.

## Licencia

Proyecto académico — UPV/EHU (Universidad del País Vasco).

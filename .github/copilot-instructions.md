# Hotel Reservation System — Copilot Instructions

This is a **Java 19** desktop application for hotel room reservations, built with **JavaFX 21** (UI), **Hibernate 6 / JPA** (persistence) and **H2** (embedded database). The project uses the **Java Platform Module System (JPMS)**. It is an academic project from UPV/EHU.

## Build & Test Commands

```bash
# Compile
mvn compile

# Run the JavaFX application
mvn javafx:run

# Run tests
mvn test

# Package
mvn package

# Generate Javadoc (output → doc/javadoc/)
mvn javadoc:javadoc
```

## Architecture

The project follows a strict **three-layer architecture**. All changes must respect these boundaries:

```
UI (JavaFX + FXML)  →  Business Logic (BlFacade)  →  Data Access (DataAccess/Hibernate)  →  Domain Model (JPA entities)
```

- **`ui/`** — JavaFX views (`ReserveRoom.java`) and FXML controllers (`ReserveRoomController.java`). Controllers receive `BlFacade` via constructor injection through `FXMLLoader`'s `ControllerFactory`. Never access `DataAccess` or Hibernate directly from this layer.
- **`businessLogic/`** — `BlFacade` interface and `BlFacadeImplementation` (singleton). All business rules live here. The UI layer depends only on the `BlFacade` interface.
- **`dataAccess/`** — `DataAccess` class manages the `EntityManager` lifecycle and all database operations using Hibernate/JPA. Exposes CRUD methods consumed exclusively by the business logic layer.
- **`domain/`** — JPA entity classes (`Hotel`, `Room`, `RoomType`, `Customer`, `Reservation`, `WaitingListEntry`, `Rate`, `HotelRoomType`, `RoomStatus`). These are plain POJOs with JPA annotations.
- **`model/`** — JavaFX-specific model classes (e.g. `AvailableRoomModel`) used as table row beans in the UI layer. These are NOT domain entities.
- **`configuration/`** — `Config` (loads `config.properties`) and `UtilDate` utilities.

## Repository Structure

```
src/main/java/
├── module-info.java                         # JPMS module definition
└── eus/ehu/hotelreservation/
    ├── businessLogic/                       # Facade interface + singleton impl
    ├── configuration/                       # Config loader, date utilities
    ├── dataAccess/                          # Hibernate/JPA persistence layer
    ├── domain/                              # JPA entities and enums
    ├── model/                               # JavaFX table models (non-entity)
    └── ui/                                  # JavaFX Application + FXML controllers
src/main/resources/
├── hibernate.cfg.xml                        # Hibernate configuration (H2 dialect)
├── log4j2.xml                               # Logging configuration
└── eus/ehu/hotelreservation/ui/
    └── reserve-room.fxml                    # FXML layout for the reservation view
config.properties                            # Runtime config (db.openmode, db.local)
doc/                                         # UML diagrams (PlantUML), Javadoc, notes
```

## Code Standards

### Java Conventions
- Source level is **Java 19**. Do not use features from Java 20+ (e.g. record patterns, string templates).
- All public classes and methods must have **Javadoc** comments. Follow the existing documentation style.
- Entity classes use **standard getter/setter** patterns (no Lombok).
- Use `java.time.LocalDate` for date handling in the business and UI layers. The data layer may still use `java.util.Date` where required by legacy JPA mappings — use `UtilDate` converters when bridging.
- Enum values in the domain (`RoomType`, `RoomStatus`) use **UPPERCASE** naming.

### JPMS (module-info.java)
- The module name is `eus.ehu.hotelreservation`.
- Domain entities are opened to `org.hibernate.orm.core` for reflection.
- JavaFX model classes are opened to `javafx.base` for property binding.
- The `ui` package is opened to `javafx.fxml` for controller injection.
- When adding new packages, update `module-info.java` accordingly.

### JPA / Hibernate
- Entity mappings use **Jakarta Persistence** annotations (`jakarta.persistence.*`), not the old `javax.persistence`.
- All new entities must be registered in both `hibernate.cfg.xml` (mapping class) and `module-info.java` (opens to `org.hibernate.orm.core`).
- The `DataAccess` class manages transactions explicitly (`db.getTransaction().begin()` / `.commit()`). Always wrap write operations in a transaction.
- The embedded H2 database file is at `~/hotelreservation`. The schema is recreated on each startup when `db.openmode=initialize`.

### JavaFX / FXML
- UI layouts are defined in **FXML** files under `src/main/resources/eus/ehu/hotelreservation/ui/`.
- Controllers use `@FXML` annotations for field injection and event handlers.
- Use `ObservableList` and `FXCollections` for table data binding.
- Styling uses **BootstrapFX** CSS classes (e.g. `.button-primary`, `.label-info`).
- The `FXMLLoader` is configured with a `ControllerFactory` that injects `BlFacade` into controllers — do not instantiate the facade directly in controllers.

### Business Rules (critical)
- Reservations are made on a **room type**, not a specific room. The concrete room assignment happens at check-in.
- Prices vary by **hotel**, **room type**, and **date** (daily `Rate` entities). Always calculate totals day-by-day.
- `AdditionalDayPrice` varies by hotel and room type but NOT by date.
- A reservation requires a **20% upfront payment**; the remainder is due at check-in.
- When no rooms of the desired type are available, the customer can join a **waiting list**.

## Key Guidelines

1. **Respect layer boundaries** — UI → Business Logic → Data Access → Domain. Never skip layers.
2. **Maintain the facade pattern** — all business operations go through `BlFacade` / `BlFacadeImplementation`.
3. **Write Javadoc** for all public APIs. Document `@param`, `@return`, and `@throws` tags.
4. **Keep entities as plain POJOs** — no business logic in domain classes. Business rules belong in `BlFacadeImplementation` or `DataAccess`.
5. **Use `LocalDate` in business and UI code**. Convert to/from `java.util.Date` only at the data access boundary using `UtilDate`.
6. **Update `module-info.java`** whenever adding new packages or dependencies.
7. **Register new JPA entities** in `hibernate.cfg.xml` and open the package in `module-info.java`.
8. **Write unit tests** for new business logic. Use **JUnit 5** (`org.junit.jupiter`) and **AssertJ** for assertions. Tests go under `src/test/java/`.
9. **Do not change `config.properties` defaults** without documenting the impact — `db.openmode=initialize` recreates the entire database on startup.
10. **Follow existing naming conventions** — the base package is `eus.ehu.hotelreservation`, matching the university domain.

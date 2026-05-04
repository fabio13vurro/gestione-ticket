# 🎫 TicketManager — Enterprise Ticketing System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Thymeleaf](https://img.shields.io/badge/Frontend-Thymeleaf%20%2B%20Bootstrap%205-blue.svg)](https://www.thymeleaf.org/)
[![Database](https://img.shields.io/badge/Database-MySQL%20%2B%20MongoDB-blue.svg)](https://www.mysql.com/)

**TicketManager** è una piattaforma professionale per la gestione dell'assistenza clienti e del workflow operativo. Progettata con un'architettura robusta basata su Spring Boot e un frontend moderno in stile "Glassmorphism", offre una soluzione end-to-end per il monitoraggio e la risoluzione dei ticket.

---

## 🚀 Caratteristiche Principali

### 👥 Gestione Utenti & Ruoli
- **Multi-Role Access Control**: Accesso granulare per **Admin**, **Operatori** e **Clienti**.
- **User Dashboard**: Vista personalizzata in base al ruolo.

### 🎫 Workflow dei Ticket
- **Ciclo di Vita Completo**: Gestione degli stati (Aperto → In Lavorazione → In Attesa → Risolto → Chiuso).
- **Assegnazione Automatica**: Bilanciamento intelligente del carico di lavoro tra gli operatori disponibili.
- **SLA Management**: Calcolo automatico della scadenza in base alla priorità e gestione dei ticket scaduti (Over SLA).

### 💬 Collaborazione & Storico
- **Commenti Interni/Esterni**: Supporto per la comunicazione tra clienti e staff operativo.
- **Audit Trail**: Tracciamento completo di ogni cambio di stato con data, ora e utente.

### 📊 Analytics & Reporting
- **Dashboard Amministrativa**: Grafici interattivi (Chart.js) per analizzare i trend annuali, il tasso di risoluzione e le performance degli operatori.
- **Integrazione Polyglot**: Utilizzo di **MySQL** per i dati transazionali e **MongoDB** per l'archiviazione e le statistiche avanzate.

---

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.4, Spring Data JPA, Spring Security, GraphQL.
- **Frontend**: Thymeleaf, Bootstrap 5.3, JavaScript (Vanilla), CSS3 (Custom Glassmorphism).
- **Database**: MySQL 9.0, MongoDB.
- **Messaging**: RabbitMQ (Spring AMQP).
- **Observability**: Spring Actuator, Prometheus, Micrometer.
- **Testing**: JUnit 5, H2 Database.

---

## 📂 Struttura del Progetto

```text
src/main/
├── java/.../gestione_ticket/
│   ├── config/           # Configurazioni Security, RabbitMQ, GraphQL
│   ├── controllers/      
│   │   ├── pages/        # Page Controller per Thymeleaf (Admin, Operatore, Cliente)
│   │   ├── jobs/         # Controller per task pianificati e importazione dati
│   │   └── REST/         # Endpoint API REST per integrazioni esterne
│   ├── services/         # Logica di business (SLA, Archiviazione, Notifiche)
│   ├── repositories/     # Repository JPA per MySQL
│   ├── mongodb/          
│   │   ├── documents/    # Documenti per statistiche e storico Over SLA
│   │   └── repositories/ # Repository per persistenza su MongoDB
│   ├── entities/         # Entity JPA (Ticket, Utente, Commento, Storico)
│   ├── DTOs/             # Data Transfer Objects per le API
│   ├── mappers/          # Mapper per conversione tra Entity e DTO
│   └── Models/           # Modelli specifici per l'interfaccia GraphQL
└── resources/
    ├── static/           # Asset statici (CSS, Immagini, JS)
    ├── templates/        
    │   ├── admin/        # Dashboard amministrativa e gestione utenti
    │   ├── operatore/    # Gestione operativa ticket e commenti
    │   ├── cliente/      # Interfaccia creazione e monitoraggio ticket
    │   └── fragments/    # Componenti UI riutilizzabili (Navbar, Footer, Head)
    ├── graphql/          # Schemi per le query GraphQL
    └── application.properties
```

---

## ⚙️ Installazione e Configurazione

### Prerequisiti
- **JDK 17** o superiore.
- **Maven 3.8+**.
- **MySQL** e **MongoDB** attivi.
- (Opzionale) **RabbitMQ**.

### Setup
1. Clonare il repository.
2. Configurare le credenziali database in `src/main/resources/application.properties`.
3. Eseguire l'installazione delle dipendenze:
   ```bash
   mvn clean install
   ```
4. Avviare l'applicazione:
   ```bash
   mvn spring-boot:run
   ```

---

## 📖 Documentazione API
L'applicazione integra **Swagger UI** per esplorare gli endpoint REST disponibili:
- **Swagger**: `http://localhost:8080/swagger-ui/index.html`

---

## 🎨 Design System
Il frontend utilizza un tema **Premium Glassmorphism** con:
- Palette colori curata (Indigo/Slate).
- Componenti UI responsive e accessibili.
- Micro-animazioni e hover effects dinamici.
- Dashboard intuitive con visualizzazione dati in tempo reale.
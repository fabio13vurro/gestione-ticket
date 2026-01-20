
# 🎫 Sistema di Gestione Ticket – Backend REST (Spring Boot)

## 📌 Descrizione
Progetto backend realizzato con **Spring Boot**, che gestisce le operazioni CRUD di base per quattro entità principali:

- **Utente**
- **Ticket**
- **Storico_Stato**
- **Commento**

Il progetto è attualmente in fase iniziale e implementa solo le funzionalità fondamentali.  
È predisposto per future estensioni come workflow degli stati, commenti interni/esterni, SLA e assegnazione automatica degli operatori.

---

## 🏗️ Tecnologie
- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA
- MySQL
- Lombok
- SpringDoc OpenAPI (Swagger)

---

## 📦 Funzionalità attuali
✔️ CRUD **Utente**  
✔️ CRUD **Ticket**  
✔️ CRUD **Storico_Stato**  
✔️ CRUD **Commento**

Ogni entità ha:
- Repository JPA
- Service dedicato
- Controller REST

---

## 📚 Documentazione API
Swagger UI è disponibile su:
http://localhost:8080/swagger-ui/index.html
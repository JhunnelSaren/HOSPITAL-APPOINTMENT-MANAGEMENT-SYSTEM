# Master System Mapping & Group Module Distribution

**Project Title:** Hospital Appointment Management System (MedConnect)  
**Document Type:** Master System Architecture & Team Module Distribution Document  
**Target Architecture:** Android Studio + Java (Client) | Node.js + Express (Server) | MySQL (Database)  

---

## 1. Project Overview

The **Hospital Appointment Management System (MedConnect)** is a comprehensive client-server healthcare solution designed to digitize and streamline hospital appointment scheduling, patient queuing, clinical consultations, vital signs tracking, and administrative record management. 

By replacing manual paper-based scheduling and walk-in congestion with a centralized digital system, MedConnect allows:
* **Patients:** To register accounts, view available medical departments and doctor schedules, book or cancel appointments, and review past medical visit records.
* **Doctors:** To view their daily consultation schedules, manage patient queues, log medical consultations, and issue digital prescriptions.
* **Nurses:** To manage patient check-ins, record vital signs (blood pressure, temperature, heart rate, weight), and route patients into active doctor queues.
* **Admins:** To manage user accounts, oversee doctor and department directories, review audit logs, and ensure smooth system operations.

---

## 2. System Architecture

The project follows a standard **Three-Tier Client-Server Architecture**:

```
+---------------------------------------------------------------------------------+
|                                 CLIENT TIER                                     |
|                         (Android Studio + Java App)                             |
|                                                                                 |
|  [ Patient Screens ]      [ Doctor Screens ]       [ Nurse Screens ]            |
|  - Registration/Login     - Daily Schedule         - Queue Management           |
|  - Booking & History      - Patient Consultations  - Vital Signs Entry          |
+---------------------------------------------------------------------------------+
                                       ^
                                       | HTTP / REST API (JSON)
                                       | via Volley & ADB Reverse Proxy
                                       v
+---------------------------------------------------------------------------------+
|                                 SERVER TIER                                     |
|                              (Node.js + Express)                                |
|                                                                                 |
|  [ Auth & Security ]     [ Appointment Processor ]  [ Schedule Validator ]      |
|  [ API Routing ]         [ Department Management ]  [ Queue Controller ]        |
+---------------------------------------------------------------------------------+
                                       ^
                                       | SQL Queries via
                                       | mysql2 Connection Pool
                                       v
+---------------------------------------------------------------------------------+
|                                DATABASE TIER                                    |
|                                  (MySQL)                                        |
|                                                                                 |
|  Tables: users, patients, doctors, departments, schedules, appointments,         |
|         vital_signs, nurse_queue, consultations, prescriptions, audit_logs       |
+---------------------------------------------------------------------------------+
```

### 1. Client Tier (Android Studio + Java)
* **Environment:** Native Android Application developed using Android Studio and Java.
* **Role:** Provides user interface activities, handles UI event listeners, validates client inputs, and renders backend data returned as JSON.
* **Communication Library:** **Volley** library for asynchronous HTTP network calls.

### 2. Server Tier (Node.js + Express)
* **Environment:** Node.js runtime environment running an Express web framework application.
* **Role:** Handles REST API routes (`/api/login`, `/api/register`, `/api/appointments`, `/api/vitals`, etc.), enforces authentication logic, performs schedule validation to prevent double-booking, and coordinates database requests.

### 3. Database Tier (MySQL)
* **Environment:** Relational Database Management System (RDBMS).
* **Role:** Stores persistent hospital data across normalized tables with primary key and foreign key constraints ensuring data integrity.

---

## 3. Tier Component Details

### Client-Side Overview (Android Java)
* **User Interface:** XML Layout files paired with Java Activities (`MainActivity`, `RegisterActivity`, `VerificationActivity`, `PatientDashboardActivity`, `DoctorDashboardActivity`, `NurseDashboardActivity`).
* **View Binding:** Directly binds XML components to Activity logic.
* **Request Handler:** Volley `RequestQueue` handles asynchronous REST API calls to prevent UI thread freezing.
* **Session Management:** Stores session tokens and user role details locally during app lifecycle.

### Server-Side Overview (Node.js REST API)
* **Router Layer:** Express router handling endpoints for Auth, Patients, Doctors, Nurses, Appointments, and Administration.
* **Controller Logic:** Business logic validating request bodies, processing appointment status transitions, checking time-slot availability, and generating responses.
* **Connection Pooling:** Uses `mysql2` module with connection pooling to query the MySQL database efficiently.

### Database-Side Overview (MySQL Schema)
Primary database tables include:
1. `users`: Holds authentication credentials, full names, email addresses, roles (`Patient`, `Doctor`, `Nurse`, `Admin`), and verification status.
2. `patients`: Stores patient profiles (contact numbers, addresses, birth dates, medical history).
3. `doctors`: Contains doctor profiles, specialties, contact info, and `department_id` references.
4. `departments`: Stores department names (Cardiology, Pediatrics, etc.) and descriptions.
5. `schedules`: Stores doctor shift schedules, consultation dates, time slots, and availability statuses.
6. `appointments`: Stores booking records linking `patient_id`, `doctor_id`, `schedule_id`, status (`Pending`, `Confirmed`, `Completed`, `Cancelled`), and chief complaint.
7. `nurse_queue`: Tracks patient queuing order for vital signs recording and doctor routing.
8. `vital_signs`: Stores nurse-recorded patient metrics (blood pressure, temperature, heart rate, weight, height).
9. `consultations`: Records doctor consultation notes, diagnoses, and treatment plans.
10. `prescriptions`: Stores prescribed medications, dosages, and instructions per consultation.
11. `notifications`: Stores system alerts and reminder messages for users.
12. `audit_logs`: Records system events and administrative action logs for auditing.

---

## 4. Client-Server Communication & Data Flow

1. **Initiation:** The Android Client constructs a JSON payload (e.g. email & password for login or booking details).
2. **Transmission:** The client sends an HTTP request (`GET`, `POST`, `PUT`, `DELETE`) via Volley to `http://localhost:3000/api/...`.
3. **ADB Reverse Proxying:** Port forwarding via `adb reverse tcp:3000 tcp:3000` routes Android app local requests directly to the host machine's Node.js server.
4. **Server Processing:** The Node.js Express server validates the payload, executes parameterized SQL queries against the MySQL database, and receives the result set.
5. **Response Delivery:** The server formats the outcome into a JSON response structure containing a status flag (`"success": true/false`), message strings, and data payloads.
6. **Client UI Update:** The Android client parses the JSON response and updates screen elements, refreshes `RecyclerView` items, or alerts the user via a `Toast`.

---

## 5. Overall System Flow

```
[ Patient Registers ] ---> [ Verification Code ] ---> [ Patient Logs In ]
                                                            |
                                                            v
                                            [ Patient Dashboard ]
                                                            |
                                                            v
                                            [ Browse Doctors/Departments ]
                                                            |
                                                            v
                                            [ Select Available Slot ]
                                                            |
                                                            v
                                            [ Book Appointment ]
                                                            |
                                                            v
                                            [ Node.js Validates Slot ]
                                                            |
                                                            v
                                            [ Saved to MySQL DB ]
                                                            |
                                                            v
[ Nurse Queue Intake ] <--------------------- [ Appointment Confirmed ]
          |
          v
[ Nurse Records Vital Signs ]
          |
          v
[ Patient Routed to Doctor ]
          |
          v
[ Doctor Performs Consultation ] ---> [ Enters Diagnosis & Prescription ]
                                                      |
                                                      v
                                        [ Record Saved & Completed ]
```

---

## 6. User Roles and Access Control

1. **Patient:**
   * Can register, verify email, log in, view medical departments, view doctors and schedules, book appointments, cancel upcoming appointments, view personal appointment history, and update profile details.
2. **Doctor:**
   * Can log in, view personal daily consultation schedule, view queued patient list, view recorded vital signs, enter consultation notes and diagnoses, and issue prescriptions.
3. **Nurse:**
   * Can log in, view today's patient arrivals, record patient vital signs (blood pressure, temperature, heart rate, weight), manage patient queue status, and route patients to doctor consultation rooms.
4. **Admin:**
   * Can manage user roles and accounts, maintain doctor and department listings, monitor audit logs, and oversee system configuration.

---

## 7. Group Member Task Distribution

Our project task responsibilities are divided among our three group members as follows:

### MEMBER 1 – SAREN, JHUNNEL LUNA (PROJECT LEADER)
* **Primary Focus:** Android Client Development & Client-Server Integration
* **Specific Responsibilities:**
  * Android Client Architecture & Layout Design in Java & Android Studio.
  * Patient Screen Implementations (Registration, Login, Verification, Dashboard, Doctor Listing, Department Viewing, Available Schedules, Booking Form, Cancellation Dialog, Appointment History, Patient Profile).
  * Client-side Navigation Flow and Activity transitions.
  * Client-Server Integration using Volley HTTP requests.
  * Form input validation, Toast message handling, and client error handling.
  * Overall Project Coordination and Documentation Management.

### MEMBER 2 – SERVANDIL, ED LAURENCE
* **Primary Focus:** Node.js Server & Backend API Development
* **Specific Responsibilities:**
  * Node.js and Express REST API Server Development.
  * User Authentication & Session Verification Logic.
  * Doctor Directory and Department Management API Routes.
  * Appointment Processing Endpoints (Create, Read, Update Status, Cancel).
  * Schedule Validation Engine (Preventing double-booking and schedule overlaps).
  * Backend API Testing and Integration Endpoints.

### MEMBER 3 – IMPROGO, KEM
* **Primary Focus:** MySQL Database & Clinical / Nurse Support Modules
* **Specific Responsibilities:**
  * MySQL Database Design, Relational Schema Definition, and DDL scripts.
  * Foreign Key Relationships, Constraints, and Database Optimization.
  * Nurse Queue Management System Backend & Data Layer.
  * Vital Signs Tracking Module Data Architecture.
  * Clinical Consultations & Digital Prescriptions Storage logic.
  * Notifications System, Audit Logging, and Supporting Admin Database Functions.

---

## 8. Module Distribution Matrix

| System Module | Primary Member Responsible | Architecture Layer | Core Components |
| :--- | :--- | :--- | :--- |
| **Patient Registration & Login UI** | Member 1 (Saren, Jhunnel Luna) | Client (Android/Java) | `MainActivity`, `RegisterActivity`, `VerificationActivity` |
| **Patient Dashboard & Navigation** | Member 1 (Saren, Jhunnel Luna) | Client (Android/Java) | `PatientDashboardActivity`, Navigation Listeners, UI Layouts |
| **Doctor & Department Views** | Member 1 (Saren, Jhunnel Luna) | Client (Android/Java) | `DoctorListingActivity`, `DepartmentViewingActivity` |
| **Appointment Booking & History UI** | Member 1 (Saren, Jhunnel Luna) | Client (Android/Java) | Booking Dialogs, History `RecyclerView`, Cancellation Dialogs |
| **Authentication & Session Server APIs**| Member 2 (Servandil, Ed Laurence) | Server (Node.js/Express) | Auth Routes, Hashing, Token Generation |
| **Doctor & Department REST APIs** | Member 2 (Servandil, Ed Laurence) | Server (Node.js/Express) | `/api/doctors`, `/api/departments` controllers |
| **Appointment Processing & Validation** | Member 2 (Servandil, Ed Laurence) | Server (Node.js/Express) | Booking Validation Engine, Overlap Checks |
| **MySQL Relational Database Schema** | Member 3 (Improgo, Kem) | Database (MySQL) | Table Schemas, Constraints, Foreign Keys |
| **Nurse Queue & Vital Signs System** | Member 3 (Improgo, Kem) | Database & Server | `nurse_queue`, `vital_signs` tables and logic |
| **Consultations & Prescriptions System** | Member 3 (Improgo, Kem) | Database & Server | `consultations`, `prescriptions` tables and logic |
| **Audit Logs & Admin Functions** | Member 3 (Improgo, Kem) | Database & Server | `audit_logs` tracking and database maintenance |

---

## 9. Development Tools & Software Stack

* **Client Development:** Android Studio (Ladybug / Jellyfish / Hedgehog), Java Development Kit (JDK 17), Android SDK, Volley Library, ViewBinding.
* **Server Development:** Node.js, Express.js framework, npm packages (`express`, `mysql2`, `cors`, `body-parser`).
* **Database Management:** MySQL Server 8.0+, MySQL Workbench / phpMyAdmin.
* **Network & Debugging Tools:** Android Debug Bridge (`adb`), Postman (for API testing).
* **Version Control:** Git, GitHub (Repository: `HospitalAppointmentApp`, Branch: `mapping-client-leader`).

---
*Document prepared and submitted by Member 1 (SAREN, JHUNNEL LUNA - Project Leader) on behalf of Group 2.*

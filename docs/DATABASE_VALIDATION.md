# Database Validation Rules & Scope - Hospital Appointment Management System

This document defines the database validation rules, data integrity constraints, and scope guidelines for the **Hospital Appointment Management System**.

---

## 1. Core Database Validation Rules

To ensure data integrity, security, and reliable clinic operations, the following validation rules are enforced across the database and application layers:

1. **Required Fields Cannot Be Empty**:
   - Mandatory attributes (such as user credentials, patient names, doctor specialties, appointment dates, and prescription dosages) must not be left null or empty. Database constraints (`NOT NULL`) and input form validation ensure all essential data is captured.

2. **Username Must Be Unique**:
   - The `username` field in the `users` table has a unique index (`UNIQUE`). The system verifies that no two accounts can share the same username during registration or profile updates.

3. **Appointment Schedule Must Be Available**:
   - When booking an appointment, the system validates that the selected `schedule_id` in `doctor_schedules` exists and that the doctor is marked as available (`is_available = TRUE`) for that date and time slot, preventing double-booking or scheduling outside working hours.

4. **Users Can Only Access Permitted Records**:
   - Role-based access control (RBAC) ensures users can only read or modify records relevant to their role (e.g., patients view only their own appointments and prescriptions; nurses manage queues and vital signs; doctors manage consultations and prescriptions; admins oversee system-wide data).

5. **Completed Records Must Not Be Deleted Without Proper Authorization**:
   - Historical records such as completed consultations, prescriptions, and audit logs are protected against unauthorized deletion. Completed appointments and medical records are retained for auditing and patient history continuity, requiring administrative privileges or soft-deletion policies if updates are necessary.

---

## 2. Important Scope & Source Note

> **IMPORTANT**: The project proposal contains an introductory reference in the Database Design section regarding student obligations, tuition, and payment allocations. 
> 
> Pursuant to project requirements:
> - That introductory sentence is recognized as an inconsistency and is **strictly excluded** from the hospital system implementation.
> - The system focuses exclusively on **hospital appointments, queuing, vital signs, doctor consultations, prescriptions, notifications, and administrative staff management**.
> - **Do not introduce student payment, tuition, or cashier modules.** All validation rules and database structures adhere strictly to the medical appointment management domain.

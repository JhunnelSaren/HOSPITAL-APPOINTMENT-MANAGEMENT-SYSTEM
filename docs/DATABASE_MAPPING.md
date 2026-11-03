# Database Mapping - Hospital Appointment Management System

This document outlines the database schema and table mapping for the **Hospital Appointment Management System**. The database is designed using MySQL to support user authentication, patient profiles, medical staff management, appointments, queuing, vital signs recording, consultations, prescriptions, notifications, and audit logging.

---

## 1. users
- **Purpose**: Stores core authentication and login credentials for all system users (Patients, Doctors, Nurses, and Administrators).
- **Primary Key**: `user_id` (INT, Auto-increment)
- **Important Fields**:
  - `username` (VARCHAR, Unique): Login username.
  - `password_hash` (VARCHAR): Encrypted password for secure authentication.
  - `role` (ENUM: 'admin', 'patient', 'doctor', 'nurse'): Determines system permissions and user access level.
  - `created_at` (TIMESTAMP): Account creation timestamp.
- **Foreign Keys**: None
- **Connected Tables**: `patients`, `doctors`, `nurses`, `notifications`, `audit_logs`

---

## 2. patients
- **Purpose**: Stores personal and demographic information specific to patients registered in the hospital system.
- **Primary Key**: `patient_id` (INT, Auto-increment)
- **Important Fields**:
  - `user_id` (INT): Links the patient profile to their login account.
  - `first_name` (VARCHAR): Patient's first name.
  - `last_name` (VARCHAR): Patient's last name.
  - `date_of_birth` (DATE): Patient's birthdate.
  - `gender` (VARCHAR): Patient's gender.
  - `contact_number` (VARCHAR): Phone number for notifications and contact.
  - `address` (TEXT): Home address.
- **Foreign Keys**:
  - `user_id` references `users(user_id)`
- **Connected Tables**: `users`, `appointments`

---

## 3. doctors
- **Purpose**: Stores professional and departmental information for medical doctors.
- **Primary Key**: `doctor_id` (INT, Auto-increment)
- **Important Fields**:
  - `user_id` (INT): Links the doctor profile to their login account.
  - `department_id` (INT): Department where the doctor practices.
  - `first_name` (VARCHAR): Doctor's first name.
  - `last_name` (VARCHAR): Doctor's last name.
  - `specialization` (VARCHAR): Medical specialty (e.g., Cardiology, Pediatrics).
  - `contact_number` (VARCHAR): Professional phone number.
- **Foreign Keys**:
  - `user_id` references `users(user_id)`
  - `department_id` references `departments(department_id)`
- **Connected Tables**: `users`, `departments`, `doctor_schedules`, `appointments`, `consultations`

---

## 4. nurses
- **Purpose**: Stores information for nursing staff responsible for queue management and vital signs recording.
- **Primary Key**: `nurse_id` (INT, Auto-increment)
- **Important Fields**:
  - `user_id` (INT): Links the nurse profile to their login account.
  - `first_name` (VARCHAR): Nurse's first name.
  - `last_name` (VARCHAR): Nurse's last name.
  - `shift` (VARCHAR): Assigned working shift (e.g., Morning, Night).
- **Foreign Keys**:
  - `user_id` references `users(user_id)`
- **Connected Tables**: `users`, `queues`, `vital_signs`

---

## 5. departments
- **Purpose**: Represents hospital departments or medical units (e.g., General Medicine, Pediatrics, Cardiology).
- **Primary Key**: `department_id` (INT, Auto-increment)
- **Important Fields**:
  - `department_name` (VARCHAR): Name of the department.
  - `description` (TEXT): Brief overview of department services.
- **Foreign Keys**: None
- **Connected Tables**: `doctors`

---

## 6. doctor_schedules
- **Purpose**: Defines working days, available time slots, and consultation hours for doctors.
- **Primary Key**: `schedule_id` (INT, Auto-increment)
- **Important Fields**:
  - `doctor_id` (INT): The doctor associated with this schedule.
  - `day_of_week` (VARCHAR): Day of the week (e.g., Monday, Tuesday).
  - `start_time` (TIME): Shift start time.
  - `end_time` (TIME): Shift end time.
  - `is_available` (BOOLEAN): Availability status.
- **Foreign Keys**:
  - `doctor_id` references `doctors(doctor_id)`
- **Connected Tables**: `doctors`, `appointments`

---

## 7. appointments
- **Purpose**: Manages appointment bookings between patients and doctors on specific schedules.
- **Primary Key**: `appointment_id` (INT, Auto-increment)
- **Important Fields**:
  - `patient_id` (INT): Patient booking the appointment.
  - `doctor_id` (INT): Doctor assigned to the appointment.
  - `schedule_id` (INT): Specific working schedule slot.
  - `appointment_date` (DATE): Date of the appointment.
  - `status` (ENUM: 'Scheduled', 'Completed', 'Cancelled'): Current appointment status.
- **Foreign Keys**:
  - `patient_id` references `patients(patient_id)`
  - `doctor_id` references `doctors(doctor_id)`
  - `schedule_id` references `doctor_schedules(schedule_id)`
- **Connected Tables**: `patients`, `doctors`, `doctor_schedules`, `queues`, `vital_signs`, `consultations`

---

## 8. queues
- **Purpose**: Handles on-site patient queueing and waiting list numbers for daily clinic visits.
- **Primary Key**: `queue_id` (INT, Auto-increment)
- **Important Fields**:
  - `appointment_id` (INT): Linked appointment.
  - `queue_number` (INT): Assigned queue number for the day.
  - `status` (ENUM: 'Waiting', 'In-Progress', 'Completed', 'Skipped'): Queue progression status.
  - `called_at` (TIMESTAMP): Time when the patient was called by the nurse.
- **Foreign Keys**:
  - `appointment_id` references `appointments(appointment_id)`
- **Connected Tables**: `appointments`, `nurses`

---

## 9. vital_signs
- **Purpose**: Records physical measurements taken by nurses prior to doctor consultation.
- **Primary Key**: `vital_id` (INT, Auto-increment)
- **Important Fields**:
  - `appointment_id` (INT): Associated appointment.
  - `nurse_id` (INT): Nurse who recorded the vitals.
  - `temperature` (DECIMAL): Body temperature in Celsius/Fahrenheit.
  - `blood_pressure` (VARCHAR): Systolic/Diastolic blood pressure reading.
  - `pulse_rate` (INT): Heart beats per minute.
  - `weight` (DECIMAL): Patient weight in kg.
  - `recorded_at` (TIMESTAMP): Time of recording.
- **Foreign Keys**:
  - `appointment_id` references `appointments(appointment_id)`
  - `nurse_id` references `nurses(nurse_id)`
- **Connected Tables**: `appointments`, `nurses`

---

## 10. consultations
- **Purpose**: Stores doctor consultation notes, diagnoses, and medical observations during the visit.
- **Primary Key**: `consultation_id` (INT, Auto-increment)
- **Important Fields**:
  - `appointment_id` (INT): Associated appointment.
  - `doctor_id` (INT): Doctor conducting the consultation.
  - `diagnosis` (TEXT): Medical diagnosis findings.
  - `notes` (TEXT): Doctor's clinical notes and recommendations.
  - `consultation_date` (TIMESTAMP): Date and time of consultation.
- **Foreign Keys**:
  - `appointment_id` references `appointments(appointment_id)`
  - `doctor_id` references `doctors(doctor_id)`
- **Connected Tables**: `appointments`, `doctors`, `prescriptions`

---

## 11. prescriptions
- **Purpose**: Represents official prescriptions issued by doctors during consultations.
- **Primary Key**: `prescription_id` (INT, Auto-increment)
- **Important Fields**:
  - `consultation_id` (INT): Associated consultation.
  - `issued_date` (TIMESTAMP): Date prescription was issued.
- **Foreign Keys**:
  - `consultation_id` references `consultations(consultation_id)`
- **Connected Tables**: `consultations`, `prescription_items`

---

## 12. prescription_items
- **Purpose**: Detailed breakdown of individual medicines, dosages, and instructions within a prescription.
- **Primary Key**: `item_id` (INT, Auto-increment)
- **Important Fields**:
  - `prescription_id` (INT): Parent prescription.
  - `medicine_name` (VARCHAR): Name of the prescribed medicine.
  - `dosage` (VARCHAR): Dosage strength (e.g., 500mg).
  - `instructions` (TEXT): Administration instructions (e.g., Take twice daily after meals).
- **Foreign Keys**:
  - `prescription_id` references `prescriptions(prescription_id)`
- **Connected Tables**: `prescriptions`

---

## 13. notifications
- **Purpose**: Stores in-system notification alerts for users regarding appointments, queue updates, or system messages.
- **Primary Key**: `notification_id` (INT, Auto-increment)
- **Important Fields**:
  - `user_id` (INT): Recipient user.
  - `message` (TEXT): Notification content.
  - `is_read` (BOOLEAN): Read/unread status.
  - `created_at` (TIMESTAMP): Creation time.
- **Foreign Keys**:
  - `user_id` references `users(user_id)`
- **Connected Tables**: `users`

---

## 14. audit_logs
- **Purpose**: Tracks important system activities and security actions performed by users for administrative auditing.
- **Primary Key**: `log_id` (INT, Auto-increment)
- **Important Fields**:
  - `user_id` (INT): User who performed the action.
  - `action` (VARCHAR): Description of the action (e.g., 'CREATED_APPOINTMENT', 'UPDATED_SCHEDULE').
  - `timestamp` (TIMESTAMP): Time of action.
- **Foreign Keys**:
  - `user_id` references `users(user_id)`
- **Connected Tables**: `users`

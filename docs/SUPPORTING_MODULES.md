# Supporting Module Mapping - Hospital Appointment Management System

This document provides a detailed mapping and description of the core supporting modules in the **Hospital Appointment Management System**, outlining functional workflows for nurses, doctors, administrators, and system logs.

---

## A. Nurse Queue Management
The Nurse Queue Module streamlines on-site clinic flow and patient arrival management on the day of the appointment.
- **Check Patient Appointments**: Nurses view scheduled appointments for the day to confirm patient arrival at the clinic reception.
- **Assign Queue Numbers**: Upon arrival check-in, the system assigns a sequential queue number linked to the patient's appointment.
- **Manage the Waiting List**: Nurses view and organize the live waiting list (`queues` table) ordered by arrival and appointment time.
- **Call the Next Patient**: Nurses trigger a call action for the next patient in line, updating the queue status and notifying the patient/doctor.
- **Update Queue Status**: Nurses update queue states (`Waiting`, `In-Progress`, `Completed`, `Skipped`) as patients move through triage and consultation.

---

## B. Vital Signs
The Vital Signs Module enables nurses to record critical preliminary health measurements before the doctor consultation.
- **Record Temperature**: Input body temperature readings (e.g., in °C or °F).
- **Record Blood Pressure**: Input systolic and diastolic blood pressure readings.
- **Record Pulse Rate**: Input heart rate beats per minute.
- **Record Weight**: Input patient body weight.
- **Association**: Automatically associates recorded vitals (`vital_signs` table) with the specific `appointment_id` and the logging `nurse_id`.

---

## C. Doctor Consultation
The Doctor Consultation Module empowers physicians to conduct examinations and document clinical findings.
- **View Appointments**: Doctors access their daily appointment list and assigned patient schedules.
- **Save Consultation Notes**: Record clinical observations, symptoms, and examination notes into the `consultations` table.
- **Record Diagnosis**: Document the official medical diagnosis for the patient visit.

---

## D. Prescriptions
The Prescriptions Module allows doctors to prescribe medications during or immediately following a consultation.
- **Create Prescriptions**: Generate a new prescription record (`prescriptions` table) linked to the consultation.
- **Add Medicine Names**: Input specific pharmaceutical drug names required for treatment.
- **Record Dosage**: Specify strength and dosage details (e.g., 500mg, 1 tablet).
- **Record Instructions**: Provide clear patient instructions (`prescription_items` table) on how and when to take the medication (e.g., twice daily after meals).

---

## E. Notifications and Audit Logs
The System Support Module maintains transparency, alerts, and security tracking across the application.
- **Store System Notifications**: Automatically generate and store in-app notifications (`notifications` table) for users regarding appointment confirmations, queue alerts, or status changes.
- **Track Important User Actions**: Record key activities (`audit_logs` table) such as account logins, user creations, appointment updates, and schedule modifications for administrative security oversight.

---

## F. Admin Supporting Functions
The Administrative Module provides system management tools for hospital operations and staff coordination.
- **Manage Users**: Create, view, update, or deactivate user accounts (`users` table) across all roles (Patients, Doctors, Nurses, Admins).
- **Manage Doctors**: Register and maintain doctor profiles (`doctors` table), including specialties and department assignments.
- **Manage Nurses**: Register and maintain nurse staff accounts (`nurses` table) and shift assignments.
- **Manage Schedules**: Set up and update working hours, available days, and time slots (`doctor_schedules` table).
- **View Basic Reports**: Access summary reports of hospital appointments, daily patient volume, and system activity logs.

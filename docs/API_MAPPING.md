# API Mapping Document

Hospital Appointment Management System - REST API Endpoint Specifications  
**Assigned Role:** Member 2 (Server-Side Mapping and Documentation)  
**Project Tech Stack:** Android Studio (Java) + Node.js (Backend Server) + MySQL (Database)

---

## Overview

This document defines the proposed REST API endpoint mapping between the Android Client and the Node.js backend server. All requests and responses use JSON format (`Content-Type: application/json`).

---

## 1. Authentication Endpoints

### Register Patient / User
- **HTTP Method:** `POST`
- **Endpoint Path:** `/api/register`
- **Purpose:** Registers a new user account (Patient, Doctor, or Nurse) in the system.
- **Required Information (JSON Body):**
  ```json
  {
    "full_name": "John Doe",
    "email": "john@example.com",
    "password": "securepassword123",
    "role": "Patient"
  }
  ```
- **Expected Response:**
  - **Success (201 Created):**
    ```json
    {
      "success": true,
      "message": "Registration successful. Verification code sent.",
      "user_id": 1
    }
    ```
  - **Failure (400 Bad Request):**
    ```json
    {
      "success": false,
      "message": "Email already registered."
    }
    ```

### User Login
- **HTTP Method:** `POST`
- **Endpoint Path:** `/api/login`
- **Purpose:** Authenticates user credentials and returns user role for dashboard routing.
- **Required Information (JSON Body):**
  ```json
  {
    "email": "john@example.com",
    "password": "securepassword123"
  }
  ```
- **Expected Response:**
  - **Success (200 OK):**
    ```json
    {
      "success": true,
      "message": "Login successful",
      "user": {
        "id": 1,
        "full_name": "John Doe",
        "email": "john@example.com",
        "role": "Patient"
      }
    }
    ```
  - **Failure (401 Unauthorized):**
    ```json
    {
      "success": false,
      "message": "Invalid email or password"
    }
    ```

### Verify Account
- **HTTP Method:** `POST`
- **Endpoint Path:** `/api/verify`
- **Purpose:** Verifies a newly registered user's account using a 6-digit verification code.
- **Required Information (JSON Body):**
  ```json
  {
    "email": "john@example.com",
    "code": "123456"
  }
  ```
- **Expected Response:**
  - **Success (200 OK):**
    ```json
    {
      "success": true,
      "message": "Account verified successfully"
    }
    ```
  - **Failure (400 Bad Request):**
    ```json
    {
      "success": false,
      "message": "Invalid or expired verification code"
    }
    ```

---

## 2. Patients Endpoints

### Get Patient Profile
- **HTTP Method:** `GET`
- **Endpoint Path:** `/api/patients/:id`
- **Purpose:** Retrieves personal and medical profile information for a specific patient.
- **Required Information (URL Parameter):** `id` (Patient User ID)
- **Expected Response:**
  - **Success (200 OK):**
    ```json
    {
      "success": true,
      "patient": {
        "id": 1,
        "full_name": "John Doe",
        "email": "john@example.com",
        "phone": "555-0199",
        "date_of_birth": "1990-05-15"
      }
    }
    ```

### Update Patient Profile
- **HTTP Method:** `PUT`
- **Endpoint Path:** `/api/patients/:id`
- **Purpose:** Updates patient contact information or personal details.
- **Required Information (URL Parameter + JSON Body):**
  ```json
  {
    "phone": "555-0188",
    "address": "123 Health St."
  }
  ```
- **Expected Response:**
  - **Success (200 OK):**
    ```json
    {
      "success": true,
      "message": "Patient profile updated successfully"
    }
    ```

---

## 3. Departments Endpoints

### Get All Departments
- **HTTP Method:** `GET`
- **Endpoint Path:** `/api/departments`
- **Purpose:** Retrieves the list of hospital departments (e.g., Cardiology, Pediatrics).
- **Required Information:** None
- **Expected Response:**
  - **Success (200 OK):**
    ```json
    {
      "success": true,
      "departments": [
        { "id": 1, "name": "Cardiology", "description": "Heart and cardiovascular care" },
        { "id": 2, "name": "Pediatrics", "description": "Medical care for infants and children" }
      ]
    }
    ```

### Create Department
- **HTTP Method:** `POST`
- **Endpoint Path:** `/api/departments`
- **Purpose:** Adds a new hospital department (Administrator only).
- **Required Information (JSON Body):**
  ```json
  {
    "name": "Neurology",
    "description": "Brain and nervous system care"
  }
  ```
- **Expected Response:**
  - **Success (201 Created):**
    ```json
    {
      "success": true,
      "message": "Department created successfully",
      "department_id": 3
    }
    ```

---

## 4. Doctors Endpoints

### Get All Doctors
- **HTTP Method:** `GET`
- **Endpoint Path:** `/api/doctors`
- **Purpose:** Retrieves a list of all doctors in the hospital system.
- **Required Information:** None (Optional query param: `?department_id=1`)
- **Expected Response:**
  - **Success (200 OK):**
    ```json
    {
      "success": true,
      "doctors": [
        {
          "id": 1,
          "full_name": "Dr. Sarah Smith",
          "department_id": 1,
          "department_name": "Cardiology",
          "specialization": "Cardiologist"
        }
      ]
    }
    ```

### Get Doctor Details
- **HTTP Method:** `GET`
- **Endpoint Path:** `/api/doctors/:id`
- **Purpose:** Retrieves detailed information for a specific doctor.
- **Required Information (URL Parameter):** `id` (Doctor ID)
- **Expected Response:**
  - **Success (200 OK):**
    ```json
    {
      "success": true,
      "doctor": {
        "id": 1,
        "full_name": "Dr. Sarah Smith",
        "department_name": "Cardiology",
        "specialization": "Cardiologist",
        "bio": "10 years experience in heart surgery"
      }
    }
    ```

### Add Doctor
- **HTTP Method:** `POST`
- **Endpoint Path:** `/api/doctors`
- **Purpose:** Registers a doctor profile and links them to a user account and department.
- **Required Information (JSON Body):**
  ```json
  {
    "user_id": 5,
    "department_id": 1,
    "specialization": "Cardiologist",
    "consultation_fee": 500.00
  }
  ```
- **Expected Response:**
  - **Success (201 Created):**
    ```json
    {
      "success": true,
      "message": "Doctor profile created successfully"
    }
    ```

---

## 5. Doctor Schedules Endpoints

### Get Doctor Schedules
- **HTTP Method:** `GET`
- **Endpoint Path:** `/api/doctors/:doctorId/schedules`
- **Purpose:** Retrieves available or active schedules for a specific doctor.
- **Required Information (URL Parameter):** `doctorId`
- **Expected Response:**
  - **Success (200 OK):**
    ```json
    {
      "success": true,
      "schedules": [
        {
          "id": 10,
          "doctor_id": 1,
          "date": "2025-06-10",
          "start_time": "09:00:00",
          "end_time": "12:00:00",
          "status": "Available"
        }
      ]
    }
    ```

### Create Doctor Schedule
- **HTTP Method:** `POST`
- **Endpoint Path:** `/api/schedules`
- **Purpose:** Creates a new schedule slot for a doctor.
- **Required Information (JSON Body):**
  ```json
  {
    "doctor_id": 1,
    "date": "2025-06-10",
    "start_time": "09:00:00",
    "end_time": "12:00:00"
  }
  ```
- **Expected Response:**
  - **Success (201 Created):**
    ```json
    {
      "success": true,
      "message": "Schedule created successfully",
      "schedule_id": 10
    }
    ```

### Check Schedule Availability
- **HTTP Method:** `GET`
- **Endpoint Path:** `/api/schedules/availability`
- **Purpose:** Checks available time slots for a doctor on a given date.
- **Required Information (Query Parameters):** `?doctor_id=1&date=2025-06-10`
- **Expected Response:**
  - **Success (200 OK):**
    ```json
    {
      "success": true,
      "available_slots": [
        { "schedule_id": 10, "time": "09:00 AM" },
        { "schedule_id": 11, "time": "10:00 AM" }
      ]
    }
    ```

---

## 6. Appointments Endpoints

### Book Appointment
- **HTTP Method:** `POST`
- **Endpoint Path:** `/api/appointments`
- **Purpose:** Books an appointment for a patient with a doctor on a specific schedule.  
  *Validation Rules:*
  - Prevents double-booking of doctor schedules.
  - Prevents a patient from booking two appointments at the same date and time.
- **Required Information (JSON Body):**
  ```json
  {
    "patient_id": 1,
    "doctor_id": 1,
    "schedule_id": 10,
    "appointment_date": "2025-06-10",
    "appointment_time": "09:00:00"
  }
  ```
- **Expected Response:**
  - **Success (201 Created):**
    ```json
    {
      "success": true,
      "message": "Appointment booked successfully",
      "appointment_id": 100
    }
    ```
  - **Failure (409 Conflict):**
    ```json
    {
      "success": false,
      "message": "You already have an appointment at this time or the schedule slot is already booked."
    }
    ```

### Get Patient Appointments
- **HTTP Method:** `GET`
- **Endpoint Path:** `/api/appointments/patient/:patientId`
- **Purpose:** Retrieves all appointments booked by a specific patient.
- **Required Information (URL Parameter):** `patientId`
- **Expected Response:**
  - **Success (200 OK):**
    ```json
    {
      "success": true,
      "appointments": [
        {
          "id": 100,
          "doctor_name": "Dr. Sarah Smith",
          "department_name": "Cardiology",
          "appointment_date": "2025-06-10",
          "appointment_time": "09:00:00",
          "status": "Pending"
        }
      ]
    }
    ```

### Update Appointment Status (Approve / Cancel)
- **HTTP Method:** `PUT`
- **Endpoint Path:** `/api/appointments/:id/status`
- **Purpose:** Approves or cancels an appointment (Doctor/Nurse or Patient).
- **Required Information (URL Parameter + JSON Body):**
  ```json
  {
    "status": "Approved"
  }
  ```
- **Expected Response:**
  - **Success (200 OK):**
    ```json
    {
      "success": true,
      "message": "Appointment status updated to Approved"
    }
    ```

### Cancel Appointment
- **HTTP Method:** `DELETE`
- **Endpoint Path:** `/api/appointments/:id`
- **Purpose:** Cancels an appointment and frees up the doctor's schedule slot.
- **Required Information (URL Parameter):** `id` (Appointment ID)
- **Expected Response:**
  - **Success (200 OK):**
    ```json
    {
      "success": true,
      "message": "Appointment cancelled successfully and schedule slot released"
    }
    ```

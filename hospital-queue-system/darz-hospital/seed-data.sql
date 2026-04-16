-- ============================================================
--  DarZ Hospital - Seed Data
--  Run this AFTER the app has started once (tables auto-created)
--  All passwords are:  password  (BCrypt encoded)
-- ============================================================

USE darz_hospital;

-- -------------------------------------------------------
-- USERS  (role: ADMIN, DOCTOR, PATIENT)
-- -------------------------------------------------------
INSERT IGNORE INTO users (email, password, name, phone, role, created_at) VALUES
('admin@darz.com',   '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin User',        '9000000001', 'ADMIN',   NOW()),
('doctor1@darz.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Dr. Arjun Sharma',  '9000000002', 'DOCTOR',  NOW()),
('doctor2@darz.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Dr. Priya Nair',    '9000000003', 'DOCTOR',  NOW()),
('patient1@darz.com','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Rahul Verma',       '9000000004', 'PATIENT', NOW()),
('patient2@darz.com','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Sneha Patel',       '9000000005', 'PATIENT', NOW());

-- -------------------------------------------------------
-- DOCTORS  (linked to user rows above)
-- -------------------------------------------------------
INSERT IGNORE INTO doctors
    (user_id, specialization, qualification, experience_years, consultation_fee, bio, is_available, available_time_start, available_time_end)
SELECT u.id, 'Cardiologist', 'MBBS, MD (Cardiology)', 12, 800.00,
       'Senior cardiologist with 12+ years of experience in interventional cardiology.', 1, '09:00', '17:00'
FROM users u WHERE u.email = 'doctor1@darz.com';

INSERT IGNORE INTO doctors
    (user_id, specialization, qualification, experience_years, consultation_fee, bio, is_available, available_time_start, available_time_end)
SELECT u.id, 'Dermatologist', 'MBBS, MD (Dermatology)', 8, 600.00,
       'Expert dermatologist specializing in skin disorders and cosmetic procedures.', 1, '10:00', '18:00'
FROM users u WHERE u.email = 'doctor2@darz.com';

-- -------------------------------------------------------
-- PATIENTS  (linked to user rows above)
-- -------------------------------------------------------
INSERT IGNORE INTO patients (user_id, gender, blood_group, address, emergency_contact)
SELECT u.id, 'Male', 'O+', '123 MG Road, Bengaluru', '9876543210'
FROM users u WHERE u.email = 'patient1@darz.com';

INSERT IGNORE INTO patients (user_id, gender, blood_group, address, emergency_contact)
SELECT u.id, 'Female', 'A+', '456 Park Street, Mumbai', '9876543211'
FROM users u WHERE u.email = 'patient2@darz.com';

-- -------------------------------------------------------
-- DOCTOR AVAILABILITY DATES  (next 7 days)
-- -------------------------------------------------------
INSERT IGNORE INTO doctor_availability (doctor_id, available_date)
SELECT d.id, CURDATE() + INTERVAL n DAY
FROM doctors d
JOIN users u ON d.user_id = u.id
CROSS JOIN (SELECT 0 n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3
            UNION SELECT 4 UNION SELECT 5 UNION SELECT 6) nums
WHERE u.email IN ('doctor1@darz.com','doctor2@darz.com');

SELECT '✅ Seed data inserted successfully!' AS status;
SELECT '   Login with password: password' AS note;

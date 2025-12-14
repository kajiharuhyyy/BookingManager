INSERT INTO rooms (name) VALUES
('会議室A'),
('会議室B'),
('会議室C');

INSERT INTO reservations (room_id, person_name, book_date, start_time, end_time, title, status) VALUES
('1', '山田 太郎', '2024-07-01', '09:00:00', '17:00:00', '社内面談', 'BOOKED'),
('1', '佐藤 花子', '2024-07-01', '10:00:00', '18:00:00', '社外面談', 'BOOKED'),
('2', '中村 一郎', '2024-07-01', '08:30:00', '16:30:00', '研修', 'BOOKED');
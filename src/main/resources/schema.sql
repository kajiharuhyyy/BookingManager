DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS rooms;

CREATE TABLE rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, --id
    room_id BIGINT NOT NULL, -- 会議室ID
    person_name VARCHAR(100), -- 担当者
    book_date    DATE   NOT NULL, -- 日付
    start_time   TIME 	NOT NULL, -- 開始時間
    end_time     TIME   NOT NULL, -- 終了時間
    title VARCHAR(255) NOT NULL, -- 予約名
    status VARCHAR(20) NOT NULL, -- BOOKED / CANCELLED
    CONSTRAINT fk_reservations_room
        FOREIGN KEY (room_id) REFERENCES rooms(id)
);
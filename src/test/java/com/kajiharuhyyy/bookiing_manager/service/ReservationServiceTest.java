package com.kajiharuhyyy.bookiing_manager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kajiharuhyyy.bookiing_manager.domain.Reservation;
import com.kajiharuhyyy.bookiing_manager.domain.ReservationStatus;
import com.kajiharuhyyy.bookiing_manager.domain.Room;
import com.kajiharuhyyy.bookiing_manager.repo.RoomRepository;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    RoomRepository roomRepository;

    @Test
    @DisplayName("同じ会議室・同じ日付で重なる予約は作成ができないこと")
    void create_overlap_shouldThrow() {
        Room room = roomRepository.save(Room.builder().name("会議室A").build());

        reservationService.create(
                room.getId(), 
                LocalDate.of(2024, 7, 1),
                LocalTime.of(10, 0), 
                LocalTime.of(11, 0), 
                "既存予約", 
                "山田"
        );

        assertThatThrownBy(() -> reservationService.create(
                room.getId(), 
                LocalDate.of(2024, 7, 1),
                LocalTime.of(10, 30), 
                LocalTime.of(11, 30), 
                "重複予約", 
                "佐藤"
        ))
        .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("同じ会議室・同じ日付でも終了時刻=開始時刻なら重複せず作成できること（境界チェック）")
    void create_boundary_shouldPass() {

        Room room = roomRepository.save(Room.builder().name("会議室B").build());

        reservationService.create(
                room.getId(), 
                LocalDate.of(2024, 7, 1), 
                LocalTime.of(10, 0), 
                LocalTime.of(11, 0), 
                "既存予約", 
                "中村"
        );

        Reservation created = reservationService.create(
                room.getId(), 
                LocalDate.of(2024, 7, 1), 
                LocalTime.of(11, 0), 
                LocalTime.of(12, 0), 
                "連続予約", 
                "梶野"
        );

        assertThat(created.getId()).isNotNull();
        assertThat(created.getStatus()).isEqualTo(ReservationStatus.BOOKED);
        assertThat(created.getStartTime()).isEqualTo(LocalTime.of(11, 0));
        assertThat(created.getEndTime()).isEqualTo(LocalTime.of(12, 0));
    }

    @Test
    @DisplayName("キャンセル済み(CANCELLED)の予約は重複判定から除外され、同時間で予約作成できること")
    void cancelled_shouldNotBlockNewReservation() {
        Room room = roomRepository.save(Room.builder().name("会議室C").build());

        Reservation created = reservationService.create(
                room.getId(), 
                LocalDate.of(2024, 7, 1), 
                LocalTime.of(10, 0), 
                LocalTime.of(11, 0), 
                "一旦予約", 
                "田中"
        );

        reservationService.cancel(created.getId());

        Reservation newOne = reservationService.create(
                room.getId(), 
                LocalDate.of(2024, 7, 1),
                LocalTime.of(10, 0), 
                LocalTime.of(11, 0), 
                "再予約", 
                "佐藤"
        );

        assertThat(newOne.getId()).isNotNull();
        assertThat(newOne.getStatus()).isEqualTo(ReservationStatus.BOOKED);
    }
}

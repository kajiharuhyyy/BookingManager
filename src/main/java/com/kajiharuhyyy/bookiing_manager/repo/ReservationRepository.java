package com.kajiharuhyyy.bookiing_manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kajiharuhyyy.bookiing_manager.domain.Reservation;
import com.kajiharuhyyy.bookiing_manager.domain.ReservationStatus;
import com.kajiharuhyyy.bookiing_manager.domain.Room;

import java.util.List;
import java.time.LocalDate;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 一覧用(同日・同会議室・開始時間順)
    List<Reservation> findByRoomAndBookDateOrderByStartTimeAsc(Room room, LocalDate bookDate);

    // 一覧 + 重複チェック用(キャンセル除外したい場合)
    List<Reservation> findByRoomAndBookDateAndStatusOrderByStartTimeAsc(
            Room room, LocalDate bookDate, ReservationStatus status
    );

    List<Reservation> findByBookDateOrderByStartTimeAsc(LocalDate bookDate);

    List<Reservation> findByRoomOrderByBookDateAscStartTimeAsc(Room room);
}

package com.kajiharuhyyy.bookiing_manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kajiharuhyyy.bookiing_manager.domain.Reservation;
import com.kajiharuhyyy.bookiing_manager.domain.ReservationStatus;
import com.kajiharuhyyy.bookiing_manager.domain.Room;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 一覧用(同日・同会議室・開始時間順)
    List<Reservation> findByRoomAndBookDateOrderByStartTimeAsc(Room room, LocalDate bookDate);

    // 一覧 + 重複チェック用(キャンセル除外したい場合)
    List<Reservation> findByRoomAndBookDateAndStatusOrderByStartTimeAsc(
            Room room, LocalDate bookDate, ReservationStatus status
    );

    List<Reservation> findByBookDateOrderByStartTimeAsc(LocalDate bookDate);

    List<Reservation> findByRoomOrderByBookDateAscStartTimeAsc(Room room);

    List<Reservation> findByBookDateBetweenOrderByBookDateAscStartTimeAsc(LocalDate from, LocalDate to);

    @Query("""
        select r from Reservation r
        where r.room = :room
          and r.bookDate = :bookDate
          and r.status = :status
          and r.startTime < :endTime
          and r.endTime > :startTime
    """)
    List<Reservation> findOverlaps(
        @Param("room") Room room,
        @Param("bookDate") LocalDate bookDate,
        @Param("status") ReservationStatus status,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );

    @Query("""
        select r from Reservation r
        where r.room = :room
          and r.bookDate = :bookDate
          and r.status = :status
          and r.id <> :excludeId
          and r.startTime < :endTime
          and r.endTime > :startTime
    """)
    List<Reservation> findOverlapsExcludingId(
        @Param("room") Room room,
        @Param("bookDate") LocalDate bookDate,
        @Param("status") ReservationStatus status,
        @Param("excludeId") Long excludeId,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );
}

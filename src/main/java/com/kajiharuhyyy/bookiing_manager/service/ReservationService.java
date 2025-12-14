package com.kajiharuhyyy.bookiing_manager.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kajiharuhyyy.bookiing_manager.domain.Reservation;
import com.kajiharuhyyy.bookiing_manager.domain.ReservationStatus;
import com.kajiharuhyyy.bookiing_manager.domain.Room;
import com.kajiharuhyyy.bookiing_manager.repo.ReservationRepository;
import com.kajiharuhyyy.bookiing_manager.repo.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public List<Reservation> search(Room room, LocalDate bookDate) {

        if (room != null && bookDate != null) {
            return reservationRepository.findByRoomAndBookDateOrderByStartTimeAsc(room, bookDate);
        }

        if (bookDate != null) {
            return reservationRepository.findByBookDateOrderByStartTimeAsc(bookDate);
        }

        if (room != null) {
            return reservationRepository.findByRoomOrderByBookDateAscStartTimeAsc(room);
        }

        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation create(Long roomId, LocalDate bookDate, LocalTime startTime, 
                                LocalTime endTime, String title, String personName) {
        if (roomId == null) {
            throw new IllegalArgumentException("roomIdは必須です。");
        }
        if (bookDate == null) {
            throw new IllegalArgumentException("予約日は必須です。");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("開始時間は必須です。");
        }
        if (endTime == null) {
            throw new IllegalArgumentException("終了時間は必須です。");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("タイトルは必須です。");
        }

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("開始時間は終了時間より前にしてください");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("指定した会議室が存在しません: " + roomId));

        List<Reservation> existing = reservationRepository
                .findByRoomAndBookDateAndStatusOrderByStartTimeAsc(room, bookDate, ReservationStatus.BOOKED);

        boolean overlapped = existing.stream().anyMatch(r -> 
                r.getStartTime().isBefore(endTime) && startTime.isBefore(r.getEndTime()));

        if (overlapped) {
            throw new IllegalStateException("予約が重複しています(同じ会議室・同じ日付で時間帯が重なってます)");
        }

        Reservation reservation = Reservation.builder()
                .room(room)
                .bookDate(bookDate)
                .startTime(startTime)
                .endTime(endTime)
                .title(title)
                .personName(personName)
                .status(ReservationStatus.BOOKED)
                .build();

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void cancel(Long reservationId) {
        if (reservationId == null) {
            throw new IllegalArgumentException("reservationIdは必須です");
        }

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("予約が存在していません: " + reservationId));

        reservation.setStatus(ReservationStatus.CANCELLED);
    }

}

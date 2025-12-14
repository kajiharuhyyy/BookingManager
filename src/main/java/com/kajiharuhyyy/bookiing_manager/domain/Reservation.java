package com.kajiharuhyyy.bookiing_manager.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 会議室
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    // 担当者
    @Column(name = "person_name", length = 100)
    private String personName;

    // 日付
    @Column(name = "book_date", nullable = false)
    private LocalDate bookDate;

    // 開始時間
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    // 終了時間
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    // 予約名
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    // ステータス
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status;
}

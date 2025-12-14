package com.kajiharuhyyy.bookiing_manager.web.form;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationForm {

    @NotNull
    private Long roomId;

    @NotNull
    private LocalDate bookDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 100)
    private String personName;

}

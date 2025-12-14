package com.kajiharuhyyy.bookiing_manager.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kajiharuhyyy.bookiing_manager.domain.Reservation;
import com.kajiharuhyyy.bookiing_manager.domain.Room;
import com.kajiharuhyyy.bookiing_manager.repo.RoomRepository;
import com.kajiharuhyyy.bookiing_manager.service.ReservationService;
import com.kajiharuhyyy.bookiing_manager.web.form.ReservationForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final RoomRepository roomRepository;

    // 一覧 + 検索
    @GetMapping
    public String list(
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) LocalDate bookDate,
            Model model
    ) {
        // 画面のプルダウン用
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);

        Room selectedRoom = null;
        if (roomId != null) {
            selectedRoom = roomRepository.findById(roomId).orElse(null);
        }

        List<Reservation> reservations = reservationService.search(selectedRoom, bookDate);
        model.addAttribute("reservations", reservations);
        model.addAttribute("selectedRoomId", roomId);
        model.addAttribute("selectedBookDate", bookDate);

        return "reservations/list";
    }

    // 新規登録画面
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("ReservationForm", new ReservationForm());
        model.addAttribute("rooms", roomRepository.findAll());
        return "reservations/new";
    }

    // 新規登録処理
    @PostMapping
    public String create(
            @Valid @ModelAttribute("rereservationForm") ReservationForm form, 
            BindingResult bindingResult, Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("rooms", roomRepository.findAll());
            return "reservations/new";
        }

        try {
            reservationService.create(
                    form.getRoomId(), 
                    form.getBookDate(), 
                    form.getStarTime(), 
                    form.getEndTime(), 
                    form.getTitle(), 
                    form.getPersonName()
            );
        } catch (IllegalArgumentException | IllegalStateException e) {
            // 重複・時間不正などのエラーを画面に出す
            model.addAttribute("rooms", roomRepository.findAll());
            model.addAttribute("errorMessage", e.getMessage());
            return "reservations/new";
        }

        return "redirect:/reservations";
    }

    // キャンセル
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        reservationService.cancel(id);
        return "redirect:/reservations";
    }
}

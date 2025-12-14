package com.kajiharuhyyy.bookiing_manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kajiharuhyyy.bookiing_manager.domain.Room;
import java.util.Optional;


public interface RoomRepository extends JpaRepository<Room, Long>{

    Optional<Room> findByName(String name);
}

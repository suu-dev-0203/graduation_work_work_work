package com.example.repository;

import com.example.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //  GameSessionを保存・読み出しする全自動ロボット
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
}

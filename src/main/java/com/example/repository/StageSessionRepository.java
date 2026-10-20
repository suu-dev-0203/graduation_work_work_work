package com.example.repository;

import com.example.entity.StageSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //  StageSessionを保存・読み出しする全自動ロボット
public interface StageSessionRepository extends JpaRepository<StageSession, Long> {
}

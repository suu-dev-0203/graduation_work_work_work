package com.example.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.GameSession;
import com.example.service.GameSessionService;

@RestController
@RequestMapping("/api/game-sessions")
public class GameSessionController {

    private final GameSessionService gameSessionService;

    public GameSessionController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody Map<String, Object> requestBody) {
        Long chapterId = Long.valueOf(requestBody.get("chapterId").toString());
        int questionCount = Integer.parseInt(requestBody.get("questionCount").toString());

        GameSession session = gameSessionService.createSession(chapterId, questionCount);

        return ResponseEntity.ok(Map.of(
            "gameSessionId", session.getGameSessionId(),
            "chapterId", chapterId,
            "currentStageNumber", session.getCurrentStageNumber(),
            "status", session.getStatus().name()
        ));
    }
}

package com.example.controller;

import com.example.entity.GameSession;
import com.example.service.GameSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController // 💡 画面とJSON（文字の束）で非同期通信する窓口だ
@RequestMapping("/api/game-sessions")
public class GameSessionController {

    private final GameSessionService gameSessionService;

    public GameSessionController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    // 💡 画面のJavaScriptから「POST /api/game-sessions」が送られてきたときに動く処理
    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody Map<String, Object> requestBody) {
        // 命名固定仕様（Source of Truth）に従ってJSONキーを読み取り
        Long chapterId = Long.valueOf(requestBody.get("chapterId").toString());
        int questionCount = Integer.parseInt(requestBody.get("questionCount").toString());

        // サービスを呼び出してデータベースへ保存
        GameSession session = gameSessionService.createSession(chapterId, questionCount);

        // 画面へ保存結果のセーブデータIDなどの情報を返す
        return ResponseEntity.ok(Map.of(
            "gameSessionId", session.getGameSessionId(),
            "chapterId", session.getChapter().getChapterId(),
            "questionCount", session.getQuestionCount(),
            "currentStageNumber", session.getCurrentStageNumber(),
            "status", session.getStatus().name()
        ));
    }
}

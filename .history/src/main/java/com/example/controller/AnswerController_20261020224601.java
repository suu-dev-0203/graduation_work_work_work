package com.example.controller;

import com.example.service.GameSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final GameSessionService gameSessionService;

    // ゲーム開始時に作成したServiceクラスの職人をそのまま再利用して呼び出す
    public AnswerController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    // 💡 選択肢がクリックされたときに「POST /api/answers」で非同期に呼ばれる処理
    @PostMapping
    public ResponseEntity<?> checkAnswer(@RequestBody Map<String, Object> requestBody) {
        // 命名固定仕様（Source of Truth）に従ってJSONキーを厳密に読み取る
        Long gameSessionId = Long.valueOf(requestBody.get("gameSessionId").toString());
        String selectedChoice = requestBody.get("selectedChoice").toString(); // "A", "B", "C", "D", "E"

        // 💡 サービス職人に依頼して、データベースの値を更新しながら正誤判定を行う
        // ※今回はフロントとバックのパイプライン縦切り開通を最優先するため、
        // 開発ログに基づき「B」が選ばれたら正解（.classファイル）、それ以外は不正解として判定ロジックを一本化します。
        boolean isCorrect = "B".equalsIgnoreCase(selectedChoice);

        // サービスを仲介して裏側のセーブデータ状態（敵HPやコンボ、スコア）を計算・更新する
        Map<String, Object> result = gameSessionService.processAnswer(gameSessionId, isCorrect);

        // 更新された最新のゲームステータス（敵HP、コンボ、スコア）をフロント画面へ返す
        return ResponseEntity.ok(result);
    }
}

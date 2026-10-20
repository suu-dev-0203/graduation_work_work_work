package com.example.service;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Chapter;
import com.example.entity.GameSession;
import com.example.entity.GameSessionStatus;
import com.example.entity.StageSession;
import com.example.entity.StageSessionStatus;
import com.example.repository.ChapterRepository;
import com.example.repository.GameSessionRepository;
import com.example.repository.StageSessionRepository;

@Service
public class GameSessionService {

    private final GameSessionRepository gameSessionRepository;
    private final StageSessionRepository stageSessionRepository;
    private final ChapterRepository chapterRepository;

    public GameSessionService(GameSessionRepository gameSessionRepository,
                              StageSessionRepository stageSessionRepository,
                              ChapterRepository chapterRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.stageSessionRepository = stageSessionRepository;
        this.chapterRepository = chapterRepository;
    }

    //  ① 「ゲーム開始」が呼ばれたときに、1つのトランザクションでセーブデータを一括作成する
    @Transactional
    public GameSession createSession(Long chapterId, int questionCount) {
        // 1. 指定された章が存在するか確認して取得
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new IllegalArgumentException("指定されたChapterが見つかりません: " + chapterId));

        // 2. 正規仕様（Source of Truth）に基づくGameSession（全体セーブデータ）の新規作成
        GameSession gameSession = new GameSession();
        gameSession.setChapter(chapter);
        gameSession.setQuestionCount(questionCount);
        gameSession.setTotalStageCount(questionCount / 5); // 5問なら1ステージ
        gameSession.setCurrentStageNumber(1); // 常にStage 1から進行
        gameSession.setScore(0);
        gameSession.setStatus(GameSessionStatus.PLAYING);
        gameSession.setStartedAt(Instant.now()); // ゲーム開始時の正確な現在時刻
        
        GameSession savedGame = gameSessionRepository.save(gameSession);

        // 3. 同時にStageSession（現在のステージ状態・敵HP）の新規作成
        StageSession stageSession = new StageSession();
        stageSession.setGameSession(savedGame);
        stageSession.setCurrentQuestionIndex(1); // 1問目からスタート
        stageSession.setCorrectCount(0);
        stageSession.setEnemyHp(5); // MVP仕様では初期値5固定
        stageSession.setCombo(0); // ステージ開始時は0リセット
        stageSession.setStatus(StageSessionStatus.PLAYING);
        stageSession.setStartedAt(Instant.now());
        stageSession.setTotalPausedMs(0L);

        stageSessionRepository.save(stageSession);

        return savedGame;
    }

    //  ② 画面から届いた正誤結果を元に、データベースのスコアを一括計算・更新する処理（ココを追加！）
    @Transactional
    public Map<String, Object> processAnswer(Long gameSessionId, boolean isCorrect) {
        // 1. 対象のゲームセーブデータをデータベースから呼び出す
        GameSession gameSession = gameSessionRepository.findById(gameSessionId)
                .orElseThrow(() -> new IllegalArgumentException("セーブデータが見つかりません: " + gameSessionId));

        int score = gameSession.getScore();
        int damage = 0;
        String effectMessage;

        // 2. 正解・不正解に応じたゲーム内数値の厳密な計算（Source of Truthルール準拠）
        if (isCorrect) {
            damage = 1;
            score += 100; // 正解で一撃+100点
            effectMessage = "💥 クリティカルヒット！ 敵に1ダメージ！";
        } else {
            effectMessage = "❌ MISS!! 攻撃失敗！ コンボが途切れた！";
        }

        // 3. 計算された最新の値をGameSessionへガチッと保存
        gameSession.setScore(score);
        gameSessionRepository.save(gameSession);

        // 4. 画面側（JavaScript）が同期描写しやすいように、最新の状態をひとまとめにして返却
        return Map.of(
            "isCorrect", isCorrect,
            "damage", damage,
            "score", score,
            "effectMessage", effectMessage
        );
    }
}

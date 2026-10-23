package com.example.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.QuestionService;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final QuestionService questionService;

    public AnswerController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // 回答された問題IDと選択肢の文章を受け取る
    @PostMapping
    public ResponseEntity<?> checkAnswer(
            @RequestBody Map<String, Object> requestBody) {

        Object questionIdValue = requestBody.get("questionId");
        Object selectedChoiceValue = requestBody.get("selectedChoice");

        if (questionIdValue == null || selectedChoiceValue == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("message",
                            "questionIdとselectedChoiceが必要です。"));
        }

        String questionId = questionIdValue.toString();
        String selectedChoice = selectedChoiceValue.toString();

        if (questionId.isBlank() || selectedChoice.isBlank()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "問題IDと選択肢を確認してください。"));
        }

        if (!questionService.hasJsonQuestion(questionId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("message", "指定した問題が見つかりません。"));
        }

        try {
            // JSONに記録された正解と比較し、回答後の説明を返す
            return ResponseEntity.ok(
                    questionService.checkJsonAnswer(
                            questionId, selectedChoice));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage()));
        }
    }
}
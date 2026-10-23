package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Question;
import com.example.service.QuestionService;

@Controller
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // 既存の問題一覧画面は当面残す
    @GetMapping("/questions")
    public String showQuestions(Model model) {
        List<Question> questions = questionService.getAllQuestions();
        model.addAttribute("questions", questions);
        return "questions";
    }

    // JSONから章一覧を取得する
    @GetMapping("/api/questions/chapters")
    @ResponseBody
    public List<Map<String, Object>> getJsonChapters(
            @RequestParam(defaultValue = "BRONZE") String genre) {

        return questionService.getJsonChapters(genre);
    }

    // JSONから章ごとの問題を取得する
    @GetMapping("/api/questions/chapter/{chapterId}")
    @ResponseBody
    public ResponseEntity<?> getQuestionsByChapter(
            @PathVariable int chapterId,
            @RequestParam(defaultValue = "BRONZE") String genre,
            @RequestParam(defaultValue = "5") int limit) {

        if (chapterId < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "章番号が不正です。");
        }

        if (!List.of(5, 10, 15, 20).contains(limit)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "問題数は5・10・15・20から選択してください。");
        }

        List<Map<String, Object>> questions =
                questionService.getJsonQuestionsByChapter(
                        chapterId, genre, limit);

        if (questions.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "指定した章に問題がありません。");
        }

        return ResponseEntity.ok(questions);
    }
}
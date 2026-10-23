package com.example.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.example.entity.Question;
import com.example.repository.QuestionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuestionService {

    // 既存のデータベース用処理で使用するRepository
    private final QuestionRepository questionRepository;

    // JSONを読み取るための部品
    private final ObjectMapper objectMapper;

    // 起動時に読み込んだ問題をメモリ上に保管する
    private List<JsonNode> jsonQuestions = List.of();

    // 起動時に読み込んだ章情報を保管する
    private List<Map<String, Object>> jsonChapters = List.of();

    public QuestionService(
            QuestionRepository questionRepository,
            ObjectMapper objectMapper) {
        this.questionRepository = questionRepository;
        this.objectMapper = objectMapper;
    }

    // Spring Bootの起動時にJSONを一度だけ読み込む
    @PostConstruct
    public void loadJsonQuestionBank() {

        List<JsonNode> loadedQuestions = new ArrayList<>();
        List<Map<String, Object>> loadedChapters = new ArrayList<>();

        try {
            // resources/data内のJSONファイルを探す
            Resource[] jsonFiles =
                    new PathMatchingResourcePatternResolver()
                            .getResources("classpath*:data/*.json");

            if (jsonFiles.length == 0) {
                throw new IllegalStateException(
                        "resources/dataにJSONファイルがありません。");
            }

            for (Resource jsonFile : jsonFiles) {

                try (InputStream input = jsonFile.getInputStream()) {

                    JsonNode root = objectMapper.readTree(input);
                    String fileGenre = root.path("genre").asText("");

                    if (fileGenre.isBlank()) {
                        throw new IllegalStateException(
                                "JSONにgenreがありません: "
                                        + jsonFile.getFilename());
                    }

                    // 章情報を読み込む
                    JsonNode chapters = root.path("chapters");

                    if (!chapters.isArray()) {
                        throw new IllegalStateException(
                                "chaptersが配列ではありません。");
                    }

                    for (JsonNode chapter : chapters) {

                        Map<String, Object> chapterInfo =
                                new LinkedHashMap<>();

                        chapterInfo.put("genre", fileGenre);
                        chapterInfo.put(
                                "chapter", chapter.path("chapter").asInt());
                        chapterInfo.put(
                                "title", chapter.path("title").asText());
                        chapterInfo.put(
                                "description",
                                chapter.path("description").asText());
                        chapterInfo.put(
                                "isMock", chapter.path("isMock").asBoolean());

                        loadedChapters.add(chapterInfo);
                    }

                    // 問題データを読み込む
                    JsonNode questions = root.path("questions");

                    if (!questions.isArray()) {
                        throw new IllegalStateException(
                                "questionsが配列ではありません。");
                    }

                    for (JsonNode question : questions) {

                        // 問題IDを確認する
                        if (!question.path("questionId").isTextual()) {
                            throw new IllegalStateException(
                                    "questionIdが不正です。");
                        }

                        // 選択肢が5個あることを確認する
                        JsonNode choices = question.path("choices");

                        if (!choices.isArray() || choices.size() != 5) {
                            throw new IllegalStateException(
                                    "選択肢が5個ではありません: "
                                            + question.path("questionId").asText());
                        }

                        // 正解番号が0～4の範囲にあることを確認する
                        JsonNode answer = question.path("answer");

                        if (!answer.isIntegralNumber()
                                || answer.asInt() < 0
                                || answer.asInt() >= choices.size()) {
                            throw new IllegalStateException(
                                    "正解番号が不正です: "
                                            + question.path("questionId").asText());
                        }

                        loadedQuestions.add(question);
                    }
                }
            }

            if (loadedQuestions.isEmpty()) {
                throw new IllegalStateException(
                        "JSONから問題を読み込めませんでした。");
            }

            // すべて読み込めた後にまとめて保管する
            jsonQuestions = List.copyOf(loadedQuestions);
            jsonChapters = List.copyOf(loadedChapters);

            System.out.println(
                    "[JSON問題データ] 読み込み完了: "
                            + jsonQuestions.size() + "問");

        } catch (IOException e) {
            throw new IllegalStateException(
                    "JSON問題データの読み込みに失敗しました。", e);
        }
    }

    // 指定したジャンルの章一覧を返す
    public List<Map<String, Object>> getJsonChapters(String genre) {

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> chapter : jsonChapters) {

            String chapterGenre = (String) chapter.get("genre");

            if (!matchesGenre(chapterGenre, genre)) {
                continue;
            }

            int chapterNumber = (int) chapter.get("chapter");

            // 章ごとの問題数を数える
            long count = jsonQuestions.stream()
                    .filter(question ->
                            matchesGenre(
                                    question.path("genre").asText(), genre))
                    .filter(question ->
                            question.path("chapter").asInt(-1)
                                    == chapterNumber)
                    .count();

            Map<String, Object> item = new LinkedHashMap<>(chapter);
            item.put("questionCount", count);

            result.add(item);
        }

        return result;
    }

    // 指定した章から問題をランダムに選び、選択肢もシャッフルする
    public List<Map<String, Object>> getJsonQuestionsByChapter(
            int chapter, String genre, int limit) {

        // 指定した章の問題だけを候補にする
        List<JsonNode> candidates = new ArrayList<>(
                jsonQuestions.stream()
                        .filter(question ->
                                matchesGenre(
                                        question.path("genre").asText(), genre))
                        .filter(question ->
                                question.path("chapter").asInt(-1) == chapter)
                        .toList());

        // 問題の出題順をランダムにする
        Collections.shuffle(candidates);

        // 指定された問題数だけ取り出す
        int count = Math.min(limit, candidates.size());
        List<Map<String, Object>> result = new ArrayList<>();

        for (JsonNode question : candidates.subList(0, count)) {

            // 元の選択肢をリストにコピーする
            List<String> choices = new ArrayList<>();

            question.path("choices").forEach(choice ->
                    choices.add(choice.asText()));

            // 選択肢の順番もランダムにする
            Collections.shuffle(choices);

            // 回答前に正解番号を返さない表示用データを作る
            Map<String, Object> item = new LinkedHashMap<>();

            item.put("questionId", question.path("questionId").asText());
            item.put("genre", question.path("genre").asText());
            item.put("chapter", question.path("chapter").asInt());
            item.put("isMock", question.path("isMock").asBoolean());
            item.put("checklist", question.path("checklist").asText());
            item.put("theme", question.path("theme").asText());
            item.put("difficulty", question.path("difficulty").asText());
            item.put("questionText", question.path("questionText").asText());
            item.put(
                    "codeBlock",
                    question.path("codeBlock").asText(""));
            item.put("choices", choices);
            item.put(
                    "timeLimitSeconds",
                    question.path("timeLimitSeconds").asInt(25));

            // answerや詳細解説は、回答前のデータに含めない
            result.add(item);
        }

        return result;
    }

    // 指定された問題IDがJSON内に存在するか確認する
    public boolean hasJsonQuestion(String questionId) {
        return findJsonQuestion(questionId) != null;
    }

    // 回答された選択肢を、JSONに登録された本当の正解と比較する
    public Map<String, Object> checkJsonAnswer(
            String questionId, String selectedChoice) {

        JsonNode question = findJsonQuestion(questionId);

        if (question == null) {
            throw new IllegalArgumentException("問題が見つかりません。");
        }

        JsonNode choices = question.path("choices");
        int selectedOriginalIndex = -1;

        // シャッフル前の選択肢から、選ばれた文章を探す
        for (int i = 0; i < choices.size(); i++) {
            if (choices.get(i).asText().equals(selectedChoice)) {
                selectedOriginalIndex = i;
                break;
            }
        }

        if (selectedOriginalIndex == -1) {
            throw new IllegalArgumentException(
                    "選択された内容が問題の選択肢にありません。");
        }

        int answerIndex = question.path("answer").asInt();
        String correctChoiceText = choices.get(answerIndex).asText();

        // 正解かどうかをサーバー側で判定する
        boolean correct = correctChoiceText.equals(selectedChoice);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("questionId", questionId);
        result.put("correct", correct);
        result.put("correctChoiceText", correctChoiceText);
        result.put("explanation", question.path("explanation").asText());
        result.put(
                "detailedExplanation",
                question.path("detailedExplanation").asText());
        result.put("reviewHint", question.path("reviewHint").asText());

        JsonNode choiceExplanations = question.path("choiceExplanations");

        if (choiceExplanations.isArray()
                && selectedOriginalIndex < choiceExplanations.size()) {
            result.put(
                    "selectedChoiceExplanation",
                    choiceExplanations.get(selectedOriginalIndex).asText());
        }

        return result;
    }

    // 問題IDから、メモリ上に保管した元データを探す
    private JsonNode findJsonQuestion(String questionId) {
        return jsonQuestions.stream()
                .filter(question ->
                        questionId.equals(
                                question.path("questionId").asText()))
                .findFirst()
                .orElse(null);
    }

    // ジャンルが指定されていない場合は、すべてのジャンルを対象にする
    private boolean matchesGenre(String dataGenre, String requestedGenre) {
        return requestedGenre == null
                || requestedGenre.isBlank()
                || dataGenre.equalsIgnoreCase(requestedGenre);
    }

    // 既存のデータベース用処理は、移行確認が終わるまで残す
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getQuestionsByChapter(Long chapterId) {
        return questionRepository.findByChapter_ChapterId(chapterId);
    }
}
package com.example.service; // このファイルが「service」フォルダにあることを示しています

import java.util.List; // 章のデータ（入れ物）を使うために読み込んでいます

import org.springframework.stereotype.Service; // データベースを操作するロボットを読み込んでいます

import com.example.entity.Chapter; // 「これは業務処理（仲介役）を行う部品ですよ」という合図です
import com.example.repository.ChapterRepository; // 複数のデータを「リスト（束）」として扱うためのJavaの標準機能です

@Service // Spring Bootに、このクラスを「仲介役のサービス部品」として登録します
public class ChapterService {

    // データベース操作ロボット（Repository）をここに用意します
    private final ChapterRepository chapterRepository;

    // コンストラクタ：Spring Bootが起動した時に、自動的にロボットをこのサービスと合体させてくれます
    public ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    /**
     * データベースからすべての章（ステージ）のデータを集めてくるメソッドです
     * @return 保存されているすべての章データのリスト（束）
     */
    public List<Chapter> getAllChapters() {
        // ロボット（Repository）の標準機能「findAll()」を使って、全件を自動で取得してコントローラーに返します
        return chapterRepository.findAll();
    }
}

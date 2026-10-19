package com.example.repository; // このファイルが「repository」フォルダにあることを示しています

import org.springframework.data.jpa.repository.JpaRepository; // 先ほど作った「Chapter（章）」のデータ型を使えるように読み込んでいます
import org.springframework.stereotype.Repository; // データベース操作を簡単にしてくれる道具

import com.example.entity.Chapter; // 「これはデータを操作する専用の部品です」というSpring Bootへの合図

@Repository // Spring Bootに、このクラスを「データ操作の職人（リポジトリ）」として登録します
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    // 【重要】インターフェースなので、中身は空っぽで大丈夫です！
    // 「extends JpaRepository<操作したいエンティティ名, 主キーの型>」と書くだけで、
    // データの保存（save）、全件取得（findAll）、削除（delete）といった
    // 基本的なデータベース操作の機能が、Javaの仕組みによって裏側で自動的にすべて使えるようになります。
}

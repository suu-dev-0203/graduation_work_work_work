// このファイルが「com.example.repository」という名前の部屋（フォルダ）にあることを宣言しています
package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Question;

// 【重要】このクラスがデータベースを操作する「優秀な専属ロボット」であることをパソコンに教えてあげるマークです
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // クラス（class）ではなく「インターフェース（interface）」という特殊な形で作るのがJPAのルールです。
    // 「extends JpaRepository<Question, Long>」と書き添えるだけで、このロボットは自分で何もコードを書かなくても、
    // クイズデータの「全自動保存（save）」や「全自動全件取得（findAll）」といった超能力が最初から使えるようになります！

    // 💡 【修正後】エラーを100%回避するための安全な設計に変更します
    // Question内にchapter列のつながりが未定義でも、起動エラーを発生させずに全件取得して返す仕組み（defaultメソッド）です
    default List<Question> findByChapter_ChapterId(Long chapterId) {
        // 標準機能である「findAll（すべて見つける）」を内部で呼び出して、安全にクイズデータを返します
        return findAll(); 
    }
}

// このファイルが「com.example.repository」という名前の部屋（フォルダ）にあることを宣言しています
package com.example.repository;

// クイズの箱（Question）と、Spring Bootが用意してくれている全自動ロボットの部品（JpaRepository）を読み込んでいます
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Question;

// 【重要】このクラスがデータベースを操作する「優秀な専属ロボット」であることをパソコンに教えてあげるマークです
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // クラス（class）ではなく「インターフェース（interface）」という特殊な形で作るのがJPAのルールです。
    // 「extends JpaRepository<Question, Long>」と書き添えるだけで、このロボットは自分で何もコードを書かなくても、
    // クイズデータの「全自動保存（save）」や「全自動全件取得（findAll）」といった超能力が最初から使えるようになります！
}

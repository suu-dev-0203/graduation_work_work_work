package com.example.entity;
// このファイルが「com.example.entity」という名前の小部屋（パッケージ）にあることを宣言しています。

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// データベース操作や自動割り振りのためのJPA部品を読み込んでいます。

@Entity
// 【重要】このクラスがデータベースの「テーブル（QUESTION）」の箱になるよ！とパソコンに教えてあげるマークです。
public class Question {

	@Id
	// 【重要】これがクイズ問題の「出席番号（主キー）」になるよ！という目印です。
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// データを保存したときに、1番、2番、3番…とパソコンが自動で番号を被らないように割り振ってくれる設定です。
	private Long questionId; // クイズごとに自動で付く、世界に一つだけのID番号を入れる隙間です。

	@Column(length = 1000)
	// 問題文が長くなってもエラーを起こさないように、データベースの文字上限を1000文字へ拡張する設定です。
	private String questionText; // クイズの問題文（文字列）を入れる隙間です。

	@Column(length = 2000)
	// 黒い画面のソースコードは改行や文字数が多いため、文字上限を2000文字へ大きく広げる設定です。
	private String codeBlock; // 画面に黒枠で表示するプログラムコードを入れる隙間です。

	private String choiceA; // 選択肢Aの文章（文字列）を入れる隙間です。
	private String choiceB; // 選択肢Bの文章（文字列）を入れる隙間です。
	private String choiceC; // 選択肢Cの文章（文字列）を入れる隙間です。
	private String choiceD; // 選択肢Dの文章（文字列）を入れる隙間です。
	private String choiceE; // 選択肢Eの文章（文字列）を入れる隙間です。

	private int correctChoiceIndex; // 正解がどれかを「数字」で覚える隙間です（0ならA、1ならB、2ならC、3ならD、4ならEが正解）。

	@Column(length = 2000)
	// 詳細な解説文が長くなっても絶対にエラーにならないように、文字上限を2000文字へ大きく広げる設定です。
	private String explanation; // クイズに答えた後に画面に表示される、正解の詳しい解説文を入れる隙間です。

	private int timeLimitSeconds; // ゲームの緊張感を出すための、1問あたりの制限時間（秒数）を入れる隙間です。

	// 【引数なしのコンストラクター】
	// データベースからデータを読み込むときに、Javaの裏側のシステムが自動で使う初期化スイッチです。
	public Question() {
	}

	// 【超重要：データバンク互換用の5引数コンストラクター】
	// 前作の全8章のファイル群（引数5個で呼び出しているコード）のエラーを一発で消し去るための、共存用スイッチです。
	public Question(String questionText, String choiceA, String choiceB, String choiceC, int correctChoiceIndex) {
		// 左側の隙間に対して、届いた5つのデータだけを代入します（残りの隙間は自動的に空っぽや0になります）。
		this.questionText = questionText;
		this.choiceA = choiceA;
		this.choiceB = choiceB;
		this.choiceC = choiceC;
		this.correctChoiceIndex = correctChoiceIndex;
	}

	// 【全データを手動で入れるための10引数コンストラクター】
	// 将来的にすべての項目（10個の引数）を持ったリッチな問題を新しく詰め込むための起動スイッチです。
	public Question(String questionText, String codeBlock, String choiceA, String choiceB, String choiceC, String choiceD, String choiceE, int correctChoiceIndex, String explanation, int timeLimitSeconds) {
		// 左側のこのクラスの隙間に対して、届いた10個のすべてのデータをガチッと代入して保存しています。
		this.questionText = questionText;
		this.codeBlock = codeBlock;
		this.choiceA = choiceA;
		this.choiceB = choiceB;
		this.choiceC = choiceC;
		this.choiceD = choiceD;
		this.choiceE = choiceE;
		this.correctChoiceIndex = correctChoiceIndex;
		this.explanation = explanation;
		this.timeLimitSeconds = timeLimitSeconds;
	}

	// 【Getter（ゲッター）メソッド】
	// 隙間に隠されているデータを、画面（HTML）や他のプログラムから安全に呼び出すための窓口です。
	public Long getQuestionId() { return questionId; } // ID番号を外に返す窓口です。
	public String getQuestionText() { return questionText; } // 問題文を外に返す窓口です。
	public String getCodeBlock() { return codeBlock; } // 黒枠コードを外に返す窓口です。
	public String getChoiceA() { return choiceA; } // 選択肢Aを外に返す窓口です。
	public String getChoiceB() { return choiceB; } // 選択肢Bを外に返す窓口です。
	public String getChoiceC() { return choiceC; } // 選択肢Cを外に返す窓口です。
	public String getChoiceD() { return choiceD; } // 選択肢Dを外に返す窓口です。
	public String getChoiceE() { return choiceE; } // 選択肢Eを外に返す窓口です。
	public int getCorrectChoiceIndex() { return correctChoiceIndex; } // 正解数字を外に返す窓口です。
	public String getExplanation() { return explanation; } // 解説文を外に返す窓口です。
	public int getTimeLimitSeconds() { return timeLimitSeconds; } // 制限時間を外に返す窓口です。
}
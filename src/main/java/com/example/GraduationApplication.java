package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//  このクラスが、このWebアプリ全体の「起動スイッチ」になる。
@SpringBootApplication
public class GraduationApplication {

    public static void main(String[] args) {
        //  この一行が動くことで、どこつぶの時のようにWebサーバーが裏側で自動で立ち上がる。
        SpringApplication.run(GraduationApplication.class, args);
    }
}

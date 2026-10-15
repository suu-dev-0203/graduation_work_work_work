package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 💡 このマークをつけることで、このクラスが「Webからの通信を受け付ける窓口（コントローラー）」になります！
@RestController
public class HelloController {

    // 💡 ブラウザで「http://localhost:8080/hello」にアクセスしたときに、このメソッドが動きます！
    @GetMapping("/hello")
    public String sayHello() {
        // 💡 この文字がそのままブラウザの画面にぽつんと表示されます。
        return "Hello, Java Game World!";
    }
}

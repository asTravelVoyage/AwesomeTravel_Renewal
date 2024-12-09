package renewal.awesome_travel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller(value = "/")
public class MainController {

    @GetMapping
    public String main(Model model) {
        System.out.println("get");
        model.addAttribute("engineTest", "타임리프 테스트");
        return "main";
    }
}

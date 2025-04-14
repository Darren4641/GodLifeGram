package com.godlife.godlifegram.view;

import com.godlife.godlifegram.post.application.dto.response.MyPostCountResponseSvcDto;
import com.godlife.godlifegram.post.application.service.PostService;
import com.godlife.godlifegram.user.application.dto.response.SigninResponseSvcDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final PostService postService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/post-detail")
    public String detail(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        model.addAttribute("user", user);
        return "post-detail";
    }

    @GetMapping("/me")
    public String me(HttpSession session, Model model) {
        SigninResponseSvcDto user = (SigninResponseSvcDto) session.getAttribute("user");
        if(user == null) {
            return "index";
        }
        MyPostCountResponseSvcDto myPostCountResponseSvcDto = postService.getMyPostCount(user.getEmail());
        model.addAttribute("user", user);
        model.addAttribute("postInfo", myPostCountResponseSvcDto);
        return "me";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

}

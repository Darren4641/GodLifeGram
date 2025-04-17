package com.godlife.godlifegram.view;

import com.godlife.godlifegram.post.application.dto.response.MyPostCountResponseSvcDto;
import com.godlife.godlifegram.post.application.service.PostService;
import com.godlife.godlifegram.post.domain.Post;
import com.godlife.godlifegram.post.ui.dto.response.ViewResponseDto;
import com.godlife.godlifegram.user.application.dto.response.SigninResponseSvcDto;
import com.godlife.godlifegram.user.application.service.AuthService;
import com.godlife.godlifegram.user.ui.dto.response.ProfileResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final PostService postService;
    private final AuthService authService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/post-detail")
    public String detail(@RequestParam("id") Long id, HttpSession session, Model model) {
        SigninResponseSvcDto user = (SigninResponseSvcDto) session.getAttribute("user");
        if(user != null && "zxz4641@gmail.com".equals(user.getEmail())) {
            user.isAdmin();
        }
        ViewResponseDto post = null;
        try {
            post = postService.getPost(id, "og");
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("user", user);
        if(post != null)    model.addAttribute("post", post);

        return "post-detail";
    }

    @GetMapping("/me")
    public String me(HttpSession session, Model model) {
        SigninResponseSvcDto user = (SigninResponseSvcDto) session.getAttribute("user");
        if(user == null) {
            return "index";
        }
        MyPostCountResponseSvcDto myPostCountResponseSvcDto = postService.getMyPostCount(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("postInfo", myPostCountResponseSvcDto);
        return "me";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model, @RequestParam("id") Long id) {
        SigninResponseSvcDto user = (SigninResponseSvcDto) session.getAttribute("user");
        model.addAttribute("user", user);
        ProfileResponseDto profileResponseDto = null;

        try {
            profileResponseDto = authService.getUserProfile(id);
        } catch (Exception e) {
            return "index";
        }

        MyPostCountResponseSvcDto postCountResponseSvcDto = postService.getMyPostCount(id);
        model.addAttribute("profile", profileResponseDto);
        model.addAttribute("profileInfo", postCountResponseSvcDto);

        return "profile";
    }


}

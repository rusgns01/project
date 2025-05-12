//package controller;
//
//import entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import repository.UserRepository;
//import service.FollowService;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/")
//public class FollowController {
//
//    private final FollowService followService;
//    private final UserRepository userRepository;
//
//    @GetMapping("/")
//    public String followPage(Model model) {
//        model.addAttribute("users", userRepository.findAll());
//        return "index";
//    }
//
//    @PostMapping("/do")
//    public String followUser(@RequestParam Long fromUserId, @RequestParam Long toUserId, Model model) {
//        User fromUser = userRepository.findById(fromUserId).orElseThrow();
//        User toUser = userRepository.findById(toUserId).orElseThrow();
//
//        String message = followService.follow(fromUser, toUser);
//        model.addAttribute("message", message);
//        model.addAttribute("users", userRepository.findAll());
//
//        return "index";
//    }
//}

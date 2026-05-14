package com.pos.restaurantpos.controller;

import com.pos.restaurantpos.entity.Member;
import com.pos.restaurantpos.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cashier/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberService.findAll());
        return "cashier/members";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        model.addAttribute("member", new Member());
        return "cashier/member-form";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        model.addAttribute("member", memberService.findById(id));
        return "cashier/member-form";
    }
}

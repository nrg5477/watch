package com.codingrecipe.member.controller;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    //@GetMapping("/member/save")
    @GetMapping("/save")
    public String saveForm(){
        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        //dto 객체 만들어서 db에 넘기기
        int saveResult = memberService.save(memberDTO);
        if (saveResult>0){ //가입성공
            return "login";
        }
        else { //가입실패
            return "save";
        }
    }

    @GetMapping("/login") //로그인 페이지를 띄우는 메소드
    public String loginForm(){
        return "login";
    }
    @PostMapping("/login") //로그인 처리를 하는 메소드
    public String login(@ModelAttribute MemberDTO memberDTO,
                        HttpSession session){
        boolean loginResult = memberService.login(memberDTO);
        if (loginResult){
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
            return "main";
        }
        else {
            return "login";
        }
    }

    @GetMapping("/")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    @GetMapping
    public String findById(@RequestParam("id") long id, Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "detail";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id){
        memberService.delete(id);
        return "redirect:/member/";
    }

    //수정화면 요청
    @GetMapping("/update")
    public String updateForm(HttpSession session, Model model){
        //세션에 저장된 나의 이메일 가져오기
        String loginEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByMemberEmail(loginEmail);
        model.addAttribute("member", memberDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        boolean resulte = memberService.update(memberDTO);
        if (resulte){
            return "redirect:/member?id="+memberDTO.getId();
        }
        else {
            return "index";
        }
    }

    @PostMapping("/email-check")
    public @ResponseBody String emailCheck(@RequestParam("memberEmail") String memberEmail){
        System.out.println("memberEmail = "+memberEmail);
        String checkResult = memberService.emailCheck(memberEmail);
        return checkResult;
    }
}

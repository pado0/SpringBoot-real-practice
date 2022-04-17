package com.pado.SpringBootrealpractice.controller;

import com.pado.SpringBootrealpractice.domain.Address;
import com.pado.SpringBootrealpractice.domain.Member;
import com.pado.SpringBootrealpractice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService; // 컨트롤러는 서비스를 주로 갖다 씀.

    @GetMapping(value = "/members/new")
    public String createForm(Model model) { // model ? : 컨트롤러에서 뷰로 넘어갈 때, 데이터를 실어 보내기 위함.
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    //Member객체를 쓰지 않고 MemberForm을 따로 쓰는 이유는, 둘이 완전 같지는 않기 떄문. 깔끔하게 화면에 핏한 폼 데이터를 만드는게 낫다.
    public String create(@Valid MemberForm form, BindingResult result) { //@valid: form에 있는 validation을 쓰는구나 확인. BindingResult: 결과에 에러가 있으면 실행되는 코드
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);
        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        // 지금은 손 안대고 데이터를 그대로 뿌려도 되어 이렇게 뿌리지만,
        List<Member> members = memberService.fineMembers(); // 이것도 엔티티를 바로 쓰는 것 보다는 dto로 변환하는게 낫다
        model.addAttribute("members", members); // 멤버 서비슬에서 멤버를 읽어 모델로 넘겨준다.
        return "members/memberList";
    }
}

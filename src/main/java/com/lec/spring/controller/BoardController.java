package com.lec.spring.controller;

import com.lec.spring.domain.Post;
import com.lec.spring.domain.PostValidator;
import com.lec.spring.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// Controller layer
// request 처리 ~ response
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    public BoardController() {
        System.out.println("BoardController() 생성");
    }

    @GetMapping("/write")
    public void write() {
        System.out.println("write url로 요청해서 write함수가 실행됨.");
    }

    @PostMapping("/write")
    // 이 어노테이션은 HTTP POST 요청을 처리하는 메서드임을 나타냅니다.
    // "/write" 경로에 대한 POST 요청이 발생하면 이 메서드가 호출됩니다.
    public String writeOk(
            // writeOk라는 메서드를 선언합니다. 이 메서드는 문자열을 반환하며, 다양한 매개변수를 받습니다.
            @Valid Post post
            // Post 객체를 받습니다. @Valid 어노테이션은 데이터 바인딩 시 유효성 검증을 수행하라는 것을 나타냅니다.
            // 이는 주로 입력 데이터의 유효성을 검사하는 데 사용됩니다.
            , BindingResult result
            // 유효성 검증 결과를 담고 있는 객체입니다. 유효성 검증 중에 발생한 오류나 문제를 확인할 수 있습니다.
            , Model model // 매개변수 선언시 BindingReult 보다 Model 을 뒤에 두어야 한다.
            // Spring MVC의 모델 객체로, 뷰에 데이터를 전달하는 데 사용됩니다.
            , RedirectAttributes redirectAttrs
            // 리다이렉트 시 데이터를 전달하기 위한 객체입니다.
    ) {

         System.out.println("Post: " + post);
        // 콘솔에 현재 받은 Post 객체의 내용을 출력합니다. 개발 중 디버깅이나 확인을 위한 용도로 사용됩니다.

        // validation 에러가 있었다면 redirect 할거다!
        if (result.hasErrors()) {

            // addAttribute
            //    request parameters로 값을 전달.  redirect URL에 query string 으로 값이 담김
            //    request.getParameter에서 해당 값에 액세스 가능
            // addFlashAttribute
            //    일회성. 한번 사용하면 Redirect후 값이 소멸
            //    request parameters로 값을 전달하지않음
            //    '객체'로 값을 그대로 전달
            redirectAttrs.addFlashAttribute("user", post.getUser());
            redirectAttrs.addFlashAttribute("subject", post.getSubject());
            redirectAttrs.addFlashAttribute("content", post.getContent());

            for (var err : result.getFieldErrors()) {
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/board/write";
        }

        model.addAttribute("result", boardService.write(post));
        // boardService의 write 메서드를 호출하여 게시물을 작성하고, 그 결과를 "result"라는 이름으로 모델에 추가합니다. 이렇게 모델에 데이터를 추가하면 이후에 뷰에서 해당 데이터를 사용할 수 있습니다.

        return "board/writeOk";
        // 뷰의 이름을 반환합니다. 여기서는 "board/writeOk"로 설정되어 있으므로, 작성이 완료된 후에 사용자를 "writeOk" 뷰로 이동시킵니다.
    }


    // 특정 id 의 글 조회
    // 트랜잭션 처리
    // 1. 조회수 증가 (UPDATE)
    // 2. 글 읽어오기 (SELECT)
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        System.out.println("class BoardController detail() id값:" + id);
        model.addAttribute("post", boardService.detail(id));
        System.out.println("class BoardController detail() model값:" + model);

        return "board/detail";
    }

    @GetMapping("/list")
    public void list(Model model) {
        System.out.println("@GetMapping(\"/list\") public void list(Model model) 실행");
        List<Post> list = boardService.list();
        System.out.println(list);

        model.addAttribute("list", list);
        System.out.println(model);
    }

    @GetMapping("/update/{id}")
    // 이 메서드는 HTTP GET 요청을 처리하며, "/update/{id}" 경로에 매핑됩니다.
    // {id}는 경로 변수로, 실제로는 해당 변수에 들어오는 값에 따라 동적으로 경로가 매핑됩니다.
    public String update(@PathVariable Long id, Model model) {
        // 메서드 시그니처에서 @PathVariable 어노테이션은 {id} 경로 변수를 메서드 파라미터로 매핑합니다.
        // 즉, 경로에서 추출한 id 값이 메서드 파라미터로 전달됩니다. Model은 Spring MVC에서 사용되는 데이터 모델로,
        // 뷰에 데이터를 전달하는 데 사용됩니다.
        Post post = boardService.selectById(id);
        // boardService라는 이름의 서비스(또는 서비스 계층)에서 selectById 메서드를 호출하여
        // id에 해당하는 Post 객체를 가져옵니다. 이 코드는 데이터베이스에서 특정 ID의 게시물을 조회하는 로직을 나타냅니다.
        model.addAttribute("post", post);
        //  가져온 Post 객체를 model에 "post"라는 이름으로 추가합니다.
        //  이렇게 하면 뷰에서 해당 데이터를 사용할 수 있습니다.
        return "board/update";
        // 마지막으로, 이 메서드는 "board/update"라는 뷰 이름을 반환합니다.
        // Spring MVC에서는 해당 뷰 이름을 찾아 렌더링되는 뷰로 사용합니다.
        // 이 경우, "board/update"는 뷰 리졸버(View Resolver)를 통해 실제 뷰로 매핑됩니다.
    }


    @PostMapping("/update")
    // 이 어노테이션은 HTTP POST 요청을 처리하는 메서드임을 나타냅니다.
    // "/update" 경로에 대한 POST 요청이 발생하면 이 메서드가 호출됩니다.
    public String updateOk(
            // updateOk라는 메서드를 선언합니다. 이 메서드는 문자열을 반환하며, 다양한 매개변수를 받습니다.
            @Valid Post post
            // Post 객체를 받습니다. @Valid 어노테이션은 데이터 바인딩 시 유효성 검증을 수행하라는 것을 나타냅니다. 이는 주로 입력 데이터의 유효성을 검사하는 데 사용됩니다.
            , BindingResult result
            // 유효성 검증 결과를 담고 있는 객체입니다. 유효성 검증 중에 발생한 오류나 문제를 확인할 수 있습니다.
            , Model model
            // Spring MVC의 모델 객체로, 뷰에 데이터를 전달하는 데 사용됩니다.
            , RedirectAttributes redirectAttrs
            // 리다이렉트 시 데이터를 전달하기 위한 객체입니다.
    ) {
        if (result.hasErrors()) {
            // 만약 유효성 검증에서 오류가 발생했다면 실행됩니다.

            redirectAttrs.addFlashAttribute("subject", post.getSubject());
            // 리다이렉트 시에 "subject"라는 이름으로 post.getSubject() 값을 전달합니다. addFlashAttribute를 사용하면 리다이렉트 시 한 번만 사용할 수 있는 플래시 속성을 추가할 수 있습니다.
            redirectAttrs.addFlashAttribute("content", post.getContent());
            // 리다이렉트 시에 "content"라는 이름으로 post.getContent() 값을 전달합니다.

            for (var err : result.getFieldErrors()) {
                // 유효성 검증에서 발생한 각각의 필드 오류에 대해, 리다이렉트 시에 "error_{필드이름}"이라는 이름으로 오류 코드를 전달합니다. 이는 리다이렉트 후에 오류 정보를 화면에 표시하는 데 사용될 수 있습니다.
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/board/update/" + post.getId();
            // 유효성 검증에서 오류가 발생했을 경우, 게시물 업데이트 화면으로 리다이렉트합니다.
        }

        model.addAttribute("result", boardService.update(post));
        // 유효성 검증에서 오류가 발생하지 않았을 경우, boardService의 update 메서드를 호출하여 게시물을 업데이트하고, 그 결과를 "result"라는 이름으로 모델에 추가합니다.

        return "board/updateOk";
        // 유효성 검증에서 오류가 발생하지 않았을 경우, 업데이트가 성공했음을 나타내는 "board/updateOk" 뷰로 이동합니다.
    }


    @PostMapping("/delete")
    public String deleteOk(Long id, Model model) {
        System.out.println("보드컨트롤러의 deleteOkg함수의 매개변수id값 = " + id);
        int result = boardService.deleteById(id);
        model.addAttribute("result", result);

        return "board/deleteOk";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        System.out.println("initBinder() 호출");
        binder.setValidator(new PostValidator());
    }


} // end Controller

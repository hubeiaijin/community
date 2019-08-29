package com.ajin.test.controller;

import com.ajin.test.dto.PaginationDTO;
import com.ajin.test.model.User;
import com.ajin.test.service.QuestionService;
import com.ajin.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 2019/8/6.
 */
@Controller
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    /**
     * 展示主页面数据
     * @param request
     * @return
     */
    @GetMapping("/index")
    public String testDemo(HttpServletRequest request,Model model,
                           @RequestParam(name = "page", defaultValue = "1") Integer page,
                           @RequestParam(name = "size", defaultValue = "2") Integer size,
                           @RequestParam(name = "search", required = false) String search,
                           @RequestParam(name = "tag", required = false) String tag){
        //如果方法上不加 @ResponseBody 则就会去templates文件中找 helloworld.html文件去渲染到

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")){
                String token=cookie.getValue();
                User user=userService.findByToken(token);
                if (user !=null){
                    request.getSession().setAttribute("user",user);//把user对象放到session中
                }
                break;
            }
        }
        PaginationDTO paginationDTO = questionService.list(search, tag, page, size);
      //  List<String> tags = hotTagCache.getHots();
        model.addAttribute("pagination", paginationDTO);
        model.addAttribute("search", search);
        model.addAttribute("tag", tag);
      //  model.addAttribute("tags", tags);

        return "index";
    }

   /* @GetMapping("/index")
    public String testIndex(Model model,
                            @RequestParam(name = "page", defaultValue = "1") Integer page,
                            @RequestParam(name = "size", defaultValue = "10") Integer size,
                            @RequestParam(name = "search", required = false) String search,
                            @RequestParam(name = "tag", required = false) String tag){




        return "index";
    }*/


}


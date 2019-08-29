package com.ajin.test.controller;

import com.ajin.test.dto.AccessTokenDTO;
import com.ajin.test.dto.GithubUser;
import com.ajin.test.mapper.UserMapper;
import com.ajin.test.model.User;
import com.ajin.test.provider.GithubProvider;
import com.ajin.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by Administrator on 2019/8/7.
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;



    @GetMapping("/callback")
    public String callBack(String code, String state, HttpServletRequest request, HttpServletResponse response){
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        //获取accessToken
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //通过accessToken 获取用户信息
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        if (user !=null){
            //登录成功写入cookies和session
            User user1=new User();
            user1.setToken(UUID.randomUUID().toString());//设置token  其他的数据从前段数据获得
            user1.setAccountId(user.getId()+"");
            user1.setName(user.getName());
            user1.setAvatarUrl(user.getAvatarUrl());//照片地址
            user1.setGmtCreate(System.currentTimeMillis());
            user1.setGmtModified(user1.getGmtCreate());
            userService.createOrUpdate(user1);
            Cookie cookie=new Cookie("token",user1.getToken());
            cookie.setMaxAge(60 * 60 * 24 * 30);
            //把token放入cookies
            response.addCookie(cookie);
          //把信息存入到session中
         //   request.getSession().setAttribute("user",user);//把cookies存入到session中
            return "redirect:/index";
        }else{
            return "redirect:/index";
        }
    }
}

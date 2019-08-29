package com.ajin.test.dto;

import lombok.Data;

/**
 * 返回用户信息类
 * Created by Administrator on 2019/8/7.
 */
@Data
public class GithubUser {

    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;

}

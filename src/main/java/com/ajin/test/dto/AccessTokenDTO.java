package com.ajin.test.dto;

import lombok.Data;

/**
 * Created by Administrator on 2019/8/7.
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}

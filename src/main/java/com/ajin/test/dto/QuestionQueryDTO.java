package com.ajin.test.dto;

import lombok.Data;

/**
 * Created by Administrator on 2019/8/22.
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private String tag;
    private Integer page;
    private Integer size;
}

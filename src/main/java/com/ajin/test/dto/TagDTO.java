package com.ajin.test.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2019/8/13.
 */
@Data
public class TagDTO {

    private String categoryName;
    private List<String> tags;
}

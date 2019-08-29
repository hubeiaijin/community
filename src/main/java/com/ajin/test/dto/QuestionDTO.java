package com.ajin.test.dto;

import com.ajin.test.model.User;
import lombok.Data;

/**
 * Created by Administrator on 2019/8/16.
 * 组合实体类 用户和问题
 *
 */
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}

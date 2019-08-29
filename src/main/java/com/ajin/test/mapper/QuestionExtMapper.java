package com.ajin.test.mapper;

import com.ajin.test.dto.QuestionQueryDTO;
import com.ajin.test.model.Question;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2019/8/22.
 */
@Component("questionExtMapper")
public interface QuestionExtMapper {
    int incView(Question record);

    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}

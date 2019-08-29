package com.ajin.test.service;

import com.ajin.test.dto.PaginationDTO;
import com.ajin.test.dto.QuestionDTO;
import com.ajin.test.dto.QuestionQueryDTO;
import com.ajin.test.exception.CustomizeErrorCode;
import com.ajin.test.exception.CustomizeException;
import com.ajin.test.mapper.QuestionExtMapper;
import com.ajin.test.mapper.QuestionMapper;
import com.ajin.test.mapper.UserMapper;
import com.ajin.test.model.PageResult;
import com.ajin.test.model.Question;
import com.ajin.test.model.QuestionExample;
import com.ajin.test.model.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2019/8/14.
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;



    /**
     * 添加及更新问题
     * @param question
     */
    @Transactional
    public void createOrUpdate(Question question){
        //如果没有主键则表示 新增
        if (question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }else{
            //更新问题
            //根据id查询 问题
            Question dbQuestion = questionMapper.selectByPrimaryKey(question.getId());
            //先判断问题是否存在
            if(dbQuestion==null){
                //抛出问题没有找到异常
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            if(dbQuestion.getCreator().longValue() !=question.getCreator().longValue()){
                throw  new CustomizeException(CustomizeErrorCode.INVALID_INPUT);
            }

            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());

            QuestionExample example=new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }


        }

    }

    /**
     * 根据id进行查询 用户和问题
     * @param id
     * @return
     */
    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;

    }

    /**
     *根据用户id查询的结果返回
     * @param page 页数
     * @param size  每页数量
     * @return
     */
    public PaginationDTO list(Long userId, Integer page, Integer size) {
       // PageHelper.startPage(page,size);
     //   List<Question> questions =(Page<>) questionMapper.selectByExample(null);

        PaginationDTO paginationDTO=new PaginationDTO();
        Integer totalPage;//总页数
        QuestionExample questionExample=new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount  = (int) questionMapper.countByExample(questionExample);//总条数
        if (totalCount % size==0){//能够整除
            totalPage=totalCount/size;
        }else{
            totalPage=totalCount/size +1;
        }
        //对传入的page值进行判断
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        paginationDTO.setPagination(totalPage,page);//设置总页数和当前页数
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample, new RowBounds(offset, size));
        /*List<QuestionDTO> questionDTOList=new ArrayList<>();
        for (Question question : questions) {
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);//把question中的属性复制到questionDTO中
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }*/
        paginationDTO.setData(getListQuestionDTO(questions));

        return paginationDTO;
    }

    /**
     * 多条件分页查询
     * @param search
     * @param tag
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(String search, String tag, Integer page, Integer size) {

        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays
                    .stream(tags)
                    .filter(StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", ""))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));
        }


        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        if(StringUtils.isNotBlank(tag)){
            tag = tag.replace("+", "").replace("*", "");
            questionQueryDTO.setTag(tag);
        }
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        Integer offset = page < 1 ? 0 : size * (page - 1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);

       /* List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }*/


        paginationDTO.setData(getListQuestionDTO(questions));

        return paginationDTO;
    }

    private List<QuestionDTO> getListQuestionDTO(List<Question> questions){


        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        return questionDTOList;
    }



}

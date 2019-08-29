package com.ajin.test.service;

import com.ajin.test.exception.CustomizeErrorCode;
import com.ajin.test.exception.CustomizeException;
import com.ajin.test.mapper.UserMapper;
import com.ajin.test.model.User;
import com.ajin.test.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.undo.CannotUndoException;
import java.util.List;

/**
 * Created by Administrator on 2019/8/14.
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 添加或者更新信息
     * @param user
     */
    public void createOrUpdate(User user) {

        if(user==null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        UserExample example=new UserExample();
        example.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(example);
        //如果数据库中没有该用户信息 则重新添加一条数据到数据库中
        if (users.size()==0){
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            User dbUser = users.get(0);//获取数据库中用户信息
            user.setGmtModified(System.currentTimeMillis());
            UserExample userExample=new UserExample();
            userExample.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(user,userExample);
        }

    }

    /**
     *
     * @param token
     * @return
     */
    public User findByToken(String token){
        UserExample example=new UserExample();
        example.createCriteria().andTokenEqualTo(token);
        List<User> users = userMapper.selectByExample(example);
        return users.get(0);
    }
}

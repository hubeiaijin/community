package com.ajin.test.dto;

import lombok.Data;

/**
 * Created by Administrator on 2019/8/23.
 */
@Data
public class HotTagDTO implements Comparable{

    private String name;
    private Integer priority;
    @Override
    public int compareTo(Object o) {
        return this.getPriority()-((HotTagDTO)o).getPriority();
    }
}

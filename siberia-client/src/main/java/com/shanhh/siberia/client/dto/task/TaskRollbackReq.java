package com.shanhh.siberia.client.dto.task;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2018-05-30 17:01
 */
@Data
@NoArgsConstructor
public class TaskRollbackReq implements Serializable {

    private int taskId;
    private String createBy;

}

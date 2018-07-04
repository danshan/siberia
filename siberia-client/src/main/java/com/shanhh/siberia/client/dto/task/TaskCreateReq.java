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
public class TaskCreateReq implements Serializable {

    private int envId;
    private int deploymentId;
    private String createBy;

}

package com.shanhh.siberia.client.dto.pipeline;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-22 11:28
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class PipelineDeploymentDTO implements Serializable {

    private int id;
    private int pipelineId;
    private String project;
    private String module;
    private int buildNo;
    private int appId;

    private List<PipelineTaskDTO> tasks;

    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

    public void addTask(PipelineTaskDTO task) {
        if (this.tasks == null) {
            this.tasks = Lists.newLinkedList();
        }
        tasks.add(task);
    }

}

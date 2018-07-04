package com.shanhh.siberia.client.dto.task;

import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-23 10:26
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class TaskDTO implements Serializable {

    private int id;
    private PipelineDeploymentDTO deployment;
    private List<String> nodes;
    private EnvDTO env;
    private Date startTime;
    private Date endTime;
    private TaskStatus status;
    private Memo memo;

    private int version;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

    @Data
    @NoArgsConstructor
    @ToString
    public static class Memo implements Serializable {
        private int percent;
        private int rollbackVersion;
    }

}

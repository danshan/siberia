package com.shanhh.siberia.client.dto.pipeline;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-22 11:28
 */
@Data
@NoArgsConstructor
public class PipelineDeploymentDTO implements Serializable {

    private int id;
    private int pipelineId;
    private String project;
    private String module;
    private int buildNo;

    private List<PipelineTaskDTO> taskList;

    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

    public void addTask(PipelineTaskDTO task) {
        if (this.taskList == null) {
            this.taskList = Lists.newLinkedList();
        }
        taskList.add(task);
    }

    public static PipelineDeploymentDTO mock() {
        String[] project = {
                "google",
                "apple",
                "amazon",
        };

        String[] module = {
                "ui",
                "gateway",
                "service",
        };

        String[] user = {
                "付小小",
                "曲丽丽",
                "林东东",
                "周星星",
                "吴加好",
                "朱偏右",
                "鱼酱",
                "乐哥",
                "谭小仪",
                "仲尼",
        };

        PipelineDeploymentDTO mock = new PipelineDeploymentDTO();
        mock.setId(RandomUtils.nextInt());
        mock.setPipelineId(RandomUtils.nextInt());
        mock.setProject(project[RandomUtils.nextInt() % project.length]);
        mock.setModule(mock.getProject() + "-" + module[RandomUtils.nextInt() % module.length]);
        mock.setCreateBy(user[RandomUtils.nextInt() % user.length]);
        mock.setUpdateBy(user[RandomUtils.nextInt() % user.length]);
        mock.setCreateTime(new Date(new Date().getTime() - 1000 * 60 * 60 * 2 * RandomUtils.nextInt(5, 10)));
        mock.setUpdateTime(new Date(new Date().getTime() - 1000 * 60 * 60 * 2 * RandomUtils.nextInt(1, 5)));
        return mock;
    }

}

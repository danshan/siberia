package com.shanhh.siberia.client.dto.task;

import com.shanhh.siberia.client.dto.settings.EnvDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.RandomUtils;

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
    private int pipelineId;
    private List<String> nodes;
    private EnvDTO env;
    private String project;
    private String module;
    private int buildNo;
    private Date startTime;
    private Date endTime;

    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

    public static TaskDTO mock() {
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

        TaskDTO mock = new TaskDTO();
        mock.setId(RandomUtils.nextInt());
        mock.setPipelineId(RandomUtils.nextInt());
        mock.setProject(project[RandomUtils.nextInt() % project.length]);
        mock.setModule(mock.getProject() + "-" + module[RandomUtils.nextInt() % module.length]);
        mock.setBuildNo(RandomUtils.nextInt());
        mock.setStartTime(new Date(new Date().getTime() - 1000 * 60 * 60 * 2 * RandomUtils.nextInt(5, 10)));
        mock.setEndTime(new Date(new Date().getTime() - 1000 * 60 * 60 * 2 * RandomUtils.nextInt(1, 5)));
        mock.setCreateBy(user[RandomUtils.nextInt() % user.length]);
        mock.setUpdateBy(user[RandomUtils.nextInt() % user.length]);
        mock.setCreateTime(new Date(new Date().getTime() - 1000 * 60 * 60 * 2 * RandomUtils.nextInt(5, 10)));
        mock.setUpdateTime(new Date(new Date().getTime() - 1000 * 60 * 60 * 2 * RandomUtils.nextInt(1, 5)));
        return mock;
    }
}

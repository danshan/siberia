package com.shanhh.siberia.web.repo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author shanhonghao
 * @since 2018-05-23 14:39
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
@Entity
public class PipelineDeployment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int pipelineId;
    private String project;
    private String module;
    private int buildNo;

//    private List<PipelineTaskDTO> taskList;

    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;
}

package com.shanhh.siberia.web.repo.entity;

import com.shanhh.siberia.client.dto.task.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @author shanhonghao
 * @since 2018-05-28 15:21
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    private int pipelineId;
    @Column(nullable = false)
    private String nodes = "";
    @OneToOne
    private Env env;
    @Column(nullable = false)
    private String project;
    @Column(nullable = false)
    private String module;
    @Column(nullable = false)
    private int buildNo;
    private Date startTime;
    private Date endTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Version
    private int version;

    @Column(nullable = false, updatable = false)
    private String createBy;
    @Column(nullable = false)
    private String updateBy;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date createTime;
    @Column(insertable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date updateTime;
}

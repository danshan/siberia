package com.shanhh.siberia.web.repo.entity;

import com.shanhh.siberia.client.dto.task.TaskStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
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
    @OneToOne
    private PipelineDeployment deployment;
    @Column(nullable = false)
    private String nodes = "";
    @OneToOne
    private Env env;
    private Date startTime;
    private Date endTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Column(nullable = false)
    @Convert(converter = JpaConverterJson.class)
    private Memo memo;

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

    @Data
    @NoArgsConstructor
    @ToString
    public static class Memo implements Serializable {
        private int percent;
        private int rollbackVersion;
    }
}

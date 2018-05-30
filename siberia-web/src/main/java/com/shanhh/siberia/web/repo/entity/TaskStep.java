package com.shanhh.siberia.web.repo.entity;

import com.shanhh.siberia.client.dto.task.TaskStepResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Dan
 * @since 2016-06-04 15:06
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
@Entity
public class TaskStep implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    private int taskId;
    @Column(nullable = false)
    private String step = "";
    @Column(nullable = false)
    private String detail = "";
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStepResult result;
    private Date beginTime;
    private Date endTime;

    @Column(nullable = false, updatable = false)
    private String createBy;
    @Column(nullable = false)
    private String updateBy;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date createTime;
    @Column(insertable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date updateTime;

}

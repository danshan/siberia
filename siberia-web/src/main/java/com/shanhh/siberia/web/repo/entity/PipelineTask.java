package com.shanhh.siberia.web.repo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author shanhonghao
 * @since 2018-06-19 15:08
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
@Entity
public class PipelineTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToOne
    private PipelineDeployment deployment;
    @OneToOne
    private Env env;
}

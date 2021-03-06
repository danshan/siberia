package com.shanhh.siberia.web.repo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * @author shanhonghao
 * @since 2018-05-30 13:41
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
@Entity
public class AppConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    private int appId;
    @ManyToOne
    private Env env;
    @Convert(converter = JpaConverterJson.class)
    private Map<String, Object> content;
    @Column(nullable = false, updatable = false)
    private String createBy;
    @Column(nullable = false)
    private String updateBy;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date createTime;
    @Column(insertable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date updateTime;
}

package com.shanhh.siberia.web.repo.entity;

import com.shanhh.siberia.client.dto.app.AppType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @author shanhonghao
 * @since 2018-05-30 11:46
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
@Entity
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    private String project;
    @Column(nullable = false)
    private String module;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppType appType;
//    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, mappedBy = "appId")
//    private List<AppConfig> configs = Lists.newLinkedList();

    @Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private boolean deleted;
    @Column(nullable = false, updatable = false)
    private String createBy;
    @Column(nullable = false)
    private String updateBy;

    @Column(insertable = false, updatable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date createTime;
    @Column(insertable = false, columnDefinition = "TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date updateTime;

}

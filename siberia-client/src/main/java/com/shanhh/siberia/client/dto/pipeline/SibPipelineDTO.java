package com.shanhh.siberia.client.dto.pipeline;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 发布流程
 *
 * @author Dan
 * @since 2016-03-15 13:43
 */
@Data
@NoArgsConstructor
public class SibPipelineDTO implements Serializable {

    private int id;
    private String title;
    private String desc;
    private String createBy;
    private String updateBy;
    private List<String> relatedUserList = Lists.newLinkedList();
    private Date createTime;
    private Date updateTime;

    private ProjectInfo projectInfo = new ProjectInfo();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Data
    @NoArgsConstructor
    public static class ProjectInfo implements Serializable {
        private List<Project> projects = Lists.newLinkedList();

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    @Data
    @NoArgsConstructor
    public static class Project implements Serializable {
        private String name;
        private String rev;
        private JenkinsJob job = new JenkinsJob();

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    @Data
    @NoArgsConstructor
    public static class PrismInfo implements Serializable {
        private int id;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    @Data
    @NoArgsConstructor
    public static class JenkinsJob implements Serializable {
        private int buildNo;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }


}

package com.shanhh.siberia.client.dto.app;

import com.google.common.collect.Maps;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author shanhonghao
 * @since 2018-05-29 14:53
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class AppDTO implements Serializable {

    private int id;
    private String project;
    private String module;
    private AppType appType;
    private List<AppConfigDTO> configs;

    private boolean deleted;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

    public AppConfigDTO getConfigByEnv(EnvDTO env) {
        Optional<AppConfigDTO> defaultConfig = this.getConfigs().stream().filter(config -> config.getEnv().getId() == 0).findFirst();
        Optional<AppConfigDTO> envConfing = this.getConfigs().stream().filter(config -> config.getEnv().getId() == env.getId()).findFirst();

        Map<String, Object> mergedConfigs = Maps.newHashMap();
        defaultConfig.ifPresent(config -> mergedConfigs.putAll(config.getContent()));
        envConfing.ifPresent(config -> mergedConfigs.putAll(config.getContent()));

        AppConfigDTO result = new AppConfigDTO();
        result.setEnv(env);
        result.setContent(mergedConfigs);
        return result;
    }

}

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
    private List<Config> configs;

    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

    @Data
    @NoArgsConstructor
    @ToString
    public static class Config implements Serializable {
        private EnvDTO env;
        private Map<String, Object> configs;
    }

    public Config getConfigByEnv(EnvDTO env) {
        Optional<Config> defaultConfig = this.getConfigs().stream().filter(config -> config.getEnv().getId() == 0).findFirst();
        Optional<Config> envConfing = this.getConfigs().stream().filter(config -> config.getEnv().getId() == env.getId()).findFirst();

        Map<String, Object> mergedConfigs = Maps.newHashMap();
        defaultConfig.ifPresent(config -> mergedConfigs.putAll(config.getConfigs()));
        envConfing.ifPresent(config -> mergedConfigs.putAll(config.getConfigs()));


        Config result = new Config();
        result.setEnv(env);
        result.setConfigs(mergedConfigs);
        return result;
    }

}

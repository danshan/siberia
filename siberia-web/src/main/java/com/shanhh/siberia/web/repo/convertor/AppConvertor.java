package com.shanhh.siberia.web.repo.convertor;

import com.shanhh.siberia.client.dto.app.AppConfigDTO;
import com.shanhh.siberia.client.dto.app.AppDTO;
import com.shanhh.siberia.client.dto.app.AppHostDTO;
import com.shanhh.siberia.client.dto.app.AppLockDTO;
import com.shanhh.siberia.web.repo.entity.App;
import com.shanhh.siberia.web.repo.entity.AppConfig;
import com.shanhh.siberia.web.repo.entity.AppHost;
import com.shanhh.siberia.web.repo.entity.AppLock;
import org.springframework.beans.BeanUtils;

/**
 * @author shanhonghao
 * @since 2018-05-30 17:22
 */
public class AppConvertor {

    public static AppDTO toDTO(App app) {
        if (app == null) {
            return null;
        }
        AppDTO dto = new AppDTO();
        BeanUtils.copyProperties(app, dto);
        return dto;
    }

    public static App toPO(AppDTO dto) {
        if (dto == null) {
            return null;
        }
        App po = new App();
        BeanUtils.copyProperties(dto, po);
        return po;
    }

    public static AppLockDTO toDTO(AppLock lock) {
        if (lock == null) {
            return null;
        }
        AppLockDTO dto = new AppLockDTO();
        BeanUtils.copyProperties(lock, dto);
        dto.setEnv(EnvConvertor.toDTO(lock.getEnv()));
        return dto;
    }

    public static AppHostDTO toDTO(AppHost host) {
        if (host == null) {
            return null;
        }
        AppHostDTO dto = new AppHostDTO();
        BeanUtils.copyProperties(host, dto);
        dto.setEnv(EnvConvertor.toDTO(host.getEnv()));
        return dto;
    }

    public static AppConfigDTO toDTO(AppConfig config) {
        if (config == null) {
            return null;
        }
        AppConfigDTO dto = new AppConfigDTO();
        BeanUtils.copyProperties(config, dto);
        dto.setEnv(EnvConvertor.toDTO(config.getEnv()));
        return dto;
    }

    public static AppConfig toPO(AppConfigDTO config) {
        if (config == null) {
            return null;
        }
        AppConfig po = new AppConfig();
        BeanUtils.copyProperties(config, po);
        po.setEnv(EnvConvertor.toPO(config.getEnv()));
        return po;
    }

}

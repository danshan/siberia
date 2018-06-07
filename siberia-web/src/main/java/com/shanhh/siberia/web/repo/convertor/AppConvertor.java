package com.shanhh.siberia.web.repo.convertor;

import com.shanhh.siberia.client.dto.app.AppDTO;
import com.shanhh.siberia.client.dto.app.AppHostDTO;
import com.shanhh.siberia.client.dto.app.AppLockDTO;
import com.shanhh.siberia.web.repo.entity.App;
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
        if (host== null) {
            return null;
        }
        AppHostDTO dto = new AppHostDTO();
        BeanUtils.copyProperties(host, dto);
        dto.setEnv(EnvConvertor.toDTO(host.getEnv()));
        return dto;
    }

}

package com.shanhh.siberia.web.repo.convertor;

import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.repo.entity.Env;
import org.springframework.beans.BeanUtils;

/**
 * @author shanhonghao
 * @since 2018-06-13 15:39
 */
public class SettingsConvertor {

    public static EnvDTO toDTO(Env env) {
        EnvDTO dto = new EnvDTO();
        BeanUtils.copyProperties(env, dto);
        return dto;
    }

    public static Env toPO(EnvDTO env) {
        Env po = new Env();
        BeanUtils.copyProperties(env, po);
        return po;
    }
}

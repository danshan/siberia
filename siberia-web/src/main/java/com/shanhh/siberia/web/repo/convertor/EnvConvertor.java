package com.shanhh.siberia.web.repo.convertor;

import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.repo.entity.Env;
import org.springframework.beans.BeanUtils;

/**
 * @author shanhonghao
 * @since 2018-05-30 17:24
 */
public class EnvConvertor {

    public static Env toPO(EnvDTO envDTO) {
        if (envDTO == null) {
            return null;
        }
        Env env = new Env();
        BeanUtils.copyProperties(envDTO, env);
        return env;
    }

    public static EnvDTO toDTO(Env env) {
        if (env== null) {
            return null;
        }
        EnvDTO envDTO = new EnvDTO();
        BeanUtils.copyProperties(env, envDTO);
        return envDTO;
    }

}

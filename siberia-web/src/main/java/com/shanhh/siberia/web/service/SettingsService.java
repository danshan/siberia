package com.shanhh.siberia.web.service;

import com.shanhh.siberia.client.dto.settings.EnvCreateReq;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.client.dto.settings.EnvUpdateReq;

import java.util.List;
import java.util.Optional;

/**
 * @author shanhonghao
 * @since 2018-05-24 16:14
 */
public interface SettingsService {
    List<EnvDTO> findEnvs();

    Optional<EnvDTO> deleteEnvById(int envId);

    Optional<EnvDTO> updateEnvById(EnvUpdateReq envDTO);

    Optional<EnvDTO> createEnv(EnvCreateReq env);

    Optional<EnvDTO> loadEnvById(int envId);
}

package com.shanhh.siberia.web.service;

import com.shanhh.siberia.client.dto.settings.EnvDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * @author shanhonghao
 * @since 2018-05-24 16:14
 */
public interface SettingsService {
    Page<EnvDTO> paginateEnvs(int pageNum, int pageSize);

    Optional<EnvDTO> deleteEnvById(int envId);

    Optional<EnvDTO> updateEnvById(EnvDTO envDTO);

    Optional<EnvDTO> createEnv(EnvDTO env);
}

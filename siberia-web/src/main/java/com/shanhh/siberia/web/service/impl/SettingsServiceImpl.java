package com.shanhh.siberia.web.service.impl;

import com.google.common.base.Preconditions;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.repo.EnvRepo;
import com.shanhh.siberia.web.repo.convertor.EnvConvertor;
import com.shanhh.siberia.web.repo.convertor.SettingsConvertor;
import com.shanhh.siberia.web.repo.entity.Env;
import com.shanhh.siberia.web.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author shanhonghao
 * @since 2018-05-24 16:18
 */
@Service
@Slf4j
public class SettingsServiceImpl implements SettingsService {

    @Resource
    private EnvRepo envRepo;

    @Override
    public List<EnvDTO> findEnvs() {
        List<Env> envs = envRepo.findByDeleted(false);
        return envs.stream().map(EnvConvertor::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<EnvDTO> deleteEnvById(int envId) {
        Env exists = envRepo.findOne(envId);
        if (exists == null) {
            return Optional.empty();
        } else {
            envRepo.delete(envId);
            log.info("env deleted, {}", exists);
            return Optional.of(SettingsConvertor.toDTO(exists));
        }
    }

    @Override
    public Optional<EnvDTO> updateEnvById(EnvDTO env) {
        Preconditions.checkArgument(env.getId() > 0);
        Env saved = envRepo.save(SettingsConvertor.toPO(env));
        log.info("env updated, {}", saved);
        return Optional.ofNullable(SettingsConvertor.toDTO(saved));
    }

    @Override
    public Optional<EnvDTO> createEnv(EnvDTO env) {
        Preconditions.checkArgument(env.getId() == 0);
        Env saved = envRepo.save(SettingsConvertor.toPO(env));
        log.info("env created, {}", saved);
        return Optional.ofNullable(SettingsConvertor.toDTO(saved));
    }

    @Override
    public Optional<EnvDTO> loadEnvById(int envId) {
        Env env = envRepo.findOne(envId);
        return Optional.ofNullable(SettingsConvertor.toDTO(env));
    }

}

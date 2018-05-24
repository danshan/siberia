package com.shanhh.siberia.web.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.repo.EnvRepo;
import com.shanhh.siberia.web.repo.entity.Env;
import com.shanhh.siberia.web.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

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
        List<EnvDTO> result = Lists.newLinkedList();

        Iterable<Env> envs = envRepo.findAll();
        envs.forEach(env -> result.add(convert(env)));
        return result;
    }

    @Override
    public Optional<EnvDTO> deleteEnvById(int envId) {
        Env exists = envRepo.findOne(envId);
        if (exists == null) {
            return Optional.empty();
        } else {
            envRepo.delete(envId);
            log.info("env deleted, {}", exists);
            return Optional.of(convert(exists));
        }
    }

    @Override
    public Optional<EnvDTO> updateEnvById(EnvDTO env) {
        Preconditions.checkArgument(env.getId() > 0);
        Env saved = envRepo.save(convert(env));
        log.info("env updated, {}", saved);
        return Optional.ofNullable(convert(saved));
    }

    @Override
    public Optional<EnvDTO> createEnv(EnvDTO env) {
        Preconditions.checkArgument(env.getId() == 0);
        Env saved = envRepo.save(convert(env));
        log.info("env created, {}", saved);
        return Optional.ofNullable(convert(saved));
    }

    private EnvDTO convert(Env env) {
        EnvDTO dto = new EnvDTO();
        BeanUtils.copyProperties(env, dto);
        return dto;
    }

    private Env convert(EnvDTO env) {
        Env po = new Env();
        BeanUtils.copyProperties(env, po);
        return po;
    }

}

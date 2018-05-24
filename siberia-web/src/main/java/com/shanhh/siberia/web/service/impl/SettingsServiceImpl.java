package com.shanhh.siberia.web.service.impl;

import com.google.common.collect.Lists;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.repo.SettingsRepo;
import com.shanhh.siberia.web.repo.entity.Env;
import com.shanhh.siberia.web.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
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
    private SettingsRepo settingsRepo;

    @Override
    public List<EnvDTO> findEnvs() {
        List<EnvDTO> result = Lists.newLinkedList();

        Iterable<Env> envs = settingsRepo.findAll();
        envs.forEach(env -> {
            EnvDTO dto = new EnvDTO();
            BeanUtils.copyProperties(env, dto);
            result.add(dto);
        });
        return result;
    }

    @Override
    public Optional<EnvDTO> deleteEnvById(int envId) {
        return Optional.empty();
    }
}

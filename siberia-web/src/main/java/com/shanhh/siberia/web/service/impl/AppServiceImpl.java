package com.shanhh.siberia.web.service.impl;

import com.google.common.base.Preconditions;
import com.shanhh.siberia.client.dto.app.*;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.repo.AppConfigRepo;
import com.shanhh.siberia.web.repo.AppHostRepo;
import com.shanhh.siberia.web.repo.AppLockRepo;
import com.shanhh.siberia.web.repo.AppRepo;
import com.shanhh.siberia.web.repo.convertor.AppConvertor;
import com.shanhh.siberia.web.repo.convertor.EnvConvertor;
import com.shanhh.siberia.web.repo.entity.*;
import com.shanhh.siberia.web.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author shanhonghao
 * @since 2018-05-30 11:30
 */
@Service
@Slf4j
public class AppServiceImpl implements AppService {

    @Resource
    private AppRepo appRepo;
    @Resource
    private AppLockRepo appLockRepo;
    @Resource
    private AppHostRepo appHostRepo;
    @Resource
    private AppConfigRepo appConfigRepo;

    @Override
    public Optional<AppDTO> loadAppByModule(String project, String module) {
        App app = appRepo.findByProjectAndModule(StringUtils.trimToEmpty(project), module);
        return Optional.ofNullable(AppConvertor.toDTO(app));
    }

    @Override
    public Optional<AppDTO> loadAppById(int appId) {
        App app = appRepo.findOne(appId);
        return Optional.ofNullable(AppConvertor.toDTO(app));
    }

    @Override
    public Page<AppDTO> paginateApps(int pageNum, int pageSize) {
        Page<AppDTO> apps = appRepo.findAll(new PageRequest(pageNum, pageSize)).map(AppConvertor::toDTO);
        return apps;
    }

    @Override
    public Page<AppLockDTO> paginateAppLocks(int pageNum, int pageSize) {
        Page<AppLockDTO> appLocks = appLockRepo.findAll(new PageRequest(pageNum, pageSize)).map(AppConvertor::toDTO);
        return appLocks;
    }

    @Override
    public int updateLockStatus(String project, String module, EnvDTO env, LockStatus lockStatus, String operator) {
        Preconditions.checkArgument(StringUtils.isNotBlank(project), "app should not be empty");
        Preconditions.checkArgument(env != null, "env should not be empty");

        Env envPo = new Env();
        envPo.setId(env.getId());
        if (appLockRepo.findByProjectAndModuleAndEnv(project, module, envPo) != null) {
            return appLockRepo.updateAppLockStatus(project, module, envPo, lockStatus, operator);
        } else {
            AppLock appLock = new AppLock();
            appLock.setProject(StringUtils.trimToEmpty(project));
            appLock.setModule(StringUtils.trimToEmpty(module));
            appLock.setEnv(envPo);
            appLock.setLockStatus(lockStatus);
            appLock.setCreateBy(operator);
            appLock.setUpdateBy(operator);

            AppLock result = appLockRepo.save(appLock);
            return result != null ? 1 : 0;
        }
    }

    @Override
    public Optional<AppHostDTO> loadAppHostByEnv(String project, String module, EnvDTO env) {
        AppHost appHost = appHostRepo.findByProjectAndModuleAndEnv(project, module, EnvConvertor.toPO(env));
        return Optional.ofNullable(AppConvertor.toDTO(appHost));
    }

    @Override
    public List<AppConfigDTO> findConfigsByAppId(int appId) {
        List<AppConfig> configs = appConfigRepo.findByAppId(appId);
        return configs.stream().map(AppConvertor::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<AppConfigDTO> loadConfigById(int appId, int configId) {
        if (appId > 0) {
            return Optional.ofNullable(AppConvertor.toDTO(appConfigRepo.findByAppIdAndId(appId, configId)));
        } else {
            return Optional.ofNullable(AppConvertor.toDTO(appConfigRepo.findOne(configId)));
        }
    }

    @Override
    public Optional<AppConfigDTO> createConfig(AppConfigDTO config) {
        if (config.getId() > 0) {
            return this.updateConfigById(config);
        }
        AppConfig saved = appConfigRepo.save(AppConvertor.toPO(config));
        return Optional.ofNullable(AppConvertor.toDTO(saved));
    }

    @Override
    public Optional<AppConfigDTO> updateConfigById(AppConfigDTO config) {
        AppConfig saved = appConfigRepo.save(AppConvertor.toPO(config));
        return Optional.ofNullable(AppConvertor.toDTO(saved));
    }

}

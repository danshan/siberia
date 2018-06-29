package com.shanhh.siberia.web.service.impl;

import com.google.common.base.Preconditions;
import com.shanhh.siberia.client.dto.app.*;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.repo.*;
import com.shanhh.siberia.web.repo.convertor.AppConvertor;
import com.shanhh.siberia.web.repo.convertor.EnvConvertor;
import com.shanhh.siberia.web.repo.entity.*;
import com.shanhh.siberia.web.resource.errors.BadRequestAlertException;
import com.shanhh.siberia.web.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
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
    @Resource
    private EnvRepo envRepo;

    @Override
    public Optional<AppDTO> createApp(AppCreateReq appCreateReq) {
        App exists = appRepo.findByProjectAndModuleAndDeleted(
                StringUtils.trimToEmpty(appCreateReq.getProject()),
                StringUtils.trimToEmpty(appCreateReq.getModule()),
                false);
        Preconditions.checkArgument(exists == null, "app with project and module already exists");

        AppDTO toSave = new AppDTO();
        toSave.setProject(StringUtils.trimToEmpty(appCreateReq.getProject()));
        toSave.setModule(StringUtils.trimToEmpty(appCreateReq.getModule()));
        toSave.setAppType(appCreateReq.getAppType());

        toSave.setCreateBy("sys");
        toSave.setUpdateBy("sys");
        App saved = appRepo.save(AppConvertor.toPO(toSave));
        log.info("app created, {}", saved);

        return Optional.ofNullable(AppConvertor.toDTO(saved));
    }

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
    public Optional<AppDTO> updateAppById(AppDTO app) {
        AppDTO exists = this.loadAppById(app.getId()).orElseThrow(() -> new BadRequestAlertException("app not exists", "app", "appId"));
        exists.setProject(app.getProject());
        exists.setModule(app.getModule());
        exists.setUpdateTime(new Date());
        App result = appRepo.save(AppConvertor.toPO(exists));
        log.info("app updated: {}", result);
        return Optional.ofNullable(AppConvertor.toDTO(result));
    }

    @Override
    public Optional<AppDTO> deleteAppById(int appId) {
        AppDTO app = this.loadAppById(appId).orElseThrow(() -> new BadRequestAlertException("app not exists", "appId", "appId"));
        app.setDeleted(true);
        app.setUpdateTime(new Date());
        App result = appRepo.save(AppConvertor.toPO(app));
        return Optional.ofNullable(AppConvertor.toDTO(result));
    }

    @Override
    public Page<AppDTO> paginateApps(int pageNum, int pageSize) {
        Page<AppDTO> apps = appRepo.findByDeleted(false, new PageRequest(pageNum, pageSize)).map(AppConvertor::toDTO);
        return apps;
    }

    @Override
    public Page<AppLockDTO> paginateAppLocks(int pageNum, int pageSize) {
        Page<AppLockDTO> appLocks = appLockRepo.findAll(new PageRequest(pageNum, pageSize)).map(AppConvertor::toDTO);
        return appLocks;
    }

    @Override
    public Optional<AppLockDTO> updateLockStatus(String project, String module, EnvDTO env, LockStatus lockStatus, String operator) {
        Preconditions.checkArgument(StringUtils.isNotBlank(project), "app should not be empty");
        Preconditions.checkArgument(env != null, "env should not be empty");

        Env envPo = new Env();
        envPo.setId(env.getId());
        AppLock exists = appLockRepo.findByProjectAndModuleAndEnv(project, module, envPo);
        if (exists != null) {
            exists.setUpdateBy(operator);
            exists.setUpdateTime(new Date());
            exists.setLockStatus(lockStatus);

            AppLock result = appLockRepo.save(exists);
            return Optional.ofNullable(AppConvertor.toDTO(result));
        } else {
            AppLock appLock = new AppLock();
            appLock.setProject(StringUtils.trimToEmpty(project));
            appLock.setModule(StringUtils.trimToEmpty(module));
            appLock.setEnv(envPo);
            appLock.setLockStatus(lockStatus);
            appLock.setCreateBy(operator);
            appLock.setUpdateBy(operator);

            AppLock result = appLockRepo.save(appLock);
            return Optional.ofNullable(AppConvertor.toDTO(result));
        }
    }

    @Override
    public Optional<AppLockDTO> updateLockStatus(int appLockId, LockStatus lockStatus, String operator) {
        Preconditions.checkArgument(appLockId > 0, "app lock id should not be empty");
        AppLock exists = appLockRepo.findOne(appLockId);
        Preconditions.checkArgument(exists != null);

        exists.setLockStatus(lockStatus);
        exists.setUpdateBy(operator);
        exists.setUpdateTime(new Date());
        AppLock result = appLockRepo.save(exists);
        return Optional.ofNullable(AppConvertor.toDTO(result));
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
    public Optional<AppConfigDTO> loadConfigByEnv(int appId, int envId) {
        return Optional.ofNullable(AppConvertor.toDTO(appConfigRepo.findByAppIdAndEnvId(appId, envId)));
    }

    @Override
    @Transactional
    public Optional<AppConfigDTO> updateConfigByEnv(AppConfigUpdateReq config) {
        Optional<AppConfigDTO> exists = this.loadConfigByEnv(config.getAppId(), config.getEnvId());
        if (exists.isPresent()) {
            AppConfigDTO existsConfig = exists.get();
            existsConfig.setContent(config.getContent());
            existsConfig.setUpdateBy(config.getOperator());
            appConfigRepo.updateById(existsConfig.getId(), existsConfig.getContent(), existsConfig.getUpdateBy());
            return Optional.of(existsConfig);
        } else {
            return Optional.ofNullable(this.createAppConfig(config));
        }
    }

    private AppConfigDTO createAppConfig(AppConfigUpdateReq config) {
        AppConfig po = new AppConfig();
        po.setEnv(envRepo.findOne(config.getEnvId()));
        po.setContent(config.getContent());
        po.setCreateBy(config.getOperator());
        po.setUpdateBy(config.getOperator());
        po.setAppId(config.getAppId());
        return AppConvertor.toDTO(appConfigRepo.save(po));
    }

    @Override
    public List<AppHostDTO> findHostsById(int appId) {
        List<AppHost> hosts = appHostRepo.findByAppId(appId);
        return hosts.stream().map(AppConvertor::toDTO).collect(Collectors.toList());
    }

}

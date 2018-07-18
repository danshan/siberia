package com.shanhh.siberia.web.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.shanhh.siberia.client.dto.app.*;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.config.WebSocketConfiguration;
import com.shanhh.siberia.web.repo.*;
import com.shanhh.siberia.web.repo.convertor.AppConvertor;
import com.shanhh.siberia.web.repo.convertor.EnvConvertor;
import com.shanhh.siberia.web.repo.entity.*;
import com.shanhh.siberia.web.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

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
        AppDTO exists = this.loadAppById(app.getId()).orElseThrow(() -> new IllegalArgumentException("app not exists"));
        exists.setProject(app.getProject());
        exists.setModule(app.getModule());
        exists.setUpdateTime(new Date());
        App result = appRepo.save(AppConvertor.toPO(exists));
        log.info("app updated: {}", result);
        return Optional.ofNullable(AppConvertor.toDTO(result));
    }

    @Override
    public Optional<AppDTO> deleteAppById(int appId) {
        AppDTO app = this.loadAppById(appId).orElseThrow(() -> new IllegalArgumentException("app not exists"));
        app.setDeleted(true);
        app.setUpdateTime(new Date());
        App result = appRepo.save(AppConvertor.toPO(app));
        log.info("app deleted: {}", result);
        return Optional.ofNullable(AppConvertor.toDTO(result));
    }

    @Override
    public Page<AppDTO> paginateApps(int pageNum, int pageSize) {
        Page<AppDTO> apps = appRepo
                .findByDeleted(false, new PageRequest(pageNum, pageSize, Sort.Direction.DESC, "id"))
                .map(AppConvertor::toDTO);
        return apps;
    }

    @Override
    public Page<AppLockDTO> paginateAppLocks(int pageNum, int pageSize, int envId) {
        Page<AppLockDTO> appLocks;
        if (envId > 0) {
            Env env = envRepo.findOne(envId);
            Preconditions.checkArgument(env != null, "env not exists");
            appLocks = appLockRepo.findByEnv(env, new PageRequest(pageNum, pageSize, Sort.Direction.DESC, "id"))
                    .map(AppConvertor::toDTO);
        } else {
            appLocks = appLockRepo.findAll(new PageRequest(pageNum, pageSize, Sort.Direction.DESC, "id"))
                    .map(AppConvertor::toDTO);
        }

        return appLocks;
    }

    @Override
    public Optional<AppLockDTO> loadLockByApp(int appId, int envId) {
        return Optional.ofNullable(AppConvertor.toDTO(appLockRepo.findByAppIdAndEnvId(appId, envId)));
    }

    @Override
    public Optional<AppLockDTO> updateLockStatus(int appId, EnvDTO env, LockStatus lockStatus, String operator) {
        Preconditions.checkArgument(appId > 0, "app should not be empty");
        Preconditions.checkArgument(env != null, "env should not be empty");

        AppLock exists = appLockRepo.findByAppIdAndEnvId(appId, env.getId());
        if (exists != null) {
            exists.setUpdateBy(operator);
            exists.setUpdateTime(new Date());
            exists.setLockStatus(lockStatus);

            AppLock saved = appLockRepo.save(exists);
            log.info("app lock updated: {}", saved);

            AppLockDTO result = AppConvertor.toDTO(saved);
            this.pushAppLockUpdatedEvent(result, lockStatus, operator);

            return Optional.ofNullable(result);
        } else {
            AppLock appLock = new AppLock();
            appLock.setApp(appRepo.findOne(appId));
            appLock.setEnv(EnvConvertor.toPO(env));
            appLock.setLockStatus(lockStatus);
            appLock.setCreateBy(operator);
            appLock.setUpdateBy(operator);

            AppLock saved = appLockRepo.save(appLock);
            log.info("app lock created: {}", saved);

            AppLockDTO result = AppConvertor.toDTO(saved);
            this.pushAppLockUpdatedEvent(result, lockStatus, operator);

            return Optional.ofNullable(result);
        }
    }

    private void pushAppLockUpdatedEvent(AppLockDTO lock, LockStatus lockStatus, String updateBy) {
        Map<String, Object> msg = ImmutableMap.<String, Object>builder()
                .put("appLockId", lock.getId())
                .put("status", lockStatus)
                .put("updateBy", updateBy)
                .build();
        simpMessagingTemplate.convertAndSend(WebSocketConfiguration.APP_LOCK_LIST, msg);
    }

    @Override
    public Optional<AppLockDTO> updateLockStatus(int appLockId, LockStatus lockStatus, String operator) {
        Preconditions.checkArgument(appLockId > 0, "app lock id should not be empty");
        AppLock exists = appLockRepo.findOne(appLockId);
        Preconditions.checkArgument(exists != null);

        exists.setLockStatus(lockStatus);
        exists.setUpdateBy(operator);
        exists.setUpdateTime(new Date());
        AppLock saved = appLockRepo.save(exists);
        log.info("app lock updated: {}", saved);

        AppLockDTO result = AppConvertor.toDTO(saved);
        this.pushAppLockUpdatedEvent(result, lockStatus, operator);

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<AppHostDTO> loadAppHostByAppAndEnv(int appId, int envId) {
        AppHost appHost = appHostRepo.findByAppIdAndEnvId(appId, envId);
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
            existsConfig.setUpdateTime(new Date());
            AppConfig saved = appConfigRepo.save(AppConvertor.toPO(existsConfig));
            log.info("app config updated: {}", saved);
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
        AppConfig saved = appConfigRepo.save(po);
        log.info("app config created, {}", saved);
        return AppConvertor.toDTO(saved);
    }

    @Override
    public List<AppHostDTO> findHostsById(int appId) {
        List<AppHost> hosts = appHostRepo.findByAppId(appId);
        return hosts.stream().map(AppConvertor::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<AppHostDTO> loadHostByEnv(int appId, int envId) {
        return Optional.ofNullable(AppConvertor.toDTO(appHostRepo.findByAppIdAndEnvId(appId, envId)));
    }

    @Override
    @Transactional
    public Optional<AppHostDTO> updateHostByEnv(AppHostUpdateReq host) {
        Optional<AppHostDTO> exists = this.loadHostByEnv(host.getAppId(), host.getEnvId());
        if (exists.isPresent()) {
            AppHostDTO existsHost = exists.get();
            existsHost.setHosts(host.getHosts());
            existsHost.setUpdateBy(host.getOperator());
            existsHost.setUpdateTime(new Date());
            AppHost saved = appHostRepo.save(AppConvertor.toPO(existsHost));
            log.info("app hosts updated: {}", saved);
            return Optional.of(existsHost);
        } else {
            return Optional.ofNullable(this.createAppHost(host));
        }
    }

    private AppHostDTO createAppHost(AppHostUpdateReq host) {
        App app = appRepo.findOne(host.getAppId());
        Preconditions.checkArgument(app != null, "app not exists");

        AppHost po = new AppHost();
        BeanUtils.copyProperties(app, po, "id");
        BeanUtils.copyProperties(host, po, "id");

        po.setEnv(envRepo.findOne(host.getEnvId()));
        po.setCreateBy(host.getOperator());
        po.setUpdateBy(host.getOperator());
        AppHost saved = appHostRepo.save(po);
        log.info("app host created, {}", saved);
        return AppConvertor.toDTO(saved);
    }

}

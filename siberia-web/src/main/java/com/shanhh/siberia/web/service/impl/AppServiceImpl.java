package com.shanhh.siberia.web.service.impl;

import com.google.common.base.Preconditions;
import com.shanhh.siberia.client.dto.app.AppDTO;
import com.shanhh.siberia.client.dto.app.AppLockDTO;
import com.shanhh.siberia.client.dto.app.LockStatus;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.repo.AppLockRepo;
import com.shanhh.siberia.web.repo.AppRepo;
import com.shanhh.siberia.web.repo.convertor.AppConvertor;
import com.shanhh.siberia.web.repo.entity.App;
import com.shanhh.siberia.web.repo.entity.AppLock;
import com.shanhh.siberia.web.repo.entity.Env;
import com.shanhh.siberia.web.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

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

    @Override
    public Optional<AppDTO> loadAppByModule(String project, String module) {
        App app = appRepo.findByProjectAndModule(StringUtils.trimToEmpty(project), module);
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

}

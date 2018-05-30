package com.shanhh.siberia.web.service.impl;

import com.shanhh.siberia.client.dto.app.AppDTO;
import com.shanhh.siberia.client.dto.app.AppLockDTO;
import com.shanhh.siberia.client.dto.app.LockStatus;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.repo.AppLockRepo;
import com.shanhh.siberia.web.repo.AppRepo;
import com.shanhh.siberia.web.repo.entity.App;
import com.shanhh.siberia.web.repo.entity.AppLock;
import com.shanhh.siberia.web.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
        return Optional.ofNullable(convert(app));
    }

    @Override
    public Page<AppDTO> paginateApps(int pageNum, int pageSize) {
        Page<AppDTO> apps = appRepo.findAll(new PageRequest(pageNum, pageSize)).map(this::convert);
        return apps;
    }

    @Override
    public Page<AppLockDTO> paginateAppLocks(int pageNum, int pageSize) {
        Page<AppLockDTO> appLocks = appLockRepo.findAll(new PageRequest(pageNum, pageSize)).map(this::convert);
        return appLocks;
    }

    @Override
    public int updateLockStatus(String project, String module, EnvDTO env, LockStatus lockStatus, String operator) {
        return 0;
    }

    private AppDTO convert(App app) {
        if (app == null) {
            return null;
        }
        AppDTO dto = new AppDTO();
        BeanUtils.copyProperties(app, dto);
        return dto;
    }

    private AppLockDTO convert(AppLock lock) {
        if (lock == null) {
            return null;
        }
        AppLockDTO dto = new AppLockDTO();
        BeanUtils.copyProperties(lock, dto);
        return dto;
    }

}

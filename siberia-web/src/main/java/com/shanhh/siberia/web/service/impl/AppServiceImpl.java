package com.shanhh.siberia.web.service.impl;

import com.shanhh.siberia.client.dto.app.AppDTO;
import com.shanhh.siberia.client.dto.app.AppLockDTO;
import com.shanhh.siberia.client.dto.app.LockStatus;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.web.repo.AppRepo;
import com.shanhh.siberia.web.repo.entity.App;
import com.shanhh.siberia.web.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
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

    @Override
    public Optional<AppDTO> loadAppByModule(String project, String module) {
        App app = appRepo.findByProjectAndModule(StringUtils.trimToEmpty(project), module);
        return Optional.ofNullable(convert(app));
    }

    @Override
    public Page<AppLockDTO> paginateAppLock(int start, int limit) {
        return null;
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

}

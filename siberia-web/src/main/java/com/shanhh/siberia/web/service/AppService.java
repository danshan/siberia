package com.shanhh.siberia.web.service;

import com.shanhh.siberia.client.dto.app.AppDTO;
import com.shanhh.siberia.client.dto.app.AppLockDTO;
import com.shanhh.siberia.client.dto.app.LockStatus;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * @author shanhonghao
 * @since 2018-05-29 14:52
 */
public interface AppService {

    Optional<AppDTO> loadAppByModule(String project, String module);

    Page<AppLockDTO> paginateAppLock(int start, int limit);

    int updateLockStatus(String project, String module, EnvDTO env, LockStatus lockStatus, String operator);

}

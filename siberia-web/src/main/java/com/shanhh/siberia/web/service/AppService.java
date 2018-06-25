package com.shanhh.siberia.web.service;

import com.shanhh.siberia.client.dto.app.*;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author shanhonghao
 * @since 2018-05-29 14:52
 */
public interface AppService {

    Optional<AppDTO> createApp(AppCreateReq appCreateReq);

    Optional<AppDTO> loadAppByModule(String project, String module);

    Optional<AppDTO> loadAppById(int appId);

    Optional<AppDTO> updateAppById(AppDTO app);

    Optional<AppDTO> deleteAppById(int appId);

    Page<AppDTO> paginateApps(int pageNum, int pageSize);

    Page<AppLockDTO> paginateAppLocks(int pageNum, int pageSize);

    Optional<AppLockDTO> updateLockStatus(String project, String module, EnvDTO env, LockStatus lockStatus, String operator);

    Optional<AppLockDTO> updateLockStatus(int appLockId, LockStatus lockStatus, String operator);

    Optional<AppHostDTO> loadAppHostByEnv(String project, String module, EnvDTO env);

    List<AppConfigDTO> findConfigsByAppId(int appId);

    Optional<AppConfigDTO> loadConfigById(int appId, int configId);

    Optional<AppConfigDTO> createConfig(AppConfigDTO config);

    Optional<AppConfigDTO> updateConfigById(AppConfigDTO config);

    List<AppHostDTO> findHostsById(int appId);
}

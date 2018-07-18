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

    Page<AppLockDTO> paginateAppLocks(int pageNum, int pageSize, int envId);

    Optional<AppLockDTO> loadLockByApp(int appId, int envId);

    Optional<AppLockDTO> updateLockStatus(int appId, EnvDTO env, LockStatus lockStatus, String operator);

    Optional<AppLockDTO> updateLockStatus(int appLockId, LockStatus lockStatus, String operator);

    Optional<AppHostDTO> loadAppHostByAppAndEnv(int appId, int envId);

    List<AppConfigDTO> findConfigsByAppId(int appId);

    Optional<AppConfigDTO> loadConfigById(int appId, int configId);

    Optional<AppConfigDTO> loadConfigByEnv(int appId, int envId);

    Optional<AppConfigDTO> updateConfigByEnv(AppConfigUpdateReq config);

    List<AppHostDTO> findHostsById(int appId);

    Optional<AppHostDTO> loadHostByEnv(int appId, int envId);

    Optional<AppHostDTO> updateHostByEnv(AppHostUpdateReq host);
}

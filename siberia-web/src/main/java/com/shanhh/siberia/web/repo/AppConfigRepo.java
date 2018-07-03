package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.AppConfig;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author shanhonghao
 * @since 2018-06-12 09:24
 */
public interface AppConfigRepo extends PagingAndSortingRepository<AppConfig, Integer> {

    List<AppConfig> findByAppId(int appId);

    AppConfig findByAppIdAndId(int appId, int id);

    AppConfig findByAppIdAndEnvId(int appId, int envId);

}

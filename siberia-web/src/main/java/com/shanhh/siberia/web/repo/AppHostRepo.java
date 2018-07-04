package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.AppHost;
import com.shanhh.siberia.web.repo.entity.Env;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-06-07 10:18
 */
public interface AppHostRepo extends PagingAndSortingRepository<AppHost, Integer> {

    List<AppHost> findByAppId (int appId);

    AppHost findByAppIdAndEnvId(int appId, int envId);
}

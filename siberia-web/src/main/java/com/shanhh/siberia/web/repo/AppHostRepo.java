package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.AppHost;
import com.shanhh.siberia.web.repo.entity.Env;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author shanhonghao
 * @since 2018-06-07 10:18
 */
public interface AppHostRepo extends PagingAndSortingRepository<AppHost, Integer> {

    AppHost findByProjectAndModuleAndEnv(String project, String module, Env env);

}

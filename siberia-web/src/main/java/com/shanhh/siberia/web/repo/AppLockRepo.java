package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.AppLock;
import com.shanhh.siberia.web.repo.entity.Env;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author shanhonghao
 * @since 2018-05-30 14:04
 */
public interface AppLockRepo extends PagingAndSortingRepository<AppLock, Integer> {

    AppLock findByAppIdAndEnvId(int appId, int envId);

    Page<AppLock> findByEnv(Env env, Pageable pageable);
}

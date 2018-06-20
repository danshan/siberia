package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.client.dto.app.LockStatus;
import com.shanhh.siberia.web.repo.entity.AppLock;
import com.shanhh.siberia.web.repo.entity.Env;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author shanhonghao
 * @since 2018-05-30 14:04
 */
public interface AppLockRepo extends PagingAndSortingRepository<AppLock, Integer> {

    AppLock findByProjectAndModuleAndEnv(String project, String module, Env env);

    @Modifying
    @Query("update AppLock set lockStatus=:lockStatus, updateBy=:updateBy, updateTime=current_time where project=:project and module=:module and env=:env")
    AppLock updateAppLockStatus(@Param("project") String project,
                                             @Param("module") String module,
                                             @Param("env") Env env,
                                             @Param("lockStatus") LockStatus lockStatus,
                                             @Param("updateBy") String updateBy);
}

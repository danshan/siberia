package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.AppLock;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author shanhonghao
 * @since 2018-05-30 14:04
 */
public interface AppLockRepo extends PagingAndSortingRepository<AppLock, Integer> {
}

package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.AppConfig;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-06-12 09:24
 */
public interface AppConfigRepo extends PagingAndSortingRepository<AppConfig, Integer> {

    List<AppConfig> findByAppId(int appId);
}

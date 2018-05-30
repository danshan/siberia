package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.App;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author shanhonghao
 * @since 2018-05-30 11:46
 */
public interface AppRepo extends PagingAndSortingRepository<App, Integer> {

    App findByProjectAndModule(String project, String module);
}

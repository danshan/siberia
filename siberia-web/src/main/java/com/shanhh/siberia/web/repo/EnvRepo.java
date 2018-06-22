package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.web.repo.entity.Env;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-24 16:19
 */
public interface EnvRepo extends PagingAndSortingRepository<Env, Integer> {

    List<Env> findByDeleted(boolean deleted);

}

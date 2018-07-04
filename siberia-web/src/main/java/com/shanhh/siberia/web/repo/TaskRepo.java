package com.shanhh.siberia.web.repo;

import com.shanhh.siberia.client.dto.task.TaskStatus;
import com.shanhh.siberia.web.repo.entity.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-28 16:11
 */
public interface TaskRepo extends PagingAndSortingRepository<Task, Integer> {

    List<Task> findByStatus(TaskStatus status);

    @Query("from Task where deployment.id = :deploymentId")
    List<Task> findStatusByDeploymentId(@Param("deploymentId") int deploymentId);

    @Modifying
    @Query("update Task set status=:taskStatus, startTime=current_timestamp, updateTime=current_time where id = :taskId")
    int updateTaskStatusForStartById(@Param("taskId") int taskId,
                                     @Param("taskStatus") TaskStatus taskStatus);

    @Modifying
    @Query("update Task set status=:taskStatus, endTime=current_timestamp, updateTime=current_time where id = :taskId")
    int updateTaskStatusForEndById(@Param("taskId") int taskId,
                                   @Param("taskStatus") TaskStatus taskStatus);

    @Modifying
    @Query("update Task set nodes=:nodes, updateTime=current_timestamp where id = :taskId")
    int updateTaskNodesById(@Param("taskId") int taskId,
                            @Param("nodes") String nodes);

    @Modifying
    @Query("update Task set status=:status, memo=:memo, updateTime=current_timestamp where id = :taskId")
    int updateTaskStatusAndMemoById(@Param("taskId") int taskId,
                                    @Param("status") TaskStatus status,
                                    @Param("memo") Task.Memo memo);
}

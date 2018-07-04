package com.shanhh.siberia.web.service.impl;

import com.google.common.collect.Lists;
import com.shanhh.siberia.client.dto.app.AppDTO;
import com.shanhh.siberia.client.dto.pipeline.*;
import com.shanhh.siberia.client.dto.settings.EnvDTO;
import com.shanhh.siberia.client.dto.task.TaskCreateReq;
import com.shanhh.siberia.web.repo.PipelineDeploymentRepo;
import com.shanhh.siberia.web.repo.PipelineRepo;
import com.shanhh.siberia.web.repo.PipelineTaskRepo;
import com.shanhh.siberia.web.repo.convertor.PipelineConvertor;
import com.shanhh.siberia.web.repo.entity.Pipeline;
import com.shanhh.siberia.web.repo.entity.PipelineDeployment;
import com.shanhh.siberia.web.repo.entity.PipelineTask;
import com.shanhh.siberia.web.resource.errors.BadRequestAlertException;
import com.shanhh.siberia.web.service.AppService;
import com.shanhh.siberia.web.service.PipelineService;
import com.shanhh.siberia.web.service.SettingsService;
import com.shanhh.siberia.web.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author shanhonghao
 * @since 2018-05-18 15:58
 */
@Service
@Slf4j
public class PipelineServiceImpl implements PipelineService {

    @Resource
    private PipelineRepo pipelineRepo;
    @Resource
    private PipelineDeploymentRepo pipelineDeploymentRepo;
    @Resource
    private PipelineTaskRepo pipelineTaskRepo;
    @Resource
    private AppService appService;
    @Resource
    private SettingsService settingsService;
    @Resource
    private TaskService taskService;

    @Override
    public Page<PipelineDTO> paginatePipelines(int pageNum, int pageSize, PipelineStatus status) {
        List<PipelineStatus> statusVals = Stream.of(PipelineStatus.values())
                .filter(s -> s != PipelineStatus.UNKNOWN)
                .collect(Collectors.toList());
        if (status != PipelineStatus.UNKNOWN) {
            statusVals = Lists.newArrayList(status);
        }

        Page<PipelineDTO> pipelines = pipelineRepo.findByStatus(statusVals, new PageRequest(pageNum, pageSize)).map(PipelineConvertor::toDTO);
        return pipelines;
    }

    @Override
    public Page<PipelineDeploymentDTO> paginatePipelineDeployments(int pageNum, int pageSize, int pipelineId) {
        Page<PipelineDeploymentDTO> results = pipelineDeploymentRepo.findByPipelineId(pipelineId, new PageRequest(pageNum, pageSize))
                .map(PipelineConvertor::toDTO);
        return results;
    }

    @Override
    public Optional<PipelineDeploymentDTO> loadPipelineDeploymentById(int deploymentId) {
        PipelineDeployment deployment = pipelineDeploymentRepo.findOne(deploymentId);
        return Optional.ofNullable(PipelineConvertor.toDTO(deployment));
    }

    @Override
    public Optional<PipelineDTO> loadPipeline(int pipelineId) {
        Pipeline exists = pipelineRepo.findOne(pipelineId);
        if (exists == null) {
            return Optional.empty();
        } else {
            PipelineDTO result = new PipelineDTO();
            BeanUtils.copyProperties(exists, result);
            return Optional.of(result);
        }
    }

    @Override
    public Optional<PipelineDTO> createPipeline(String title, String description, String createBy) {
        Pipeline pipeline = new Pipeline();
        pipeline.setTitle(StringUtils.trimToEmpty(title));
        pipeline.setDescription(StringUtils.trimToEmpty(description));
        pipeline.setStatus(PipelineStatus.RUNNING);
        pipeline.setCreateBy(StringUtils.trimToEmpty(createBy));
        pipeline.setUpdateBy(StringUtils.trimToEmpty(createBy));
        Pipeline saved = pipelineRepo.save(pipeline);

        log.info("pipeline created, {}", saved);
        return Optional.ofNullable(PipelineConvertor.toDTO(saved));
    }

    @Override
    public Optional<PipelineDTO> updatePipelineById(PipelineUpdateReq request) {
        PipelineDTO pipeline = loadPipeline(request.getPipelineId()).orElseThrow(() -> new BadRequestAlertException("pipeline not exists", "pipelineId", "pipelineId"));
        pipeline.setTitle(StringUtils.trimToEmpty(request.getTitle()));
        pipeline.setDescription(StringUtils.trimToEmpty(request.getDescription()));
        pipeline.setUpdateBy(request.getUpdateBy());
        pipeline.setUpdateTime(new Date());

        Pipeline updated = pipelineRepo.save(PipelineConvertor.toPO(pipeline));
        log.info("pipeline updated, {}", request);
        return Optional.ofNullable(PipelineConvertor.toDTO(updated));
    }

    @Override
    public Optional<PipelineDTO> updatePipelineStatusById(PipelineStatusUpdateReq request) {
        PipelineDTO pipeline = loadPipeline(request.getPipelineId()).orElseThrow(() -> new BadRequestAlertException("pipeline not exists", "pipelineId", "pipelineId"));
        pipeline.setStatus(request.getStatus());
        pipeline.setUpdateBy(request.getUpdateBy());
        pipeline.setUpdateTime(new Date());

        Pipeline updated = pipelineRepo.save(PipelineConvertor.toPO(pipeline));
        log.info("pipeline status updated to {}, pipeline={}", request.getStatus(), request);
        return Optional.ofNullable(PipelineConvertor.toDTO(updated));
    }

    @Override
    public Optional<PipelineDeploymentDTO> createPipelineDeployment(PipelineDeploymentCreateReq request) {
        AppDTO app = appService.loadAppByModule(request.getProject(), request.getModule())
                .orElseThrow(() -> new BadRequestAlertException("app not found", "request", "module"));
        PipelineDeploymentDTO deployment = new PipelineDeploymentDTO();
        deployment.setPipelineId(request.getPipelineId());
        deployment.setBuildNo(request.getBuildNo());
        deployment.setCreateBy(StringUtils.trimToEmpty(request.getCreateBy()));
        deployment.setUpdateBy(deployment.getCreateBy());
        deployment.setApp(app);

        PipelineDeployment saved = pipelineDeploymentRepo.save(PipelineConvertor.toPO(deployment));
        log.info("pipeline deployment created, {}", saved);
        return Optional.ofNullable(PipelineConvertor.toDTO(saved));
    }

    @Override
    public Optional<PipelineTaskDTO> createPipelineTask(PipelineTaskReq req) {
        EnvDTO env = settingsService.loadEnvById(req.getEnvId()).orElseThrow(() -> new BadRequestAlertException("env not exists", "envId", "envId"));
        PipelineDeploymentDTO deployment = loadPipelineDeploymentById(req.getDeploymentId()).orElseThrow(() -> new BadRequestAlertException("deployment not exists", "deploymentId", "deploymentId"));

        PipelineTaskDTO task = new PipelineTaskDTO();
        task.setEnv(env);
        task.setDeploymentId(deployment.getId());
        task.setCreateBy(req.getCreateBy());
        task.setUpdateBy(req.getCreateBy());
        PipelineTask saved = pipelineTaskRepo.save(PipelineConvertor.toPO(task));

        TaskCreateReq taskReq = new TaskCreateReq();
        taskReq.setEnvId(env.getId());
        taskReq.setDeploymentId(deployment.getId());
        taskReq.setCreateBy(req.getCreateBy());
        taskService.createTask(taskReq);

        log.info("pipeline task created, {}", saved);
        return Optional.ofNullable(PipelineConvertor.toDTO(saved));
    }

}

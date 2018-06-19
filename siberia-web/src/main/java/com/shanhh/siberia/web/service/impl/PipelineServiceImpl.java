package com.shanhh.siberia.web.service.impl;

import com.shanhh.siberia.client.dto.pipeline.PipelineDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineTaskDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineTaskReq;
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
import java.util.Optional;

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
    public Page<PipelineDTO> paginatePipelines(int pageNum, int pageSize) {
        Page<PipelineDTO> pipelines = pipelineRepo.findAll(new PageRequest(pageNum, pageSize)).map(PipelineConvertor::toDTO);
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
        pipeline.setCreateBy(StringUtils.trimToEmpty(createBy));
        pipeline.setUpdateBy(StringUtils.trimToEmpty(createBy));
        Pipeline saved = pipelineRepo.save(pipeline);

        return Optional.ofNullable(PipelineConvertor.toDTO(saved));
    }

    @Override
    public Optional<PipelineDeploymentDTO> createPipelineDeployment(int pipelineId, String project, String module, int buildNo, String createBy) {
        PipelineDeploymentDTO deployment = new PipelineDeploymentDTO();
        deployment.setPipelineId(pipelineId);
        deployment.setProject(StringUtils.trimToEmpty(project));
        deployment.setModule(StringUtils.trimToEmpty(module));
        deployment.setBuildNo(buildNo);
        deployment.setCreateBy(StringUtils.trimToEmpty(createBy));
        deployment.setUpdateBy(StringUtils.trimToEmpty(createBy));
        deployment.setApp(appService.loadAppByModule(deployment.getProject(), deployment.getModule())
                .orElseThrow(() -> new BadRequestAlertException("Not app exists for moulde", "module", "module")));

        PipelineDeployment saved = pipelineDeploymentRepo.save(PipelineConvertor.toPO(deployment));
        return Optional.ofNullable(PipelineConvertor.toDTO(saved));
    }

    @Override
    public Optional<PipelineTaskDTO> createPipelineTask(PipelineTaskReq req) {
        EnvDTO env = settingsService.loadEnvById(req.getEnvId()).orElseThrow(() -> new BadRequestAlertException("env not exists", "envId", "envId"));
        PipelineDeploymentDTO deployment = loadPipelineDeploymentById(req.getDeploymentId()).orElseThrow(() -> new BadRequestAlertException("deployment not exists", "deploymentId", "deploymentId"));

        PipelineTaskDTO task = new PipelineTaskDTO();
        task.setEnv(env);
        task.setDeployment(deployment);
        task.setCreateBy(req.getCreateBy());
        task.setUpdateBy(req.getCreateBy());
        PipelineTask saved = pipelineTaskRepo.save(PipelineConvertor.toPO(task));

        TaskCreateReq taskReq = new TaskCreateReq();
        taskReq.setEnvId(env.getId());
        taskReq.setPipelineId(deployment.getPipelineId());
        taskReq.setProject(deployment.getProject());
        taskReq.setModule(deployment.getModule());
        taskReq.setBuildNo(deployment.getBuildNo());
        taskReq.setCreateBy(req.getCreateBy());
        taskService.createTask(taskReq);

        return Optional.ofNullable(PipelineConvertor.toDTO(saved));
    }

}

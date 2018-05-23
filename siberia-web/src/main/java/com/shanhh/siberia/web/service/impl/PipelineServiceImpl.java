package com.shanhh.siberia.web.service.impl;

import com.google.common.collect.Lists;
import com.shanhh.siberia.client.dto.pipeline.PipelineDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;
import com.shanhh.siberia.web.repo.PipelineRepo;
import com.shanhh.siberia.web.repo.entity.Pipeline;
import com.shanhh.siberia.web.service.PipelineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
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

    @Override
    public Page<PipelineDTO> paginatePipelines(int pageNum, int pageSize) {
        Page<PipelineDTO> pipelines = pipelineRepo.findAll(new PageRequest(pageNum, pageSize)).map(this::convert);
        return pipelines;
    }

    @Override
    public Page<PipelineDeploymentDTO> paginatePipelineDeployments(int pageNum, int pageSize) {
        List<PipelineDeploymentDTO> results = Lists.newLinkedList();
        for (int i = 0; i < pageSize; i++) {
            results.add(PipelineDeploymentDTO.mock());
        }
        return new PageImpl<>(results);
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
    public PipelineDTO createPipeline(String title, String description, String createBy) {
        Pipeline pipeline = new Pipeline();
        pipeline.setTitle(StringUtils.trimToEmpty(title));
        pipeline.setDescription(StringUtils.trimToEmpty(description));
        pipeline.setCreateBy(StringUtils.trimToEmpty(createBy));
        pipeline.setUpdateBy(StringUtils.trimToEmpty(createBy));
        Pipeline saved = pipelineRepo.save(pipeline);

        return convert(saved);
    }

    private PipelineDTO convert(Pipeline po) {
        PipelineDTO dto = new PipelineDTO();
        BeanUtils.copyProperties(po, dto);
        return dto;
    }

}

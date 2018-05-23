package com.shanhh.siberia.web.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.shanhh.siberia.client.dto.pipeline.PipelineDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;
import com.shanhh.siberia.web.repo.PipelineRepo;
import com.shanhh.siberia.web.repo.entity.Pipeline;
import com.shanhh.siberia.web.service.PipelineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public PageInfo<PipelineDTO> paginatePipelines(int pageNum, int pageSize) {
        List<PipelineDTO> results = Lists.newLinkedList();
        for (int i = 0; i < pageSize; i++) {
            results.add(PipelineDTO.mock());
        }
        return new PageInfo<>(results);
    }

    @Override
    public PageInfo<PipelineDeploymentDTO> paginatePipelineDeployments(int pageNum, int pageSize) {
        List<PipelineDeploymentDTO> results = Lists.newLinkedList();
        for (int i = 0; i < pageSize; i++) {
            results.add(PipelineDeploymentDTO.mock());
        }
        return new PageInfo<>(results);
    }

    @Override
    public PipelineDTO loadPipeline(String pipelineId) {
        return PipelineDTO.mock();
    }

    @Override
    public PipelineDTO createPipeline(String title, String description, String createBy) {
        Pipeline pipeline = new Pipeline();
        pipeline.setTitle(StringUtils.trimToEmpty(title));
        pipeline.setDescription(StringUtils.trimToEmpty(description));
        pipeline.setCreateBy(StringUtils.trimToEmpty(createBy));
        pipeline.setUpdateBy(StringUtils.trimToEmpty(createBy));
        Pipeline saved = pipelineRepo.save(pipeline);

        PipelineDTO result = new PipelineDTO();
        BeanUtils.copyProperties(saved, result);
        return result;
    }

}

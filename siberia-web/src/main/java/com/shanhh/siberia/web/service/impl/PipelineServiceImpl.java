package com.shanhh.siberia.web.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.shanhh.siberia.client.dto.pipeline.PipelineDTO;
import com.shanhh.siberia.web.service.PipelineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanhonghao
 * @since 2018-05-18 15:58
 */
@Service
@Slf4j
public class PipelineServiceImpl implements PipelineService {

    @Override
    public PageInfo<PipelineDTO> paginatePipelines(int pageNum, int pageSize) {
        List<PipelineDTO> results = Lists.newLinkedList();
        for (int i = 0; i < pageSize; i++) {
            results.add(PipelineDTO.mock());
        }
        return new PageInfo<>(results);
    }

}

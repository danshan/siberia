package com.shanhh.siberia.web.repo.convertor;

import com.shanhh.siberia.client.dto.pipeline.PipelineDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;
import com.shanhh.siberia.web.repo.entity.Pipeline;
import com.shanhh.siberia.web.repo.entity.PipelineDeployment;
import org.springframework.beans.BeanUtils;

/**
 * @author shanhonghao
 * @since 2018-06-13 15:43
 */
public class PipelineConvertor {

    public static PipelineDTO toDTO(Pipeline po) {
        if (po == null) {
            return null;
        }
        PipelineDTO dto = new PipelineDTO();
        BeanUtils.copyProperties(po, dto);
        return dto;
    }

    public static PipelineDeploymentDTO toDTO(PipelineDeployment po) {
        if (po == null) {
            return null;
        }
        PipelineDeploymentDTO dto = new PipelineDeploymentDTO();
        BeanUtils.copyProperties(po, dto);
        dto.setApp(AppConvertor.toDTO(po.getApp()));
        return dto;
    }

    public static PipelineDeployment toPO(PipelineDeploymentDTO dto) {
        if (dto == null) {
            return null;
        }
        PipelineDeployment po = new PipelineDeployment();
        BeanUtils.copyProperties(dto, po);
        po.setApp(AppConvertor.toPO(dto.getApp()));
        return po;
    }
}

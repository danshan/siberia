package com.shanhh.siberia.web.repo.convertor;

import com.shanhh.siberia.client.dto.pipeline.PipelineDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineDeploymentDTO;
import com.shanhh.siberia.client.dto.pipeline.PipelineTaskDTO;
import com.shanhh.siberia.web.repo.entity.Pipeline;
import com.shanhh.siberia.web.repo.entity.PipelineDeployment;
import com.shanhh.siberia.web.repo.entity.PipelineTask;
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

    public static Pipeline toPO(PipelineDTO dto) {
        if (dto == null) {
            return null;
        }
        Pipeline po = new Pipeline();
        BeanUtils.copyProperties(dto, po);
        return po;
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

    public static PipelineTaskDTO toDTO(PipelineTask po) {
        if (po == null) {
            return null;
        }
        PipelineTaskDTO dto = new PipelineTaskDTO();
        BeanUtils.copyProperties(po, dto);
        dto.setEnv(SettingsConvertor.toDTO(po.getEnv()));
        return dto;
    }

    public static PipelineTask toPO(PipelineTaskDTO dto) {
        if (dto == null) {
            return null;
        }
        PipelineTask po = new PipelineTask();
        BeanUtils.copyProperties(dto, po);
        po.setEnv(SettingsConvertor.toPO(dto.getEnv()));
        return po;
    }
}


package com.shanhh.siberia.client.dto.pipeline;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2018-05-22 11:30
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class PipelineTaskDTO implements Serializable {

}

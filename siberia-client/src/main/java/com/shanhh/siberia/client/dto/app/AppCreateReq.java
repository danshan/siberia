package com.shanhh.siberia.client.dto.app;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2018-06-25 15:13
 */
@Data
@NoArgsConstructor
public class AppCreateReq implements Serializable  {
    private String project;
    private String module;
    private AppType appType;
}

package com.shanhh.siberia.client.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2017-12-20 10:55
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RespCode implements Serializable {
    private String code = "200";
    private String message = "success";
}
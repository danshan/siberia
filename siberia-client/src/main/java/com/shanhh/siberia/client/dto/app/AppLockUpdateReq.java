package com.shanhh.siberia.client.dto.app;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2018-06-20 14:31
 */
@Data
@NoArgsConstructor
public class AppLockUpdateReq implements Serializable {
    private int appLockId;
    private LockStatus lockStatus;
}

package com.shanhh.siberia.web.service.ansible;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.exec.ExecuteException;


/**
 * ansible 执行结果
 *
 * @author Dan
 * @since 2016-06-22 15:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnsibleResult {
    private int exitCode;
    private ExecuteException executeException;
}

package com.shanhh.siberia.client.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author shanhonghao
 * @since 2018-05-23 11:19
 */
@Data
@NoArgsConstructor
@ToString
public class CurrentUserDTO implements Serializable {
    private String id;
    private String name;
    private String avatar;

    public static CurrentUserDTO mock() {
        CurrentUserDTO mock = new CurrentUserDTO();
        mock.setId("0009849");
        mock.setName("FuckGFW");
        mock.setAvatar("https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png");
        return mock;
    }
}

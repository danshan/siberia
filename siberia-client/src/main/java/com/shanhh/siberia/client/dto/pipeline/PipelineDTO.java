package com.shanhh.siberia.client.dto.pipeline;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.RandomUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 发布流程
 *
 * @author Dan
 * @since 2016-03-15 13:43
 */
@Data
@NoArgsConstructor
@ToString(exclude = {"createTime", "updateTime"})
public class PipelineDTO implements Serializable {

    private int id;
    @NotNull
    @Size(min = 1, max = 255)
    private String title;
    @NotNull
    @Size(min = 0, max = 65535)
    private String description;
    private String createBy;
    private String updateBy;
    private Date createTime;
    private Date updateTime;

    public static PipelineDTO mock() {
        String[] title = {
                "Alipay",
                "Angular",
                "Ant Design",
                "Ant Design Pro",
                "Bootstrap",
                "React",
                "Vue",
                "Webpack",
        };

        String[] desc = {
                "那是一种内在的东西， 他们到达不了，也无法触及的",
                "希望是一个好东西，也许是最好的，好东西是不会消亡的",
                "生命就像一盒巧克力，结果往往出人意料",
                "城镇中有那么多的酒馆，她却偏偏走进了我的酒馆",
                "那时候我只会想自己想要什么，从不想自己拥有什么",
        };

        String[] user = {
                "付小小",
                "曲丽丽",
                "林东东",
                "周星星",
                "吴加好",
                "朱偏右",
                "鱼酱",
                "乐哥",
                "谭小仪",
                "仲尼",
        };

        PipelineDTO mock = new PipelineDTO();
        mock.setId(RandomUtils.nextInt());
        mock.setTitle(title[RandomUtils.nextInt() % title.length]);
        mock.setDescription(desc[RandomUtils.nextInt() % desc.length]);
        mock.setCreateBy(user[RandomUtils.nextInt() % user.length]);
        mock.setUpdateBy(user[RandomUtils.nextInt() % user.length]);
        mock.setCreateTime(new Date(new Date().getTime() - 1000 * 60 * 60 * 2 * RandomUtils.nextInt(5, 10)));
        mock.setUpdateTime(new Date(new Date().getTime() - 1000 * 60 * 60 * 2 * RandomUtils.nextInt(1, 5)));
        return mock;
    }

}

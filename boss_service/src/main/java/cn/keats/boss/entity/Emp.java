package cn.keats.boss.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 这是MyBatis Generator自动生成的Model Class.
 * 对应的数据表是 : emp
 * @author Keats
 * @date 2020-01-28 22:24:57
 */
@Data
public class Emp implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 入职时间
     */
    private Date joinTime;

    /**
     * 性别 0 未知 1 男 2 女
     */
    private Byte sex;

    /**
     * 地址
     */
    private String address;

    /**
     * 教育背景
     */
    private String educationBackground;

    /**
     * 年龄
     */
    private Byte age;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;

}
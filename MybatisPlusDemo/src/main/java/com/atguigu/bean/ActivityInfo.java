package com.atguigu.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 活动表
 * </p>
 *
 * @author atguigu
 * @since 2023-08-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("activity_info")
public class ActivityInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动类型（1：满减，2：折扣）
     */
    private String activityType;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 开始时间
     */
    private Timestamp startTime;

    /**
     * 结束时间
     */
    private Timestamp endTime;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 修改时间
     */
    private Timestamp operateTime;
}

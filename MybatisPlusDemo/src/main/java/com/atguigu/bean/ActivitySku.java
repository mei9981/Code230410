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
 * 活动商品关联表
 * </p>
 *
 * @author atguigu
 * @since 2023-08-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("activity_sku")
public class ActivitySku implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动id 
     */
    private Long activityId;

    /**
     * sku_id
     */
    private Long skuId;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 修改时间
     */
    private Timestamp operateTime;
}

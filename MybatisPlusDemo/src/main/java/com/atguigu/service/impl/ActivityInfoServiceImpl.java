package com.atguigu.service.impl;

import com.atguigu.bean.ActivityInfo;
import com.atguigu.mapper.ActivityInfoMapper;
import com.atguigu.service.ActivityInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-08-21
 */
@Service
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {

}

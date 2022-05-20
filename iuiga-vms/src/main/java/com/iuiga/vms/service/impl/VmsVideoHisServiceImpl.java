package com.iuiga.vms.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iuiga.common.utils.SecurityUtils;
import com.iuiga.vms.domain.VmsVideo;
import com.iuiga.vms.domain.VmsVideoHis;
import com.iuiga.vms.mapper.VmsVideoHisMapper;
import com.iuiga.vms.service.VmsVideoHisService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 视频历史Service
 */
@Service
public class VmsVideoHisServiceImpl extends ServiceImpl<VmsVideoHisMapper, VmsVideoHis> implements VmsVideoHisService {

    /**
     * 生成视频历史
     *
     * @param vmsVideoList
     */
    @Override
    public void generateVideoHistory(List<VmsVideo> vmsVideoList) {
        List<VmsVideoHis> hisList = new ArrayList<>();
        for (VmsVideo vmsVideo : vmsVideoList) {
            VmsVideoHis his = new VmsVideoHis();
            his.setVideoUuid(vmsVideo.getVideoUuid());
            his.setSnapShot(JSON.toJSONString(vmsVideo));
            his.setCreateBy(SecurityUtils.getUsername());
            his.setCreateTime(new Date());
            hisList.add(his);
        }
        if(hisList.size()>0) {
            saveBatch(hisList);
        }
    }
}

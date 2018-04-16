package com.sdwfqin.baidumaplib.o2o;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.trace.api.track.HistoryTrackResponse;

/**
 * 描述：O2O地图监听器
 *
 * @author zhangqin
 * @date 2018/4/12
 */
public interface O2OMapListener {

    /**
     * 页面加载完成
     */
    void initOk();

    /**
     * 当前位置
     */
    void onReceiveLocation(BDLocation location);

    /**
     * 地址转经纬度
     * @param result
     */
    void onGetGeoCodeResult(GeoCodeResult result);

    /**
     * 历史轨迹
     */
    void onHistoryTrackCallback(HistoryTrackResponse response);
}

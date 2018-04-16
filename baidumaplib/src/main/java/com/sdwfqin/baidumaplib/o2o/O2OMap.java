package com.sdwfqin.baidumaplib.o2o;

import com.baidu.mapapi.search.route.PlanNode;
import com.sdwfqin.baidumaplib.utils.MarkerBean;

import java.util.List;

/**
 * 描述：
 *
 * @author zhangqin
 * @date 2018/4/12
 */
interface O2OMap {

    /**
     * 地图监听器
     *
     * @param o2OMapListener
     */
    void setO2OMapListener(O2OMapListener o2OMapListener);

    /**
     * 开始获取历史轨迹(循环)
     */
    void startGetTraceHistory();

    /**
     * 获取指定时间段的历史轨迹（单次）
     */
    void startGetTraceHistory(long startTime, long endTime);

    /**
     * 开启轨迹记录
     */
    void startTrace();

    /**
     * 获取当前位置
     */
    void startLocation();

    /**
     * 指定坐标居中
     */
    void setCenter(double latitude, double longitude);

    /**
     * 绘制Marker
     */
    void drawMarker(List<MarkerBean> beanList);

    /**
     * 停止轨迹记录,这是个服务，可以不停止
     */
    void stopTrace();

    /**
     * 停止全部百度地图服务
     */
    void onDestroyMapAll();

    /**
     * 路线规划
     */
    void startRoutePlan(PlanNode stNode, PlanNode enNode);

    /**
     * 地址转经纬度
     *
     * @param city
     * @param address
     */
    void geocode(String city, String address);
}

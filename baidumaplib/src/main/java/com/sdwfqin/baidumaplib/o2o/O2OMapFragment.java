package com.sdwfqin.baidumaplib.o2o;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.MotionEvent;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.PushMessage;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.sdwfqin.baidumaplib.R;
import com.sdwfqin.baidumaplib.utils.MarkerBean;
import com.sdwfqin.baidumaplib.utils.overlayutil.DrivingRouteOverlay;
import com.sdwfqin.quicklib.base.BaseFragment;
import com.sdwfqin.quicklib.utils.eventbus.Event;
import com.sdwfqin.quicklib.utils.eventbus.EventBusUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述：百度地图O2O
 *
 * @author zhangqin
 * @date 2018/4/11
 */
public class O2OMapFragment extends BaseFragment implements O2OMap {

    // 标记点
    public final static int CURRENT = 1;
    public final static int SHOP = 2;
    public final static int USER = 3;
    /**
     * 是否是买家
     */
    public static boolean isUser = false;
    /**
     * 实时轨迹是否居中
     */
    public static boolean isQueHis = false;

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    /**
     * 轨迹服务
     */
    private Trace mTrace;
    /**
     * 轨迹服务客户端
     */
    private LBSTraceClient mTraceClient;
    /**
     * 轨迹服务监听器
     */
    private OnTraceListener mTraceListener;
    /**
     * 位置客户端
     */
    public LocationClient mLocationClient = null;
    /**
     * 定位监听器
     */
    private MyLocationListener mLocationListener = null;
    /**
     * 轨迹服务ID
     */
    private long mServiceId = 163068;
    /**
     * 设备标识
     */
    private String mEntityName = "myTrace";
    /**
     * 获取历史轨迹的计时器
     */
    private Timer mTimer;
    /**
     * 路线规划监听器
     */
    private OnGetRoutePlanResultListener mGetRoutePlanResultListener;
    /**
     * 骑行路线规划
     */
    private RoutePlanSearch mRoutePlanSearch;
    /**
     * 轨迹监听器
     */
    private OnTrackListener mTrackListener;
    /**
     * 历史轨迹请求实例
     */
    private HistoryTrackRequest mHistoryTrackRequest;
    /**
     * 我的轨迹
     */
    private Polyline mPolyline;

    /**
     * 获取历史轨迹
     */
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            EventBusUtil.sendEvent(new Event(1));
        }
    };

    /**
     * 坐标转换
     */
    private GeoCoder mGeoCoder;

    /**
     * 当前经纬度信息
     */
    public double mCurrentLatitude;
    public double mCurrentLongitude;
    /**
     * 商家经纬度信息
     */
    public double mShopLatitude;
    public double mShopLongitude;
    /**
     * 买家经纬度信息
     */
    public double mUserLatitude;
    public double mUserLongitude;

    /**
     * 我的位置标记点
     */
    private Marker mCurrentMarker = null;
    /**
     * 商家位置标记点
     */
    private Marker mShopMarker = null;
    /**
     * 买家位置标记点
     */
    private Marker mUserMarker = null;

    /**
     * 自定义监听器
     */
    private O2OMapListener mO2OMapListener;

    public static O2OMapFragment newInstance(long serviceId, String entityName) {

        Bundle args = new Bundle();
        args.putLong("serviceId", serviceId);
        args.putString("entityName", entityName);
        O2OMapFragment fragment = new O2OMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_baidu_map;
    }

    @Override
    protected void lazyLoadShow(boolean isLoad) {

    }

    @Override
    protected void initEventAndData() {
        mServiceId = getArguments().getLong("serviceId");
        mEntityName = getArguments().getString("entityName");

        mMapView = mView.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        mRoutePlanSearch = RoutePlanSearch.newInstance();
        mTimer = new Timer();
        mLocationListener = new MyLocationListener();
        mGeoCoder = GeoCoder.newInstance();
        initGetGeoCodeResultListener();
        initLocation();
        initTrace();
        initOnTraceListener();
        initTraceHistoryListener();
        initRoutePlanResultListener();

        mBaiduMap.setOnMapTouchListener(
                motionEvent -> {
                    // 滑动冲突
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        mView.getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        mView.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    isQueHis = false;
                });

        mO2OMapListener.initOk();

    }

    /**
     * 地图监听器
     *
     * @param o2OMapListener
     */
    public void setO2OMapListener(O2OMapListener o2OMapListener) {
        mO2OMapListener = o2OMapListener;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case 1:
                // 开始时间(单位：秒)
                long startTime = System.currentTimeMillis() / 1000 - 2 * 60 * 60;
                // 结束时间(单位：秒)
                long endTime = System.currentTimeMillis() / 1000;
                getTraceHistory(startTime, endTime);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

        //位置采集周期
        // 在Android 6.0及以上系统，若定制手机使用到doze模式，请求将应用添加到白名单。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = AppUtils.getAppPackageName();
            PowerManager powerManager = (PowerManager) Utils.getApp().getSystemService(Context.POWER_SERVICE);
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoring) {
                Intent intent = new Intent(
                        Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    /**
     * 初始化轨迹服务
     */
    private void initTrace() {
        // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
        boolean isNeedObjectStorage = true;
        // 定位周期(单位:秒)
        int gatherInterval = 1;
        // 打包回传周期(单位:秒)
        int packInterval = 3;
        // 初始化轨迹服务
        mTrace = new Trace(mServiceId, mEntityName, isNeedObjectStorage);
        // 初始化轨迹服务客户端
        mTraceClient = new LBSTraceClient(Utils.getApp());
        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);
    }

    /**
     * 初始化轨迹服务监听器
     */
    private void initOnTraceListener() {
        mTraceListener = new OnTraceListener() {
            @Override
            public void onBindServiceCallback(int i, String s) {
                LogUtils.e(i + " + " + s);
            }

            // 开启服务回调
            @Override
            public void onStartTraceCallback(int status, String message) {
                LogUtils.e(status + " + " + message);
                if (status == 0) {
                    // 开启采集
                    mTraceClient.startGather(mTraceListener);
                } else {
                    showMsg(message);
                }
            }

            // 停止服务回调
            @Override
            public void onStopTraceCallback(int status, String message) {
            }

            // 开启采集回调
            @Override
            public void onStartGatherCallback(int status, String message) {
                LogUtils.e(status + " + " + message);
            }

            // 停止采集回调
            @Override
            public void onStopGatherCallback(int status, String message) {
            }

            // 推送回调
            @Override
            public void onPushCallback(byte messageNo, PushMessage message) {
                LogUtils.e(messageNo + " + " + message);
            }

            @Override
            public void onInitBOSCallback(int i, String s) {

            }
        };
    }

    /**
     * 获取位置
     */
    private void initLocation() {
        mLocationClient = new LocationClient(Utils.getApp());
        //声明LocationClient类
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(0);
        // 需要地址信息
        option.setIsNeedAddress(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        option.setOpenGps(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(true);
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        mLocationClient.setLocOption(option);
    }

    /**
     * 位置监听
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            LogUtils.e(location.getAddrStr());

            //获取纬度信息
            mCurrentLatitude = location.getLatitude();
            //获取经度信息
            mCurrentLongitude = location.getLongitude();
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

            mO2OMapListener.onReceiveLocation(location);

        }
    }

    /**
     * 经纬度转换监听器
     */
    private void initGetGeoCodeResultListener() {
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {

                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                    showMsg("没有检索到结果");
                }

                mO2OMapListener.onGetGeoCodeResult(result);
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                LogUtils.e("geoCoder" + reverseGeoCodeResult.error);
            }
        });
    }

    /**
     * 路径规划监听器
     */
    private void initRoutePlanResultListener() {
        mGetRoutePlanResultListener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    showMsg("抱歉，未找到结果");
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    // getSuggestAddrInfo()
                    showMsg("起终点或途经点地址有岐义");
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    zoomToSpan();
                } else {
                    LogUtils.e("route result", "结果数<0");
                    return;
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            /**
             * 获取骑行线路规划结果
             */
            @Override
            public void onGetBikingRouteResult(BikingRouteResult result) {

            }
        };

        mRoutePlanSearch.setOnGetRoutePlanResultListener(mGetRoutePlanResultListener);
    }

    /**
     * 初始化轨迹监听器
     */
    private void initTraceHistoryListener() {
        // 初始化轨迹监听器
        mTrackListener = new OnTrackListener() {
            // 历史轨迹回调
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                List<LatLng> trackPoints = new ArrayList<>();
                List<TrackPoint> points = response.getTrackPoints();//获取轨迹点
                try {
                    for (TrackPoint trackPoint : points) {
                        //将轨迹点转化为地图画图层的LatLng类
                        trackPoints.add(convertTrace2Map(trackPoint.getLocation()));
                    }
                } catch (Exception e) {
                    LogUtils.e(e);
                }
                drawLine(trackPoints);
                mO2OMapListener.onHistoryTrackCallback(response);
            }
        };
    }

    /**
     * 初始化历史轨迹设置
     */
    private void getTraceHistory(long startTime, long endTime) {
        // 请求标识
        int tag = 1;
        // 创建历史轨迹请求实例
        HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(tag, mServiceId, mEntityName);

        //设置轨迹查询起止时间
        // 设置开始时间
        historyTrackRequest.setStartTime(startTime);
        // 设置结束时间
        historyTrackRequest.setEndTime(endTime);
        historyTrackRequest.setPageSize(5000);
        // 设置需要纠偏
        historyTrackRequest.setProcessed(true);
        // 创建纠偏选项实例
        ProcessOption processOption = new ProcessOption();
        // 设置需要去噪
        processOption.setNeedDenoise(true);
        // 设置需要抽稀
        processOption.setNeedVacuate(true);
        // 设置需要绑路
        processOption.setNeedMapMatch(true);
        // 设置精度过滤值(定位精度大于100米的过滤掉)
        processOption.setRadiusThreshold(100);
        // 设置纠偏选项
        historyTrackRequest.setProcessOption(processOption);
        // 查询历史轨迹
        mTraceClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
    }

    /**
     * 绘制历史轨迹
     *
     * @param trackPoints
     */
    private void drawLine(List<LatLng> trackPoints) {

        if (trackPoints.size() < 2) {
            return;
        }

        OverlayOptions overlayOptions = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(trackPoints);

        if (mCurrentMarker != null) {
            mCurrentMarker.setPosition(trackPoints.get(trackPoints.size() - 1));
        } else {
            List<MarkerBean> beanList = new ArrayList<>();
            LatLng latLng = trackPoints.get(trackPoints.size() - 1);
            beanList.add(new MarkerBean(CURRENT, latLng.latitude, latLng.longitude, R.drawable.green, "骑手"));
            // 绘制
            drawMarker(beanList);
        }
        if (isQueHis) {
            setCenter(trackPoints.get(trackPoints.size() - 1).latitude,
                    trackPoints.get(trackPoints.size() - 1).longitude);
        }

        if (mPolyline == null) {
            mPolyline = (Polyline) mBaiduMap.addOverlay(overlayOptions);
        } else {
            mPolyline.setPoints(trackPoints);
        }
    }

    /**
     * 将轨迹坐标对象转换为地图坐标对象
     */
    public LatLng convertTrace2Map(com.baidu.trace.model.LatLng traceLatLng) {
        return new LatLng(traceLatLng.latitude, traceLatLng.longitude);
    }

    @Override
    public void startGetTraceHistory() {
        isQueHis = true;
        mTimer.schedule(mTimerTask, 1000, 3000);
    }

    @Override
    public void startGetTraceHistory(long startTime, long endTime) {
        getTraceHistory(startTime, endTime);
    }

    @Override
    public void startTrace() {
        // 开启轨迹上传服务
        mTraceClient.startTrace(mTrace, mTraceListener);
    }

    @Override
    public void startLocation() {
        mLocationClient.start();
    }

    /**
     * 指定坐标居中
     */
    @Override
    public void setCenter(double latitude, double longitude) {
        LatLng ll = new LatLng(latitude, longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(17f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * 缩放地图，使所有Overlay都在合适的视野内
     * </p>
     */
    public void zoomToSpan() {

        List<Marker> markers = new ArrayList<>();
        markers.add(mCurrentMarker);
        markers.add(mShopMarker);
        markers.add(mUserMarker);

        if (markers.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                if (marker != null) {
                    try {
                        builder.include(marker.getPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }
    }

    @Override
    public void drawMarker(List<MarkerBean> beanList) {
        for (MarkerBean markerBean : beanList) {
            //定义Maker坐标点
            LatLng point = new LatLng(markerBean.getLatitude(), markerBean.getLontitude());
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(markerBean.getImg());
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            if (markerBean.getId() == CURRENT) {
                mCurrentMarker = (Marker) mBaiduMap.addOverlay(option);
            } else if (markerBean.getId() == SHOP) {
                mShopMarker = (Marker) mBaiduMap.addOverlay(option);
            } else if (markerBean.getId() == USER) {
                mUserMarker = (Marker) mBaiduMap.addOverlay(option);
            } else {
                mBaiduMap.addOverlay(option);
            }

//            //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
//            Bundle bundle = new Bundle();
//            //info必须实现序列化接口
//            bundle.putSerializable("data", markerBean);
//            mMarker.setExtraInfo(bundle);
        }
    }

    @Override
    public void stopTrace() {
        // 关闭服务并停止采集
        mTraceClient.stopTrace(mTrace, mTraceListener);
    }

    @Override
    public void onDestroyMapAll() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (mRoutePlanSearch != null) {
            mRoutePlanSearch.destroy();
        }
        stopTrace();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (mGeoCoder != null) {
            mGeoCoder.destroy();
        }
        isUser = false;
    }

    @Override
    public void startRoutePlan(PlanNode stNode, PlanNode enNode) {
        mRoutePlanSearch
                .drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode)
                        .to(enNode));
    }

    @Override
    public void geocode(String city, String address) {
        GeoCodeOption geoCodeOption = new GeoCodeOption();
        geoCodeOption.city(city);
        geoCodeOption.address(address);
        mGeoCoder.geocode(geoCodeOption);
    }
}

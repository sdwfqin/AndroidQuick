package com.sdwfqin.quickseed.ui.mvp.contract;

import com.sdwfqin.quicklib.mvp.BasePresenter;
import com.sdwfqin.quicklib.mvp.BaseView;
import com.sdwfqin.quickseed.ui.mvvm.WeatherBean;

/**
 * mvp 接口
 * <p>
 * 
 * @author 张钦
 * @date 2020/4/16
 */
public interface WeatherContract {

    public interface WeatherView extends BaseView {
        void refreshView(WeatherBean weatherBean);
    }
    public interface WeatherPresenter extends BasePresenter<WeatherView> {
        void loadData();
    }
}

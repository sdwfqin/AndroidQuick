package com.sdwfqin.quickseed.ui.mvp.contract;

import com.sdwfqin.quicklib.mvp.IBasePresenter;
import com.sdwfqin.quicklib.mvp.IBaseView;
import com.sdwfqin.quickseed.ui.mvvm.WeatherBean;

/**
 * mvp 接口
 * <p>
 * 
 * @author 张钦
 * @date 2020/4/16
 */
public interface WeatherContract {

    interface WeatherView extends IBaseView {
        void refreshView(WeatherBean weatherBean);
    }
    interface WeatherPresenter extends IBasePresenter<WeatherView> {
        void loadData();
    }
}

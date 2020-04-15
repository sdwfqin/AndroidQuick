package com.sdwfqin.quicklib.mvvm;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * BaseViewModel
 * <p>
 *
 * @author 张钦
 * @date 2020/4/15
 */
public class BaseViewModel extends ViewModel {

    /**
     * 加载窗状态
     */
    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    /**
     * 通用网络请求异常
     */
    public final MutableLiveData<Throwable> networkError = new MutableLiveData<>();
}

package com.sdwfqin.quickseed.mvpretrofit;

import com.sdwfqin.quickseed.mvpretrofit.demomodel.LoginModel;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * 描述：Api接口
 *
 * @author 张钦
 * @date 2017/9/25
 */
public interface ServiceApi {

    /**
     * 测试接口
     */
    @POST("test")
    Observable<BaseResponse<LoginModel>> login();
}

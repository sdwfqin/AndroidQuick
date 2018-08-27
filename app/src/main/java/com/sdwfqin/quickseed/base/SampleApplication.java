package com.sdwfqin.quickseed.base;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * 描述：tinker热更新配置
 *
 * @author 张钦
 * @date 2018/8/23
 */
public class SampleApplication extends TinkerApplication {

    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL,
                "com.sdwfqin.quickseed.base.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader",
                false);
    }
}

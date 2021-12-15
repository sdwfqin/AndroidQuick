package io.github.sdwfqin.quickseed;

import com.didichuxing.doraemonkit.DoKit;

import dagger.hilt.android.HiltAndroidApp;
import io.github.sdwfqin.samplecommonlibrary.base.SampleApplication;

@HiltAndroidApp
public class App extends SampleApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        new DoKit.Builder(this).build();
    }
}

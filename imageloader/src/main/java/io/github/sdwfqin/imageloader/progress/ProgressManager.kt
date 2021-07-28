package io.github.sdwfqin.imageloader.progress

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.*

/**
 * @author by sunfusheng on 2017/6/14.
 */
object ProgressManager {

    private val listenersMap = Collections.synchronizedMap(HashMap<String, OnProgressListener>())

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addNetworkInterceptor { chain: Interceptor.Chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                response.newBuilder()
                    .body(
                        ProgressResponseBody(
                            request.url().toString(),
                            LISTENER,
                            response.body()!!
                        )
                    )
                    .build()
            }
            .build()
    }

    /**
     * 拦截器
     */
    private val LISTENER: ProgressResponseBody.InternalProgressListener =
        object : ProgressResponseBody.InternalProgressListener {
            override fun onProgress(url: String, bytesRead: Long, totalBytes: Long) {
                val onProgressListener = getProgressListener(url)
                if (onProgressListener != null) {
                    val percentage = (bytesRead * 1f / totalBytes * 100f).toInt()
                    onProgressListener.onLoading(percentage)
                    if (percentage >= 100) {
                        onProgressListener.onLoadSuccess()
                        removeListener(url)
                    }
                }
            }
        }

    @JvmStatic
    fun addListener(url: String, listener: OnProgressListener?) {
        if (!TextUtils.isEmpty(url) && listener != null) {
            listenersMap[url] = listener
        }
    }

    fun removeListener(url: String) {
        if (!TextUtils.isEmpty(url)) {
            listenersMap.remove(url)
        }
    }

    fun getProgressListener(url: String): OnProgressListener? {
        return if (TextUtils.isEmpty(url) || listenersMap.size == 0) {
            null
        } else listenersMap[url]
    }
}
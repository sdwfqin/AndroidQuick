package io.github.sdwfqin.widget.pictureupload

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import io.github.sdwfqin.widget.R
import java.util.*

/**
 * 描述：九宫格图片上传view
 *
 * @author zhangqin
 * @date 2018/5/31
 */
class PictureUploadView<T : PictureUploadModel> constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null
) : FrameLayout(
    mContext, attrs
) {
    /**
     * 最大图片数
     */
    private var mMaxSize = 9

    /**
     * 每行最大图片数
     */
    private var mMaxCol = 3

    /**
     * 图片数据
     */
    private var mDataList: MutableList<T?> = ArrayList()

    /**
     * 布局
     */
    private var itemLayout = R.layout.quick_item_upload_img
    private val mRecyclerView: RecyclerView
    private lateinit var mUploadAdapter: PictureUploadAdapter<T>
    private var mCallback: PictureUploadCallback<T>? = null
    private lateinit var mGridLayoutManager: GridLayoutManager

    init {
        LayoutInflater.from(mContext).inflate(R.layout.quick_picture_upload_view, this)
        mRecyclerView = findViewById(R.id.rv)
        mRecyclerView.isNestedScrollingEnabled = false
        mDataList.add(null)
        initList()
    }

    private fun initList() {
        mGridLayoutManager = GridLayoutManager(mContext, mMaxCol)
        mRecyclerView.layoutManager = mGridLayoutManager
        // 设置padding
        val decoration: RecyclerView.ItemDecoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = ConvertUtils.dp2px(3f)
                outRect.top = ConvertUtils.dp2px(3f)
                outRect.left = ConvertUtils.dp2px(3f)
                outRect.right = ConvertUtils.dp2px(3f)
            }
        }
        mRecyclerView.addItemDecoration(decoration)
        mUploadAdapter = PictureUploadAdapter(itemLayout, mDataList)
        mRecyclerView.adapter = mUploadAdapter
        mUploadAdapter.setOnItemChildClickListener { _, view: View, position: Int ->
            val tempData: MutableList<T> = mutableListOf()
            tempData.addAll(mUploadAdapter.data.filterNotNull())
            val i = view.id
            if (i == R.id.ii_img) {
                val size = mDataList.size
                if (mDataList[size - 1] == null && position == size - 1) {
                    // 如果最后一个为null则添加
                    mCallback?.onAddPic(mMaxSize - (mDataList.size - 1), tempData)
                } else {
                    val item = mUploadAdapter.getItem(position)!!
                    mCallback?.click(position, item, tempData)
                }
            } else if (i == R.id.ii_del) {
                val isRemove = mCallback?.remove(position, tempData)
                // 返回false不进行删除操作
                if (isRemove != false) {
                    if (mDataList.size == mMaxSize && mDataList[mDataList.size - 1] != null) {
                        mUploadAdapter.removeAt(position)
                        mDataList.add(null)
                        mUploadAdapter.setList(mDataList)
                    } else {
                        mUploadAdapter.removeAt(position)
                    }
                }
            }
        }
    }

    /**
     * 设置每行对多展示多少张图片
     */
    fun setMaxColumn(maxCol: Int) {
        mMaxCol = maxCol
        mGridLayoutManager.spanCount = maxCol
    }

    /**
     * 设置最大图片数
     */
    fun setMaxSize(maxSize: Int) {
        mMaxSize = maxSize
    }

    /**
     * 设置布局
     */
    fun setItemLayout(@LayoutRes itemLayout: Int) {
        this.itemLayout = itemLayout
    }

    /**
     * 获取所有图片的地址
     */
    val data: List<T>
        get() {
            return mDataList.filterNotNull()
        }

    /**
     * 设置图片
     */
    fun setNewData(data: MutableList<T>?) {
        mDataList.clear()
        mDataList.addAll(data ?: mutableListOf())
        if (mDataList.size < mMaxSize) {
            // 如果数量小于最大值，添加一个null作为占位符
            mDataList.add(null)
        }
        mUploadAdapter.setList(mDataList)
    }

    /**
     * 添加图片
     */
    fun addData(data: T) {
        val list: MutableList<T> = mutableListOf()
        list.add(data)
        addData(list)
    }

    /**
     * 添加图片
     */
    fun addData(data: MutableList<T>) {
        val size = mDataList.size
        if (size <= mMaxSize && mDataList[size - 1] == null) {
            mDataList.removeAt(mDataList.size - 1)
        }
        mDataList.addAll(data)
        if (mDataList.size < mMaxSize) {
            // 如果数量小于最大值，添加一个null作为占位符
            mDataList.add(null)
        }
        mUploadAdapter.setList(mDataList)
    }

    /**
     * 移除全部图片
     */
    fun removeAll() {
        mUploadAdapter.setList(null)
        mDataList.clear()
    }

    /**
     * 设置监听器
     */
    fun setPicUploadCallback(pictureUploadCallback: PictureUploadCallback<T>) {
        mCallback = pictureUploadCallback
    }
}
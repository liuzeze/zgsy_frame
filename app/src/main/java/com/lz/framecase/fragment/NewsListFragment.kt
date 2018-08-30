package com.lz.framecase.fragment

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.support.v7.widget.PopupMenu
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.lz.framecase.R
import com.lz.framecase.base.BaseFragment
import com.lz.framecase.bean.NewsDataBean
import com.lz.framecase.fragment.adapter.NewsListAdapter
import com.lz.framecase.fragment.presenter.NewsListContract
import com.lz.framecase.fragment.presenter.NewsListPresenter
import com.lz.inject_annotation.InjectComponet
import com.lz.utilslib.interceptor.utils.ShareAction
import com.vondear.rxtool.RxTimeTool
import kotlinx.android.synthetic.main.fragment_news_list.*

/**
 * -----------作者----------日期----------变更内容-----
 * -          刘泽      2018-08-29       创建class
 */
@InjectComponet
class NewsListFragment : BaseFragment<NewsListPresenter>(), NewsListContract.View {

    private var gson: Gson? = null
    private var category: String = ""
    private var mNewsBean = ArrayList<NewsDataBean>()
    private var newsListAdapter: NewsListAdapter? = null

    companion object {
        fun getInstance(s: String): NewsListFragment {
            val newsListFragment = NewsListFragment()
            val bundle = Bundle();
            bundle.putString("category", s)
            newsListFragment.arguments = bundle
            return newsListFragment
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_news_list
    }

    override fun init() {
        initValue()
        initListener()
        mPresenter.getNewLists(category)
    }

    private fun initListener() {
        SwipeRefreshLayout.setOnRefreshListener {
            mNewsBean.clear()
            mPresenter.dataList.clear()
            mPresenter.getNewLists(category)
        }
        newsListAdapter?.setOnItemChildClickListener { baseQuickAdapter: BaseQuickAdapter<Any, BaseViewHolder>, view: View, position: Int ->
            when (view.id) {
                R.id.tv_dots -> {
                    val popupMenu = PopupMenu(mContext,
                            view, Gravity.END)
                    popupMenu.inflate(R.menu.menu_share)
                    popupMenu.setOnMenuItemClickListener { menu ->
                        val itemId = menu.itemId
                        if (itemId == R.id.action_share) {
                            ShareAction.send(mContext, mNewsBean.get(position).title + "\n" + mNewsBean.get(position).share_url)
                        }
                        false
                    }
                    popupMenu.show()
                }
                else -> {
                }
            }
        }
        newsListAdapter?.setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener {
            mPresenter.getNewLists(category)
        }, RecyclerView)


    }

    private var time: String = ""
    private fun initValue() {
        gson = Gson()
        category = arguments?.getString("category")!!
        newsListAdapter = NewsListAdapter(mNewsBean)
        RecyclerView.adapter = newsListAdapter
        time = (RxTimeTool.getCurTimeMills() / 1000).toString()


    }

    override fun getNewsListSuccess(bean: java.util.ArrayList<NewsDataBean>, has_more_to_refresh: Boolean) {
        /*if (!has_more_to_refresh) {
            //数据全部加载完毕
            newsListAdapter?.loadMoreEnd()
        } else {
            SwipeRefreshLayout.setRefreshing(false)
            mNewsBean.addAll(bean)
            newsListAdapter?.notifyItemRangeChanged(newsListAdapter!!.itemCount, bean.size)
            //成功获取更多数据
            newsListAdapter?.loadMoreComplete();

        }*/
        SwipeRefreshLayout.setRefreshing(false)
        mNewsBean.addAll(bean)
        newsListAdapter?.notifyItemRangeChanged(newsListAdapter!!.itemCount, bean.size)
        //成功获取更多数据
        newsListAdapter?.loadMoreComplete();


    }

    override fun showErrorMsg(msg: String?) {
        super.showErrorMsg(msg)
        newsListAdapter?.loadMoreFail()
    }
}

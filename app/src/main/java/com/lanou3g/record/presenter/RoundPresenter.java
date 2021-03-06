package com.lanou3g.record.presenter;

import com.lanou3g.record.model.RoundModelImpl;
import com.lanou3g.record.model.RoundModelInterface;
import com.lanou3g.record.model.entity.BallEntity;
import com.lanou3g.record.ui.RoundViewInterface;

/**
 * 本类由: Risky 创建于: 15/10/29.
 * <p>继承Presenter的基类,泛型为View的接口类型
 * <p>实现接口为:网络数据刷新和加载的回调接口;读取数据库的回调接口
 */
public class RoundPresenter extends BasePresenter<RoundViewInterface>
    implements NetworkConnect<BallEntity>,DBReadable<BallEntity>{

    /** View层的实现类,也就是Activity */
//    private RoundViewInterface mRoundView;
    /** Model层的实现类 */
    private RoundModelInterface mModel;

    /** 构造方法内初始化Model层,通过形参传递初始化View层 */
    public RoundPresenter() {
//        this.mRoundView = mRoundView;
        this.mModel = new RoundModelImpl();
    }

    /** 开始获取网络数据 */
    public void startNetConnect(String url){
        // View层的showLoading方法
        getView().showLoading();
        // Model层的获取网络数据方法,将NetworkRefreshConnect的实例,也就是本类传到Model
        mModel.startRefreshConnect(url, this);
    }

    /**
     * 下拉刷新时会执行这个方法,调用的是本类的startNetConnect方法
     * @param refreshUrl 刷新的接口地址
     */
    public void refresh(String refreshUrl){
        startNetConnect(refreshUrl);
    }

    /**
     * 上拉加载时会执行这个方法
     * @param loadUrl
     */
    public void load(String loadUrl){
        getView().showLoading();
        // Model层的加载数据
        mModel.startLoadConnect(loadUrl, this);
    }

    @Override
    public void onRefreshCompleted(BallEntity result) {
        // 数据刷新成功,会回调执行此方法
        // 执行View层的隐藏Loading和现实数据
        getView().hideLoading();
        getView().showRefreshData(result);
    }

    @Override
    public void onFailed() {
        // 数据刷新或加载失败,会回调执行此方法
        getView().dealError();
        mModel.readDB(this);
        getView().hideLoading();
    }

    @Override
    public void onReadDB(BallEntity result) {
        // 读取数据库回调,执行View层的展示数据方法
        getView().showRefreshData(result);
    }

    @Override
    public void onLoadCompleted(BallEntity result) {
        // 数据加载成功,会回调执行此方法
        getView().hideLoading();
        getView().showLoadData(result);
    }
}

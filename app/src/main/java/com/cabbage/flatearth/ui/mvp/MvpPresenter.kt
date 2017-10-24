package com.cabbage.flatearth.ui.mvp

interface MvpPresenter<V : MvpView> {

    var mvpView: V?
}

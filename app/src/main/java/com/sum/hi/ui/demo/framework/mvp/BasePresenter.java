package com.sum.hi.ui.demo.framework.mvp;

/**
 * @创建者 mingyan.su
 * @创建时间 2022/07/24 10:26
 * @类描述 ${TODO}Presnter:用于处理业务数据逻辑，并通过持有View接口把数据回传给View层。进行View绑定的工作
 */
public class BasePresenter<IView extends BaseView> {
    //根据mvp的模型，Presenter需要持有View的对象，才能将处理好的数据回传回去
    //如果定义为BaseView则无法获取真正的数据类型，所以这里需要使用泛型
    protected IView mView;

    public void attach(IView view) {
        this.mView = view;
    }

    public void detach() {
        this.mView = null;
    }
}

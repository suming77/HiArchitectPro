package com.sum.hi.ui.viewmodel

import android.text.TextUtils
import androidx.lifecycle.*
import com.sum.hi.hilibrary.annotation.HiCallback
import com.sum.hi.hilibrary.annotation.HiResponse
import com.sum.hi.ui.BuildConfig
import com.sum.hi.ui.http.ApiFactory
import com.sum.hi.ui.http.api.GoodsDetailApi
import com.sum.hi.ui.model.*
import java.lang.Exception

/**
 * @author smy
 * @date   2022/7/12 17:39
 * @desc
 */
class GoodsDetailViewModel(val goodsId: String?) : ViewModel() {
    companion object {
        //ViewModelProvider.NewInstanceFactory实际就是拿到class对象modelClass.newInstance()从而构造出一个实例对象
        private class DetailViewModelFactory(val goodsId: String?) :
            ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val constructor = modelClass.getConstructor(String::class.java)
                //如果没有goodsId参数就报异常了
                try {
                    constructor?.let {
                        return it.newInstance(goodsId)
                    }
                } catch (e: Exception) {
                    //ignore
                }
                return super.create(modelClass)
            }
        }

        fun get(goodsId: String?, viewModelStoreOwner: ViewModelStoreOwner): GoodsDetailViewModel {
            return ViewModelProvider(viewModelStoreOwner, DetailViewModelFactory(goodsId)).get(
                GoodsDetailViewModel::class.java
            )
        }
    }

    fun queryGoodsDetail(): LiveData<DetailModel?> {
        val pageData = MutableLiveData<DetailModel>()
        if (!TextUtils.isEmpty(goodsId)) {

//            ApiFactory.create(GoodsDetailApi::class.java).queryGoodsDetail(goodsId!!)
//                .enqueue(object : HiCallback<DetailModel> {
//                    override fun onSuccess(response: HiResponse<DetailModel>) {
//                        if (response.successful && response.data!=null){
//                            pageData.postValue(response.data)
//                        }else{
//                            pageData.postValue(null)
//                        }
//                    }
//
//                    override fun onFailed(throwable: Throwable) {
//                        pageData.postValue(null)
//                        if (BuildConfig.DEBUG){
//                            throwable.printStackTrace()
//                        }
//                    }
//                })

            val url1 =
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg9.51tietu.net%2Fpic%2F2019-091119%2F3yu4knoncz13yu4knoncz1.jpg&refer=http%3A%2F%2Fimg9.51tietu.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1659680201&t=e1b1570fddb12162c791dcc011c51eb8"
            val url =
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F4k%2Fs%2F02%2F2109242306111155-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660275005&t=faeb269997b7402022a7d9f8bebdeee7"
            val sliderImages = mutableListOf<SliderImage>()
            val sliderImage1 = SliderImage(1, url)
            val sliderImage2 = SliderImage(2, url1)
            sliderImages.add(sliderImage1)
            sliderImages.add(sliderImage2)

            val commentModels = mutableListOf<CommentModel>()
            val head1 =
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201506%2F24%2F20150624003609_5cWrV.thumb.700_0.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660275495&t=5ec5b61bee78fd4cd31746fb41737275"
            val head2 =
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F202004%2F01%2F20200401165912_SyYdQ.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660275495&t=91a1808d1f3d2103267b5be159f930a9"
            val head3 =
                "https://pics5.baidu.com/feed/00e93901213fb80e18f91c3e0ebdb42bb83894a4.jpeg?token=a36c485d8022cf38914f9dff1957c73d&s=50342C764552E3CC1E403C640200F07A"
            val head4 =
                "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2F80%2Fv2-c348c322c87c8cd7220297a14f3e39f3_720w.jpg%3Fsource%3D1940ef5c&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1660275495&t=a832484cc0028f1455030c54733c9592"
            val commentModel1 = CommentModel(head1, "衣服颜色很好，穿起来很舒服，喜欢的亲赶紧下单，准备回购", "Sandm 旧颜")
            val commentModel2 = CommentModel(head2, "衣服颜色很好，穿起来很舒服，喜欢的亲赶紧下单，准备回购", "Sandm 旧颜")
            val commentModel3 = CommentModel(head3, "衣服颜色很好，穿起来很舒服，喜欢的亲赶紧下单，准备回购", "Sandm 旧颜")
            val commentModel4 = CommentModel(head4, "衣服颜色很好，穿起来很舒服，喜欢的亲赶紧下单，准备回购", "Sandm 旧颜")
            commentModels.add(commentModel1)
            commentModels.add(commentModel2)
            commentModels.add(commentModel3)
            commentModels.add(commentModel4)

            val shop = Shop("10万+件", "描述相符 高 服务态度 高 物流服务 中", "2432", head4, "架构师二号杂货铺")

            val flowGoods = mutableListOf<GoodsModel>()
            val goods = GoodsModel(
                "1",
                "全场包邮 7天无理由 满200减50",
                "0",
                "0",
                "商品名称",
                "99.99",
                false,
                null,
                null,
                head4,
                sliderImages,
                "标记"
            )
            for (i in 0..10) {
                flowGoods.add(goods)
            }

            val goodAttr = mutableListOf<MutableMap<String, String>>()
            val m = mutableMapOf<String, String>()
            m["风格"] = "简约大气/韩版"
            val m2 = mutableMapOf<String, String>()
            m2["流行元素/工艺"] = "系带，纽扣"
            goodAttr.add(m)
            goodAttr.add(m2)
            goodAttr.add(m)
            goodAttr.add(m2)

            val detailModel = DetailModel(
                "2",
                "商品评价(2398)",
                "质量很好(5) 衣服很好(15) 面料很好(25) 有气质(35) 有弹性(54) 颜色很正(57) 正品(554) 显瘦(543) 有弹性(35) 质量太差(85)",
                commentModels,
                "已拼23848件",
                "2022-7-13 11:34",
                flowGoods,
                sliderImages,
                goodAttr,
                "高档面料，柔软舒适，透气，版型超好，厂家直销，质量保证，闪电发货不掉色，不起球，快递默认顺丰",
                "157492022409",
                "2022新季节夏装短袖",
                "58.9",
                true,
                flowGoods,
                "$309",
                shop,
                "",
                sliderImages,
                "退货包运费 极速退款 全场包邮 极速退货 商品很好 物流很快 物美价廉 包装很精致 颜色很好"
            )
            pageData.postValue(detailModel)
        }
        return pageData
    }

}
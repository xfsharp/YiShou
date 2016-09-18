package com.xf.yishou.compare;

import com.xf.yishou.entity.Goods;

import java.util.Comparator;

/**
 * Created by xsp on 2016/9/18.
 */
public class TimeAscComprator implements Comparator<Goods>{
    @Override
    public int compare(Goods lhs, Goods rhs) {
        float t1 = lhs.getGoodsId();
        float t2 = rhs.getGoodsId();
        if (t1 > t2) {
            return -1;
        } else if (t1 < t2) {
            return 1;
        } else {
            return 0;
        }
    }
}

package com.xf.yishou.compare;

import com.xf.yishou.entity.Goods;

import java.util.Comparator;

/**
 * Created by xsp on 2016/9/18.
 */
public class PriceAscComparator implements Comparator<Goods> {

    @Override
    public int compare(Goods g1, Goods g2) {
        float p1 = g1.getPrice();
        float p2 = g2.getPrice();
        if (p1 < p2){
            return -1;
        }else if (p1 > p2){
            return 1;
        }else {
            return 0;
        }
    }
}

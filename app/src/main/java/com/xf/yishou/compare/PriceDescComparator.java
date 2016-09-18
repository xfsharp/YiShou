package com.xf.yishou.compare;

import com.xf.yishou.entity.Goods;

import java.util.Comparator;

/**
 * Created by xsp on 2016/9/18.
 */
public class PriceDescComparator implements Comparator<Goods>{
    @Override
    public int compare(Goods lhs, Goods rhs) {
        float p1 = lhs.getPrice();
        float p2 = rhs.getPrice();
        if(p1 < p2){
            return 1;
        } else if(p1 > p2){
            return -1;
        } else{
            return 0;
        }
    }
}

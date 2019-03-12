package com.fuwenjun.projectUtils.bloomFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;

public class BloomFilterTest {
    private static final int insertions=1000000;
    @Test
    public void test(){
        //初始化存储string数据的布隆过滤器,大小设置为100w,不能设置为0
        BloomFilter<CharSequence> bf=BloomFilter.create(Funnels.stringFunnel(), insertions,0.0001);
        
        //初始化一个存储string数据的set，初始化大小100w
        Set<String> sets = new HashSet<>(insertions);
        //初始化一个存储string数据的set，初始化大小100w
        List<String> lists = new ArrayList<String>(insertions);
        //向三个容器初始化100万个随机并且唯一的字符串---初始化操作
        for (int i = 0; i < insertions; i++) {
            String uuid = UUID.randomUUID().toString();
            bf.put(uuid);
            sets.add(uuid);
            lists.add(uuid);
        }
        int wrong = 0;//布隆过滤器错误判断的次数
        int right = 0;//布隆过滤器正确判断的次数
        for (int i = 0; i < 10000; i++) {
            String test = i%100==0?lists.get(i/100):UUID.randomUUID().toString();//按照一定比例选择bf中肯定存在的字符串
            if(bf.mightContain(test)){
                if(sets.contains(test)){
                    right ++;
                }else{
                    wrong ++;
                }
            }
        }
        System.out.println("=================right====================="+right);//100
        System.out.println("=================wrong====================="+wrong);
    }
    
    
    
}

package org.bumishi.toolbox.datasync;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author qiang.xie
 * @date 2016/9/19
 * 给定两个两个map集合s1,s2,分析它们的差异,得到s1中存在s2中不存在的元素、s1中存在s2中存在的元素、s1中不存在s2中存在的元素
 * 业务应用：以s1为标准，将s1的数据同步到s2中
 */
public class MapCollectionDiff {

    private Collection<Map<String, Object>> s1;

    private Collection<Map<String, Object>> s2;

    private List<String> compareFields;//用于比较两个集合中的元素相同的字段

    private Collection news = new ArrayList();//需要新增到s2的

    private Collection updates = new ArrayList();//需要从s2更新的

    private Collection removes = new ArrayList();//需要从s2删除的

    public MapCollectionDiff(Collection s1, Collection s2, List<String> pkFields) {
        this.s1 = s1;
        this.s2 = s2;
        this.compareFields = pkFields;
    }

    public void analysisDiff() {

        if ((s1 == null || s1.isEmpty()) && (s2 == null || s2.isEmpty())) {
            return;
        }
        if (s1 == null || s1.isEmpty()) {
            removes = s2;
            return;
        }

        if (s2 == null || s2.isEmpty()) {
            news = s1;
            return;
        }

        for (Map<String, Object> s1item : s1) {
            if (contains(s2, s1item)) {
                updates.add(s1item);
            } else {
                news.add(s1item);
            }
        }

        for (Map<String, Object> s2item : s2) {
            if (!contains(s1, s2item)) {
                removes.add(s2item);
            }
        }

    }

    public Collection getNews() {
        return news;
    }

    public Collection getUpdates() {
        return updates;
    }

    public Collection getRemoves() {
        return removes;
    }

    public boolean contains(Collection<Map<String, Object>> collection, Map<String, Object> object) {

        for (Map<String, Object> item : collection) {
            boolean equals = true;
            for (String pk : compareFields) {
                if (!item.get(pk).equals(object.get(pk))) {
                    equals = false;
                    break;
                }
            }
            if (equals) {
                return true;
            }
        }
        return false;
    }
}

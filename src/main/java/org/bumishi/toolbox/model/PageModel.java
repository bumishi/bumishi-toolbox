package org.bumishi.toolbox.model;

import java.util.List;

/**
 * Created by xieqiang on 2016/11/26.
 */
public class PageModel<T> {

    private int page=1;

    private int size=20;

    private List<T> list;

    private boolean hasNext;//是否还有下一页

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}

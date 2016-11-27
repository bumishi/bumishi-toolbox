package org.bumishi.toolbox.model;

/**
 * 通用树节点写操作
 * Created by xieqiang on 2016/11/27.
 */
public interface TreeNodeCommandRepositry<T extends TreeNode> {

    void add(T node);

    void update(T node);

    void remove(String id);

    void disable(String id);

    void enable(String id);
}

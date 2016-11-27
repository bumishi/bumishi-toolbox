package org.bumishi.toolbox.model;

import java.util.List;

/**
 * 通用树节点查询仓储
 * Created by xieqiang on 2016/11/26.
 */
public interface TreeNodeQueryRepositry<T extends TreeNode> {

    /***
     * 查出所有节点
     * @return
     */
    List<T> list();


    T get(String id);

    /***
     * 查出根节点(Level=1)列表，每个根节点包含下面所有层级的子节点
     * @return
     */
    List<T> rootList();
}

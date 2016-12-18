package org.bumishi.toolbox.model.repositry;

import org.bumishi.toolbox.model.NavigationNode;

/**
 * 可导航的节点仓储，菜单，分类都是可导航的对象
 * Created by xieqiang on 2016/11/26.
 */
public interface NavigationNodeRepositry extends TreeNodeQueryRepositry<NavigationNode>,TreeNodeCommandRepositry<NavigationNode> {

}

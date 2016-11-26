package org.bumishi.toolbox.model;

/**
 * Created by xieqiang on 2016/9/17.
 * 导航节点，可用于菜单数，分类数场景
 */
public class NavigationNode extends TreeNode {

    /**链接*/
    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

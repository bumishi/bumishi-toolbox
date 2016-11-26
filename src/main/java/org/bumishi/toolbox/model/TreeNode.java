package org.bumishi.toolbox.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 树模型
 * Created by xieqiang on 2016/10/29.
 * id   label   path        level order
 * 1    水果      0           1   1
 * 2    苹果      0,1         2   1
 * 3    梨子      0,1         2   2
 * 4    雪梨      0,1,3       3   1
 * 5    鸭梨      0,1,3       3   2
 */
public class TreeNode {

    private String id;

    private String label;

    private String path="0";  //父节点的路径与父节点的id路径，用","分开，0表示父节点是根节点

    private int order=1;  //排序

    private int type;//扩展字段。节点类型，供不同业务区分

    private String style;//样式，方便ui展现

    /** 状态 是否禁用*/
    private boolean disabled;

    private List<? extends TreeNode> childNodes=new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getLevel() {
        if(path==null){
            return 1;
        }
        return path.split(",").length;
    }


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TreeNode newChildNode(String nodeId, String label, int order){
        TreeNode node=new TreeNode();
        node.path =this.path +","+this.id;
        node.id=nodeId;
        node.order=order;
        node.label=label;
        return node;
    }

    public List<? extends TreeNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<? extends TreeNode> childNodes) {
        this.childNodes = childNodes;
    }

    @Override
    public String toString() {
        return label+"-"+ path +"-"+id+"-"+order;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeNode treeNode = (TreeNode) o;

        return id != null ? id.equals(treeNode.id) : treeNode.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }



}

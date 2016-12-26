package org.bumishi.toolbox.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据treeNode构建树结构
 * Created by xieqiang on 2016/11/26.
 */
public class TreeModel {

    private List<? extends TreeNode> nodes;

    public TreeModel(List<? extends TreeNode> nodes){
        this.nodes=nodes;
    }

    public List<? extends TreeNode> buildTree() {
        return buildTree(false);
    }
    /***
     * 以level==1的节点作为开始节点构建树结构
     * @return
     */
    public List<? extends TreeNode> buildTree(boolean includeDisabled) {
        if (isEmpty(nodes)){
            return null;
        }
        List<? extends TreeNode> firstLevels = nodes.stream().filter(node -> node.getLevel() == 1).collect(Collectors.toList());
        if (!includeDisabled) {
            firstLevels = firstLevels.stream().filter(node -> !node.isDisabled()).collect(Collectors.toList());
        }
        sortByOrder(firstLevels);
        firstLevels.stream().forEach(node-> setChildren(node,nodes));
        return firstLevels;
    }


    private  void setChildren(TreeNode currentNode, List<? extends TreeNode> nodeList){
        List<? extends TreeNode> childrens=nodeList.stream().filter(node->(!node.isDisabled() && node.getPath().equals(currentNode.getPath()+","+currentNode.getId()))).collect(Collectors.toList());
        currentNode.setChildNodes(childrens);
        if (isEmpty(childrens)){
            return;
        }
        sortByOrder(childrens);
        childrens.stream().forEach(node-> setChildren(node,nodeList));

    }

    public static void sortByOrder(List<? extends TreeNode> firstLevels) {
        firstLevels.sort((node1,node2)->Integer.valueOf(node1.getOrder()).compareTo(Integer.valueOf(node2.getOrder())));
    }


    /***
     * 按数结构给节点排序
     * @param nodes
     */
    public static void sortByTree(List<? extends TreeNode> nodes) {
        if(isEmpty(nodes)){
            return;
        }
        sortByOrder(nodes);
        nodes.sort((o1, o2) -> (o1.getPath()+","+o1.getId()).compareTo(o2.getPath()+","+o2.getId()));
    }

    private static boolean isEmpty(List nodes) {
        return nodes == null || nodes.isEmpty();
    }


    //按节点的父子层次顺序展示
    private void printTreeToConsole(){
        if (isEmpty(nodes)){
            return;
        }

        sortByTree(nodes);

        nodes.stream().forEach(node->{
            if(node.isDisabled()){
                return;
            }
            for(int i=1;i<node.getLevel();i++){
                System.out.print("\t");
            }
            System.out.println(node);
        });
    }


    //以第一层为起点，递归方式展示父子层次树
    private  void printFirstLevelTreeToConsole(List<? extends TreeNode> nodes){
        if (isEmpty(nodes)){
            return;
        }
        nodes.forEach(item->{
            if(item.isDisabled()){
                return;
            }
            for(int i=1;i<item.getLevel();i++){
                System.out.print("\t");
            }
            System.out.println(item);

            printFirstLevelTreeToConsole(item.getChildNodes());
        });
    }

    public static void main(String[] arg){
        List<TreeNode> nodes=new ArrayList<>();
        TreeNode fruit=new TreeNode();
        fruit.setId("1");
        fruit.setLabel("水果");
        fruit.setOrder(2);
        nodes.add(fruit);

        TreeNode apple=fruit.newChildNode("7","苹果",2);
        nodes.add(apple);
        nodes.add(apple.newChildNode("4","红富士",2));
        nodes.add(apple.newChildNode("15","山东苹果",1));

        TreeNode lizi=fruit.newChildNode("e8","梨子",1);
        nodes.add(lizi);
        nodes.add(lizi.newChildNode("7r7","雪梨",1));
        nodes.add(lizi.newChildNode("t31o","鸭梨",2));

        TreeNode shucai=new TreeNode();
        shucai.setId("a101");
        shucai.setLabel("蔬菜");
        shucai.setOrder(1);
        shucai.setDisabled(true);
        nodes.add(shucai);
        nodes.add(shucai.newChildNode("213","白菜",2));
        TreeModel treeModel=new TreeModel(nodes);
        treeModel.printTreeToConsole();

        System.out.println("====================");
        List<? extends TreeNode> tree= treeModel.buildTree();
        treeModel.printFirstLevelTreeToConsole(tree);






    }
}

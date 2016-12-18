package org.bumishi.toolbox.model.repositry.jdbc;

import org.bumishi.toolbox.model.NavigationNode;
import org.bumishi.toolbox.model.TreeModel;
import org.bumishi.toolbox.model.repositry.NavigationNodeRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * 抽象可导航节点jdbc仓储
 * Created by xieqiang on 2016/11/27.
 */
public abstract class AbstractNavigationNodeJdbcRepositry implements NavigationNodeRepositry {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    
    @Override
    public List<NavigationNode> list() {
        return jdbcTemplate.query("select * from "+getTable()+"", BeanPropertyRowMapper.newInstance(NavigationNode.class));

    }

    @Override
    public void add(NavigationNode catalog) {
        jdbcTemplate.update("INSERT "+getTable()+" (id,label,path,`level`,`order`,`url`,`type`,`style`,`disabled`) VALUES (?,?,?,?,?,?,?,?,?)", catalog.getId(), catalog.getLabel(), catalog.getPath(), catalog.getLevel(), catalog.getOrder(), catalog.getUrl(), catalog.getType(), catalog.getStyle(), catalog.isDisabled() ? 1 : 0);
    }

    @Override
    public void update(NavigationNode catalog) {
        jdbcTemplate.update("update "+getTable()+" SET label=?,`order`=?,url=?,disabled=?,`type`=?,`style`=? WHERE id=?", catalog.getLabel(), catalog.getOrder(), catalog.getUrl(), catalog.isDisabled() ? 1 : 0, catalog.getType(), catalog.getStyle(), catalog.getId());

    }

    @Override
    public void remove(String id) {
        jdbcTemplate.update("DELETE FROM "+getTable()+" WHERE id=?",id);
    }

    @Override
    public void disable(String id) {
        jdbcTemplate.update("update "+getTable()+" SET disabled=1 WHERE id=?",id);

    }

    @Override
    public void enable(String id) {
        jdbcTemplate.update("update "+getTable()+" SET disabled=0 WHERE id=?",id);
    }

    @Override
    public NavigationNode get(String id) {
        try{
            return jdbcTemplate.queryForObject("select * from "+getTable()+" where id=?",BeanPropertyRowMapper.newInstance(NavigationNode.class),id);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<NavigationNode> rootList() {
        return (List<NavigationNode>) new TreeModel(jdbcTemplate.query("select * from "+getTable()+" where level=1",BeanPropertyRowMapper.newInstance(NavigationNode.class))).buildTree();
    }
    
    protected abstract String getTable();


}

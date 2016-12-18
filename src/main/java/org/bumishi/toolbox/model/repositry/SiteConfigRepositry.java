package org.bumishi.toolbox.model.repositry;


import org.bumishi.toolbox.model.SiteConfig;

/**
 * Created by xieqiang on 2016/12/18.
 */
public interface SiteConfigRepositry {

    void update(SiteConfig siteConfig);

    SiteConfig get(String id);
}

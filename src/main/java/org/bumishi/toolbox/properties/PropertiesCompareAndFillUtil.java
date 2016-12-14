package org.bumishi.toolbox.properties;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * 将目标文件的同名属性的值放到源文件中的同名属性，并按属性名排序输出。源文件中不存在目标文件中的属性不会处理
 *
 * @author qiang.xie
 * @date 2016/10/31
 */
public class PropertiesCompareAndFillUtil {
    public static void main(String[] arg) throws Exception {

    }

    private static void fillValue(File destFile, File sourceFile) throws IOException {
        Properties dest = new Properties();

        FileReader dr = new FileReader(destFile);
        dest.load(dr);

        Properties source = new Properties();
        FileReader sr = new FileReader(sourceFile);
        source.load(sr);

        dr.close();
        sr.close();

        dest.keySet().stream().filter(key -> source.containsKey(key)).forEach(key -> {
            source.put(key, dest.get(key));
        });

        List<Object> keys = Lists.newArrayList(source.keySet());
        keys.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));

        FileWriter w = new FileWriter(sourceFile);
        keys.stream().forEach(key -> {
            try {
                w.write(key + "=" + source.get(key).toString());
                w.write("\r\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        w.close();
    }
}

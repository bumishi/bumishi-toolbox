package org.bumishi.toolbox.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;

import java.io.File;


public class QiNiuApi {

    private Auth auth;

    public QiNiuApi(String accessKey, String secretKey) {
        auth = Auth.create(accessKey, secretKey);
    }

    public Response upload(File file, String bucketName) {

        //创建上传对象
        UploadManager uploadManager = new UploadManager(new Configuration(Zone.autoZone()));

        //上传到七牛后保存的文件名
        String key = file.getName();
        try {
            //调用put方法上传
            Response res = uploadManager.put(file, key, auth.uploadToken(bucketName));
            //打印返回的信息
            System.out.println(res.bodyString());
            return res;
        } catch (QiniuException e) {
            e.printStackTrace();
            Response r = e.response;
            if (r == null) {
                return null;
            }
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
            return r;
        }

    }


    public FileInfo[] list(String bucket) {
        try {
            //调用listFiles方法列举指定空间的指定文件
            //参数一：bucket    空间名
            //参数二：prefix    文件名前缀
            //参数三：marker    上一次获取文件列表时返回的 marker
            //参数四：limit     每次迭代的长度限制，最大1000，推荐值 100
            //参数五：delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
            BucketManager bucketManager = new BucketManager(auth, new Configuration(Zone.zone0()));
            FileListing fileListing = bucketManager.listFiles(bucket, null, null, 100, null);
            FileInfo[] items = fileListing.items;
            for (FileInfo fileInfo : items) {
                System.out.println(fileInfo.key);
            }
            return items;
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
            return null;
        }

    }

    public static void main(String[] a) {
        QiNiuApi api = new QiNiuApi("access-key", "securt-key");
        api.upload(new File("F:\\newserver.txt"), "bumishi-blog");
        System.out.println(api.list("bumishi-blog"));
    }

}

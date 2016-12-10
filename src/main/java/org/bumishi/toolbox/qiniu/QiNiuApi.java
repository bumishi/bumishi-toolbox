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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Random;


public class QiNiuApi {

    private Auth auth;

    public QiNiuApi(String accessKey, String secretKey) {
        auth = Auth.create(accessKey, secretKey);
    }

    public Response upload(byte[] file, String fileName, String bucketName) {

        //创建上传对象
        UploadManager uploadManager = new UploadManager(new Configuration(Zone.autoZone()));

        try {
            //调用put方法上传
            Response res = uploadManager.put(file, fileName, auth.uploadToken(bucketName));
            return res;
        } catch (QiniuException e) {
            e.printStackTrace();
            Response r = e.response;
            if (r == null) {
                return null;
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

            return items;
        } catch (QiniuException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] a) throws IOException {
        QiNiuApi api = new QiNiuApi("access-key", "securt-key");
        //上传到七牛后保存的文件名
        String key = System.currentTimeMillis() + "" + new Random().nextInt(10000);
        byte[] bytes = Files.readAllBytes(FileSystems.getDefault().getPath("f:", "newserver.txt"));
        api.upload(bytes, key, "bumishi-blog");
        System.out.println(api.list("bumishi-blog"));
    }

}

package org.bumishi.toolbox.tulin;

/**
 * Created by xieqiang on 2017/1/15.
 */
public class TulinClient {

    private String apiKey;

    private String secret;

    public TulinClient(String apiKey, String secret){
        this.apiKey=apiKey;
        this.secret=secret;
    }

    public String post(String cmd,String userid){
        //待加密的json数据
        String data = "{\"key\":\""+apiKey+"\",\"info\":\""+cmd+"\"}";
        String timestamp = String.valueOf(System.currentTimeMillis());

        //生成密钥
        String keyParam = secret+timestamp+apiKey;
        String key = Md5.MD5(keyParam);

        //加密
        Aes mc = new Aes(key);
        data = mc.encrypt(data);
        //封装请求参数
        StringBuilder body=new StringBuilder();
        body.append("{\"key\":\""+apiKey+"\"");
        body.append(",\"timestamp\":\""+timestamp+"\"");
        body.append(",\"info\":\""+data+"\"");
        if(userid!=null && !userid.trim().equals("")){
            body.append(",\"userid\":\""+userid+"\"");
        }
        body.append("}");
        //请求图灵api
        return PostServer.SendPost(body.toString(), "http://www.tuling123.com/openapi/api");
    }

    public static void main(String[] arg){
        //System.out.println(new TulinClient("key","123").post("你好"));

    }
}

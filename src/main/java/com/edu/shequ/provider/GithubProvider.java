package com.edu.shequ.provider;

import com.alibaba.fastjson.JSON;
import com.edu.shequ.dto.AccessTokenDTO;
import com.edu.shequ.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;


//@Component紧紧的把当前类初始化到spring服务器的上下文

/**
 * //@Controller将当前的类作为路由api的承载者
 * //避免使用以下  spring ioc功能强大之处
 * public class GithubProvider {
 * public static void main(String[] args){
 * GithubProvider githubProvider =new GithubProvider();
 * }
 * }
 */
@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            return string.split("&")[0].split("=")[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            return JSON.parseObject(string, GithubUser.class);
        } catch (IOException e) {
        }
        return null;
    }
}

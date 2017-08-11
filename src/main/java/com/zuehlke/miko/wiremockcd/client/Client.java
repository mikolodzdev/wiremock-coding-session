package com.zuehlke.miko.wiremockcd.client;

import com.zuehlke.miko.wiremockcd.infra.impl.RestClient;

public class Client {
    public static void main(String args[]) {
        RestClient rc = new RestClient(null);

        System.out.println(rc.getPosts());
        System.out.println(rc.getPost(1));
        System.out.println(rc.createPost(10, "My Post Title", "This is an awesome Body."));
        System.out.println(rc.getPostsOfUser(10));
        System.out.println(rc.updatePost(99, "My NEW Post Title", "This is an even more awesome Body."));
    }
}

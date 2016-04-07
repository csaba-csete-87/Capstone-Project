package com.nordlogic.imgursmostviral;

import com.nordlogic.imgursmostviral.data.PostsRepositories;
import com.nordlogic.imgursmostviral.data.repositories.PostsRepository;
import com.nordlogic.imgursmostviral.data.api.PostsServiceApiImpl;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class Injection {
    public static PostsRepository providePostsRepository() {
        return PostsRepositories.getMainRepoInstance(new PostsServiceApiImpl());
    }
}

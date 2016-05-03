package com.csabacsete.imgursmostviral;

import android.support.v4.app.Fragment;

import com.csabacsete.imgursmostviral.data.PostsRepositories;
import com.csabacsete.imgursmostviral.data.api.PostsServiceApiImpl;
import com.csabacsete.imgursmostviral.data.repositories.PostsRepository;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class Injection {
    public static PostsRepository provideServerRepository() {
        return PostsRepositories.getServerRepoInstance(new PostsServiceApiImpl());
    }

    public static PostsRepository provideLocalRepository(Fragment fragment) {
        return PostsRepositories.getLocalRepoInstance(fragment);
    }
}

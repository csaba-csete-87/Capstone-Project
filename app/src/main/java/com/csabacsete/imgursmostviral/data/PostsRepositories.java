package com.csabacsete.imgursmostviral.data;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.csabacsete.imgursmostviral.data.api.PostsServiceApi;
import com.csabacsete.imgursmostviral.data.repositories.PostsRepository;
import com.csabacsete.imgursmostviral.data.repositories.PostsRepositoryApiImpl;
import com.csabacsete.imgursmostviral.data.repositories.PostsRepositoryLocalImpl;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class PostsRepositories {

    private static PostsRepository serverRepository = null;
    private static PostsRepository localRepository = null;

    public synchronized static PostsRepository getServerRepoInstance(@NonNull PostsServiceApi postsServiceApi) {
        checkNotNull(postsServiceApi);
        if (null == serverRepository) {
            serverRepository = new PostsRepositoryApiImpl(postsServiceApi);
        }
        return serverRepository;
    }

    public synchronized static PostsRepository getLocalRepoInstance(@NonNull Fragment fragment) {
        checkNotNull(fragment);
        if (null == localRepository) {
            localRepository = new PostsRepositoryLocalImpl(fragment);
        }
        return localRepository;
    }
}

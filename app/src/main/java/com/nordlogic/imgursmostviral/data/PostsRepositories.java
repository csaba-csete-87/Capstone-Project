package com.nordlogic.imgursmostviral.data;

import android.support.annotation.NonNull;

import com.nordlogic.imgursmostviral.data.api.PostsServiceApi;
import com.nordlogic.imgursmostviral.data.repositories.PostsRepository;
import com.nordlogic.imgursmostviral.data.repositories.PostsRepositoryImpl;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by csaba.csete on 2016-02-24.
 */
public class PostsRepositories {

    private static PostsRepository repository = null;

    public synchronized static PostsRepository getMainRepoInstance(@NonNull PostsServiceApi postsServiceApi) {
        checkNotNull(postsServiceApi);
        if (null == repository) {
            repository = new PostsRepositoryImpl(postsServiceApi);
        }
        return repository;
    }
}

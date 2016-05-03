/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.csabacsete.imgursmostviral.data.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 */
public class ImgurContract {

    public static final String CONTENT_AUTHORITY = "com.csabacsete.imgursmostviral";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POST = "post";

    public static final class PostEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POST).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POST;

        public static final String TABLE_NAME = "post";

        public static final String COLUMN_POST_ID = "postId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGES_COUNT = "imagesCount";
        public static final String COLUMN_POINTS = "points";
        public static final String COLUMN_COVER = "cover";
        public static final String COLUMN_DATETIME = "datetime";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_GIFV = "gifv";
        public static final String COLUMN_COMMENT_COUNT = "commentCount";
        public static final String COLUMN_IS_ALBUM = "isAlbum";
        public static final String COLUMN_ACCOUNT_URL = "accountUrl";

        public static Uri buildPostUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildAllPosts() {
            return CONTENT_URI.normalizeScheme();
        }
    }
}

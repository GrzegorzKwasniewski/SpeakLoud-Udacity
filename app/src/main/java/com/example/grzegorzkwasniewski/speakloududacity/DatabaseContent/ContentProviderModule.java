package com.example.grzegorzkwasniewski.speakloududacity.DatabaseContent;

import android.content.Context;
import android.content.UriMatcher;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class ContentProviderModule {

    @Provides
    public DBHelper DBHelper(Context context) {
        return new DBHelper(context);
    }

    @Provides
    public UriMatcher uriMatcher() {
        return new UriMatcher(UriMatcher.NO_MATCH);
    }

}
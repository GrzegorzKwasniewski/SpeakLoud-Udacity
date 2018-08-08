package com.example.grzegorzkwasniewski.speakloududacity.DatabaseContent;

import android.content.UriMatcher;

import dagger.Component;

@Component(modules = ContentProviderModule.class)
public interface DatabaseComponent {

    DBHelper getDBHelper();

    UriMatcher getUriMatcher();

}
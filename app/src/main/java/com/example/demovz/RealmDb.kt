package com.example.demovz

import android.app.Application

class RealmDb: Application() {

    override fun onCreate() {
        super.onCreate()
//
//        Re.init(this)
//        val config = RealmConfiguration.Builder()
//            .name("test.db")
//            .allowQueriesOnUiThread(false)
//            .schemaVersion(1)
//            .deleteRealmIfMigrationNeeded()
//            .build()
//        Realm.setDefaultConfiguration(config)
    }
}
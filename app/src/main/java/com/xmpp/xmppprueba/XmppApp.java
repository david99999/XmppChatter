package com.xmpp.xmppprueba;

import android.content.Intent;
import com.facebook.stetho.Stetho;
import com.orm.SugarApp;
import com.orm.SugarContext;

/**
 * Created by david on 16/03/16.
 */
public class XmppApp extends SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        startService(new Intent(this, ConnectXmpp.class));
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
            Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
            Stetho.defaultInspectorModulesProvider(this)
        );

        // Enable command line interface
        initializerBuilder.enableDumpapp(
            Stetho.defaultDumperPluginsProvider(this)
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }

    @Override
    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }
}

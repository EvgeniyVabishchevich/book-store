package com.andersen;

import com.andersen.config.DependencyModule;
import com.andersen.config.model.ConfigModel;
import com.google.inject.Guice;
import com.google.inject.Injector;
import jakarta.servlet.http.HttpServlet;

public class EntryPoint {

    public static void main(String[] args) throws Exception {

        Injector injector = Guice.createInjector(new DependencyModule());

        ConfigModel config = injector.getInstance(ConfigModel.class);
        HttpServlet routerServlet = injector.getInstance(HttpServlet.class);

        new App(config, routerServlet).start();
    }
}
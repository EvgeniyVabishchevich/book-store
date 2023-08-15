package com.andersen;

import com.andersen.config.model.ConfigModel;
import jakarta.servlet.http.HttpServlet;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

public class App {

    private ConfigModel config;
    private HttpServlet httpServlet;
    private String appDir;

    public App(ConfigModel config, HttpServlet httpServlet, String appDir) {
        this.config = config;
        this.httpServlet = httpServlet;
        this.appDir = appDir;
    }

    public App(ConfigModel config, HttpServlet httpServlet) {
        this(config, httpServlet, System.getProperty("java.io.tmpdir"));
    }

    public void start() {
        try {

            Tomcat tomcat = new Tomcat();
            tomcat.setBaseDir(appDir);

            Connector httpConnector = new Connector();
            httpConnector.setPort(config.port());
            tomcat.getService().addConnector(httpConnector);

            Context servletContext = tomcat.addContext(config.contextPath(), appDir);

            tomcat.addServlet(config.contextPath(), "RouterServlet", httpServlet);
            servletContext.addServletMappingDecoded("/*", "RouterServlet");

            tomcat.start();
            tomcat.getServer().await();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
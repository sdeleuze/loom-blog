package org.apache.markt;

import java.io.File;

import org.apache.catalina.Executor;
import org.apache.catalina.Service;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.LoomExecutor;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.coyote.http11.Http11NioProtocol;

public class Main {

	public static void main(String[] args) throws Exception {
		Tomcat tomcat = new Tomcat();

		StandardContext ctx = (StandardContext) tomcat.addWebapp("", new File("target/").getAbsolutePath());
		tomcat.addServlet("", "BlockingServlet", new ServletBlocking());
		tomcat.addServlet("", "AsyncServlet", new ServletAsync());
		tomcat.addServlet("", "OverheadServlet", new Overhead());

		File additionWebInfClasses = new File("target/classes");
		WebResourceRoot resources = new StandardRoot(ctx);
		resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
		ctx.setResources(resources);

		Service service = tomcat.getService();

		Http11NioProtocol httpProtocol = new Http11NioProtocol();
		httpProtocol.setMaxKeepAliveRequests(-1);
		Connector connector = new Connector(httpProtocol);
		connector.setPort(8080);
		service.addConnector(connector);

		LoomExecutor loomExecutor = new LoomExecutor();
		loomExecutor.setName("LoomExecutor");
		Http11NioProtocol loomHttpProtocol = new Http11NioProtocol();
		loomHttpProtocol.setExecutor(loomExecutor);
		loomHttpProtocol.setMaxKeepAliveRequests(-1);
		Connector loomConnector = new Connector(loomHttpProtocol);
		loomConnector.setPort(8081);
		service.addConnector(loomConnector);

		tomcat.start();
		tomcat.getServer().await();
	}
}

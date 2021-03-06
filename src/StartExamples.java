/*
 * $Id: StartExamples.java 461192 2006-06-28 08:37:16 +0200 (Wed, 28 Jun 2006)
 * ehillenius $ $Revision: 464433 $ $Date: 2006-06-28 08:37:16 +0200 (Wed, 28
 * Jun 2006) $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * Seperate startup class for people that want to run the examples directly. Use
 * parameter -Dcom.sun.management.jmxremote to startup JMX (and e.g. connect
 * with jconsole).
 */
public class StartExamples {
	/**
	 * Used for logging.
	 */
	private static final Log log = LogFactory.getLog(StartExamples.class);

	/**
	 * Main function, starts the jetty server.
	 * 
	 * @param args
	 * @throws Exception 
	 */

	public static void main(String[] args) throws Exception {
		System.setProperty("org.mortbay.jetty.Request.maxFormContentSize", "20000000");
		Server server = new Server();
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(8080);
		server.setConnectors(new Connector[] { connector });
		WebAppContext web = new WebAppContext("webroot", "/");
		server.addHandler(web);
		try {
			server.start();
			server.join();
			// server.destroy();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}

	/**
	 * Construct.
	 */
	StartExamples() {
		super();
	}
}

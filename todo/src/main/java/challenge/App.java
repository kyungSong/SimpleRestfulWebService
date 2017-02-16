package challenge;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import java.util.Scanner;


/**
 * Class that starts jetty server.
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        ResourceConfig config = new ResourceConfig();
        config.packages("challenge");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Scanner portInput = new Scanner(System.in);
        System.out.print("Enter the port number: ");
        String portStr = portInput.nextLine();
        int portNum;
        try {

            portNum = Integer.parseInt(portStr);
            if (portStr.length() != 4) {
                System.out.println("Wrong input. Please Restart.");
                return;
            }
        } catch(NumberFormatException e) {
            System.out.println("Wrong input. Please Restart.");
            return;
        }

        Server server = new Server(portNum);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
}

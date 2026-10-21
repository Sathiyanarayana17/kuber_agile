
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class App {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(
            new InetSocketAddress("0.0.0.0", 8080), 0
        );

        server.createContext("/", exchange -> {
            String response =
                "Online CI/CD Demo: Jenkins + Docker + Kubernetes";
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set(
                "Content-Type", "text/plain; charset=UTF-8"
            );
            exchange.sendResponseHeaders(200, bytes.length);

            try (var output = exchange.getResponseBody()) {
                output.write(bytes);
            }
        });

        server.start();
        System.out.println("Application running on port 8080");
    }
}

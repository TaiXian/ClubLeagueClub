/**
 * Created by Jaband on 30/09/2016.
 */

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        System.out.println("server Running");
        server.createContext("/all", new SummonerHandlers.AllHandler());
        server.createContext("/get", new SummonerHandlers.GetHandler());
        server.createContext("/getScript", new FileHandler("myApp.js"));
        server.createContext("/put", new SummonerHandlers.PutHandler());
        server.createContext("/save", new SaveHandler());
        server.createContext("/update", new SummonerHandlers.UpdateHandler());
        server.createContext("/club", new ClubHandlers.GetClubHandler());
        server.createContext("/makeClub", new ClubHandlers.MakeClubHandler());
        server.createContext("/remove", new SummonerHandlers.RemoveHandler());
        server.createContext("/ui", new FileHandler("index.html"));
        server.setExecutor(null); // creates a default executor
        server.start();
    }
    private static class SaveHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Universe myUniverse = Universe.getUniverse();
            myUniverse.save();
            helpServer.writeResponse(t, "saved");
        }
    }
}
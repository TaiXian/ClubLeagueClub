import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

class SummonerHandlers {
    static class AllHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Universe myUniverse = Universe.getUniverse();
            String response = myUniverse.toJson();
            helpServer.writeResponse(t, response);
        }
    }
    static class GetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Universe myUniverse = Universe.getUniverse();
            String response = "";
            Map<String, String> params = helpServer.queryToMap(t.getRequestURI().getQuery());
            if(params.containsKey("name")) {
                String summonerName = params.get("name");
                response += myUniverse.getSummoner(summonerName).toJson();
            }
            else{
                response += "name not in here \nor URI not well formatted";
            }
            helpServer.writeResponse(t, response);
        }
    }
    static class RemoveHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Universe myUniverse = Universe.getUniverse();
            String response = "";
            Map<String, String> params = helpServer.queryToMap(t.getRequestURI().getQuery());
            if(params.containsKey("ID") && params.containsKey("password")) {
                int summonerID = Integer.parseInt(params.get("ID"));
                String password = params.get("password");
                if (myUniverse.removeSummoner(summonerID, password)){
                    response += "removed";
                }
                else{
                    response += "incorrect password";
                }
            }
            else{
                response += "name not in here \nor URI not well formatted";
            }
            helpServer.writeResponse(t, response);
        }
    }
    static class PutHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = makeSummonerFromRequest(t);
            helpServer.writeResponse(t, response);
        }

    }
    static class UpdateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Universe myUniverse = Universe.getUniverse();
            Map<String, String> params = helpServer.queryToMap(t.getRequestURI().getQuery());
            if(params.containsKey("name")) {
                String summonerName = params.get("name");
//                myUniverse.getSummoner(summonerName).updateDivision();
                myUniverse.updateSummoner(summonerName);
            }
            String response = myUniverse.toJson();
            helpServer.writeResponse(t, response);
        }
    }
    static String makeSummonerFromRequest(HttpExchange t){
        Universe myUniverse = Universe.getUniverse();
//        String response = "inserted \n";
        String response = "";
        Map<String, String> params = helpServer.queryToMap(t.getRequestURI().getQuery());
        if (params.containsKey("name") && params.containsKey("summoner") && params.containsKey("password")){
            String name = params.get("name");
            String summoner = params.get("summoner");
            String password = params.get("password");
            Summoner newDude = new Summoner(name, summoner, password);
            if (myUniverse.insertSummoner(newDude, password)) {
                response += newDude.toJson();
            }
            else{
                response += "password incorrect\n";
            }
        }
        else{
            response += "Bad Params For Create \n";
        }
        return response;
    }
}


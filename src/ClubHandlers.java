import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;


class ClubHandlers {
    static class GetClubHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Universe myUniverse = Universe.getUniverse();
            Map<String, String> params = helpServer.queryToMap(t.getRequestURI().getQuery());
            String response = "";
            if(params.containsKey("name")) {
                String clubName = params.get("name");
                response = SummonerCollection.toJson(myUniverse.getClub(clubName).getMembers());
            }
            else{
                response += "name not in here \nor URI not well formatted";
            }
            helpServer.writeResponse(t, response);
        }
    }
    static class MakeClubHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Universe myUniverse = Universe.getUniverse();
            Map<String, String> params = helpServer.queryToMap(t.getRequestURI().getQuery());
            String response = "";
            if(params.containsKey("name") && params.containsKey("owner") && params.containsKey("password")) {
                String clubName = params.get("name");
                String ownerName = params.get("owner");
                String password = params.get("password");
                if(myUniverse.getClubs().anySatisfy(club -> club.getName().equals(clubName))){
                    response += "name is taken";
                }
                else{
                    Summoner owner = myUniverse.getSummoner(ownerName);
                    if(null == owner){
                        response += "no such owner";
                    }
                    else{
                        Club newclub = new Club(owner.getSummonerId(), clubName, myUniverse, password);
                        response += SummonerCollection.toJson(newclub.getMembers());
                    }
                }
            }
            else{
                response += "name not in here \nor URI not well formatted";
            }
            helpServer.writeResponse(t, response);
        }
    }
    static class addToClubHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Universe myUniverse = Universe.getUniverse();
            Map<String, String> params = helpServer.queryToMap(t.getRequestURI().getQuery());
            String response = addToClubFromRequest(t, "");
            helpServer.writeResponse(t, response);
        }
    }
    static class addToClubAndMakeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = SummonerHandlers.makeSummonerFromRequest(t);
            response = addToClubFromRequest(t, response);
            helpServer.writeResponse(t, response);
        }
    }
    static class removeFromClubHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Universe myUniverse = Universe.getUniverse();
            Map<String, String> params = helpServer.queryToMap(t.getRequestURI().getQuery());
            String response = "";
            if(params.containsKey("clubName") && params.containsKey("summoner") && params.containsKey("password")) {
                String clubName = params.get("clubName");
                Integer ID = Integer.parseInt(params.get("ID"));
                String password = params.get("password");
                Club clubToRemoveFrom = myUniverse.getClub(clubName);
                if(clubToRemoveFrom!=null){
                    if(clubToRemoveFrom.removeSummoner(ID, password)){
                        response += Universe.toJson(clubToRemoveFrom.getMembers());
                    }else{
                        response += "bad Password for removal \n";
                    }
                }
                else{
                    response+= "club does not exist \n";
                }
            }
            else{
                response += "bad Params For Removal \n";
            }
            helpServer.writeResponse(t, response);
        }
    }
    static String addToClubFromRequest(HttpExchange t, String response){
        Universe myUniverse = Universe.getUniverse();
        Map<String, String> params = helpServer.queryToMap(t.getRequestURI().getQuery());
        if(params.containsKey("clubName") && params.containsKey("summoner") && params.containsKey("password")) {
            String clubName = params.get("clubName");
            Integer ID = Integer.parseInt(params.get("ID"));
            String password = params.get("password");
            Club clubToAddto = myUniverse.getClub(clubName);
            if(clubToAddto!=null){
                clubToAddto.addSummoner(ID, password);
                response += Universe.toJson(clubToAddto.getMembers());
            }
            else {
                response += "no such club \n";
            }
        }
        else{
            response += "name not in here \nor URI not well formatted\n";
        }
        return response;
    }
}

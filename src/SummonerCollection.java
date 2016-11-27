import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.list.mutable.FastList;

abstract class SummonerCollection {
    private MutableList<Summoner> summoners;
    SummonerCollection(){
        summoners = new FastList<>();
    }
    void reorder(){
        reorder(this.summoners);
    }
    Summoner getSummoner(String name) {
        return summoners.detect(each-> each.getName().equals(name));
    }
    Summoner getSummoner(int ID) {
        return summoners.detect(each-> each.getSummonerId() == ID);
    }
    MutableList<Summoner> getSummoners() {
        return summoners;
    }
    protected boolean addSummoner(Summoner addCandidate, String password){
        Summoner oldDude = getSummoners().detect(
                existingSummoner -> existingSummoner.getSummonerId().equals(addCandidate.getSummonerId()));
        if(oldDude!=null){
            if(!removeSummoner(oldDude.getSummonerId(), password)) {
                return false;
            }
        }
        summoners.add(addCandidate);
        return true;
    }
    public boolean insertSummoner(Summoner addCandidate, String password){
        if (addSummoner(addCandidate, password)) {
            SQLDatabaseConnection.insertSummoner(addCandidate);
            return true;
        }
        return false;
    }
    boolean removeSummoner(int SummonerId, String password){
        Summoner deleteCandidate = summoners.detect(each -> each.getSummonerId() == SummonerId);
        if(deleteCandidate!=null && deleteCandidate.checkPassword(password)) {
            this.summoners.remove(deleteCandidate);
            SQLDatabaseConnection.removeSummoner(deleteCandidate);
            return true;
        }
        return false;
    }

    void updateSummoner(String summonerName) {
        Summoner updateCandidate = getSummoner(summonerName);
        updateCandidate.updateDivision();
        SQLDatabaseConnection.updateSummoner(updateCandidate);
    }
    String toJson(){
        return toJson(this.summoners);
    }
    static String toJson(MutableList<Summoner> summoners){
        String response = "[";
        reorder(summoners);
        for(Summoner summoner : summoners){
            response += summoner.toJson() + ",\n";
        }
        response = response.substring(0, response.length()-2);
        response += "]";
        return response;
    }
    private static void reorder(MutableList<Summoner> summoners){
        summoners.sort(Summoner::compareTo);
        summoners.reverseThis();
    }
}

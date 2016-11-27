import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.list.mutable.FastList;

class Universe extends SummonerCollection{
    private static Universe instance = null;
    private MutableList<Club> clubs;

    void save(){
        SQLDatabaseConnection.updateSummoners(getSummoners());
    }
    private void loadFromDB(){
        System.out.println("loading from db");
        for(Summoner returnString : SQLDatabaseConnection.getSummoners()){
            addSummoner(returnString, returnString.getPasswordToken());
        }
        System.out.println("db operations complete");
    }

    private Universe(){
        super();
        this.clubs = new FastList<>();
        loadFromDB();
    }
    static Universe getUniverse(){
        if(instance == null){
            instance = new Universe();
        }
        return instance;
    }

    public MutableList<Club> getClubs(){return clubs;}
    public Club getClub(String name){
        return clubs.detect(each-> each.getName().equals(name));
    }
    public void addClub(Club addCandidate){
        clubs.add(addCandidate);
    }
    public void removeClub(String clubName){
        Club deleteCandidate = clubs.detect(each -> each.getName().equals(clubName));
        this.clubs.remove(deleteCandidate);
    }
}

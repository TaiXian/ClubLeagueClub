import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.list.mutable.FastList;

import static password.PasswordAuthentication.authenticate;
import static password.PasswordAuthentication.hash;

class Club{
    public String name;
    private String entryPasswordToken;
    private int ownerID;
    private Universe universe;
    private MutableList<Integer> members;
    Club(int owner, String name, Universe myUniverse, String entryPassword){
        super();
        this.members = new FastList<>();
        this.ownerID = owner;
        this.name = name;
        this.universe = myUniverse;
        myUniverse.addClub(this);
        this.setPassword(entryPassword);
    }
    public boolean addSummoner(Integer summonerID, String entryPassword){
        if(checkPassword(entryPassword)) {
            members.add(summonerID);
            return true;
        }
        return false;
    }
    public boolean removeSummoner(Integer summonerID, String adminPassword) {
        if(checkAdminPassword(adminPassword)){
            members.remove(summonerID);
            return true;
        }
        return false;
    }
    private void setPassword(String password){
        this.entryPasswordToken = hash(password.toCharArray());
    }
    private boolean checkPassword(String password){
        return authenticate(password.toCharArray(), this.entryPasswordToken);
    }
    private boolean checkAdminPassword(String password){
        return this.universe.getSummoner(ownerID).checkPassword(password);
    }
    MutableList<Summoner> getMembers(){
        MutableList<Summoner> returnList = new FastList<>();
        return this.universe.getSummoners().select(
                summoner -> members.contains(summoner.getSummonerId())
        );
    }
    public String getName() {
        return name;
    }
}

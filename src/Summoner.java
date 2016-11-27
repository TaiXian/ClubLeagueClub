import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static password.PasswordAuthentication.authenticate;
import static password.PasswordAuthentication.hash;

public class Summoner implements Comparable<Summoner>{
	private String name;
	private String summonerName;
	private Integer summonerId;
	private int level;

	private String passwordToken;
	
	private Tier tier;
	private Division division;
	private int lp;
	
	Summoner(String name, String summonerName, String password){
		this.name = name;
		this.summonerName = summonerName;
		this.setPassword(password);
		JSONObject info =  queryRiotForInfo(summonerName);
		this.summonerId = IDfromInfo(info);
		this.level = getLevelFromInfo(info);
		this.updateDivision();
	}
	Summoner (JSONObject allInfo){
		this.name = allInfo.get("Name").toString();
		this.summonerName = allInfo.get("Summoner_Name").toString();
		this.summonerId = Integer.parseInt(allInfo.get("Summoner_ID").toString());
		this.tier = Tier.valueOf(allInfo.get("Tier").toString());
		this.division = Division.valueOf(allInfo.get("Division").toString());
		this.lp = Integer.parseInt(allInfo.get("LP").toString());
		this.setPassword(allInfo.get("Password").toString());
	}
	Summoner (int summonerId, String name, String passwordToken, String summonerName, String division, String tier, int lp){
		this.name = name;
		this.summonerName = summonerName;
		this.summonerId = summonerId;
		this.tier = Tier.valueOf(tier);
		this.division = Division.valueOf(division);
		this.lp = lp;
		this.passwordToken = passwordToken;
	}
	private JSONObject queryRiotForInfo(String summonerName){
		JSONObject s = ImportJSon.makeRestCall(
				"https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/",summonerName);
		JSONObject innerObject = (JSONObject)s.get(summonerName.toLowerCase());
		return innerObject;
	}
	private static int IDfromInfo(JSONObject innerObject){
		String id = innerObject.get("id").toString();
		return Integer.parseInt(id);
	}
	private static int getLevelFromInfo(JSONObject innerObject){
		String level = innerObject.get("summonerLevel").toString();
		return Integer.parseInt(level);
	}
	void updateDivision(){
		if(level==30) {
			JSONObject s = ImportJSon.makeRestCall(
					"https://na.api.pvp.net/api/lol/na/v2.5/league/by-summoner/", this.summonerId.toString() + "/entry");
			if (s==null){
				setUnranked();
				return;
			}
			JSONArray array = (JSONArray) s.get(summonerId.toString());
			String playername;
			for (Object queue : array) {
				JSONObject thisQueue = (JSONObject) queue;
				if (thisQueue.get("queue").equals("RANKED_SOLO_5x5")) {
					this.tier = Tier.valueOf(thisQueue.get("tier").toString());
					JSONArray participants = (JSONArray) thisQueue.get("entries");
					for (Object participant : participants) {
						JSONObject player = (JSONObject) participant;
						playername = player.get("playerOrTeamName").toString();
						if (playername.toLowerCase().replace(" ", "").equals(this.summonerName.toLowerCase().replace(" ", ""))) {
							this.division = Division.valueOf(player.get("division").toString());
							this.lp = Integer.parseInt(player.get("leaguePoints").toString());
						}
					}
				}
			}
		}
		else{
			JSONObject info =  queryRiotForInfo(this.summonerName);
			this.level = getLevelFromInfo(info);
			if(this.level == 30){
				updateDivision();
			}
			else{
				setUnranked();
			}
		}
	}

	private void setUnranked() {
		this.tier = Tier.UNRANKED;
		this.division = Division.values()[(this.level-1)/6];
		this.lp = 0;
	}

	private void setPassword(String password){
		this.passwordToken = hash(password.toCharArray());
	}

	boolean checkPassword(String password){
		return authenticate(password.toCharArray(), this.passwordToken);
	}

	String toJson(){
		return JSONObject.toJSONString(this.toMap());
	}
	private Map<String, String> toMap(){
		Map<String, String> summoner = new HashMap<String, String>();
		summoner.put("Name", this.name);
		summoner.put("Summoner_Name", this.summonerName);
		summoner.put("Summoner_ID", this.summonerId.toString());
		summoner.put("Tier", this.tier.toString());
		summoner.put("LP", Integer.toString(this.lp));
		summoner.put("Division", this.division.toString());
		return summoner;
	}

	String getName() {
		return name;
	}
	Integer getSummonerId() {
		return summonerId;
	}

	public String getSummonerName() {
		return summonerName;
	}

	public String getPasswordToken() {
		return passwordToken;
	}

	public Tier getTier() {
		return tier;
	}

	public Division getDivision() {
		return division;
	}

	public int getLp() {
		return lp;
	}

	@Override
	public int compareTo(@NotNull Summoner second) {
		if (this.tier.value > second.tier.value){return 1;}
		else if(this.tier.value < second.tier.value){return -1;}
		else{
			if(this.division.value > second.division.value){return 1;}
			else if(this.division.value < second.division.value){return -1;}
			else{
				if(this.lp > second.lp){return 1;}
				else if(this.lp < second.lp){return -1;}
				else{
					return 0;
				}
			}
		}
	}
}

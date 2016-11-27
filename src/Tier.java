
public enum Tier {

	CHALLENGER(10),
	MASTER(9),
	DIAMOND(8),
	PLATINUM(7),
	GOLD(6),
	SILVER(5),
	BRONZE(4),
	UNRANKED(3);
	public int value;
	private Tier(int value){
		this.value = value;
	}
}

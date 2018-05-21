package requests;

public class Status {

	private String player;
	private boolean full;
	private boolean turn;
	private boolean wait;
	private boolean lost;
	private boolean requestRestart;
	private boolean restart;
	
	public Status() {
		this.player = "";
		this.full = false;
		this.turn = false;
		this.wait = false;
		this.lost = false;
		this.requestRestart = false;
		this.restart = false;
	}
	
	public Status(String player, boolean full, boolean turn, boolean wait, boolean lost, boolean rRest, boolean rest) {
		this.player = player;
		this.full = full;
		this.turn = turn;
		this.wait = wait;
		this.lost = lost;
		this.requestRestart = rRest;
		this.restart = rest;
	}

	

	@Override
	public String toString() {
		return "Status [player=" + player + ", full=" + full + ", turn=" + turn + ", wait=" + wait + ", lost=" + lost
				+ ", requestRestart=" + requestRestart + ", restart=" + restart + "]";
	}

	public boolean isFull() {
		return full;
	}

	public boolean isLost() {
		return lost;
	}

	public void setLost(boolean lost) {
		this.lost = lost;
	}

	public void setFull(boolean full) {
		this.full = full;
	}

	public boolean isTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	public boolean isWait() {
		return wait;
	}

	public void setWait(boolean wait) {
		this.wait = wait;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public boolean isRequestRestart() {
		return requestRestart;
	}

	public void setRequestRestart(boolean requestRestart) {
		this.requestRestart = requestRestart;
	}

	public boolean isRestart() {
		return restart;
	}

	public void setRestart(boolean restart) {
		this.restart = restart;
	}
	
	
	
}

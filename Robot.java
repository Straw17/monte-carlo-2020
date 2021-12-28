public class Robot implements Comparable<Robot> {
	public boolean doesAuto;
	public boolean shootsAuto;
	public double autoBalls;
	
	public double ballsPerMinute;
	public boolean doesTop;
	public double accuracyBottom;
	public double accuracyOuter;
	public double accuracyInner;
	
	public boolean climbs;
	
	public double scorePerBall;
	public double score;
	
	public Robot() {
		doesAuto = Math.random() < 0.8951; //avg: 0.8951
		shootsAuto = doesAuto ? Math.random() < 0.6896 : false; //avg: 0.6173
		autoBalls = shootsAuto ? Math.min(13, Math.max(0, Simulator.rand.nextGaussian() * 1.932 + 3.557)) : 0; //avg: 2.210
		
		ballsPerMinute = 60 * Math.max(0, (Simulator.rand.nextGaussian() * 6.617 + 7.455)/105); //avg: 4.507
		doesTop = Math.random() < 0.7424; //avg: 0.7424
		accuracyBottom = doesTop ? 0 : Math.random(); //avg: 0.1288
		accuracyOuter = doesTop ? Math.random() : 0; //avg: 0.3712
		accuracyInner = accuracyOuter * Math.random() * 0.5; //avg: 0.0928
		
		climbs = Math.random() < 0.3027; //avg: 0.3027
		
		calcScorePerBall();
		getScore();
	}
	
	public int compareTo(Robot robot) {
		if(score == robot.score) {
			return 0;
		} else if(score > robot.score) {
			return -1;
		} else {
			return 1;
		}
	}
	
	public void calcScorePerBall() {
		scorePerBall = 0;
		if(doesTop) {
			scorePerBall += 3 * (accuracyInner);
			scorePerBall += 2 * (accuracyOuter - accuracyInner);
		} else {
			scorePerBall += accuracyBottom; //+1 point per ball in bottom
		}
	}
	
	public void getScore() {
		score = 0;
		
		if(doesAuto) {
			score += 5; //+5 for moving off the line
			if(shootsAuto) {
				score += autoBalls * 2 * scorePerBall; //score for auto balls
			}
		}
		
		score += 105.0/60.0 * ballsPerMinute * scorePerBall; //score for main teleop balls
		
		if(climbs) {
			score += 25; //+25 for hanging
		} else {
			score += Math.max(30.0/60.0 * ballsPerMinute * scorePerBall, 5); //either cycles or parks
		}
	}
}

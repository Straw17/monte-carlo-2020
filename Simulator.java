import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Simulator {
	public static final Random rand = new Random();
	public static double[] averages = {0.8951, 0.6173, 2.210, 4.507, 0.7424, 0.1288, 0.3712, 0.0928, 0.3027, 27.586};
	
	boolean printRelative;
	Robot[] robots;
	double[][] robotData; //doesAuto, shootsAuto, autoBalls, ballsPerMinute, doesTop, accuracyBottom, accuracyOuter, accuracyInner, climbs, score
	
	public Simulator(int totalRobots, boolean printRelative) {
		this.printRelative = printRelative;
		if(totalRobots % 10 != 0) {
			totalRobots -= totalRobots % 10;
		}
		robots = new Robot[totalRobots];
		robotData = new double[10][totalRobots];
	}
	
	public void createRobots() {
		for(int i = 0; i < robots.length; i++) {
			robots[i] = new Robot();
		}
	}
	
	public void fillData() {
		Arrays.sort(robots);
		for(int i = 0; i < robots.length; i++) {
			robotData[0][i] = robots[i].doesAuto ? 1.0 : 0.0;
			robotData[1][i] = robots[i].shootsAuto ? 1.0 : 0.0;
			robotData[2][i] = robots[i].autoBalls;
			robotData[3][i] = robots[i].ballsPerMinute;
			robotData[4][i] = robots[i].doesTop ? 1.0 : 0.0;
			robotData[5][i] = robots[i].accuracyBottom;
			robotData[6][i] = robots[i].accuracyOuter;
			robotData[7][i] = robots[i].accuracyInner;
			robotData[8][i] = robots[i].climbs ? 1.0 : 0.0;
			robotData[9][i] = robots[i].score;
		}
		//System.out.println(Arrays.stream(robotData[9]).sum() / (double) robotData[9].length); //average score
	}
	
	public void analyzeData() {
		int tenPercent = robots.length / 10;
		double[][] percentileAverages = new double[10][10];
		
		double[][] subsection;
		for(int a = 0; a < 10; a++) {
			subsection = new double[11][tenPercent];
			for(int b = 0; b < tenPercent; b++) {
				for(int c = 0; c < 10; c++) {
					subsection[c][b] = robotData[c][a*tenPercent + b];
				}
			}
			for(int d = 0; d < 10; d++) {
				percentileAverages[a][d] = Arrays.stream(subsection[d]).sum() / (double) subsection[d].length;
				if(printRelative) percentileAverages[a][d] -= averages[d];
				percentileAverages[a][d] = Math.round(percentileAverages[a][d] * 1000.0) / 1000.0;
			}
		}
		String toPrint;
		for(int a = 0; a < 10; a++) {
			System.out.print("Top " + (a == 9 ? (a+1) : " " + (a+1)) + "0%  ");
			for(int b = 0; b < 10; b++) {
				if(percentileAverages[a][b] > 0) {
					toPrint = "+" + Double.toString(percentileAverages[a][b]);
				} else if(percentileAverages[a][b] < 0) {
					toPrint = Double.toString(percentileAverages[a][b]);
				} else {
					toPrint = " " + Double.toString(percentileAverages[a][b]);
				}
				if(toPrint.length() < 6) {
					while(toPrint.length() < 6) {
						toPrint += "0";
					}
				} else if(toPrint.length() > 6) {
					toPrint = toPrint.substring(0,6);
				}
				toPrint += "  ";
				System.out.print(toPrint);
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		while(true) {
			int robots = 0;
			while(robots < 10) {
				System.out.print("Enter number of robots (>= 10): ");
				try {
					robots = Integer.parseInt(scnr.nextLine());
				} catch(Exception e) {}
			}
			Boolean printRelative = null;
			while(printRelative == null) {
				System.out.print("Print relative? (y/n): ");
				String response = scnr.nextLine().toLowerCase();
				if(response.length() == 0) {
					continue;
				}
				if(response.charAt(0) == 'y') {
					printRelative = true;
				} else if(response.charAt(0) == 'n') {
					printRelative = false;
				} else {
					continue;
				}
			}
			Simulator monteCarlo = new Simulator(robots,printRelative);
			monteCarlo.createRobots();
			monteCarlo.fillData();
			monteCarlo.analyzeData();
		}
		
		//estimate average of truncated normal distribution
		/*double sum = 0;
		for(int i = 0; i < 10000000; i++) {
			sum += 105 * Math.max(0, (Simulator.rand.nextGaussian() * 6.617 + 7.455)/105);
		}
		System.out.println(sum / 10000000.0);*/
	}
}

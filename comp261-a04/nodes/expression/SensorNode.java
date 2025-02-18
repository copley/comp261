package expression;

import interfaces.RobotSensNode;
import java.util.Scanner;
import parser.Parser;
import robot.Robot;
import sensor.BarrelFB;
import sensor.BarrelLR;
import sensor.FuelLeftNode;
import sensor.NumBarrels;
import sensor.OpponentFB;
import sensor.OpponentLR;
import sensor.WallDist;


public class SensorNode implements RobotSensNode {

	RobotSensNode sensorNode = null;
	
	@Override
	public RobotSensNode parse(Scanner scan) {
		if (scan.hasNext(Parser.FUELLEFT)){
			sensorNode = new FuelLeftNode();
		} else if (scan.hasNext(Parser.OPPLR)){
			sensorNode = new OpponentLR();
		} else if (scan.hasNext(Parser.OPPFB)){
			sensorNode = new OpponentFB();
		} else if (scan.hasNext(Parser.NUMBARRELS)){
			sensorNode = new NumBarrels();
		} else if (scan.hasNext(Parser.BARRELLR)){
			sensorNode = new BarrelLR();
		} else if (scan.hasNext(Parser.BARRELFB)){
			sensorNode = new BarrelFB();
		} else if (scan.hasNext(Parser.WALLDIST)){
			sensorNode = new WallDist();
		}
		sensorNode.parse(scan);
		return sensorNode;
	}

	@Override
	public Integer evaluate(Robot robot) {
	
		return sensorNode.evaluate(robot);
	}

	@Override
	public String toString(){
		return sensorNode.toString();
	}

}

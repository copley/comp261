package expression;


import interfaces.RobotExpNode;
import interfaces.RobotSensNode;
import java.util.Scanner;
import operation.AddNode;
import operation.DivNode;
import operation.MulNode;
import operation.SubNode;
import parser.Parser;
import robot.Robot;


public class OpNode implements RobotExpNode {

	RobotSensNode opNode = null;
	
	@Override
	public RobotExpNode parse(Scanner scan) {

		if (scan.hasNext(Parser.ADD)){
			opNode = new AddNode();
		} else if (scan.hasNext(Parser.SUB)){
			opNode = new SubNode();
		} else if (scan.hasNext(Parser.MUL)){
			opNode = new MulNode();
		} else if (scan.hasNext(Parser.DIV)){
			opNode = new DivNode();
		} 
		opNode.parse(scan);
		return opNode;
	}
	
	@Override
	public Integer evaluate(Robot robot) {
	
		return opNode.evaluate(robot);
	}
	
	@Override
	public String toString() {

		return opNode.toString();
	}


}

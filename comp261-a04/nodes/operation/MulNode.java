package operation;

import java.util.Scanner;
import parser.Parser;
import expression.Expression;
import robot.Robot;
import interfaces.RobotExpNode;
import interfaces.RobotSensNode;


public class MulNode implements RobotSensNode {

	private Expression expNode1 = null;
	private Expression expNode2 = null;
	private int val1 = -1;
	private int val2 = -1;

	@Override
	public RobotExpNode parse(Scanner scan) {

		// "mul"
		if (!Parser.gobble(Parser.MUL, scan)) {
			Parser.fail("FAIL: Expecting " + Parser.MUL.toString(), scan);
		}
	
		// "("
		if (!Parser.gobble(Parser.OPENP, scan)) {
			Parser.fail("FAIL: Expecting " + Parser.OPENP.toString(), scan);
		}
	
		// "EXP"
		expNode1 = new Expression();
		expNode1.parse(scan);
	
		// ","
		if (!Parser.gobble(",", scan)) {
			Parser.fail("FAIL: Expecting \",\"", scan);
		}
	
		// "EXP"
		expNode2 = new Expression();
		expNode2.parse(scan);
	
		// ")"
		if (!Parser.gobble(Parser.CLOSEP, scan)) {
			Parser.fail("FAIL: Expecting " + Parser.CLOSEP.toString(), scan);
		}
		
		return this;
	}

	@Override
	public Integer evaluate(Robot robot) {

		val1 = expNode1.evaluate(robot);
		val2 = expNode2.evaluate(robot);
		return val1 * val2;
	}

	public String toString() {

		return String.format("mul ( %s, %s )", expNode1.toString(), expNode2.toString());
	}

}

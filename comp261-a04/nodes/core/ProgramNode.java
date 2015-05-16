package core;

import interfaces.RobotProgramNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import robot.Robot;

public class ProgramNode implements RobotProgramNode {

	private List<RobotProgramNode> statements = new ArrayList<RobotProgramNode>();

	@Override
	public RobotProgramNode parse(Scanner scan) {
	
		StatmentNode statement;
		while (scan.hasNext()) {
			statement = new StatmentNode();
			statements.add(statement.parse(scan));
		}
		return this;
	}

	@Override
	public void execute(Robot robot) {

		for (RobotProgramNode node : statements) {
			node.execute(robot);
		}
	}

	public String toString() {

		String s = "";
		for (RobotProgramNode n : statements) {
			s += n.toString() + '\n';
		}
		return s;
	}
}
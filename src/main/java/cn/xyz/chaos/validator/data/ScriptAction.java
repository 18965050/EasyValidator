package cn.xyz.chaos.validator.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 脚本
 * 
 * @author mfan
 */
public class ScriptAction implements Action {

	private final String	expression;
	private List<Action>	actions	= new ArrayList<Action>();

	public ScriptAction(String expression) {
		this.expression = expression;
	}

	public String getExpression() {
		return expression;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void addAction(Action action) {
		this.actions.add(action);
	}

	public void addActions(List<Action> actions) {
		this.actions.addAll(actions);
	}
}

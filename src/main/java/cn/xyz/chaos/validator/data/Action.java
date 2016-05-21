package cn.xyz.chaos.validator.data;

/**
 * Action抽象的一个执行动作
 * 
 * @author mfan
 */
public interface Action {
	void addAction(Action action);
}

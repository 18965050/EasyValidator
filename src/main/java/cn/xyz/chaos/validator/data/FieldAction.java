package cn.xyz.chaos.validator.data;

public class FieldAction implements Action {

	private Field	field;

	public FieldAction(Field field) {
		this.field = field;
	}

	@Override
	public void addAction(Action action) {
	}

	public Field getField() {
		return field;
	}
}

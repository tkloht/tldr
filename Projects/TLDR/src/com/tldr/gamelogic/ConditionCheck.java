package com.tldr.gamelogic;

import java.util.Map;

public class ConditionCheck {

	public enum Data {
		SPEED
	}

	public enum Operator {
		LE
	}

	public enum CLAZZ {
		Double
	}

	private DataParser parser;
	private DataChecker checker;

	public ConditionCheck(Map<String, String> contidion) {
		if (contidion.containsKey("data")) {
			Data data = Data.valueOf(contidion.get("data").toUpperCase());
			Operator operator = Operator.valueOf(contidion.get("operator")
					.toUpperCase());
			this.setParser(data);
			this.setChecker(this.parser.getDataClassName(), operator);
			this.checker.setValue(parser.parseData(contidion.get("value")));
		}
	}

	private void setParser(Data data) {
		if (data == Data.SPEED) {
			this.parser = new DataParser<Double>() {

				@Override
				public Double parseData(Object object) {
					String dString = (String) object;
					return Double.parseDouble(dString);
				}

				@Override
				public CLAZZ getDataClassName() {
					return CLAZZ.valueOf(Double.class.getSimpleName());
				}

			};
		}

	}

	private void setChecker(CLAZZ clazz, Operator operator) {
		if (clazz == CLAZZ.Double && operator == Operator.LE) {
			this.checker = new DataChecker<Double>() {
				double value;

				@Override
				public void setValue(Double value) {
					this.value = value;
				}

				@Override
				public boolean checkData(Double data) {
					return data < this.value;
				}

			};
		}

	}

	public boolean updateData(Object data) {
		return this.checker.checkData(parser.parseData(data));
	}

	public interface DataParser<T> {
		public CLAZZ getDataClassName();

		public T parseData(Object object);
	}

	public interface DataChecker<T> {
		public void setValue(T value);

		public boolean checkData(T data);
	}

}

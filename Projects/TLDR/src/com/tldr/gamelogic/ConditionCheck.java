package com.tldr.gamelogic;

import java.util.Map;

import com.tldr.exlap.TriggerRegister;
import com.tldr.exlap.TriggerRegister.TriggerDomains;
import com.tldr.gamelogic.GoalRegister.OnTrue;

public class ConditionCheck {

	public enum Data {
		SPEED, CURRENTGEAR
	}

	public enum Operator {
		LE, EQ
	}

	public enum CLAZZ {
		Double
	}

	private DataParser parser;
	private DataChecker checker;

	private TriggerRegister.TriggerDomains domain;
	private String identifier;
	private OnTrue onTrue;

	public ConditionCheck(Map<String, String> contidion, OnTrue onTrue) {
		if (contidion.containsKey("data")) {
			this.onTrue = onTrue;
			setIdentifier(contidion.get("data"));
			Data data = Data.valueOf(contidion.get("data").toUpperCase());
			Operator operator = Operator.valueOf(contidion.get("operator")
					.toUpperCase());
			this.setParser(data);
			this.setChecker(this.parser.getDataClassName(), operator);
			this.checker.setValue(parser.parseData(contidion.get("value")));
		}
	}

	private void setParser(Data data) {
		if (data == Data.SPEED || data == Data.CURRENTGEAR) {
			this.setDomain(TriggerDomains.EXLAP);
			this.parser = new DataParser<Double>() {

				@Override
				public Double parseData(Object object) {
					String dString = (String) object;
					double rtn =Double.parseDouble(dString);
					return rtn;
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
		if (clazz == CLAZZ.Double && operator == Operator.EQ) {
			this.checker = new DataChecker<Double>() {
				double value;

				@Override
				public void setValue(Double value) {
					this.value = value;
				}

				@Override
				public boolean checkData(Double data) {
					return data == this.value;
				}

			};
		}

	}

	@SuppressWarnings("unchecked")
	public boolean updateData(Object data) {
		boolean result = false;
		try {
			result = this.checker.checkData(parser.parseData(data));
		} catch (Exception e) {
//			e.printStackTrace();
		}
		if (result && this.onTrue != null) {
			this.onTrue.onTrue();
		}
		return result;
	}

	public TriggerRegister.TriggerDomains getDomain() {
		return domain;
	}

	public void setDomain(TriggerRegister.TriggerDomains domain) {
		this.domain = domain;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
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

package com.tldr.gamelogic;

import java.util.Map;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.tldr.exlap.TriggerRegister;
import com.tldr.exlap.TriggerRegister.TriggerDomains;
import com.tldr.gamelogic.GoalRegister.OnTrue;
import com.tldr.tools.ToolBox;

@SuppressLint("DefaultLocale")
public class ConditionCheck {

	public enum Data {
		NONE, GPS, DISPLAYEDVEHICLESPEED, CURRENTGEAR, VEHICLESPEED, SEATBELTLOCK, AIRCONDITIONINGCONTROL
	}

	public enum Operator {
		NONE, LE, EQ, EQR
	}

	public enum CLAZZ {
		Double, LatLng, String
	}

	private DataParser parser;
	private DataChecker checker;

	private TriggerRegister.TriggerDomains domain;
	private String identifier;
	private OnTrue onTrue;

	public ConditionCheck(Map<String, String> condition, OnTrue onTrue) {
		if (condition.containsKey("data")) {
			String dataString = condition.get("data");
			this.onTrue = onTrue;
			if (dataString.equals("polyline_point"))
				dataString = "gps";
			setIdentifier(dataString);
			Data data = null;
			if (containsData(dataString.toUpperCase())) {
				data = Data.valueOf(dataString.toUpperCase());
			}
			Operator operator = null;
			if (containsOperator(condition.get("operator").toUpperCase())) {
				operator = Operator.valueOf(condition.get("operator")
						.toUpperCase());
			}
			if (data == null || operator == null) {
				Log.i("TLDR",
						"Condition registation failed due to lag of coding!");
			}
			this.setParser(data);
			if (this.parser != null) {
				this.setChecker(this.parser.getDataClassName(), operator);
				if (this.parser.getDataClassName() == CLAZZ.LatLng) {
					this.checker.setValue(ToolBox.locationFromString(condition
							.get("value")));
				} else {
					this.checker.setValue(parser.parseData(condition
							.get("value")));
				}
			}

		}
	}

	private void setParser(Data data) {
		if (data == Data.SEATBELTLOCK || data == Data.AIRCONDITIONINGCONTROL) {
			this.setDomain(TriggerDomains.EXLAP);
			this.parser = new DataParser<String>() {

				@Override
				public String parseData(Object object) {
					String dString = (String) object;
					return dString;
				}

				@Override
				public CLAZZ getDataClassName() {
					return CLAZZ.valueOf(String.class.getSimpleName());
				}

			};
		}

		if (data == Data.NONE || data == Data.DISPLAYEDVEHICLESPEED
				|| data == Data.CURRENTGEAR || data == Data.VEHICLESPEED) {
			this.setDomain(TriggerDomains.EXLAP);
			this.parser = new DataParser<Double>() {

				@Override
				public Double parseData(Object object) {
					String dString = (String) object;
					if (dString.equals("null")) {
						dString = "0.0";
					}
					double rtn = Double.parseDouble(dString);
					return rtn;
				}

				@Override
				public CLAZZ getDataClassName() {
					return CLAZZ.valueOf(Double.class.getSimpleName());
				}

			};
		}

		if (data == Data.GPS) {
			this.setDomain(TriggerDomains.GPS);

			this.parser = new DataParser<LatLng>() {

				@Override
				public CLAZZ getDataClassName() {
					return CLAZZ.valueOf(LatLng.class.getSimpleName());
				}

				@Override
				public LatLng parseData(Object object) {
					LatLng lLocation = (LatLng) object;
					return lLocation;
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
		if (clazz == CLAZZ.Double && operator == Operator.EQR) {
			this.checker = new DataChecker<Double>() {
				double value;
				double variance;

				@Override
				public void setValue(Double value) {
					this.value = value;
					this.variance = value * 0.1;
				}

				@Override
				public boolean checkData(Double data) {
					// Log.i("TLDR",""+(this.value - this.variance <= data
					// && data <= this.value + this.variance));
					return this.value - this.variance <= data
							&& data <= this.value + this.variance;
				}

			};
		}
		if (clazz == CLAZZ.String && operator == Operator.EQ) {
			this.checker = new DataChecker<String>() {
				String value;

				@Override
				public void setValue(String value) {
					this.value = value;
				}

				@Override
				public boolean checkData(String data) {
					return this.value.equals(data);
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
		if (clazz == CLAZZ.LatLng && operator == Operator.EQ) {
			this.checker = new DataChecker<LatLng>() {
				private LatLng location;

				@Override
				public void setValue(LatLng value) {
					this.location = value;
				}

				@Override
				public boolean checkData(LatLng data) {
					float[] distance = new float[] { 0.0f };
					Location.distanceBetween(data.latitude, data.longitude,
							location.latitude, location.longitude, distance);
					int dist = Math.round(distance[0]);
					Log.i("TLDR", "GPS Goaldistance:" + dist);
					if (dist < 50) {
						return true;
					} else {
						return false;
					}
				}

			};
		}

	}

	public boolean successfullyInitialized() {
		return this.parser != null && this.checker != null;
	}

	@SuppressWarnings("unchecked")
	public boolean updateData(Object data) {
		// Log.i("TLDR", "updateData" + data);
		boolean result = false;
		try {
			result = this.checker.checkData(parser.parseData(data));
		} catch (Exception e) {
			e.printStackTrace();
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

	public static boolean containsData(String test) {

		for (Data c : Data.values()) {
			if (c.name().equals(test)) {
				return true;
			}
		}

		return false;
	}

	public static boolean containsOperator(String test) {

		for (Operator c : Operator.values()) {
			if (c.name().equals(test)) {
				return true;
			}
		}
		return false;
	}

}

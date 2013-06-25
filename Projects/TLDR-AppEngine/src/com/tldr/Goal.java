package com.tldr;

import javax.jdo.annotations.Persistent;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Goal {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "goal_seq_gen")
	@SequenceGenerator(name = "goal_seq_gen", sequenceName = "GOAL_SEQ")
	private Long id;

	@Persistent
	private String InterfaceUrl;

	@Persistent
	private String json_string;

	@Persistent
	private String targetValue;

	@Persistent
	private String comparator;

	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public String getComparator() {
		return comparator;
	}

	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

	public String getInterface() {
		return InterfaceUrl;
	}

	public void setInterface(String InterfaceUrl) {
		this.InterfaceUrl = InterfaceUrl;
	}
	
	public Long getId(){
		return id;
	}

}

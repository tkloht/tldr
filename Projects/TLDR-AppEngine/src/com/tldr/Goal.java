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
	  @GeneratedValue(strategy=GenerationType.AUTO, generator="goal_seq_gen")
	  @SequenceGenerator(name="goal_seq_gen", sequenceName="GOAL_SEQ")
	  private Long id;

	  @Persistent
	  private String json_string;



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJson_string() {
		return json_string;
	}

	public void setJson_string(String json_string) {
		this.json_string = json_string;
	}


}
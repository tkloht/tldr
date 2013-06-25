package com.tldr;

import java.util.List;

import javax.jdo.annotations.Persistent;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

/**
 * An entity for Android device information.
 * 
 * Its associated endpoint, DeviceInfoEndpoint.java, was directly generated from
 * this class - the Google Plugin for Eclipse allows you to generate endpoints
 * directly from entities!
 * 
 * DeviceInfoEndpoint.java will be used for registering devices with this App
 * Engine application. Registered devices will receive messages broadcast by
 * this application over Google Cloud Messaging (GCM). If you'd like to take a
 * look at the broadcasting code, check out MessageEndpoint.java.
 * 
 * For more information, see
 * http://developers.google.com/eclipse/docs/cloud_endpoints.
 * 
 * NOTE: This DeviceInfoEndpoint.java does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Entity
public class Task {
	
	  @Id
	  @GeneratedValue(strategy=GenerationType.AUTO, generator="task_seq_gen")
	  @SequenceGenerator(name="task_seq_gen", sequenceName="TASK_SEQ")
	  private Long id;
	  
	  @Persistent
	  private String title;
	  
	  @Persistent
	  private String description;
	  @Persistent
	  private double geo_lat;
	  @Persistent
	  private double geo_lon;
	  
	  @Persistent
	  private List<Long> goals;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Long> getGoals() {
		return goals;
	}

	public void setGoals(List<Long> goals) {
		this.goals = goals;
	}

	public Long getId() {
		return id;
	}

	public double getGeo_lat() {
		return geo_lat;
	}

	public void setGeo_lat(double geo_lat) {
		this.geo_lat = geo_lat;
	}

	public double getGeo_lon() {
		return geo_lon;
	}

	public void setGeo_lon(double geo_lon) {
		this.geo_lon = geo_lon;
	}

	
	
	
	  
	  


}

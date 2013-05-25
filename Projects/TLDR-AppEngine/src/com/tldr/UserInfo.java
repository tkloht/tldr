package com.tldr;

import javax.jdo.annotations.Persistent;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class UserInfo {
	
	  @Id
	  @GeneratedValue(strategy=GenerationType.AUTO, generator="user_info_seq_gen")
	  @SequenceGenerator(name="user_info_seq_gen", sequenceName="USER_INFO_SEQ")
	  private Long id;
	  
	  @Persistent
	  private String username;
	  @Persistent
	  private String email;

	  public String getEmail() {
		return email;
	}
	
	public Long getId() {
		  return id;
	}
	public void setEmail(String email) {
		this.email = email;
	}


	public String getUsername() {
	    return username;
	  }
	  

	  public void setUsername(String username) {
	    this.username = username;
	  }

}

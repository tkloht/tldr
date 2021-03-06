/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2013-06-26 16:27:34 UTC)
 * on 2013-07-07 at 16:25:23 UTC 
 * Modify at your own risk.
 */

package com.tldr.taskendpoint.model;

/**
 * Model definition for Task.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the . For a detailed explanation see:
 * <a href="http://code.google.com/p/google-api-java-client/wiki/Json">http://code.google.com/p/google-api-java-client/wiki/Json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Task extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String description;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("geo_lat")
  private java.lang.Double geoLat;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("geo_lon")
  private java.lang.Double geoLon;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.util.List<java.lang.Long> goals;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String title;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDescription() {
    return description;
  }

  /**
   * @param description description or {@code null} for none
   */
  public Task setDescription(java.lang.String description) {
    this.description = description;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getGeoLat() {
    return geoLat;
  }

  /**
   * @param geoLat geoLat or {@code null} for none
   */
  public Task setGeoLat(java.lang.Double geoLat) {
    this.geoLat = geoLat;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getGeoLon() {
    return geoLon;
  }

  /**
   * @param geoLon geoLon or {@code null} for none
   */
  public Task setGeoLon(java.lang.Double geoLon) {
    this.geoLon = geoLon;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.Long> getGoals() {
    return goals;
  }

  /**
   * @param goals goals or {@code null} for none
   */
  public Task setGoals(java.util.List<java.lang.Long> goals) {
    this.goals = goals;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public Task setId(java.lang.Long id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getTitle() {
    return title;
  }

  /**
   * @param title title or {@code null} for none
   */
  public Task setTitle(java.lang.String title) {
    this.title = title;
    return this;
  }

  @Override
  public Task set(String fieldName, Object value) {
    return (Task) super.set(fieldName, value);
  }

  @Override
  public Task clone() {
    return (Task) super.clone();
  }

}

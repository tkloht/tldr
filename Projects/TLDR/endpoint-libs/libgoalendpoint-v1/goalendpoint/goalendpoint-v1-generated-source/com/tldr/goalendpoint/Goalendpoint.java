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
 * on 2013-07-07 at 16:25:47 UTC 
 * Modify at your own risk.
 */

package com.tldr.goalendpoint;

/**
 * Service definition for Goalendpoint (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link GoalendpointRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Goalendpoint extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION == 14,
        "You are currently running with version %s of google-api-client. " +
        "You need version 1.14 of google-api-client to run version " +
        "1.14.2-beta of the  library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://swp-tldr.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "goalendpoint/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Goalendpoint(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Goalendpoint(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getGoal".
   *
   * This request holds the parameters needed by the the goalendpoint server.  After setting any
   * optional parameters, call the {@link GetGoal#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   */
  public GetGoal getGoal(java.lang.Long id) throws java.io.IOException {
    GetGoal result = new GetGoal(id);
    initialize(result);
    return result;
  }

  public class GetGoal extends GoalendpointRequest<com.tldr.goalendpoint.model.Goal> {

    private static final String REST_PATH = "goal/{id}";

    /**
     * Create a request for the method "getGoal".
     *
     * This request holds the parameters needed by the the goalendpoint server.  After setting any
     * optional parameters, call the {@link GetGoal#execute()} method to invoke the remote operation.
     * <p> {@link
     * GetGoal#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
     * be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected GetGoal(java.lang.Long id) {
      super(Goalendpoint.this, "GET", REST_PATH, null, com.tldr.goalendpoint.model.Goal.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetGoal setAlt(java.lang.String alt) {
      return (GetGoal) super.setAlt(alt);
    }

    @Override
    public GetGoal setFields(java.lang.String fields) {
      return (GetGoal) super.setFields(fields);
    }

    @Override
    public GetGoal setKey(java.lang.String key) {
      return (GetGoal) super.setKey(key);
    }

    @Override
    public GetGoal setOauthToken(java.lang.String oauthToken) {
      return (GetGoal) super.setOauthToken(oauthToken);
    }

    @Override
    public GetGoal setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetGoal) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetGoal setQuotaUser(java.lang.String quotaUser) {
      return (GetGoal) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetGoal setUserIp(java.lang.String userIp) {
      return (GetGoal) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public GetGoal setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @Override
    public GetGoal set(String parameterName, Object value) {
      return (GetGoal) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insertGoal".
   *
   * This request holds the parameters needed by the the goalendpoint server.  After setting any
   * optional parameters, call the {@link InsertGoal#execute()} method to invoke the remote operation.
   *
   * @param content the {@link com.tldr.goalendpoint.model.Goal}
   * @return the request
   */
  public InsertGoal insertGoal(com.tldr.goalendpoint.model.Goal content) throws java.io.IOException {
    InsertGoal result = new InsertGoal(content);
    initialize(result);
    return result;
  }

  public class InsertGoal extends GoalendpointRequest<com.tldr.goalendpoint.model.Goal> {

    private static final String REST_PATH = "goal";

    /**
     * Create a request for the method "insertGoal".
     *
     * This request holds the parameters needed by the the goalendpoint server.  After setting any
     * optional parameters, call the {@link InsertGoal#execute()} method to invoke the remote
     * operation. <p> {@link
     * InsertGoal#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param content the {@link com.tldr.goalendpoint.model.Goal}
     * @since 1.13
     */
    protected InsertGoal(com.tldr.goalendpoint.model.Goal content) {
      super(Goalendpoint.this, "POST", REST_PATH, content, com.tldr.goalendpoint.model.Goal.class);
    }

    @Override
    public InsertGoal setAlt(java.lang.String alt) {
      return (InsertGoal) super.setAlt(alt);
    }

    @Override
    public InsertGoal setFields(java.lang.String fields) {
      return (InsertGoal) super.setFields(fields);
    }

    @Override
    public InsertGoal setKey(java.lang.String key) {
      return (InsertGoal) super.setKey(key);
    }

    @Override
    public InsertGoal setOauthToken(java.lang.String oauthToken) {
      return (InsertGoal) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertGoal setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertGoal) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertGoal setQuotaUser(java.lang.String quotaUser) {
      return (InsertGoal) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertGoal setUserIp(java.lang.String userIp) {
      return (InsertGoal) super.setUserIp(userIp);
    }

    @Override
    public InsertGoal set(String parameterName, Object value) {
      return (InsertGoal) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "listGoal".
   *
   * This request holds the parameters needed by the the goalendpoint server.  After setting any
   * optional parameters, call the {@link ListGoal#execute()} method to invoke the remote operation.
   *
   * @return the request
   */
  public ListGoal listGoal() throws java.io.IOException {
    ListGoal result = new ListGoal();
    initialize(result);
    return result;
  }

  public class ListGoal extends GoalendpointRequest<com.tldr.goalendpoint.model.CollectionResponseGoal> {

    private static final String REST_PATH = "goal";

    /**
     * Create a request for the method "listGoal".
     *
     * This request holds the parameters needed by the the goalendpoint server.  After setting any
     * optional parameters, call the {@link ListGoal#execute()} method to invoke the remote operation.
     * <p> {@link
     * ListGoal#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @since 1.13
     */
    protected ListGoal() {
      super(Goalendpoint.this, "GET", REST_PATH, null, com.tldr.goalendpoint.model.CollectionResponseGoal.class);
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public ListGoal setAlt(java.lang.String alt) {
      return (ListGoal) super.setAlt(alt);
    }

    @Override
    public ListGoal setFields(java.lang.String fields) {
      return (ListGoal) super.setFields(fields);
    }

    @Override
    public ListGoal setKey(java.lang.String key) {
      return (ListGoal) super.setKey(key);
    }

    @Override
    public ListGoal setOauthToken(java.lang.String oauthToken) {
      return (ListGoal) super.setOauthToken(oauthToken);
    }

    @Override
    public ListGoal setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (ListGoal) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public ListGoal setQuotaUser(java.lang.String quotaUser) {
      return (ListGoal) super.setQuotaUser(quotaUser);
    }

    @Override
    public ListGoal setUserIp(java.lang.String userIp) {
      return (ListGoal) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String cursor;

    /**

     */
    public java.lang.String getCursor() {
      return cursor;
    }

    public ListGoal setCursor(java.lang.String cursor) {
      this.cursor = cursor;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Integer limit;

    /**

     */
    public java.lang.Integer getLimit() {
      return limit;
    }

    public ListGoal setLimit(java.lang.Integer limit) {
      this.limit = limit;
      return this;
    }

    @Override
    public ListGoal set(String parameterName, Object value) {
      return (ListGoal) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "removeGoal".
   *
   * This request holds the parameters needed by the the goalendpoint server.  After setting any
   * optional parameters, call the {@link RemoveGoal#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   */
  public RemoveGoal removeGoal(java.lang.Long id) throws java.io.IOException {
    RemoveGoal result = new RemoveGoal(id);
    initialize(result);
    return result;
  }

  public class RemoveGoal extends GoalendpointRequest<com.tldr.goalendpoint.model.Goal> {

    private static final String REST_PATH = "goal/{id}";

    /**
     * Create a request for the method "removeGoal".
     *
     * This request holds the parameters needed by the the goalendpoint server.  After setting any
     * optional parameters, call the {@link RemoveGoal#execute()} method to invoke the remote
     * operation. <p> {@link
     * RemoveGoal#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected RemoveGoal(java.lang.Long id) {
      super(Goalendpoint.this, "DELETE", REST_PATH, null, com.tldr.goalendpoint.model.Goal.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public RemoveGoal setAlt(java.lang.String alt) {
      return (RemoveGoal) super.setAlt(alt);
    }

    @Override
    public RemoveGoal setFields(java.lang.String fields) {
      return (RemoveGoal) super.setFields(fields);
    }

    @Override
    public RemoveGoal setKey(java.lang.String key) {
      return (RemoveGoal) super.setKey(key);
    }

    @Override
    public RemoveGoal setOauthToken(java.lang.String oauthToken) {
      return (RemoveGoal) super.setOauthToken(oauthToken);
    }

    @Override
    public RemoveGoal setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (RemoveGoal) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public RemoveGoal setQuotaUser(java.lang.String quotaUser) {
      return (RemoveGoal) super.setQuotaUser(quotaUser);
    }

    @Override
    public RemoveGoal setUserIp(java.lang.String userIp) {
      return (RemoveGoal) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public RemoveGoal setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @Override
    public RemoveGoal set(String parameterName, Object value) {
      return (RemoveGoal) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "updateGoal".
   *
   * This request holds the parameters needed by the the goalendpoint server.  After setting any
   * optional parameters, call the {@link UpdateGoal#execute()} method to invoke the remote operation.
   *
   * @param content the {@link com.tldr.goalendpoint.model.Goal}
   * @return the request
   */
  public UpdateGoal updateGoal(com.tldr.goalendpoint.model.Goal content) throws java.io.IOException {
    UpdateGoal result = new UpdateGoal(content);
    initialize(result);
    return result;
  }

  public class UpdateGoal extends GoalendpointRequest<com.tldr.goalendpoint.model.Goal> {

    private static final String REST_PATH = "goal";

    /**
     * Create a request for the method "updateGoal".
     *
     * This request holds the parameters needed by the the goalendpoint server.  After setting any
     * optional parameters, call the {@link UpdateGoal#execute()} method to invoke the remote
     * operation. <p> {@link
     * UpdateGoal#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param content the {@link com.tldr.goalendpoint.model.Goal}
     * @since 1.13
     */
    protected UpdateGoal(com.tldr.goalendpoint.model.Goal content) {
      super(Goalendpoint.this, "PUT", REST_PATH, content, com.tldr.goalendpoint.model.Goal.class);
    }

    @Override
    public UpdateGoal setAlt(java.lang.String alt) {
      return (UpdateGoal) super.setAlt(alt);
    }

    @Override
    public UpdateGoal setFields(java.lang.String fields) {
      return (UpdateGoal) super.setFields(fields);
    }

    @Override
    public UpdateGoal setKey(java.lang.String key) {
      return (UpdateGoal) super.setKey(key);
    }

    @Override
    public UpdateGoal setOauthToken(java.lang.String oauthToken) {
      return (UpdateGoal) super.setOauthToken(oauthToken);
    }

    @Override
    public UpdateGoal setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (UpdateGoal) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public UpdateGoal setQuotaUser(java.lang.String quotaUser) {
      return (UpdateGoal) super.setQuotaUser(quotaUser);
    }

    @Override
    public UpdateGoal setUserIp(java.lang.String userIp) {
      return (UpdateGoal) super.setUserIp(userIp);
    }

    @Override
    public UpdateGoal set(String parameterName, Object value) {
      return (UpdateGoal) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link Goalendpoint}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Goalendpoint}. */
    @Override
    public Goalendpoint build() {
      return new Goalendpoint(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link GoalendpointRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setGoalendpointRequestInitializer(
        GoalendpointRequestInitializer goalendpointRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(goalendpointRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}

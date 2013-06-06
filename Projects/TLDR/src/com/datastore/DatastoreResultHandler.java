package com.datastore;

public interface DatastoreResultHandler {

	/**
	 * This method will be called by the UserInfoDatastores if a request delivers results.
	 * @param requestName
	 *            - Name of the Request which produced the handleRequestResult
	 *            callback. Each Datastore has its requestNames stored in Static
	 *            Variables.
	 * @param result - Result of the Request. Varies between Datastore Requests.
	 */
	public void handleRequestResult(int requestId, Object result);

}

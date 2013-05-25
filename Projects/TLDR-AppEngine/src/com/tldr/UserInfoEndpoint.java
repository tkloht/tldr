package com.tldr;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.appengine.datanucleus.query.JPACursorHelper;

@Api(name = "userinfoendpoint", namespace = @ApiNamespace(ownerDomain = "tldr.com", ownerName = "tldr.com", packagePath = "com.tldr"), clientIds = {
		"511171351776-3o8dc555nqai62t3pe4m7ubrgc58i2ge.apps.googleusercontent.com",
		"511171351776.apps.googleusercontent.com" }, audiences = { "511171351776-3o8dc555nqai62t3pe4m7ubrgc58i2ge.apps.googleusercontent.com" })
public class UserInfoEndpoint {

	

	/**
	 * Provides the ability to insert a new UserInfo entity.
	 */
	@ApiMethod(name = "registerUserInfo")
	public UserInfo register(UserInfo userinfo, User user)
			throws OAuthRequestException, IOException {
		if (user != null) {
			EntityManager mgr = getEntityManager();
			try {
				if (containsUserInfo(userinfo)) {
					throw new EntityExistsException("Object already exists");
				}
				mgr.persist(userinfo);
			} catch (EntityExistsException e) {
				mgr.getTransaction().begin();
				if(userinfo.getId()!=null&&userinfo.getUsername()!=null){
					if(isUsernameAvailable(userinfo.getUsername())){
					userinfo = mgr.merge(userinfo);
					}
				}
				mgr.getTransaction().commit();
				userinfo = findByEmail(userinfo.getEmail());
				
				
			} finally {
				mgr.close();
			}
			return userinfo;
		} else
			throw new OAuthRequestException("You are not authenticated!");
	}

	private UserInfo findByEmail(String email) {
		EntityManager mgr = getEntityManager();
		UserInfo lReturn = null;
		try {
			mgr.getTransaction().begin();
			Query q = mgr
					.createQuery("Select u from UserInfo u where u.email=?1");
			q.setParameter(1, email);
			List<UserInfo> result = q.getResultList();
			if (!result.isEmpty()) {
				lReturn = result.get(0);

			} else
				lReturn = null;
			mgr.getTransaction().commit();
		} catch (Exception e) {
			lReturn = null;
		}
		return lReturn;
	}

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listUserInfo")
	public CollectionResponse<UserInfo> listUserInfo(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<UserInfo> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from UserInfo as UserInfo");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<UserInfo>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (UserInfo obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<UserInfo> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getUserInfo")
	public UserInfo getUserInfo(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		UserInfo userinfo = null;
		try {
			userinfo = mgr.find(UserInfo.class, id);
		} finally {
			mgr.close();
		}
		return userinfo;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
	 * 
	 * @param userinfo
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateUserInfo")
	public UserInfo updateUserInfo(UserInfo userinfo) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsUserInfo(userinfo)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(userinfo);
		} finally {
			mgr.close();
		}
		return userinfo;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 * @return The deleted entity.
	 */
	@ApiMethod(name = "removeUserInfo")
	public UserInfo removeUserInfo(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		UserInfo userinfo = null;
		try {
			userinfo = mgr.find(UserInfo.class, id);
			mgr.remove(userinfo);
		} finally {
			mgr.close();
		}
		return userinfo;
	}

	
	private boolean isUsernameAvailable(String username) {
		EntityManager mgr = getEntityManager();
		boolean contains = false;
		try {
			mgr.getTransaction().begin();

			Query q = mgr
					.createQuery("select u from UserInfo u where u.username=?1");
			q.setParameter(1, username);
			List<?> list = q.getResultList();
			if (!list.isEmpty())
				contains = true;

			
			mgr.getTransaction().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		mgr.close();

		return !contains;		
	}
	private boolean containsUserInfo(UserInfo userinfo) {
		EntityManager mgr = getEntityManager();
		boolean contains = false;
		try {
			mgr.getTransaction().begin();
			if (userinfo.getEmail() != null) {
				Query q = mgr
						.createQuery("select u from UserInfo u where u.email=?1");
				q.setParameter(1, userinfo.getEmail());
				List<?> list = q.getResultList();
				if (!list.isEmpty())
					contains = true;

			} 
			mgr.getTransaction().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		mgr.close();

		return contains;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}

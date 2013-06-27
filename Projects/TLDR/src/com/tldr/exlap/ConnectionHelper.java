package com.tldr.exlap;

import java.io.IOException;
import java.util.Enumeration;

import com.tldr.GlobalData;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import de.exlap.AbstractInterface;
import de.exlap.DataElement;
import de.exlap.DataListener;
import de.exlap.DataObject;
import de.exlap.ExlapClient;
import de.exlap.ExlapException;
import de.exlap.Url;
import de.exlap.UrlList;
import de.exlap.discovery.DiscoveryListener;
import de.exlap.discovery.DiscoveryManager;
import de.exlap.discovery.ServiceDescription;

public class ConnectionHelper implements DataListener, DiscoveryListener {

	private ExlapClient ec;
	private DataListener dataListener = this;

	private Handler m_carInfoHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			DataObject dataObject = (DataObject) msg.obj;
			for (int i = 0; i < dataObject.size(); i++) {
				DataElement dataElement = dataObject.getElement(i);
				if (dataElement.getState() == 1) {
					// Check and work with incoming messages
					@SuppressWarnings("unused")
					String value = dataElement.getValue().toString().equals("") ? "null"
							: dataElement.getValue().toString();
				}
			}
		}
	};

	public void onData(DataObject dataObject) {
		System.out.println(">>> Got <Data/>: " + dataObject.toString());
		if (dataObject != null) {
			m_carInfoHandler.sendMessage(m_carInfoHandler.obtainMessage(1,
					dataObject));
		}
	}

	public void killClient() {
		ec.shutdown();
	}

	public boolean isConnected() {
		return ec.isConnected();
	}

	/**
	 * Opens a connection to the given address.
	 * 
	 * @param address
	 *            Socket address e.g. "socket://192.168.1.76:28500"
	 */
	public void startService(final String address) {
		Thread service = new Thread(new Runnable() {
			public void run() {
				ec = new ExlapClient(address);
				ec.addDataListener(dataListener);
				ec.connect();
				if (ec.isConnected()) {
					Log.i("ConnectionHelper", "isConnected on Port" + address);
				} else
					Log.i("ConnectionHelper", "Unable to connect to: "
							+ address);
			}
		});
		service.start();
	}

	/**
	 * Subscribe to all available {@link AbstractInterface interfaces}
	 */
	public void subscribeAll() {
		new SubscribeAllInterfaces().execute();
	}

	public void subscribe(String... subscriptionInterface) {
		new SubscribeInterface().execute(subscriptionInterface);
	}

	private class SubscribeInterface extends
			AsyncTask<String, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				System.out.println("subscribing");
				UrlList urlList = ec.getDir("*");
				@SuppressWarnings("unchecked")
				Url url = urlList.getElement(params[0]);
				if (url.getType() == Url.TYPE_OBJECT) {
					String urlName = url.getName();
					System.out.print("Interface on \"" + urlName + "\"...");
					System.out.println(" DONE. Interface="
							+ ec.getInterface(urlName).toString());
					System.out.print("Subscribe to: \"" + urlName + "\"...");
					ec.subscribeObject(urlName, 100);
					System.out.println(" DONE.");
				}
				return true;
			} catch (Exception e) {
				Log.e("tldr-exlap", e.getLocalizedMessage());
			}
			return false;
		}
	}

	private class SubscribeAllInterfaces extends
			AsyncTask<Void, Integer, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				System.out.println("subscribing");
				UrlList urlList = ec.getDir("*");
				@SuppressWarnings("unchecked")
				Enumeration<Url> elements = urlList.elements();
				while (elements.hasMoreElements()) {
					Url url = elements.nextElement();
					if (url.getType() == Url.TYPE_OBJECT) {
						String urlName = url.getName();
						System.out.print("Interface on \"" + urlName + "\"...");
						System.out.println(" DONE. Interface="
								+ ec.getInterface(urlName).toString());
						System.out
								.print("Subscribe to: \"" + urlName + "\"...");
						ec.subscribeObject(urlName, 100);
						System.out.println(" DONE.");
					}
				}
				return urlList.size();

			} catch (Exception e) {
				Log.e("tldr-exlap", e.getLocalizedMessage());
			}
			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			Log.e("tldr-exlap", "subscribed to " + result + " interfaces");
		}
	}

	public void unsubscribeAll() throws IllegalArgumentException, IOException,
			ExlapException {
		System.out.println("unsubscribing");
		UrlList urlList = ec.getDir("*");
		@SuppressWarnings("unchecked")
		Enumeration<Url> elements = urlList.elements();
		while (elements.hasMoreElements()) {
			Url url = elements.nextElement();
			if (url.getType() == Url.TYPE_OBJECT) {
				String urlName = url.getName();
				ec.unsubscribeObject(urlName);
				System.out.println(" UNSUBSCRIBE DONE.");
			}
		}

	}

	public void discoveryEvent(int eventType,
			ServiceDescription serviceDescription) {
		switch (eventType) {
		case DiscoveryListener.SERVICE_CHANGED:
			System.out.println("CHANGED:" + serviceDescription.toString());
			break;
		case DiscoveryListener.SERVICE_GONE:
			System.out.println("GONE   :" + serviceDescription.toString());
			break;
		case DiscoveryListener.SERVICE_NEW:
			startService(serviceDescription.getAddress());
			break;
		default:
			break;
		}
	}

	public void discoveryFinished(boolean discoverySuccessfull) {
		System.out.println("Discovery finished or terminated");
	}

	public void performDiscovery() throws IOException {
		if (GlobalData.getCurrentUser().getEmail()
				.equals("doriankno@googlemail.com")) {
			 Log.e("tldr-exlap", "dorian workaround");
			 this.startService("socket://192.168.1.106:28500");
		} else {
			DiscoveryManager disco = new DiscoveryManager(
					DiscoveryManager.SCHEME_SOCKET);
			try {
				disco.discoverServices(this, null, true);
			} catch (Exception e) {
				System.out.println("ERROR. Root cause: " + e.getMessage());
			}
		}

	}
}

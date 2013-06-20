package com.tldr.tools;

import android.app.Activity;
import android.widget.TextSwitcher;



public class TextSwitchAnimator extends Thread{
	Activity context;
	TextSwitcher txtSwitch;
	String text;
	int current=0;
	public TextSwitchAnimator(TextSwitcher txtSwitch, String text, Activity context) {
		// TODO Auto-generated constructor stub
		super();
		this.txtSwitch=txtSwitch;
		this.text=text;
		this.context=context;
		
	}
	
	private static String createRandomString(int length){
		String lReturn="";
		for(int i=0; i<length; i++){
			lReturn+=(int)(Math.random()*10);
		}
		return lReturn;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			for(int i=1; i<=text.length(); i++){
				current++;
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						txtSwitch.setText(text.substring(0, current)+createRandomString(text.length()-current));
						
					}
				});
				Thread.sleep(50);
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						txtSwitch.setText(text.substring(0, current)+createRandomString(text.length()-current));
						
					}
				});
				Thread.sleep(50);
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						txtSwitch.setText(text.substring(0, current)+createRandomString(text.length()-current));
						
					}
				});
				Thread.sleep(50);
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						txtSwitch.setText(text.substring(0, current)+createRandomString(text.length()-current));
						
					}
				});
				Thread.sleep(50);
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						txtSwitch.setText(text.substring(0, current)+createRandomString(text.length()-current));
						
					}
				});
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

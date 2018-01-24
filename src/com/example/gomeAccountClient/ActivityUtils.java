package com.example.gomeAccountClient;

import java.util.List;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityUtils {
	private static final String TAG = "lilei_ActivityUtils";
	
	public static void alert(Context context,String str){
	    Log.d(TAG, "alert() str:"+str);
	    Toast toast = Toast.makeText(context,str,Toast.LENGTH_LONG);
	    LinearLayout linearLayout = (LinearLayout)toast.getView();
	    TextView tv = (TextView)linearLayout.findViewById(com.android.internal.R.id.message); 
	    //tv.setSingleLine(false);
	    Log.i(TAG, "alert() linearLayout:"+linearLayout+" tv:"+tv);
	    toast.show();
	}
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();  
			}
		}
		return false;  
	}
	public static boolean checkNetwork(Context context){
		if(!isNetworkConnected(context)){
			Log.e(TAG, "checkNetwork 111 network unavaiable!!");
			//ActivityUtils.alert(context,context.getResources().getString(R.string.alert_network_unavaiable));
			showSetWifiDialog(context);   
			
			return false;
		}else{
			Log.d(TAG, "checkNetwork 222 network ok");
			//ActivityUtils.alert(context,"network ok!!");
		}
		return true;
	}
	static void showSetWifiDialog(final Context context){
        Dialog alertDialog = new AlertDialog.Builder(context).   
                setTitle(context.getResources().getString(R.string.dialog_title_no_network)).   
                setIcon(R.drawable.ic_launcher).
                setPositiveButton(context.getResources().getString(R.string.txt_set_network), new DialogInterface.OnClickListener() {   
                    @Override   
                    public void onClick(DialogInterface dialog, int which) {   
                        // TODO Auto-generated method stub  
                    	Log.i(TAG, "onClick setwifi");
                    	context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }).
                setNeutralButton(context.getResources().getString(R.string.txt_set_network_cancel), new DialogInterface.OnClickListener() {   
                    @Override   
                    public void onClick(DialogInterface dialog, int which) {   
                        // TODO Auto-generated method stub    
                    }   
                }).   
                create();   
        alertDialog.show();
	}
	 public static boolean isNetworkAvailable(Context context)
	    {
	        //Context context = activity.getApplicationContext();
	        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
	        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        
	        if (connectivityManager == null)
	        {
	            return false;
	        }
	        else
	        {
	            // 获取NetworkInfo对象
	            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
	            
	            if (networkInfo != null && networkInfo.length > 0)
	            {
	                for (int i = 0; i < networkInfo.length; i++)
	                {
	                    Log.i(TAG, i + "===状态===" + networkInfo[i].getState());
	                    Log.i(TAG, i + "===类型===" + networkInfo[i].getTypeName());
	                    System.out.println(i + "===状态===" + networkInfo[i].getState());
	                    
	                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
	                    // 判断当前网络状态是否为连接状态
	                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
	                    {
	                        Log.i(TAG,"network available");
	                        return true;
	                    }
	                }
	            }
	        }
	        Log.i(TAG,"network not available");
	        return false;
	    }
	 ///
		 public static boolean isForeground(Activity activity) {  
		        return isForeground(activity, activity.getClass().getName());  
		 }  
	    /**
	     * 获取本机国美账号
	     * @param context
	     * @return
	     */
	    public static Account removeLatestGomeAccount(Context context){
	    	Account latestAccount = null;
	    	final AccountManager am = AccountManager.get(context); 
	    	Account [] accounts = am.getAccountsByType(Constants.ACCOUNT_TYPE);
	    	Log.e(TAG, "GomeAccountClient removeLatestGomeAccount()  accounts.length:"+(accounts ==null ?"null":accounts.length));
	    	for(int i=0;i<accounts.length;i++){
	    		Account account =  accounts[i];
	    		latestAccount = account;
	    		Log.e(TAG, "GomeAccountClient removeLatestGomeAccount()  i:"+i+" account.name:"+account.name+" account.type:"+account.type);
	    	}
	    	return latestAccount;
	    }
	    
	    /** 
	     * 判断某个界面是否在前台 
	     * 
	     * @param context   Context 
	     * @param className 界面的类名 
	     * @return 是否在前台显示 
	     */  
	    public static boolean isForeground(Context context, String className) {  
	        if (context == null || TextUtils.isEmpty(className))  
	            return false;  
	        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
	        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);  
	        if (list != null && list.size() > 0) {  
	            ComponentName cpn = list.get(0).topActivity;  
	            if (className.equals(cpn.getClassName()))  
	                return true;  
	        }  
	        return false;  
	    }
}

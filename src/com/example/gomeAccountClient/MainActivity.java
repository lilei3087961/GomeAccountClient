package com.example.gomeAccountClient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;

import com.gome.gometestyuv.TestManager;




//import com.example.test_gome_account.R;

import android.Manifest;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Matrix;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.accounts.IAccountAuthenticator;
import android.accounts.IAccountAuthenticatorResponse;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
//import android.telephony.SmsMessage;

public class MainActivity extends Activity{


	Button btnLogin;
	Button btnAccountInfo;
	TextView txtInfo;
	boolean mServiceConnected = false;
	//Hashtable<String,String> mTableAccountInfo = new Hashtable<String, String>();
	HashMap<String,String> mTableAccountInfo = new HashMap<String,String>();
	public static final String SERIALIZE_NAME = "accountInfo";
	Handler mHandler = new Handler();
	private static final String TAG = "gomeaccountservice_lilei_MainActivity";
	public static String ACTION_LOGIN_IN = "com.gome.gomeaccountservice.ACTION_GOME_ACCOUNT_LOGIN";   //发送登录 成功广播action
	public static String ACTION_UPDATE_INFO = "com.gome.gomeaccountservice.ACTION_GOME_ACCOUNT_UPDATE_INFO";   //发送账号数据更新广播
	public static String ACTION_LOGIN_OUT = "com.gome.gomeaccountservice.ACTION_GOME_ACCOUNT_LOGOUT"; //发送退出登录广播action
	public static final String ACTION_BOOT_WIZARD = "bootWizard";
	public static final String USER_CENTER_MODE= "userCenter";
    public static final String USER_CENTER_LOGIN_FEEDBACK_RESERVATION = "com.gome.usercenter.activity.ReservationActivity";
    public static final String USER_CENTER_LOGIN_FEEDBACK_TO_SERVER = "com.gome.usercenter.activity.FeedbackToServerActivity";
    public static final String KEY_START_MODE_LOGIN_FEEDBACK = "startModeLoginFeedback";  //账号启动模式登录跳转的界面
    public static final String NORMAL_MODE = "nomal";     //普通模式
    
    
	static IAccountAuthenticator mAuthenticator = null;
	static AccountAuthenticatorResponse response = null;
    //activity请求的key值
    public static final int REQUEST_CODE_LOGIN = 1;
    public static final int REQUEST_CODE_LOGIN_OUT = 2;
    public static final int REQUEST_CODE_RESET_PWD = 3;
    public static final int REQUEST_CODE_REGISTER = 4;
    public static final int REQUEST_CODE_LOGIN_INFO = 5;
    public static final int REQUEST_CODE_WEIBO_LOGIN = 6;
    
    static String mServerResCode = null;
    String mServerToken = null; //登录后服务器返回的token
	String mNickName = null;
	String mMallAddress = null;
	String mPhoneNo = null;
	String mGomeId = null;
	String mRegisterType = null;
	String mLoginPwd = null;
    String mLocalAvatarPath = null;
	String mSex = null;
	String mUserLevel = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate() ###");
		Matrix m = new Matrix();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		registerBradcast();
		//startGomeService();
		bindAuthenService(); //进入应用先绑定服务
		//testUserPermissionReques();
		initView();
	}
	void registerBradcast(){
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_LOGIN_IN);
		intentFilter.addAction(ACTION_UPDATE_INFO);
		intentFilter.addAction(ACTION_LOGIN_OUT);
		//registerReceiver(mBroadcastReceiver, intentFilter);
		getApplicationContext().registerReceiver(mBroadcastReceiver, intentFilter);
	}
	void unregisterBradcast(){
		getApplicationContext().unregisterReceiver(mBroadcastReceiver);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume()");
		super.onResume();
		if(null == mAuthenticator){
			bindAuthenService();
		}else{
			//doAuthFromService(Constants.KEY_AUTH_TOKEN_TYPE_GET_LOGIN_STATE);
			doAuthFromService(Constants.KEY_AUTH_TOKEN_TYPE_GET_LOCAL_LOGIN_STATE);
		}
		//ActivityUtils.checkNetwork(MainActivity.this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG,"onDestroy() ** mServiceConnected:"+mServiceConnected);
		super.onDestroy();
		unregisterBradcast();
		if(mServiceConnected && null != conn){
			//unbindService(conn);
			getApplication().unbindService(conn);
			mServiceConnected = false;
		}
		try{
			mAuthenticator.finishSession(response,"com.gome.gomeaccountservice",null);
		}catch(RemoteException ex){
			Log.i(TAG, "onDestroy() ex:"+ex.toString());
			ex.printStackTrace();
		}
	}
	void initView(){
		txtInfo = (TextView)findViewById(R.id.txtInfo);
		txtInfo.setText(getResources().getString(R.string.msg_login_out));

		btnLogin = (Button)findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "onClick 111 do~~ mAuthenticator:"+mAuthenticator);
				
				ActivityUtils.removeLatestGomeAccount(MainActivity.this);
				//startGomeService();
//				if(!ActivityUtils.checkNetwork(MainActivity.this)){
//					return;
//				}

				
				doAuthFromService(Constants.KEY_AUTH_TOKEN_TYPE_LOGIN);
			}
		});

		btnAccountInfo = (Button)findViewById(R.id.btn_account_info);
		btnAccountInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				int k = new TestManager().add(1, 2);
//				AccountManager accountManager= AccountManager.get(MainActivity.this);
//		        final Account account = new Account("lilei", Constants.ACCOUNT_TYPE);
//		        boolean result = accountManager.addAccountExplicitly(account,"test",null); //本地account密码存储服务端token
				Log.d(TAG, "btnAccountInfo.onClick ## test getLatestGomeAccount" );
				getLatestGomeAccount(MainActivity.this);
				if(true)
					return;
				
				doAuthFromService(Constants.KEY_AUTH_TOKEN_TYPE_LOGIN_INFO);
				//doAuthFromService(Constants.KEY_AUTH_TOKEN_TYPE_VERIFY_PWD);
			}
		});
	}
	/////////
    public static Account getLatestGomeAccount(Context context){
    	Account latestAccount = null;
    	final AccountManager am = AccountManager.get(context); 
    	Account [] accounts = am.getAccountsByType(Constants.ACCOUNT_TYPE);
    	Log.e(TAG, "getLatestGomeAccount()  accounts.length:"+(accounts ==null ?"null":accounts.length));
    	for(int i=0;i<accounts.length;i++){
    		Account account =  accounts[i];
    		latestAccount = account;
    		Log.e(TAG, "getLatestGomeAccount()  i:"+i+" account.name:"+account.name+" account.type:"+account.type);
    	}
    	return latestAccount;
    }
	void test(){
		ProgressDialog dialog = new ProgressDialog(this,com.gome.R.style.Theme_GOME_Light_Dialog);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("AAA");
		dialog.show();
	}
	//test begin by lilei
	private void bindAuthenService(){
		Log.e(TAG, "bindAuthenService() 111 mAuthenticator:"+mAuthenticator);
		if(null == mAuthenticator){
			Log.e(TAG, "bindAuthenService 222");
			Intent intent = new Intent();  
			intent.setAction("android.accounts.AccountAuthenticator"); 
			intent.setPackage("com.gome.gomeaccountservice");
			//mServiceConnected = bindService(intent, conn, Service.BIND_AUTO_CREATE);
			mServiceConnected = getApplicationContext().bindService(intent, conn, Service.BIND_AUTO_CREATE);
		}
	}
	/***
	 * 通过认证类型向账号service端执行相关账号操作请求
	 * @param authTokenType
	 */
	private void doAuthFromService(String authTokenType){
		if(null == mAuthenticator){
			Log.i(TAG, "doAuthFromServer 111 null == mAuthenticator do bindAuthenService()");
			bindAuthenService();
		}
		Log.i(TAG, "doAuthFromServer 222 mAuthenticator:"+mAuthenticator);
		if(null != authTokenType && null != mAuthenticator){
			try {
				String pwd = "1234567";
				Log.i(TAG, "doAuthFromServer 333 mServerToken:"+mServerToken+" pwd:"+pwd);
	            response = new AccountAuthenticatorResponse();
	            Bundle bundle = new Bundle();
	            bundle.putString(Constants.KEY_SERVER_TOKEN, mServerToken);
	            bundle.putString(Constants.KEY_ACCOUNT_PWD, pwd);
	            mAuthenticator.getAuthToken(response, null,authTokenType, bundle);
	            Log.v(TAG,"doAuthFromServer 444 end"); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.v(TAG,"doAuthFromServer 555 error:"+e.toString()); 
				e.printStackTrace();
			}
		}
	}
    private ServiceConnection conn = new ServiceConnection() {  
		  
        @Override  
        public void onServiceConnected(ComponentName name, IBinder service) {  
            //isBound = true;  
            //IBinder binder = (IBinder) service;  
            //bindService = binder.getService();  
            //int num = bindService.getRandomNumber();  
        	Log.e(TAG,"conn b:"+service); 
        	mAuthenticator = IAccountAuthenticator.Stub.asInterface(service);
        	Log.v(TAG,"conn mAuthenticator:"+mAuthenticator); 
        	//doAuthFromService(Constants.KEY_AUTH_TOKEN_TYPE_GET_LOGIN_STATE);
        	doAuthFromService(Constants.KEY_AUTH_TOKEN_TYPE_GET_LOCAL_LOGIN_STATE);
        }
        //client 和service连接意外丢失时，会调用该方法  
        @Override  
        public void onServiceDisconnected(ComponentName name) {  
            Log.e(TAG,"onServiceDisconnected  A");  
            mAuthenticator = null;
        }  
    };
    private class AccountAuthenticatorResponse extends IAccountAuthenticatorResponse.Stub{
    	public AccountAuthenticatorResponse(){
    		Log.e(TAG, "AccountAuthenticatorResponse.AccountAuthenticatorResponse()");
    	}
    	@Override
    	public void onResult(Bundle result) {
    		String authToken = result.getString(AccountManager.KEY_AUTHTOKEN);
    		String authtokenType = result.getString(Constants.PARAM_AUTHTOKEN_TYPE);
			String accountName = result.getString(AccountManager.KEY_ACCOUNT_NAME);//AccountManager中存储的用户名一般何Constants.KEY_ACCOUNT_NAME中一样
			String accountType = result.getString(AccountManager.KEY_ACCOUNT_TYPE);
    		Intent intent = (Intent)result.getParcelable(AccountManager.KEY_INTENT);
    		
    		Log.e(TAG, "AccountAuthenticatorResponse.onResult 000 authtokenType:"+authtokenType+" authToken:"+authToken+" intent:"+intent
    				+" accountName:"+accountName+" accountType:"+accountType);
    		if(null != authtokenType){
    			//获取登录状态
	    		if(authtokenType.equals(Constants.KEY_AUTH_TOKEN_TYPE_GET_LOGIN_STATE)
	    				|| authtokenType.equals(Constants.KEY_AUTH_TOKEN_TYPE_GET_LOCAL_LOGIN_STATE)){
	    			Log.i(TAG, "AccountAuthenticatorResponse.onResult   KEY_AUTH_TOKEN_TYPE_GET_LOGIN_STATE or KEY_AUTH_TOKEN_TYPE_GET_LOCAL_LOGIN_STATE 111");
	    			if(null != authToken && authToken.equals(Constants.KEY_SERVER_AUTHEN_SUCCESS)){  //已经登录
	    				mServerToken = result.getString(Constants.KEY_SERVER_TOKEN);
	    				mNickName = result.getString(Constants.KEY_ACCOUNT_NAME);
	        			mMallAddress = result.getString(Constants.KEY_ACCOUNT_EMAIL);  
	        			mPhoneNo = result.getString(Constants.KEY_ACCOUNT_PHONE_NUMBER);
	        			mGomeId = result.getString(Constants.KEY_ACCOUNT_GOME_ID);
	        			mRegisterType = result.getString(Constants.KEY_ACCOUNT_REGISTER_TYPE);
	        			mSex = result.getString(Constants.KEY_ACCOUNT_SEX);
	        			mLocalAvatarPath = result.getString(Constants.KEY_LOCAL_AVATAR_PATH);
	    				Log.i(TAG, "AccountAuthenticatorResponse.onResult KEY_AUTH_TOKEN_TYPE_GET_LOGIN_STATE 222 already login" +
	    					" mServerToken:"+mServerToken+" mNickName:"+mNickName+" mMallAddress:"+mMallAddress+" mPhoneNo:"
	    					+mPhoneNo+" mGomeId:"+mGomeId+" mRegisterType:"+mRegisterType+" mSex:"+mSex+" mLocalAvatarPath:"+mLocalAvatarPath);
	    				showLoginIn(accountName,accountType);
	    				
	    				mTableAccountInfo.clear();  //更新账号详细信息到本地变量，
	    				mTableAccountInfo.put(Constants.KEY_ACCOUNT_NAME, mNickName);
	    				mTableAccountInfo.put(Constants.KEY_ACCOUNT_EMAIL, mMallAddress);
	    				mTableAccountInfo.put(Constants.KEY_ACCOUNT_PHONE_NUMBER, mPhoneNo);
	    				mTableAccountInfo.put(Constants.KEY_ACCOUNT_GOME_ID, mGomeId);
	    				mTableAccountInfo.put(Constants.KEY_ACCOUNT_REGISTER_TYPE, mRegisterType);
	    				mTableAccountInfo.put(Constants.KEY_ACCOUNT_SEX, mSex);
	    				mTableAccountInfo.put(Constants.KEY_LOCAL_AVATAR_PATH, mLocalAvatarPath);
	    			}else{
	    				mServerResCode = result.getString(Constants.KEY_SERVER_RESULT_CODE);
	    				if(Constants.SERVER_TIMEOUT_RESULT_CODE.equals(mServerResCode)){ //判断是否网络获取服务器请求超时登录状态不对
	    					Log.i(TAG, "AccountAuthenticatorResponse.onResult KEY_AUTH_TOKEN_TYPE_GET_LOGIN_STATE 333 not login SERVER_TIMEOUT_RESULT_CODE");
	    				}
	    				Log.i(TAG, "AccountAuthenticatorResponse.onResult KEY_AUTH_TOKEN_TYPE_GET_LOGIN_STATE 333 not login" +
	    						" mServerResCode:"+mServerResCode);
	    				showLoginOut();
	    			}
	    			
	    		}else if(authtokenType.equals(Constants.KEY_AUTH_TOKEN_TYPE_LOGIN)){ //登录请求，返回登录界面的intent(需先判断登录状态，若未登录则调用登录请求)
	    			Log.i(TAG, "AccountAuthenticatorResponse.onResult  KEY_AUTH_TOKEN_TYPE_LOGIN 11");
    				if(null != intent){
    					//intent.setAction(ACTION_BOOT_WIZARD); //开机向导模式
//    					intent.setAction(USER_CENTER_MODE); //设置模式
//    					intent.putExtra(KEY_START_MODE_LOGIN_FEEDBACK, USER_CENTER_LOGIN_FEEDBACK_RESERVATION); //设置登录反馈界面
    					//startActivityForResult(intent,REQUEST_CODE_LOGIN);
    					startActivity(intent);
    				}
	    		}else if(authtokenType.equals(Constants.KEY_AUTH_TOKEN_TYPE_LOGIN_INFO)){//账号详情，返回账号界面的intent(需先判断登录状态，若已登录则调用账号详情请求)
	    			Log.i(TAG, "AccountAuthenticatorResponse.onResult  KEY_AUTH_TOKEN_TYPE_LOGIN_INFO ");
    				if(null != intent){
    					//startActivityForResult(intent,REQUEST_CODE_LOGIN_INFO);
    					startActivity(intent);
    				}
	    		}else if(authtokenType.equals(Constants.KEY_AUTH_TOKEN_TYPE_VERIFY_PWD)){ //验证密码
	    			boolean verifyResult = result.getBoolean(Constants.KEY_VERIFY_PWD_RESULT);
	    			Log.i(TAG, "AccountAuthenticatorResponse.onResult  KEY_AUTH_TOKEN_TYPE_VERIFY_PWD  verifyResult:"+verifyResult);
	    		}
    		}

        }
    	@Override
    	public void onRequestContinued(){
    		Log.e(TAG, "AccountAuthenticatorResponse.onRequestContinued");
    	}
    	@Override
    	public void onError(int errorCode, String errorMessage){
    		Log.e(TAG, "AccountAuthenticatorResponse.onError errorCode:"+errorCode+" errorMessage:"+errorMessage);
        }

    }
    void showLoginIn(String accountName,String accountType){
    	String msgFormat = getResources().getString(R.string.msg_login_success);
		final String msg =  String.format(msgFormat,accountName,accountType);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				txtInfo.setText(msg);

				btnAccountInfo.setVisibility(View.VISIBLE);
				btnLogin.setVisibility(View.INVISIBLE);//登录后不可再点登录
			}
		});
    }
    void showLoginOut(){
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
    			txtInfo.setText(getResources().getString(R.string.msg_login_out));

				//btnLoginOut.setVisibility(View.INVISIBLE);
				btnAccountInfo.setVisibility(View.INVISIBLE);
				
				btnLogin.setVisibility(View.VISIBLE); //退出后可登录
				//btnWeiboLogin.setVisibility(View.VISIBLE); //退出后可登录
			}
		});
    }
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			mServerToken = intent.getStringExtra(Constants.KEY_SERVER_TOKEN);
			mGomeId = intent.getStringExtra(Constants.KEY_ACCOUNT_GOME_ID);
			mPhoneNo = intent.getStringExtra(Constants.KEY_ACCOUNT_PHONE_NUMBER);
			mMallAddress = intent.getStringExtra(Constants.KEY_ACCOUNT_EMAIL);
			mNickName = intent.getStringExtra(Constants.KEY_ACCOUNT_NAME);
			mLocalAvatarPath = intent.getStringExtra(Constants.KEY_LOCAL_AVATAR_PATH);
			Log.i(TAG, "mBroadcastReceiver.onReceive******* action:"+action+" mServerToken:"+mServerToken+" mGomeId:"+mGomeId
					+" mPhoneNo:"+mPhoneNo+" mMallAddress:"+mMallAddress+" mNickName:"+mNickName+" mLocalAvatarPath:"+mLocalAvatarPath);
			//doAuthFromService(Constants.KEY_AUTH_TOKEN_TYPE_GET_LOGIN_STATE);
		}
    	
    };
//test end
	

}

package com.example.gomeAccountClient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Hashtable;


//import com.example.test_gome_account.R;

public class RegisterResultActivity extends Activity {
    private static final String TAG = "lilei_RegisterResultActivity";
    private ImageView bgImg = null;
    private TextView textNickName = null;
    private TextView textEmailAddress = null;
    private TextView textPhoneNumber = null;
    private String mNickName = null;
    private String mEmailAddress = null;
    private String mPhoneNumber = null;
    private String mHeadPortraitPath = null;
    private String mGomeId = null;
    private Bitmap mHeadBg = null;
    private String mPassword = null;
    private Button ok_button = null;
    //String accountName = "lilei";
	//String accountPwd = "123";
    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_USERNUMBER = "phoneNum";
    private static final String PARAM_USEREMAIL = "emailAddress";
    private static final String PARAM_USERHEAD = "bgImg";
    private static final String PARAM_PASSWORD = "passwd";
    private BitmapDrawable mBitmapDrawable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerresult);
        final Intent intent = getIntent();
        getInfo();
        initView();
    }

    class RegisterClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.aokBtn:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
    
    
    public void getInfo() {
    	final Intent intent = getIntent();
    	if(intent != null){
    		
	    	HashMap<String,String> mTableAccountInfo = (HashMap<String,String>)intent.getExtras().getSerializable(MainActivity.SERIALIZE_NAME);
	    	mGomeId = mTableAccountInfo.get(Constants.KEY_ACCOUNT_GOME_ID);
		    mNickName = mTableAccountInfo.get(Constants.KEY_ACCOUNT_NAME);
		    mEmailAddress = mTableAccountInfo.get(Constants.KEY_ACCOUNT_EMAIL);
		    mPhoneNumber = mTableAccountInfo.get(Constants.KEY_ACCOUNT_PHONE_NUMBER);
		    mHeadPortraitPath = Constants.HEAD_PORTRAIT_DIR_PATH+mGomeId+Constants.HEAD_PORTRAIT_SUFFIX;
		    File imageFile = new File(mHeadPortraitPath);
		    
		    Log.i(TAG, "getInfo() mNickName:"+mNickName+" mEmailAddress:"+mEmailAddress+" mPhoneNumber:"+mPhoneNumber+" mHeadPortraitPath:"+mHeadPortraitPath);
		    FileInputStream fis = null;
		    if(imageFile.exists()){
				try {
					//fis = new FileInputStream(Constants.HEAD_PORTRAIT_DIR_PATH + nickName + Constants.HEAD_PORTRAIT_SUFFIX);
					fis = new FileInputStream(mHeadPortraitPath);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }else{
		    	Log.i(TAG, "getInfo() cannot find image path£º"+mHeadPortraitPath);
		    }

	        mHeadBg  = BitmapFactory.decodeStream(fis);
    	}
    }

    public void initView() {
        bgImg = (ImageView) findViewById(R.id.aheadPhoto);
        textNickName = (TextView) findViewById(R.id.anickName);
        textEmailAddress = (TextView) findViewById(R.id.aemailAddress);
        textPhoneNumber = (TextView) findViewById(R.id.aphoneNumber);
        ok_button = (Button) findViewById(R.id.aokBtn);
        ok_button.setOnClickListener(new RegisterClickListener());
        if(mHeadBg!=null) {
        	bgImg.setImageBitmap(mHeadBg);
        	//bgImg.setBackgroundDrawable(mBitmapDrawable); 
        }
        if(mNickName!=null) {
        	textNickName.setText(mNickName);
        }
        if(mEmailAddress!=null) {
        	textEmailAddress.setText(mEmailAddress);
        }
        if(mPhoneNumber!=null) {
        	textPhoneNumber.setText(mPhoneNumber);
        }
    }

}

package com.gmail.abanoubwagih.gp_project.Log_in_auth_and_Synchronous_Data;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    public void showProgressDialog(int value) {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(value));
        mProgressDialog.setIndeterminate(true);


        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {

            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

}

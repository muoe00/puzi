package com.puzi.puzi.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.puzi.puzi.R;

import static com.puzi.puzi.ui.base.BaseFragmentActivity.activities;

/**
 * 해당 액티비티는
 * FragmentActivity가 아닐경우, 푸시이벤트와 연관 없을 경우
 * 사용합니다.
 */
public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activities.add(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activities.remove(this);
	}

	protected Animation outToTop(){
		return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_top);
	}
	
	protected Animation inFromTop() {
		return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_top);
	}
	
	protected Animation outStatusImg(){
		return AnimationUtils.loadAnimation(getActivity(), R.anim.image_out);
	}
	
	protected Animation outAlpha(){
		return AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_out);
	}
	
	protected Animation inAlpha(){
		return AnimationUtils.loadAnimation(getActivity(), R.anim.alpha250);
	}
	
	protected void doAnimationGoBottom() {
		overridePendingTransition(R.anim.top_in, R.anim.none);
	}
	
	protected void doAnimationGoTop() {
		overridePendingTransition(R.anim.top_out, R.anim.none);
	}
	
	protected void doAnimationAlphaOut() {
		overridePendingTransition(R.anim.alpha_out, R.anim.alpha_out);
	}
	
	protected void doAnimationAlphaOut500() {
		overridePendingTransition(R.anim.alpha_out500, R.anim.alpha_out500);
	}
	
	protected void doAnimationAlpha() {
		overridePendingTransition(R.anim.alpha250, R.anim.alpha_out);
	}

	protected void doAnimationAlpha1000() {
		overridePendingTransition(0, R.anim.alpha_out1000);
	}

	protected void doAnimationGoLeft() {
		overridePendingTransition(R.anim.leftin, R.anim.rightout);
	}
	
	public void doAnimationGoRight() {
		overridePendingTransition(R.anim.rightin, R.anim.leftout);
	}
	
	protected void doAnimationComeRight() {
		overridePendingTransition(R.anim.rightin, R.anim.shrink_back);
	}
	
	protected void doAnimationBackLeft() {
		overridePendingTransition(R.anim.stretch_front, R.anim.rightout);
	}
	
	protected Activity getActivity(){
		return this;
	}
	
	protected void closeInputKeyboard(EditText editText) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		doAnimationGoLeft();
	}

}

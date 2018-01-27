package kr.puzi.puzi.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 해당 액티비티는
 * FragmentActivity가 아닐경우, 푸시이벤트와 연관 없을 경우
 * 사용합니다.
 */
public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BaseFragmentActivity.activities.add(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BaseFragmentActivity.activities.remove(this);
	}

	protected Animation outToTop(){
		return AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.slide_out_top);
	}
	
	protected Animation inFromTop() {
		return AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.slide_in_top);
	}
	
	protected Animation outStatusImg(){
		return AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.image_out);
	}
	
	protected Animation outAlpha(){
		return AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.alpha_out);
	}
	
	protected Animation inAlpha(){
		return AnimationUtils.loadAnimation(getActivity(), kr.puzi.puzi.R.anim.alpha250);
	}
	
	protected void doAnimationGoBottom() {
		overridePendingTransition(kr.puzi.puzi.R.anim.top_in, kr.puzi.puzi.R.anim.none);
	}
	
	protected void doAnimationGoTop() {
		overridePendingTransition(kr.puzi.puzi.R.anim.top_out, kr.puzi.puzi.R.anim.none);
	}
	
	protected void doAnimationAlphaOut() {
		overridePendingTransition(kr.puzi.puzi.R.anim.alpha_out, kr.puzi.puzi.R.anim.alpha_out);
	}
	
	protected void doAnimationAlphaOut500() {
		overridePendingTransition(kr.puzi.puzi.R.anim.alpha_out500, kr.puzi.puzi.R.anim.alpha_out500);
	}
	
	protected void doAnimationAlpha() {
		overridePendingTransition(kr.puzi.puzi.R.anim.alpha250, kr.puzi.puzi.R.anim.alpha_out);
	}

	protected void doAnimationAlpha1000() {
		overridePendingTransition(0, kr.puzi.puzi.R.anim.alpha_out1000);
	}

	protected void doAnimationGoLeft() {
		overridePendingTransition(kr.puzi.puzi.R.anim.leftin, kr.puzi.puzi.R.anim.rightout);
	}
	
	public void doAnimationGoRight() {
		overridePendingTransition(kr.puzi.puzi.R.anim.rightin, kr.puzi.puzi.R.anim.leftout);
	}
	
	protected void doAnimationComeRight() {
		overridePendingTransition(kr.puzi.puzi.R.anim.rightin, kr.puzi.puzi.R.anim.shrink_back);
	}
	
	protected void doAnimationBackLeft() {
		overridePendingTransition(kr.puzi.puzi.R.anim.stretch_front, kr.puzi.puzi.R.anim.rightout);
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

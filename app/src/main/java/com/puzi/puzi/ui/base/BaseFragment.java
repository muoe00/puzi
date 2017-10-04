package com.puzi.puzi.ui.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.puzi.puzi.R;

public class BaseFragment extends Fragment {
	
	protected View v;
	
	protected void setView(View v){
		this.v = v;
	}	
	
	protected void doAnimationAlpha() {
		getActivity().overridePendingTransition(R.anim.alpha250, R.anim.alpha250);
	}
	
	protected void doAnimationGoLeft() {
		getActivity().overridePendingTransition(R.anim.leftin, R.anim.rightout);
	}
	
	protected void doAnimationGoRight() {
		getActivity().overridePendingTransition(R.anim.rightin, R.anim.leftout);
	}
	
	protected void doAnimationComeRight() {
		getActivity().overridePendingTransition(R.anim.rightin, R.anim.shrink_back);
	}
	
	protected void doAnimationBackLeft() {
		getActivity().overridePendingTransition(R.anim.stretch_front, R.anim.rightout);
	}
	
	protected void closeInputKeyboard(EditText editText) {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	protected void loadInputKeyboard(EditText editText) {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, editText.length());
	}
}

package kr.puzi.puzi.ui.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class BaseFragment extends Fragment {

	protected View v;

	protected void setView(View v){
		this.v = v;
	}	
	
	protected void doAnimationAlpha() {
		getActivity().overridePendingTransition(kr.puzi.puzi.R.anim.alpha250, kr.puzi.puzi.R.anim.alpha250);
	}
	
	protected void doAnimationGoLeft() {
		getActivity().overridePendingTransition(kr.puzi.puzi.R.anim.leftin, kr.puzi.puzi.R.anim.rightout);
	}
	
	protected void doAnimationGoRight() {
		getActivity().overridePendingTransition(kr.puzi.puzi.R.anim.rightin, kr.puzi.puzi.R.anim.leftout);
	}
	
	protected void doAnimationComeRight() {
		getActivity().overridePendingTransition(kr.puzi.puzi.R.anim.rightin, kr.puzi.puzi.R.anim.shrink_back);
	}
	
	protected void doAnimationBackLeft() {
		getActivity().overridePendingTransition(kr.puzi.puzi.R.anim.stretch_front, kr.puzi.puzi.R.anim.rightout);
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

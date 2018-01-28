package kr.puzi.puzi.ui.common;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import kr.puzi.puzi.R;
import kr.puzi.puzi.ui.customview.NotoTextView;

/**
 * Created by juhyun on 2018. 1. 23..
 */

public class PointDialog {
    private static Dialog dlg = null;

    public static void load(final Activity activity, final int point, final boolean saved) {
        if (dlg != null) {
            dlg.dismiss();
            dlg = null;
        }

        if (dlg == null) {
            dlg = new Dialog(activity, R.style.FullScreenTheme);
            dlg.setContentView(R.layout.view_point_save);

            RelativeLayout rl = (RelativeLayout) dlg.findViewById(R.id.rl_view_point);
            ImageView iv = (ImageView) dlg.findViewById(R.id.iv_view_point);
            // inal ImageView ivS = (ImageView) dlg.findViewById(R.id.iv_view_point_second);
            NotoTextView tv = (NotoTextView) dlg.findViewById(R.id.tv_view_point);
            NotoTextView tvPlus = (NotoTextView) dlg.findViewById(R.id.tv_view_point_plus);
            NotoTextView tvP = (NotoTextView) dlg.findViewById(R.id.tv_view_point_p);

            // ivS.setVisibility(View.INVISIBLE);

            Animation animAlpha = AnimationUtils.loadAnimation(activity, R.anim.anim_point_alpha);
            Animation animScale = AnimationUtils.loadAnimation(activity, R.anim.anim_point_scale);

            rl.startAnimation(animAlpha);
            iv.startAnimation(animScale);

            if(!saved) {
             tvP.setVisibility(View.GONE);
             tvPlus.setVisibility(View.GONE);

             tv.setText("" + point +  " / 10");
            } else {
                tv.setText("" + point);
            }

            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    close();
                }
            }, 1250);

        }
        dlg.show();
    }

    private static void close() {
        if (dlg != null) {
            dlg.dismiss();
            dlg = null;
        }
    }
}

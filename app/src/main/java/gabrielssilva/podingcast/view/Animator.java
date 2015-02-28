package gabrielssilva.podingcast.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Animator {

    private Point displaySize;

    public Animator() {
        //Display display = windowManager.getDefaultDisplay();
        this.displaySize = new Point();
        //display.getSize(this.displaySize);
    }

    public void fadeListOut(ListView listView, AnimatorListener listener, int itemIndex) {
        genericFade(listView, listener, itemIndex, 1.0f, 0.0f);
    }

    public void fadeListIn(ListView listView, AnimatorListener listener, int itemIndex) {
        genericFade(listView, listener, itemIndex, 0.0f, 1.0f);
    }


    private void genericFade(ListView listView, AnimatorListener listener, int itemIndex,
                             float initialAlpha, float finalAlpha) {
        List<android.animation.Animator> animations = new ArrayList<>();
        int animationOffset = 100;

        for (int i=0; i<listView.getChildCount(); i++) {
            View child = listView.getChildAt(i);
            child.setAlpha(initialAlpha);

            ObjectAnimator waitAnimation = ObjectAnimator.ofFloat(child, "alpha", initialAlpha);
            // Get the child position relative to the focus item
            int childIndex = Math.abs(itemIndex - i);
            waitAnimation.setDuration(childIndex * animationOffset);

            ObjectAnimator fadeAnimation = ObjectAnimator.ofFloat(child, "alpha", finalAlpha);
            fadeAnimation.setDuration(300);
            fadeAnimation.setInterpolator(new AccelerateInterpolator());

            AnimatorSet subAnimatorSet = new AnimatorSet();
            subAnimatorSet.playSequentially(waitAnimation, fadeAnimation);
            animations.add(subAnimatorSet);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animations);
        if (listener != null) {
            animatorSet.addListener(listener);
        }

        animatorSet.start();
    }
}
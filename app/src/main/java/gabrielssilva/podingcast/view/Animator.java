package gabrielssilva.podingcast.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.List;


public class Animator {

    public void fadeListOut(ViewGroup parentView, AnimatorListener listener, int itemIndex) {
        genericFade(parentView, listener, itemIndex, 1.0f, 0.0f);
    }

    public void fadeListIn(ViewGroup parentView, AnimatorListener listener, int itemIndex) {
        genericFade(parentView, listener, itemIndex, 0.0f, 1.0f);
    }


    private void genericFade(ViewGroup parentView, AnimatorListener listener, int itemIndex,
                             float initialAlpha, float finalAlpha) {
        List<android.animation.Animator> animations = new ArrayList<>();
        int animationOffset = 100;

        for (int i=0; i<parentView.getChildCount(); i++) {
            View childView = parentView.getChildAt(i);
            childView.setAlpha(initialAlpha);

            ObjectAnimator waitAnimation = ObjectAnimator.ofFloat(childView, "alpha", initialAlpha);
            // Get the child position relative to the focus item
            int childIndex = Math.abs(itemIndex - i);
            waitAnimation.setDuration(childIndex * animationOffset);

            ObjectAnimator fadeAnimation = ObjectAnimator.ofFloat(childView, "alpha", finalAlpha);
            fadeAnimation.setDuration(150);
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
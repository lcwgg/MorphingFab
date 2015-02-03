package fab.lcwgg.com.morphingfab;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        private boolean hasMorphedButton1 = false;
        private boolean hasMorphedButton2 = false;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final ImageButton fab1 = (ImageButton) rootView.findViewById(R.id.fab_button1);
            final ImageButton fab2 = (ImageButton) rootView.findViewById(R.id.fab_button2);

            fab1.setOnClickListener(this);
            fab2.setOnClickListener(this);

            return rootView;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.fab_button1) {
                if (!hasMorphedButton1) {
                    hasMorphedButton1 = true;
                    // arbitrary values for Nexus 5
                    getCornerAnimation(v.getBackground(), 100, 0, 800).start();
                } else {
                    hasMorphedButton1 = false;
                    // arbitrary values for Nexus 5
                    getCornerAnimation(v.getBackground(), 0, 100, 800).start();
                }
            } else if (v.getId() == R.id.fab_button2) {
                if (!hasMorphedButton2) {
                    hasMorphedButton2 = true;
                    // arbitrary values for Nexus 5
                    startWidthAnimation(v, 400, 1000);
                } else {
                    hasMorphedButton2 = false;
                    final int fabSize = getResources().getDimensionPixelSize(R.dimen.round_button_diameter);
                    startWidthAnimation(v, fabSize, 1000);
                }
            }
        }

        private ObjectAnimator getCornerAnimation(final Drawable background, final float fromCorner, final float toCorner, final int duration) {
            final ObjectAnimator cornerAnimation =
                    ObjectAnimator.ofFloat(background, "cornerRadius", fromCorner, toCorner);
            cornerAnimation.setDuration(duration);
            return cornerAnimation;
        }

        private void startWidthAnimation(final View view, final int toWidth, final int duration){
            final AnimatorSet set = new AnimatorSet();
            final ObjectAnimator cornerAnimation;
            final int fromWidth = view.getWidth();
            final ValueAnimator widthAnimation = ValueAnimator.ofInt(fromWidth, toWidth);

            widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    int leftOffset;
                    int rightOffset;

                    if (fromWidth > toWidth) {
                        leftOffset = (fromWidth - value) / 2;
                        rightOffset = fromWidth - leftOffset;
                    } else {
                        leftOffset = (toWidth - value) / 2;
                        rightOffset = toWidth - leftOffset;
                    }

                    view.getBackground()
                            .setBounds(leftOffset, 0, rightOffset, view.getHeight());
                    view.getLayoutParams().width = value;
                    view.requestLayout();
                }
            });
            widthAnimation.setDuration(duration);

            if (fromWidth > toWidth) {
                cornerAnimation = getCornerAnimation(view.getBackground(), 0, 60, duration);
            } else {
                cornerAnimation = getCornerAnimation(view.getBackground(), 60, 0, duration);
            }
            set.play(widthAnimation).with(cornerAnimation);
            set.start();
        }
    }
}

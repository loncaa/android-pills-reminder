package hr.magicpot.projectpliva.custome;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by xxx on 26.4.2016..
 */
public class FadeTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {
        page.setTranslationX(page.getWidth() * - position);

        if(position <= -1.0F || position >= 1.0F) { //ako je stranica previse lijevo ili previse desno
            page.setAlpha(0.0F);
        } else if( position == 0.0F ) {
            page.setAlpha(1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            page.setAlpha(1.0F - Math.abs(position));
        }
    }
}

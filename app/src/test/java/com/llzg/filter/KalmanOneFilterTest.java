package com.llzg.filter;

import org.junit.Test;

/**
 * Created by xingwx on 17-2-28.
 */

public class KalmanOneFilterTest {

    KalmanOneFilter kf = new KalmanOneFilter();
    KalmanOneFilter kf2 = new KalmanOneFilter();

    @Test
    public void testKalmanOne(){
        kf.setAngle(0.1F);
        kf2.setAngle(0.1F);

        float count =0;
        float merror=0;
        float ferror=0;

        for(int i=0; i<200; i++){

            float truth = i+5;

            float in = (float)(i+Math.random()*10);

            count += truth;
            merror += Math.abs(truth-in);

            float a  = kf.getAngle(in, 1f, 1.0F);

            ferror += Math.abs(truth-a);

            System.out.println("the truth="+(truth)+" meatured = "+in
                    +"  filtered= "+a + " merror="+(merror/(i+1))+" ferror="+(ferror)/(i+1));
        }

    }
}

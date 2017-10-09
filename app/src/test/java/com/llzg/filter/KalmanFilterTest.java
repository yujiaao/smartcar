package com.llzg.filter;

import com.llzg.jama.MatrixUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Jama.Matrix;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class KalmanFilterTest {

    private KalmanFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new KalmanFilter(2, 1);

        /* The train state is a 2d vector containing position and velocity.
         Velocity is measured in position units per timestep units. */
        filter.setStateTransition(1.0, 1.0, 0.0, 1.0);

         /* We only observe position */
        filter.setObservationModel(1.0, 0.0);

        /* The covariance matrices are blind guesses */
       // filter.getProcessNoiseCovariance().setIdentityMatrix();
        MatrixUtils.setIdentityMatrix(filter.getProcessNoiseCovariance());
       // filter.getObservationNoiseCovariance().setIdentityMatrix();
        MatrixUtils.setIdentityMatrix(filter.getObservationNoiseCovariance());

        double deviation = 1000.0;
        filter.setStateEstimate(10.0*deviation, 1000.00);
      //  filter.getEstimateCovariance().setIdentityMatrix().scaleMatrix(deviation*deviation);
        Matrix res = MatrixUtils.setIdentityMatrix(filter.getEstimateCovariance());
        MatrixUtils.scaleMatrix(res, deviation*deviation);
    }

    @After
    public void tearDown() throws Exception {
        filter = null;
    }

    @Test
    public void testTrain() {

        for (int i = 0; i < 10; ++i) {
            filter.setObservation((double) i);
            filter.update();
        }

//        Debug
        System.out.println("Estimated position: " + filter.getStateEstimate().get(0,0));
        System.out.println("Estimated velocity: " + filter.getStateEstimate().get(1,0));

        assertThat(filter.getStateEstimate().get(0, 0), is( not(0.0) ));
        assertThat(filter.getStateEstimate().get(1, 0), is( not(0.0) ));
        assertThat(filter.getStateEstimate().get(0, 0) - 0.0005, is( lessThan(9.0) ));
        assertThat(filter.getStateEstimate().get(1,0) - 0.0005, is( lessThan(1.0) ));
    }


    @Test
    public void testTrainRandom() {

        for (int i = 0; i < 10; ++i) {
            filter.setObservation((double) i+(0.5-Math.random()));
            filter.update();
        }

//        Debug
        System.out.println("Estimated position: " + filter.getStateEstimate().get(0,0));
        System.out.println("Estimated velocity: " + filter.getStateEstimate().get(1,0));

        assertThat(filter.getStateEstimate().get(0, 0), is( not(0.0) ));
        assertThat(filter.getStateEstimate().get(1, 0), is( not(0.0) ));
        assertThat(filter.getStateEstimate().get(0, 0) - 0.0005, is( lessThan(9.0+0.2) ));
        assertThat(filter.getStateEstimate().get(1,0) - 0.0005, is( lessThan(1.0+0.2) ));
    }
}
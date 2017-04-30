package io.apptik.widget;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class MultiSliderThumbPushTest {

    private MultiSlider slider;
    private MultiSlider.Thumb thumb0, thumb1, thumb2;
    private MultiSlider.OnThumbValueChangeListener listener;
    private InOrder listenerInOrder;

    @Before
    public void setup() {

        slider = new MultiSlider(InstrumentationRegistry.getTargetContext());

        slider.removeThumb(0);
        slider.removeThumb(0);

        slider.setMin(0);
        slider.setMax(20);
        slider.setStep(1);
        slider.setStepsThumbsApart(2);

        thumb0 = slider.addThumb(2);
        thumb1 = slider.addThumb(6);
        thumb2 = slider.addThumb(12);

        listener = mock(MultiSlider.OnThumbValueChangeListener.class);
        listenerInOrder = inOrder(listener);
        slider.setOnThumbValueChangeListener(listener);
    }

    private void assertThumbValues(int expectedThumb0, int expectedThumb1, int expectedThumb2) {
        assertEquals(
                asList(expectedThumb0, expectedThumb1, expectedThumb2),
                asList(thumb0.getValue(), thumb1.getValue(), thumb2.getValue()));
    }

    @Test
    public void testThumbMovedBetweenSelfAndRightNeighbour() {

        thumb1.setValue(8);

        assertThumbValues(2, 8, 12);

        listenerInOrder.verify(listener).onValueChanged(slider, thumb1, 1, 8);
        listenerInOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThumbMovedBetweenSelfAndLeftNeighbour() {

        thumb2.setValue(10);

        assertThumbValues(2, 6, 10);

        listenerInOrder.verify(listener).onValueChanged(slider, thumb2, 2, 10);
        listenerInOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThumbMovedBetweenSelfAndRightNeighbourWithPush() {

        thumb1.setValue(8, true);

        assertThumbValues(2, 8, 12);

        listenerInOrder.verify(listener).onValueChanged(slider, thumb1, 1, 8);
        listenerInOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThumbMovedBetweenSelfAndLeftNeighbourWithPush() {

        thumb2.setValue(10, true);

        assertThumbValues(2, 6, 10);

        listenerInOrder.verify(listener).onValueChanged(slider, thumb2, 2, 10);
        listenerInOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThumbMovedPastRightNeighbour() {

        thumb1.setValue(14);

        assertThumbValues(2, 10, 12);

        listenerInOrder.verify(listener).onValueChanged(slider, thumb1, 1, 10);
        listenerInOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThumbMovedPastLeftNeighbour() {

        thumb1.setValue(0);

        assertThumbValues(2, 4, 12);

        listenerInOrder.verify(listener).onValueChanged(slider, thumb1, 1, 4);
        listenerInOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThumbMovedPastRightNeighbourWithPush() {

        thumb1.setValue(14, true);

        assertThumbValues(2, 14, 16);

        listenerInOrder.verify(listener).onValueChanged(slider, thumb2, 2, 16);
        listenerInOrder.verify(listener).onValueChanged(slider, thumb1, 1, 14);
        listenerInOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThumbMovedPastLeftNeighbourWithPush() {

        thumb1.setValue(0, true);

        assertThumbValues(0, 2, 12);

        listenerInOrder.verify(listener).onValueChanged(slider, thumb0, 0, 0);
        listenerInOrder.verify(listener).onValueChanged(slider, thumb1, 1, 2);
        listenerInOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThumbMovedPastBothRightNeighbours() {

        thumb0.setValue(14);

        assertThumbValues(4, 6, 12);

        listenerInOrder.verify(listener).onValueChanged(slider, thumb0, 0, 4);
        listenerInOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThumbMovedPastBothLeftNeighbours() {

        thumb2.setValue(0);

        assertThumbValues(2, 6, 8);

        listenerInOrder.verify(listener).onValueChanged(slider, thumb2, 2, 8);
        listenerInOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThumbMovedPastBothRightNeighboursWithPush() {

        thumb0.setValue(14, true);

        assertThumbValues(14, 16, 18);

        listenerInOrder.verify(listener).onValueChanged(slider, thumb2, 2, 18);
        listenerInOrder.verify(listener).onValueChanged(slider, thumb1, 1, 16);
        listenerInOrder.verify(listener).onValueChanged(slider, thumb0, 0, 14);
        listenerInOrder.verifyNoMoreInteractions();
    }

    @Test
    public void testThumbMovedPastBothLeftNeighboursWithPush() {

        thumb2.setValue(0, true);

        assertThumbValues(0, 2, 4);

        listenerInOrder.verify(listener).onValueChanged(slider, thumb0, 0, 0);
        listenerInOrder.verify(listener).onValueChanged(slider, thumb1, 1, 2);
        listenerInOrder.verify(listener).onValueChanged(slider, thumb2, 2, 4);
        listenerInOrder.verifyNoMoreInteractions();
    }
}
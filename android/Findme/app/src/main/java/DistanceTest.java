import com.acid.findme.Distance;

import org.junit.Test;

import static org.junit.Assert.*;

public class DistanceTest {

    @Test
    public void testFormatMeter() {
       String s = Distance.format(0.3);
       assertEquals("300m",s);
    }

    @Test
    public void testFormatAbove10km() {
        String s = Distance.format(10.3);
        assertEquals("10km", s);
    }

    @Test
    public void testFormatKmWithComma() {
        String s = Distance.format(5.302);
        assertEquals("5,30km",s);
    }

}

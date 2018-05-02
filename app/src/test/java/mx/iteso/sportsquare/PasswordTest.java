package mx.iteso.sportsquare;

/**
 * Created by federicoibarra on 02/04/18.
 */



import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PasswordTest {

    SignUpActivity sua;

    @Before
    public void setUp(){
        sua = new SignUpActivity();
    }

    @Test
    public void falseVer(){
        assertEquals(false, sua.validatePass("abc"));
        assertEquals(false, sua.validatePass("abcaaA"));
        assertEquals(false, sua.validatePass("abcaa."));
        assertEquals(false, sua.validatePass("abcaaaa1"));

        assertEquals(false, sua.validatePass("abcaaaaA."));
        assertEquals(false, sua.validatePass("abcaaaaA1"));

        assertEquals(false, sua.validatePass("abcaaaa1A"));
        assertEquals(false, sua.validatePass("abcaaaa1."));

        assertEquals(false, sua.validatePass("abcaaaa.A"));
        assertEquals(false, sua.validatePass("abcaaaa.1"));
    }

    @Test
    public void trueVer(){
        assertEquals(true, sua.validatePass("abcdA1."));
        assertEquals(true, sua.validatePass("abcd1A."));
        assertEquals(true, sua.validatePass("abcd.A1"));
        assertEquals(true, sua.validatePass("abcd.1A"));
        assertEquals(true, sua.validatePass("abcdA.1"));
        assertEquals(true, sua.validatePass("abcd1.A"));
    }
}

package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

import javax.swing.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertThat;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-18 16:42:07
 */
public class CommandTest {
    private Command target;

    @Before
    public void setUp() throws Exception {
        Application app = createDummyApplication();
        Command.setApplication(app);

        target = new Command() {
            protected void doExecute(Application app) {}
        };
    }

    public static Application createDummyApplication() {
        return (Application) Proxy.newProxyInstance(
                Command.class.getClassLoader(),
                new Class<?>[]{Application.class},
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getName().equals("getName")) {
                            return "Droid@Screen";
                        }
                        if (method.getName().equals("getVersion")) {
                            return "0.1";
                        }
                        return null;
                    }
                });
    }

    @After
    public void tearDown() throws Exception {
        Command.getCmds().clear();
    }

    @Test
    public void testLoadCommand() throws Exception {
        Command c = target.loadCommand("Quit");
        assertThat(c, notNullValue());
        assertThat(c, instanceOf(QuitCommand.class));
        assertThat(c.getName(), is("quit"));
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void testLoadInvalidCommand() throws Exception {
//        target.loadCommand("Xyz");
//    }

    @Test
    public void testLoadIcon() throws Exception {
        Icon i = target.loadIcon("Shutdown");
        assertThat(i, notNullValue());
        assertThat(i, instanceOf(Icon.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadInvalidIcon() throws Exception {
        target.loadIcon("Xyz");
    }

    @Test
    public void testGet() throws Exception {
        Command c = Command.get("Quit");
        assertThat(c, notNullValue());
        assertThat(c.getName(), is("quit"));
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void testGetInvalid() throws Exception {
//        Command.get("Xyz");
//    }

}

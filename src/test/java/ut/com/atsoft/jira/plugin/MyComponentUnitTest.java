package ut.com.atsoft.jira.plugin;

import org.junit.Test;
import com.atsoft.jira.plugin.api.MyPluginComponent;
import com.atsoft.jira.plugin.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest {
    @Test
    public void testMyName() {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent", component.getName());
    }
}
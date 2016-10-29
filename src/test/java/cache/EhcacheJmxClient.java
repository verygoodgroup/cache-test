package cache;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

public class EhcacheJmxClient {

    static MBeanServerConnection mbsc;

    static ObjectName cityStatistics;
    static ObjectName hotelStatistics;

    public static void connectJmx() throws IOException, MalformedObjectNameException {
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1093/jmxrmi");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

        mbsc = jmxc.getMBeanServerConnection();

        cityStatistics = new ObjectName("net.sf.ehcache:type=CacheStatistics,CacheManager=__DEFAULT__,name=city");
        hotelStatistics = new ObjectName("net.sf.ehcache:type=CacheStatistics,CacheManager=__DEFAULT__,name=hotel");
    }

    public static String getCityHits() {
        try {
            return mbsc.getAttribute(cityStatistics, "CacheHits").toString();
        } catch (MBeanException | AttributeNotFoundException | ReflectionException | InstanceNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCityMisses() {
        try {
            return mbsc.getAttribute(cityStatistics, "CacheMisses").toString();
        } catch (MBeanException | AttributeNotFoundException | ReflectionException | InstanceNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCityCount() {
        try {
            return mbsc.getAttribute(cityStatistics, "ObjectCount").toString();
        } catch (MBeanException | AttributeNotFoundException | ReflectionException | InstanceNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHotelHits() {
        try {
            return mbsc.getAttribute(hotelStatistics, "CacheHits").toString();
        } catch (MBeanException | AttributeNotFoundException | ReflectionException | InstanceNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHotelMisses() {
        try {
            return mbsc.getAttribute(hotelStatistics, "CacheMisses").toString();
        } catch (MBeanException | AttributeNotFoundException | ReflectionException | InstanceNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHotelCount() {
        try {
            return mbsc.getAttribute(hotelStatistics, "ObjectCount").toString();
        } catch (MBeanException | AttributeNotFoundException | ReflectionException | InstanceNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

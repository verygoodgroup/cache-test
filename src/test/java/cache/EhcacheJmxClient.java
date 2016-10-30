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

        cityStatistics = getObjectName("city");
        hotelStatistics = getObjectName("hotel");
    }

    private static ObjectName getObjectName(String cacheName) throws MalformedObjectNameException {
        String ehcacheXml = EhcacheJmxClient.class.getResource("../ehcache.xml").getPath();
        return new ObjectName(String.format("javax.cache:type=CacheStatistics,CacheManager=file.%s,Cache=%s", ehcacheXml, cacheName));
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

    public static String getCityRemovals() {
        try {
            return mbsc.getAttribute(cityStatistics, "CacheRemovals").toString();
        } catch (MBeanException | AttributeNotFoundException | ReflectionException | InstanceNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCityEvictions() {
        try {
            return mbsc.getAttribute(cityStatistics, "CacheEvictions").toString();
        } catch (MBeanException | AttributeNotFoundException | ReflectionException | InstanceNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCityPuts() {
        try {
            return mbsc.getAttribute(cityStatistics, "CachePuts").toString();
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

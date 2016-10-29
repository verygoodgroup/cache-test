package sample.data.jpa;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

@SpringBootApplication
@EnableCaching
public class CacheTestApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(CacheTestApplication.class, args);
	}

	@Bean
	public RmiRegistryFactoryBean rmiRegistry() {
		final RmiRegistryFactoryBean rmiRegistryFactoryBean = new RmiRegistryFactoryBean();
		rmiRegistryFactoryBean.setPort(1093);
		rmiRegistryFactoryBean.setAlwaysCreate(true);
		return rmiRegistryFactoryBean;
	}

	@Bean
	@DependsOn("rmiRegistry")
	public ConnectorServerFactoryBean connectorServerFactoryBean() throws Exception {
		final ConnectorServerFactoryBean connectorServerFactoryBean = new ConnectorServerFactoryBean();
		connectorServerFactoryBean.setObjectName("connector:name=rmi");
		connectorServerFactoryBean.setServiceUrl(String.format("service:jmx:rmi://%s:%s/jndi/rmi://%s:%s/jmxrmi", "localhost", 1093, "localhost", 1093));
		return connectorServerFactoryBean;
	}

	@Bean
	public ManagementService managementService(CacheManager cm) {
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		ManagementService managementService = new ManagementService(cm, mBeanServer, true, true, true, true);
		managementService.init();
		return managementService;
	}
}

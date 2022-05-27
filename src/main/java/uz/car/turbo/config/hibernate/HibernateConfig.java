package uz.car.turbo.config.hibernate;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.context.support.StandardServletEnvironment;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@PropertySource(value = {"classpath:application.properties", "classpath:hibernate.properties"})
@PropertySource(name = "hibernate", value = "classpath:hibernate.properties")
public class HibernateConfig implements TransactionManagementConfigurer {

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(environment.getProperty("spring.datasource.driver-class-name"));
        dataSource.setJdbcUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUser(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));
        dataSource.setMinPoolSize(environment.getProperty("hibernate.c3p0.min_size", Integer.class));
        dataSource.setAcquireIncrement(environment.getProperty("hibernate.c3p0.acquire_increment", Integer.class));
        dataSource.setMaxPoolSize(environment.getProperty("hibernate.c3p0.max_size", Integer.class));
        dataSource.setMaxStatements(environment.getProperty("hibernate.c3p0.max_statements", Integer.class));
        return dataSource;
    }

    @Bean
    public SessionFactory sessionFactory() throws IOException, PropertyVetoException {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan("uz.car.turbo.domain");
        sessionFactoryBean.setPhysicalNamingStrategy(new PhysicalNamingStrategyImpl());
        Properties properties = (Properties) Objects.requireNonNull(((StandardServletEnvironment) environment).getPropertySources().get("hibernate")).getSource();
        sessionFactoryBean.setHibernateProperties(properties);
//        sessionFactoryBean.getHibernateProperties().put(AvailableSettings.JDBC_TIME_ZONE, TimeZone.getTimeZone("GMT"));
        sessionFactoryBean.afterPropertiesSet();
        return sessionFactoryBean.getObject();
    }

    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        HibernateTransactionManager transactionManager = null;
        try {
            transactionManager = new HibernateTransactionManager(sessionFactory());
        } catch (IOException | PropertyVetoException e) {
            e.printStackTrace();
        }
        return transactionManager;
    }
}

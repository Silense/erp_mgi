package ru.cip.ws.erp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static ru.cip.ws.erp.ConfigurationHolder.CFG_KEY_DATASOURCE_JNDI;

/**
 * Author: Upatov Egor <br>
 * Date: 11.01.2017, 23:44 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Configuration
public class PersistenceConfig {

    private static final Logger log = LoggerFactory.getLogger("CONFIG");

    @Bean(name = "dataSource")
    public DataSource lookupDatasource(@Qualifier("app.properties") Properties props) throws NamingException {
        final String jndiNameDatasource = props.getProperty(CFG_KEY_DATASOURCE_JNDI);
        log.info("Start lookup DataSource by jndiName='{}'", jndiNameDatasource);
        final Properties jndiProps = new Properties();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        final JndiTemplate jndiFactory = new JndiTemplate(jndiProps);
        final DataSource result = jndiFactory.lookup(jndiNameDatasource, DataSource.class);
        log.info("DataSource from JNDI is [@{}]", Integer.toHexString(result.hashCode()));
        return result;
    }

    @Bean(name = "hibernateProperties")
    public Properties hibernateProperties() {
        final Properties result = new Properties();
        result.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
//        result.put("hibernate.show_sql", "false");
        result.put("hibernate.format_sql", "true");
        result.put("hibernate.max_fetch_depth", "3");
        result.put("hibernate.hbm2ddl.auto", ""); // means "NONE"
        return result;
    }


    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
            @Qualifier("dataSource") final DataSource dataSource,
            @Qualifier("hibernateProperties") final Properties props
    ) {
        final LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setDataSource(dataSource);
        result.setPackagesToScan("ru.cip.ws.erp.jpa.entity");
        result.setJpaProperties(props);
        result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        log.info("Initialized 'EntityManagerFactory'[@{}]: with DataSource[@{}]",
                Integer.toHexString(result.hashCode()),
                Integer.toHexString(dataSource.hashCode())
        );
        return result;
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") final EntityManagerFactory emf
    ) {
        final JpaTransactionManager result = new JpaTransactionManager(emf);
        result.setDefaultTimeout(36000);
        log.info("Initialized 'transactionManager'[@{}]: with EntityManagerFactory[@{}]",
                Integer.toHexString(result.hashCode()),
                Integer.toHexString(emf.hashCode())
        );
        return result;
    }







}

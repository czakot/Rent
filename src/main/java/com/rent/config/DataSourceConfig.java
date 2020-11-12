/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

/**
 *
 * @author czakot
 */
@Configuration
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfig {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private long connectionTimeout;
    private String prefix;
    private String[] hosts;
    private String preferredDatabaseUrlFilename;
    private String port;
    private String dbname;
    private String params;
    private String username;
    private String password;
    private String url;

    @Profile("server_db")
    @Bean
    public DataSource getServerDataSource() {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setConnectionTimeout(connectionTimeout);
        setCredentials(dataSource);

        if (setbackToEmbeddedDatabase(dataSource)) {
            dataSource = null;
        } 
//        if (!successfulConnectionToPreferredDatabaseHost(dataSource) ||
//            !successfulConnectionToAnyDatabaseHost(dataSource) || 
//            setbackToEmbeddedDatabase(dataSource)) {
//            dataSource = null;
//        } 
        
        return dataSource;
    }
    
    @Profile("embedded_db")
    @Bean
    public DataSource getEmbeddedDataSource() {
        
        HikariDataSource dataSource = new HikariDataSource();
        setCredentials(dataSource);
        
        if (!successfulConnectionToEmbeddedDatabase(dataSource)) {
            dataSource = null;
        }

        return dataSource;
    }
    
    private boolean successfulConnectionToPreferredDatabaseHost(HikariDataSource dataSource) {
        
        String preferredDatabaseUrl = getPreferredDatabaseUrl();
        
        return preferredDatabaseUrl != null && testConnection(preferredDatabaseUrl,dataSource);
    }
    
    private String getPreferredDatabaseUrl() {
        
        String preferredUrl = null;
        try {
            Scanner sc = new Scanner(new FileReader(preferredDatabaseUrlFilename));
            if (sc.hasNextLine()) {
                preferredUrl = sc.nextLine();
            }
        } catch (FileNotFoundException ex) {
            logger.info("Preferred Database URL not reachable.");
        }
        
        return preferredUrl ;
    }

    private boolean successfulConnectionToAnyDatabaseHost(HikariDataSource dataSource) {
        boolean success = false;
        for (String host : hosts) {
            String hostUrl = buildHostUrl(host);
            if (success = testConnection(hostUrl, dataSource)) {
                savePreferredUrl(hostUrl);
                break;
            }
        }
        return success;
    }
    
    private String buildHostUrl(String host) {
        return prefix + "://" + host + ":" + port + "/" + dbname + params;
    }

    private void savePreferredUrl(String url) {
        try {
            PrintWriter pr = new PrintWriter(new File(preferredDatabaseUrlFilename));
            pr.print(url);
            pr.flush();
        } catch (FileNotFoundException ex) {
            logger.info("Creating preferred Database URL file failed. (Filename: " + preferredDatabaseUrlFilename);
        }
    }

    private boolean setbackToEmbeddedDatabase(HikariDataSource dataSource) {
        boolean success = false;
        
        logger.info("No available Database Servers => setback to Embedded Database)");
        
        Properties embeddedProperties = getProperties("classpath:application-embedded_db.properties");
        if (embeddedProperties != null) {
            loadEmbeddedProperties(embeddedProperties, dataSource);
            url = dataSource.getJdbcUrl();
            success = successfulConnectionToEmbeddedDatabase(dataSource);
        }
        
        return success;
    }

    private void loadEmbeddedProperties(Properties properties, HikariDataSource dataSource) {
        
        properties.list(System.err);
//        spring.jpa.hibernate.ddl-auto=update
//        spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
//        spring.h2.console.enabled=true
//        spring.h2.console.settings.web-allow-others=false
//        spring.h2.console.settings.trace=false
//        spring.h2.console.path=/db   
        
        dataSource =new HikariDataSource(new HikariConfig(properties));
/*        
        Properties dataSourceProperties = filterPropertiesByPrefix(properties, "spring.datasource.");
        System.err.println("--------------------");
        dataSourceProperties.list(System.err);
        dataSource =new HikariDataSource(new HikariConfig(dataSourceProperties));
        dataSource.setDataSourceProperties(properties);
*/        
        System.err.println("Url: " + dataSource.getJdbcUrl());
        System.err.println("Username: " + dataSource.getUsername());
        System.err.println("Password: " + dataSource.getPassword());
        System.err.println("Driver class name: " + dataSource.getDriverClassName());

    }
    
    private Properties getProperties(String filename) {
        Properties properties = new Properties();
        FileInputStream in;
        try {
            File file = ResourceUtils.getFile(filename);
            in = new FileInputStream(file); // file should use ISO 8859-1 character encoding and/or Unicode escapes
        } catch (FileNotFoundException ex) {
            logger.info("\"" + filename + "\" not found");
            return null;
        }
        try {
            properties.load(in);
        } catch (IOException | IllegalArgumentException ex) {
            logger.info("\"" + filename + "\" stream reading or Unicode escapes parsing error (should use ISO 8859-1 character encoding)");
            return null;
        }
        try {
            in.close();
        } catch (IOException ex) {
            logger.info("\"" + filename + "\" closing error");
        }
        
        return properties;
    }
    
    private Properties filterPropertiesByPrefix(Properties properties, String prefix) {
        
        Properties filteredProperties = new Properties();
        
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            if (key.startsWith(prefix)) {
                filteredProperties.put(key.substring(prefix.length()), properties.getProperty(key));
            }
        }
        
        return filteredProperties;
    }
    
    private boolean successfulConnectionToEmbeddedDatabase(HikariDataSource dataSource) {
        return testConnection(url, dataSource);
    }

    private void setCredentials(HikariDataSource dataSource) {
        dataSource.setUsername(username);
        dataSource.setPassword(password);
    }
    
    private boolean testConnection(String url, HikariDataSource dataSource) {
        boolean success = false;
        logger.info("Testing Database connection: " + url);
        dataSource.setJdbcUrl(url);
        try {
            dataSource.getConnection();
            logger.info("Database connected to: " + url);
            success = true;
        } catch (SQLException ex) {
            logger.info("Database connection failed: " + url);
        }
        return success;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }

    public void setPreferredDatabaseUrlFilename(String preferredDatabaseUrlFilename) {
        this.preferredDatabaseUrlFilename = preferredDatabaseUrlFilename;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public void setParams(String params) {
        this.params = params;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

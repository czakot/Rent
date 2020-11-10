/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.config;

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
import java.util.logging.Level;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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

        if (!successfulConnectionToPreferredDatabaseHost(dataSource) ||
            !successfulConnectionToAnyDatabaseHost(dataSource) || 
            tryingToUseEmbeddedDatabase(dataSource)) {
            dataSource = null;
        } 
        
        return dataSource;
    }
    
    @Profile("embedded_db")
    @Bean
    public DataSource getEmbeddedDataSource() {
        
        HikariDataSource dataSource = new HikariDataSource();
        System.err.println("username: " + username + "   password: " + password);
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

    private boolean tryingToUseEmbeddedDatabase(HikariDataSource dataSource) {
        
        Boolean success;
        logger.info("No available Database Servers => trying to use Embedded)");
        
        success = succesfulReplacementParametersForEmbedded();
        if (success) {
            setCredentials(dataSource);
            success = successfulConnectionToEmbeddedDatabase(dataSource);
        }
        return success;
    }

    private boolean succesfulReplacementParametersForEmbedded() {
        
        Properties embeddedProperties = getEmbeddedProperties("application-embedded_db.properties");
        embeddedProperties.list(System.err);
        String keyPrefix = "spring.datasource.";
        url = embeddedProperties.getProperty(keyPrefix + "url");
        username = embeddedProperties.getProperty(keyPrefix + "username");
        password = embeddedProperties.getProperty(keyPrefix + "password");

        return url != null && username != null && password != null;
    }
    
    private Properties getEmbeddedProperties(String filename) {
        Properties properties = new Properties();
        FileInputStream in;
        try {
            in = new FileInputStream(filename); // file should use ISO 8859-1 character encoding and/or Unicode escapes
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

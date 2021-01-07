/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

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
    private String driverClassName;
    
    @Profile("server_db")
    @Bean(name = "serverDataSource")
    public DataSource getServerDataSource() {

        HikariDataSource dataSource = new HikariDataSource();
        
        dataSource.setConnectionTimeout(connectionTimeout);
        setCredentials(dataSource);

        if (!successfulConnectionToPreferredDatabaseHost(dataSource) &&
            !successfulConnectionToAnyDatabaseHost(dataSource)) {
            dataSource = null;
            logger.info("No available Database Server.");
        } 
        
        return dataSource;
    }
    
    @Profile("embedded_db")
    @Bean(name = "embeddedDataSource")
    public DataSource getEmbeddedDataSource() {
        
        HikariDataSource dataSource = new HikariDataSource();
        
        setCredentials(dataSource);
        dataSource.setJdbcUrl(url);
        dataSource.setDriverClassName(driverClassName);
        if (!successfulConnectionToEmbeddedDatabase(dataSource)) {
            dataSource = null;
            logger.info("Could not reach and could not create Embedded Database.");
        }

        return dataSource;
    }
    
    private boolean successfulConnectionToPreferredDatabaseHost(HikariDataSource dataSource) {
        
        boolean successful = false;
        
        String preferredDatabaseUrl = getPreferredDatabaseUrl();
        
        if (preferredDatabaseUrl != null) {
            dataSource.setJdbcUrl(preferredDatabaseUrl);
            successful = testConnection(dataSource);
        }
        
        return successful;
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
            dataSource.setJdbcUrl(hostUrl);
            success = testConnection(dataSource);
            if (success) {
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
            PrintWriter pr = new PrintWriter(preferredDatabaseUrlFilename);
            pr.print(url);
            pr.flush();
        } catch (FileNotFoundException ex) {
            logger.info("Creating preferred Database URL file failed. (Filename: " + preferredDatabaseUrlFilename);
        }
    }

    private boolean successfulConnectionToEmbeddedDatabase(HikariDataSource dataSource) {
        return testConnection(dataSource);
    }
    
    private void setCredentials(HikariDataSource dataSource) {
        dataSource.setUsername(username);
        dataSource.setPassword(password);
    }

    private boolean testConnection(HikariDataSource dataSource) {
        boolean success = false;
        
        String testUrl = dataSource.getJdbcUrl();
        
        logger.info("Testing Database connection: " + testUrl);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            logger.info("Database connected to: " + testUrl);
            success = true;
        } catch (SQLException ex) {
            logger.info("Database connection failed: " + testUrl);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex1) {
                logger.info("Database closing failed: " + testUrl);
            }
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

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rent.config;

import com.zaxxer.hikari.HikariDataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Scanner;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    

    @Bean
    public DataSource getDataSource() {

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setConnectionTimeout(connectionTimeout);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        if (successfulConnectionToPreferredDatabaseHost(dataSource)) {
            return dataSource;
        }
        if (successfulConnectionToAnyDatabaseHostFromHosts(dataSource)) {
            return dataSource;
        }
        logger.info("No available any known Database host)");
        return null;
    }

    private boolean successfulConnectionToPreferredDatabaseHost(HikariDataSource dataSource) {
        boolean success = false;
        try {
            Scanner sc = new Scanner(new FileReader(preferredDatabaseUrlFilename));
            if (sc.hasNextLine()) {
                String preferredDatabaseUrl = sc.nextLine();
                success = testConnection(preferredDatabaseUrl,dataSource);
            }
        } catch (FileNotFoundException ex) {
            logger.info("Preferred Database URL not reachable.");
        }
        return success;
    }

    private boolean successfulConnectionToAnyDatabaseHostFromHosts(HikariDataSource dataSource) {
        boolean success = false;
        for (String host : hosts) {
            String url = prefix + "://" + host + ":" + port + "/" + dbname + params;
            if (success = testConnection(url, dataSource)) {
                break;
            }
        }
        return success;
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

    private boolean testConnection(String url, HikariDataSource dataSource) {
        boolean success = false;
        logger.info("Testing Database connection: " + url);
        dataSource.setJdbcUrl(url);
        try {
            dataSource.getConnection();
            logger.info("Database connected to: " + url);
            success = true;
            savePreferredUrl(url);
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

}

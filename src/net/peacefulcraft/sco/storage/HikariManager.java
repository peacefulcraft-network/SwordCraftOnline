package net.peacefulcraft.sco.storage;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.peacefulcraft.sco.SCOConfig;

public class HikariManager {

	private HikariDataSource ds;
	
	public HikariManager(SCOConfig c) {
		HikariConfig hc = new HikariConfig();
		// hc.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
		hc.setDriverClassName("org.mariadb.jdbc.Driver");
		hc.setJdbcUrl("jdbc:mariadb://" + c.getDb_ip() + ":3306/" + c.getDb_name());
		hc.setUsername(c.getDb_user());
		hc.setPassword(c.getDb_password());
		hc.setPoolName("SCOData");
		/*
		 * TODO: Recomended optimizations
		 * https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
		 */
		
		ds = new HikariDataSource(hc);
	}
	
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
	
	/**
	 * TOOD: Beter health checking, logging, and error reporting
	 * @return
	 */
	public boolean isAlive() {
		return (ds.isRunning() && !ds.isClosed());
	}
	
	public void close() {
		ds.close();
	}
}

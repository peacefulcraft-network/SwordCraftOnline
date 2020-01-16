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
		hc.setJdbcUrl("jdbc:mysql://" + c.getDb_ip() + ":3306/" + c.getDb_name());
		hc.setUsername(c.getDb_user());
		hc.setPassword(c.getDb_password());
		hc.setPoolName("SCOData");
		
		/*
		 * Recomended optimizations
		 * https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
		 */
		hc.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
		hc.addDataSourceProperty("cachePrepStmts", "true");
		hc.addDataSourceProperty("prepStmtCacheSize", "250");
		hc.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		hc.addDataSourceProperty("useServerPrepStmts", "true");
		hc.addDataSourceProperty("cacheResultSetMetadata", "true");
		hc.addDataSourceProperty("cacheServerConfiguration", "true");
		hc.addDataSourceProperty("maintainTimeStats", "false");
		
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

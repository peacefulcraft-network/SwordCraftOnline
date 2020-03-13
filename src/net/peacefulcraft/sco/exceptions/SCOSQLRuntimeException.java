package net.peacefulcraft.sco.exceptions;

import java.util.UUID;

public class SCOSQLRuntimeException extends RuntimeException{

	private long playerRegistryId;
	private UUID playerUUID;
	private String playerName;
	private Class origin;
	
	public SCOSQLRuntimeException(long playerRegistryId, String message, Class origin) {
		super(message);
		this.playerRegistryId = playerRegistryId;
		this.origin = origin;
	}
	
	public SCOSQLRuntimeException(UUID playerUUID, String message, Class origin) {
		super(message);
		this.playerUUID = playerUUID;
		this.origin = origin;
	}
	
	public SCOSQLRuntimeException(String playerName, String message, Class origin) {
		super(message);
		this.playerName = playerName;
		this.origin = origin;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6316521464103210154L;

}

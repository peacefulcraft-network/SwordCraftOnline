package net.peacefulcraft.sco.mythicmobs.adapters.abstracts;

public interface AbstractPlayer extends AbstractEntity {
    boolean hasPermission(String paramString);
  
    boolean isInCreativeMode();
    
    boolean isInSpectatorMode();
    
    void sendMessage(String paramString);
    
    float getExperience();
    
    void setExperience(float paramFloat);
    
    void hidePlayer(AbstractPlayer paramAbstractPlayer);
    
    boolean canSee(AbstractPlayer paramAbstractPlayer);
    
    boolean isOnline();
    
    int getLevel();
    
    void setLevel(int paramInt);
    
    void setHealthScale(double paramDouble);
    
    void setHealthScaled(boolean paramBoolean);
    
    void setPersonalTime(long paramLong, boolean paramBoolean);
    
    void resetPersonalTime();
    
    void setPersonalWeather(String paramString);
    
    void resetPersonalWeather();
    
    void setAllowFlight(boolean paramBoolean);
    
    boolean getAllowFlight();
    
    void showPlayer(AbstractPlayer paramAbstractPlayer);
    
    void setFlying(boolean paramBoolean);
    
    void setFlyingSpeed(float paramFloat);
    
    void setWalkSpeed(float paramFloat);
    
    int getFoodLevel();
    
    void setFoodLevel(int paramInt);
    
    float getFoodSaturation();
    
    void setFoodSaturation(float paramFloat);
    
    void setGameMode(AbstractGameMode paramAbstractGameMode);
}
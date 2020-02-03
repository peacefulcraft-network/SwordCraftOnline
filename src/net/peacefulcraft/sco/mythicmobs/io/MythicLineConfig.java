package net.peacefulcraft.sco.mythicmobs.io;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Color;

import net.peacefulcraft.sco.mythicmobs.adapters.BukkitColor;


public class MythicLineConfig implements GenericConfig {
    private String fileName = "Unknown";
    
    private String line;
    
    public String getFileName() {
        return this.fileName;
    }
    
    private HashMap<String, String> config = new HashMap<>();
    
    public MythicLineConfig(String fileName, String line) {
        this(line);
        this.fileName = fileName;
    }
    
    public MythicLineConfig(File file, String line) {
        this(line);
        this.fileName = file.getName();
    }
    
    public MythicLineConfig(String s) {
        s = parseBlock(s);
        this.line = s;
      //Log Error MythicLogger.debug(MythicLogger.DebugLevel.EVENT, "LOADING LineConfig FOR LINE: {0}", new Object[] { s });
        if (s.contains("{") && s.contains("}")) {
            int startPos = s.indexOf('{') + 1;
            int lastPos = s.lastIndexOf('}');
            int count = 0;
            s = s.substring(startPos, lastPos);
            for (char c : s.toCharArray()) {
                if (c == '{')
                    count++; 
                if (c == '}')
                    count--; 
            } 
            if (count != 0) {
            //TODO: Log ErrorMythicLogger.errorGenericConfig(this, "Could not load line due to unbalanced braces.");
            return;
            } 
            //MythicLogger.debug(MythicLogger.DebugLevel.EVENT, "| Normalized Line to: {0}", new Object[] { s });
            if (s.length() == 0) { return; }
            int start = 0;
            int pos = 0;
            int depth = 0;
            String lastKey = "";
            String lastVal = "";
            boolean inb = false;
            s = s + "}";
            for (char c : s.toCharArray()) {
                if (c == '{')
                    depth++; 
                if (c == '}')
                    depth--; 
                if ((c == ';' && depth == 0) || (c == '}' && depth < 0)) 
                    try {
                        String element = s.substring(start, pos);
                        if (pos - start > 0 && element.length() > 0) {
                            String key = element.substring(0, element.indexOf('=')).trim().toLowerCase();
                            String val = element.substring(element.indexOf('=') + 1).trim();
                            this.config.put(key, val);
                            //MythicLogger.debug(MythicLogger.DebugLevel.EVENT, "+ LOADED ELEMENT " + key + " == " + val, new Object[0]);
                        } 
                    } catch (Exception ex) {
                //MythicLogger.debug(MythicLogger.DebugLevel.EVENT, "! Failed to load element due to bad syntax.", new Object[0]);
                } finally {
                start = pos + 1;
                }  
            pos++;
            } 
        } else if (s.contains("[") && s.contains("]")) {
            try {
            String[] split = s.split("\\[");
            s = split[1];
            String[] split2 = s.split("\\]");
            s = split2[0];
            } catch (ArrayIndexOutOfBoundsException ex) {
            //MythicMobs.throwSevere("error-config-load-badbrackets", "Could not load LineConfig: Invalid syntax. String = {0}", new Object[] { s });
            return;
            } 
        } 
    }
    
    public String getLine() {
      return this.line;
    }
    
    public int size() {
      return this.config.size();
    }
    
    public Set<Map.Entry<String, String>> entrySet() {
      return this.config.entrySet();
    }
    
    public static String getKey(String s) {
      String key = null;
      if (s.contains("{")) {
        key = s.substring(0, s.indexOf("{"));
      } else if (s.contains("[")) {
        key = s.substring(0, s.indexOf("["));
      } else {
        key = s;
      } 
      return key;
    }
    
    public boolean getBoolean(String key) {
      return getBoolean(key, false);
    }
    
    public boolean getBoolean(String key, boolean def) {
      key = key.toLowerCase();
      String s = this.config.get(key);
      if (s == null)
        return def; 
      try {
        return Boolean.parseBoolean(s);
      } catch (Exception ex) {
        return def;
      } 
    }
    
    public boolean getBoolean(String[] key, boolean def) {
      String s = null;
      for (String k : key) {
        s = this.config.get(k.toLowerCase());
        if (s != null)
          break; 
      } 
      if (s == null)
        return def; 
      try {
        return Boolean.parseBoolean(s);
      } catch (Exception ex) {
        return def;
      } 
    }
    
    public String getString(String key) {
      return getString(key, null);
    }
    
    public String getString(String[] key) {
      return getString(key, null, new String[0]);
    }
    
    public String getString(String key, String def) {
      String s = this.config.get(key.toLowerCase());
      if (s == null)
        return def; 
      return s;
    }
    
    public String getString(String[] key, String def, String... args) {
      String s = null;
      for (String k : key) {
        s = this.config.get(k.toLowerCase());
        if (s != null)
          return s; 
      } 
      for (String a : args) {
        if (a != null)
          return a; 
      } 
      return def;
    }
    
    /*
    public PlaceholderString getPlaceholderString(String key, String def) {
      String s = getString(key, def);
      if (s == null)
        return null; 
      return PlaceholderString.of(s);
    }
    
    public PlaceholderString getPlaceholderString(String[] key, String def, String... args) {
      String s = getString(key, def, args);
      if (s == null)
        return null; 
      return PlaceholderString.of(s);
    }
    */
    
    public int getInteger(String key) {
      return getInteger(key, 0);
    }
    
    public int getInteger(String[] key) {
      return getInteger(key, 0);
    }
    
    public int getInteger(String key, int def) {
      String s = this.config.get(key.toLowerCase());
      if (s == null)
        return def; 
      try {
        return Integer.parseInt(s);
      } catch (Exception ex) {
        return def;
      } 
    }
    
    public int getInteger(String[] key, int def) {
      String s = null;
      for (String k : key) {
        s = this.config.get(k.toLowerCase());
        if (s != null)
          break; 
      } 
      if (s == null)
        return def; 
      try {
        return Integer.parseInt(s);
      } catch (Exception ex) {
        return def;
      } 
    }
    
    /*
    public PlaceholderInt getPlaceholderInteger(String key, String def) {
      String s = getString(key, def);
      if (s == null)
        return null; 
      return PlaceholderInt.of(s);
    }
    
    public PlaceholderInt getPlaceholderInteger(String[] key, String def, String... args) {
      String s = getString(key, def, args);
      if (s == null)
        return null; 
      return PlaceholderInt.of(s);
    }
    
    public PlaceholderInt getPlaceholderInteger(String key, int def) {
      return getPlaceholderInteger(key, String.valueOf(def));
    }
    
    public PlaceholderInt getPlaceholderInteger(String[] key, int def, String... args) {
      return getPlaceholderInteger(key, String.valueOf(def), args);
    }
    */
    
    public double getDouble(String key) {
      return getDouble(key, 0.0D);
    }
    
    public double getDouble(String[] key) {
      return getDouble(key, 0.0D);
    }
    
    public double getDouble(String key, double def) {
      String s = this.config.get(key.toLowerCase());
      if (s == null)
        return def; 
      try {
        return Double.parseDouble(s);
      } catch (Exception ex) {
        return def;
      } 
    }
    
    public double getDouble(String[] key, double def) {
      String s = null;
      for (String k : key) {
        s = this.config.get(k.toLowerCase());
        if (s != null)
          break; 
      } 
      if (s == null)
        return def; 
      try {
        return Double.parseDouble(s);
      } catch (Exception ex) {
        return def;
      } 
    }
    
    /*
    public PlaceholderDouble getPlaceholderDouble(String key, String def) {
      String s = getString(key, def);
      if (s == null)
        return null; 
      return PlaceholderDouble.of(s);
    }
    
    public PlaceholderDouble getPlaceholderDouble(String[] key, String def, String... args) {
      String s = getString(key, def, args);
      if (s == null)
        return null; 
      return PlaceholderDouble.of(s);
    }
    
    public PlaceholderDouble getPlaceholderDouble(String key, double def) {
      return getPlaceholderDouble(key, String.valueOf(def));
    }
    
    public PlaceholderDouble getPlaceholderDouble(String[] key, double def, String... args) {
      return getPlaceholderDouble(key, String.valueOf(def), args);
    }
    */
    
    public float getFloat(String key) {
      return getFloat(key, 0.0F);
    }
    
    public float getFloat(String[] key) {
      return getFloat(key, 0.0F);
    }
    
    public float getFloat(String key, float def) {
      String s = this.config.get(key.toLowerCase());
      if (s == null)
        return def; 
      try {
        return Float.parseFloat(s);
      } catch (Exception ex) {
        return def;
      } 
    }
    
    public float getFloat(String[] key, float def) {
      String s = null;
      for (String k : key) {
        s = this.config.get(k.toLowerCase());
        if (s != null)
          break; 
      } 
      if (s == null)
        return def; 
      try {
        return Float.parseFloat(s);
      } catch (Exception ex) {
        return def;
      } 
    }
    
    /*
    public PlaceholderFloat getPlaceholderFloat(String key, String def) {
      String s = getString(key, def);
      if (s == null)
        return null; 
      return PlaceholderFloat.of(s);
    }
    
    public PlaceholderFloat getPlaceholderFloat(String[] key, String def, String... args) {
      String s = getString(key, def, args);
      if (s == null)
        return null; 
      return PlaceholderFloat.of(s);
    }
    
    public PlaceholderFloat getPlaceholderFloat(String key, float def) {
      return getPlaceholderFloat(key, String.valueOf(def));
    }
    
    public PlaceholderFloat getPlaceholderFloat(String[] key, float def, String... args) {
      return getPlaceholderFloat(key, String.valueOf(def), args);
    }
    */
    
    public long getLong(String key) {
      return getLong(key, 0L);
    }
    
    public long getLong(String[] key) {
      return getLong(key, 0L);
    }
    
    public long getLong(String key, long def) {
      String s = this.config.get(key.toLowerCase());
      if (s == null)
        return def; 
      try {
        return Long.parseLong(s);
      } catch (Exception ex) {
        return def;
      } 
    }
    
    public long getLong(String[] key, long def) {
      String s = null;
      for (String k : key) {
        s = this.config.get(k.toLowerCase());
        if (s != null)
          break; 
      } 
      if (s == null)
        return def; 
      try {
        return Long.parseLong(s);
      } catch (Exception ex) {
        return def;
      } 
    }
    
    public Color getColor(String[] key, String def) {
      String s = null;
      for (String k : key) {
        s = this.config.get(k.toLowerCase());
        if (s != null)
          break; 
      } 
      return getColor(s, def);
    }
    
    public Color getColor(String key, String def) {
      String c = (key == null) ? def : getString(key, def);
      if (c == null)
        return null; 
      if (c.startsWith("#") && c.length() == 7)
        return BukkitColor.decode(c); 
      //MythicLogger.errorGenericConfig(this, "Couldn't parse color '" + c + "': must be in Hex or R,G,B format");
      return Color.RED;
    }
    
    /*
    public static String unparseBlock(String s) {
      if (s.contains("\"")) {
        String[] split = s.split("\"");
        int i = 0;
        String ns = "";
        for (String str : split) {
          if (i % 2 == 1) {
            ns = ns.concat("\"" + SkillString.unparseMessageSpecialChars(str) + "\"");
          } else {
            ns = ns.concat(str);
          } 
          i++;
        } 
        s = ns;
      } 
      if (s.contains("'")) {
        String[] split = s.split("'");
        int i = 0;
        String ns = "";
        for (String str : split) {
          if (i % 2 == 1) {
            ns = ns.concat("'" + SkillString.unparseMessageSpecialChars(str) + "'");
          } else {
            ns = ns.concat(str);
          } 
          i++;
        } 
        s = ns;
      } 
      int pos = 0;
      int count = 0;
      int ss = 0;
      int sc = 0;
      int ec = 0;
      String parsed = "";
      for (char c : s.toCharArray()) {
        if (c == '{') {
          if (count == 0)
            sc = pos; 
          count++;
        } 
        count--;
        if (c == '}' && count == 0) {
          ec = pos;
          String f = s.substring(ss, sc);
          String m = s.substring(sc, ec).replace(" ", "<&csp>").replace("-", "<&da>");
          String e = s.substring(ec);
          parsed = parsed + f + m;
          ss = pos;
        } 
        pos++;
      } 
      parsed = parsed + s.substring(ss, pos);
      return parsed;
    }
    */

    public static String parseBlock(String s) {
      return s.replace("<&csp>", " ").replace("<&da>", "-").trim();
    }
  }
package parssist;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

/**
* The {@code ParssistConfig} class represents a configuration manager for the Parssist application.
* It provides access to configuration properties defined in the "app.config" file.
*/
public class ParssistConfig {

   /**
    * The path to the configuration file.
    */
   private static final String CONFIG_PATH = "app.config";

   /**
    * The singleton instance of the ParssistConfig class.
    */
   private static ParssistConfig instance = null;

   /**
    * The properties loaded from the configuration file.
    */
   private Properties properties;


   /**
    * Private constructor to prevent direct instantiation.
    * Loads configuration properties from the "app.config" file.
    * @throws IOException if an I/O error occurs while reading the configuration file.
    */
   private ParssistConfig() throws IOException {
       properties = new Properties();
       try (InputStream inputStream = Objects.requireNonNull(getClass().getResource(CONFIG_PATH)).openStream();
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
           properties.load(reader);
       }
   }


   /**
    * Retrieves the singleton instance of the ParssistConfig class.
    * If the instance is not initialized, a new instance is created.
    * @return the ParssistConfig instance.
    */
   public static ParssistConfig getInstance() {
       if (instance == null) {
           try {
               instance = new ParssistConfig();
           } catch (IOException e) {
               // Exception handling if an error occurs during initialization
               return instance;
           }
       }
       return instance;
   }

   /**
    * Retrieves the value associated with the specified key.
    * @param key the key whose associated value is to be retrieved.
    * @return the value associated with the specified key.
    */
   public Object get(String key) {
       return properties.get(key);
   }

   /**
    * Retrieves the string value associated with the specified key.
    * @param key the key whose associated string value is to be retrieved.
    * @return the string value associated with the specified key.
    */
   public String getProperty(String key) {
       return properties.getProperty(key);
   }

   /**
    * Retrieves the double value associated with the specified key.
    * @param key the key whose associated double value is to be retrieved.
    * @return the double value associated with the specified key.
    * @throws NumberFormatException if the value cannot be parsed as a double.
    */
   public double getPropertyAsDouble(String key) {
       return Double.parseDouble(properties.getProperty(key));
   }

   /**
    * Retrieves the integer value associated with the specified key.
    * @param key the key whose associated integer value is to be retrieved.
    * @return the integer value associated with the specified key.
    * @throws NumberFormatException if the value cannot be parsed as an integer.
    */
   public int getPropertyAsInt(String key) {
       return Integer.parseInt(properties.getProperty(key));
   }

   /**
    * Retrieves all configuration properties.
    * @return a {@code Properties} object containing all configuration properties.
    */
   public Properties getProperties() {
       return properties;
   }
}
package application.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 *	Classe che permette la gestione dei file json.
 */
public class JsonHelper {
	/**
	 * Istanza della classe Gson che gestisce la serializzazione e deserializzazione
	 * dei file json.
	 */
	private static final Gson GSON = new Gson();
	
	/**
	 * Costruttore vuoto privato della classe per impedire la sua istanziazione.
	 */
	private JsonHelper() {
		
	}
	
	/**
	 * Metodo utilizzato per caricare un file json dalla classpath.
	 * @param <T>
	 * @param path
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws JsonSyntaxException
	 */
	public static <T> T loadJson(String path, Type type) throws IOException, JsonSyntaxException{
		try(InputStream is = JsonHelper.class.getClassLoader().getResourceAsStream(path)){
			if(is == null) {
				throw new IOException("File not found");
			}
			try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
			return GSON.fromJson(reader, type);
			}
		}
	}
}

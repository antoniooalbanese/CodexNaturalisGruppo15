package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

public class Application {
	public static void main(String[] args) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(Application.class.getClassLoader().getResourceAsStream("MazzoOro.json"), StandardCharsets.UTF_8))) {
			String line;
			StringBuilder builder = new StringBuilder();
			
			while ((line = reader.readLine()) != null) {
				builder.append(line).append("\n");
			}
			
			System.out.println(builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

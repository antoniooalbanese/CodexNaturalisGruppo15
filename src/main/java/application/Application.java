package application;

import com.google.gson.Gson;

public class Application {
	public static void main(String[] args) {
		Gson gson = new Gson();
		System.out.println(gson.toJson("Hello World"));
	}
}

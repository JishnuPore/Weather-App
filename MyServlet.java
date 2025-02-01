
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("index.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String apiKey = "a66030a74d3efbled367423518349689";
		
        String city = request.getParameter("city"); 

        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
               
                
                Scanner scanner = new Scanner(reader);
                StringBuilder responseContent = new StringBuilder();

                while (scanner.hasNext()) {
                    responseContent.append(scanner.nextLine());
                }
                scanner.close();
                
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
                
                long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
                String date = new Date(dateTimestamp);
                
                double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp");
                int temperatureCelsius = (int) (temperatureKelvin - 273.15);
               
                int humidity = jsonObject.getAsJsonObject("main").get("humidity");
                
                double windSpeed = jsonObject.getAsJsonObject("wind").get("speed");
                
                String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main");
                
                
                request.setAttribute("date", date);
                request.setAttribute("city", city);
                request.setAttribute("temperature", temperatureCelsius);
                request.setAttribute("weatherCondition", weatherCondition); 
                request.setAttribute("humidity", humidity);    
                request.setAttribute("windSpeed", windSpeed);
                request.setAttribute("weatherData", responseContent.toString());
                
                connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}

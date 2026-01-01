import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherGUIWithGeocoding extends JFrame implements ActionListener {

    private JTextField cityField;
    private JButton getWeatherBtn;
    private JTextArea resultArea;

    // Replace with your OpenWeatherMap API key (without newline or spaces)
    private final String GEO_API_KEY = "09cbe3f01f0aebd43fcd15c6fe08bda6";

    public WeatherGUIWithGeocoding() {
        setTitle("Weather Client");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cityField = new JTextField(20);
        getWeatherBtn = new JButton("Get Weather");
        resultArea = new JTextArea();
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultArea);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Enter City Name:"));
        topPanel.add(cityField);
        topPanel.add(getWeatherBtn);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        getWeatherBtn.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String city = cityField.getText().trim();
        if (!city.isEmpty()) {
            fetchWeather(city);
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a city name.");
        }
    }

    private void fetchWeather(String city) {
        try {
            // 1. Call OpenWeatherMap Geocoding API to get lat/lon
            String geoUrl = "http://api.openweathermap.org/geo/1.0/direct?q="
                    + city + "&limit=1&appid=" + GEO_API_KEY;
            JSONObject geoJson = getFirstJsonFromArray(geoUrl);

            if (geoJson == null) {
                resultArea.setText("City not found!");
                return;
            }

            double lat = geoJson.getDouble("lat");
            double lon = geoJson.getDouble("lon");

            // 2. Call Open-Meteo API for current weather
            String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude="
                    + lat + "&longitude=" + lon + "&current_weather=true";
            JSONObject weatherJson = getJson(weatherUrl);
            JSONObject currentWeather = weatherJson.getJSONObject("current_weather");

            // 3. Display structured result
            resultArea.setText("");
            resultArea.append("CURRENT WEATHER REPORT (" + city + ")\n");
            resultArea.append("----------------------------------\n");
            resultArea.append("Temperature : " + currentWeather.getDouble("temperature") + " °C\n");
            resultArea.append("Wind Speed  : " + currentWeather.getDouble("windspeed") + " km/h\n");
            resultArea.append("Wind Dir    : " + currentWeather.getInt("winddirection") + "°\n");
            resultArea.append("Time        : " + currentWeather.getString("time") + "\n");

        } catch (Exception ex) {
            resultArea.setText("Error fetching weather data:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Helper to fetch JSON from URL
    private JSONObject getJson(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        return new JSONObject(response.toString());
    }

    // Helper to fetch first JSON object from a JSON array
    private JSONObject getFirstJsonFromArray(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        JSONArray array = new JSONArray(response.toString());
        if (array.length() > 0) {
            return array.getJSONObject(0);
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        new WeatherGUIWithGeocoding();
    }
}

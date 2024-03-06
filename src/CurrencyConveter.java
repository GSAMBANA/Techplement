    import java.io.InputStreamReader;
	import java.net.HttpURLConnection;
	import java.net.URL;
	import java.util.HashMap;
	import java.util.Map;
	import java.util.Scanner;

     import com.google.gson.JsonObject;
     import com.google.gson.JsonParser;

	public class CurrencyConveter {
	    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";
	    private Map<String, Double> exchangeRates;
	    private Map<String, String> favoriteCurrencies;

	    public CurrencyConveter() {
	        exchangeRates = fetchExchangeRates();
	        favoriteCurrencies = new HashMap<>();
	    }

	    private Map<String, Double> fetchExchangeRates() {
	        try {
	            URL url = new URL(API_URL);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
	            JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();
	            return jsonResponse.getAsJsonObject("rates").entrySet()
	                    .stream()
	                    .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue().getAsDouble()), HashMap::putAll);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    public void addFavoriteCurrency(String currencyCode) {
	        if (exchangeRates.containsKey(currencyCode)) {
	            favoriteCurrencies.put(currencyCode, currencyCode);
	            System.out.println(currencyCode + " added to favorites.");
	        } else {
	            System.out.println("Invalid currency code.");
	        }
	    }

	    public void viewFavoriteCurrencies() {
	        System.out.println("Favorite Currencies:");
	        favoriteCurrencies.forEach((key, value) -> System.out.println(key));
	    }

	    public void updateFavoriteCurrency(String oldCurrencyCode, String newCurrencyCode) {
	        if (favoriteCurrencies.containsKey(oldCurrencyCode)) {
	            favoriteCurrencies.remove(oldCurrencyCode);
	            addFavoriteCurrency(newCurrencyCode);
	            System.out.println("Favorite currency updated successfully.");
	        } else {
	            System.out.println("Old currency code not found in favorites.");
	        }
	    }

	    public static void main(String[] args) {
	        CurrencyConveter converter = new CurrencyConveter();
	        Scanner scanner = new Scanner(System.in);

	        while (true) {
	            System.out.println("1. Add Favorite Currency");
	            System.out.println("2. View Favorite Currencies");
	            System.out.println("3. Update Favorite Currency");
	            System.out.println("4. Quit");
	            System.out.print("Enter your choice: ");

	            int choice = scanner.nextInt();
	            scanner.nextLine(); // Consume the newline character

	            switch (choice) {
	                case 1:
	                    System.out.print("Enter the currency code to add to favorites: ");
	                    String addCurrencyCode = scanner.nextLine().toUpperCase();
	                    converter.addFavoriteCurrency(addCurrencyCode);
	                    break;
	                case 2:
	                    converter.viewFavoriteCurrencies();
	                    break;
	                case 3:
	                    System.out.print("Enter the old currency code to update: ");
	                    String oldCurrencyCode = scanner.nextLine().toUpperCase();
	                    System.out.print("Enter the new currency code: ");
	                    String newCurrencyCode = scanner.nextLine().toUpperCase();
	                    converter.updateFavoriteCurrency(oldCurrencyCode, newCurrencyCode);
	                    break;
	                case 4:
	                    System.out.println("Exiting...");
	                    System.exit(0);
	                    break;
	                default:
	                    System.out.println("Invalid choice. Please try again.");
	            }
	        }
	    }
	}


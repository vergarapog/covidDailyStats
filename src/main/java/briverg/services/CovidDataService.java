package briverg.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import briverg.model.LocationStats;

@Service
public class CovidDataService {
	
	private static String COVID_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
	
	private List<LocationStats> allStats = new ArrayList<>();
	private int totalAll = 0;
	private int totalAllPrevDay = 0;
	
	
	
	public static String getCOVID_URL() {
		return COVID_URL;
	}



	public int getTotalAllPrevDay() {
		return totalAllPrevDay;
	}



	public List<LocationStats> getAllStats() {
		return allStats;
	}
	
	

	public int getTotalAll() {
		return totalAll;
	}









	@PostConstruct
	@Scheduled(cron ="* * 1 * * *")
	public void fetchCovidData() throws IOException, InterruptedException {
		
		List<LocationStats> newStats = new ArrayList<>();

		
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(COVID_URL))
			.build();
		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		
		
		StringReader csvReader = new StringReader(response.body());
		
		
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
		for (CSVRecord record : records) {
			LocationStats locStat = new LocationStats();
			
		    locStat.setState(record.get("Province/State"));
		    locStat.setCountry(record.get("Country/Region"));
		    
		    int latest = Integer.parseInt(record.get(record.size() - 1));
		    int prevDay = Integer.parseInt(record.get(record.size() - 2));
		    
		    locStat.setLatestTotalCases(latest);
		    locStat.setDiffFromPrevDay(latest - prevDay);
		    newStats.add(locStat);
		    
		    totalAll += Integer.parseInt(record.get(record.size() - 1));
		    totalAllPrevDay += latest - prevDay;
		}
		
		this.allStats = newStats;
		
	}
	

}

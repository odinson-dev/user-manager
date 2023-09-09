package com.convrse.accessprovider.api;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationServerApi {

  public void fetchMovieDetails() throws UnirestException {

    HttpResponse<JsonNode> response = Unirest.get("https://imdb8.p.rapidapi.com/title/get-details?tconst=tt0944947")
            .header("X-RapidAPI-Key", "17f96e630fmshc00745d8a63bec0p194c8ajsn76c83bdbdd74")
            .header("X-RapidAPI-Host", "imdb8.p.rapidapi.com").asJson();

   response.getBody().getObject().keySet().stream().forEach(k->{
        System.out.println(k + " : " + response.getBody().getObject().get(k));
    });

  }
}

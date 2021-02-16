package punteggi.client.example;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Prova {
   
    public static void main(String[] args) {
        // Crea un cliente "cli"
        Client cli = ClientBuilder.newClient();
        
        // Connette "cli" al servizio "punteggi"
        WebTarget punteggi = cli.target("http://localhost:56476/punteggi");
        
        // Aggiunge un nuovo punteggio
        Response rPost = punteggi.queryParam("giocatore","jacopo")
                                .queryParam("punteggio","345") // http://localhost:56476/punteggi?giocatore=jacopo&punteggio=345
                                .request()
                                .post(Entity.entity("", MediaType.TEXT_PLAIN));
        System.out.println("POST: " + rPost.getStatus() + " " + rPost.getStatusInfo());
        
        // Legge il punteggio inserito
        Response rGet = punteggi.path("jacopo")
                                .request()
                                .get();
        System.out.println("GET: " + rGet.getStatus() + " " + rGet.getStatusInfo());
        System.out.println(rGet.readEntity(String.class));
        
        // Aggiorna il punteggio inserito
        Response rPut = punteggi.path("jacopo")
                                .queryParam("punteggio", "895")
                                .request()
                                .put(Entity.entity("", MediaType.TEXT_PLAIN));
        System.out.println("PUT: " + rPut.getStatus() + " " + rPut.getStatusInfo());
        
        // Legge il punteggio aggiornato
        Response rGet2 = punteggi.path("jacopo")
                                .request()
                                .get();
        System.out.println("GET: " + rGet2.getStatus() + " " + rGet2.getStatusInfo());
        System.out.println(rGet2.readEntity(String.class));
        
        // Elimina il punteggio di "jacopo"
        Response rDelete = punteggi.path("jacopo")
                                .request()
                                .delete();
        System.out.println("DELETE: " + rDelete.getStatus() + " " + rDelete.getStatusInfo());
        
        // Legge il punteggio eliminato
        Response rGet3 = punteggi.path("jacopo")
                                .request()
                                .get();
        System.out.println("GET: " + rGet3.getStatus() + " " + rGet3.getStatusInfo());
        System.out.println(rGet3.readEntity(String.class));
    }
}

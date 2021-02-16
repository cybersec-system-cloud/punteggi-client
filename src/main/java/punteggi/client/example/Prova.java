package punteggi.client.example;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class Prova {
   
    public static void main(String[] args) throws InterruptedException {
        // Crea un cliente "cli"
        Client cli = ClientBuilder.newClient();
        
        // Connette "cli" al servizio "punteggi"
        WebTarget punteggi = cli.target("http://localhost:56476/punteggi");
        
        // Variabile usata per salvare risposte del servizio
        Response r;
        
        // Aggiunge un nuovo punteggio - POST
        try{ 
            r = punteggi.queryParam("giocatore","jacopo")
                                .queryParam("punteggio","345") // http://localhost:56476/punteggi?giocatore=jacopo&punteggio=345
                                .request()
                                .post(Entity.entity("", MediaType.TEXT_PLAIN));
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.out.println("POST: Il servizio 'punteggi' non è online.");
                return;
            }
            else throw e;
        } 
        // Se la risorsa jacopo è stata creata, stampa la sua URI
        if(r.getStatus() == Status.CREATED.getStatusCode()) {
            System.out.println("POST: " + r.getHeaders().get("location"));
        }
        else {
            System.err.println("POST: " + r.getStatus() + " " + r.getStatusInfo());
        }

        
        // Legge il punteggio inserito - GET1
        try{ 
            r = punteggi.path("jacopo").request().get();
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.out.println("GET1: Il servizio 'punteggi' non è online.");
                return;
            }
            else throw e;
        } 
        // Se ha trovato la risorsa "jacopo", ne stampa il contenuto
        if(r.getStatus() == Status.OK.getStatusCode()) {
            System.out.println("GET1: " + r.readEntity(String.class));
        }
        else { 
            System.err.println("GET1: " + r.getStatus() + " " + r.getStatusInfo());
        }
        
        // Aggiorna il punteggio inserito - PUT
        try{
            r = punteggi.path("jacopo")
                        .queryParam("punteggio", "895")
                        .request()
                        .put(Entity.entity("", MediaType.TEXT_PLAIN));
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.out.println("PUT: Il servizio 'punteggi' non è online.");
                return;
            }
            else throw e;
        } 
        // Se aggiornato, stampa un messaggio di conferma
        if(r.getStatus() == Status.OK.getStatusCode()) {
            System.out.println("PUT: Risorsa 'jacopo' aggiornata");
        }
        else {
            System.err.println("PUT: " + r.getStatus() + " " + r.getStatusInfo());
        }
        
        // Legge il punteggio aggiornato - GET2
        try{ 
            r = punteggi.path("jacopo").request().get();
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.out.println("GET2: Il servizio 'punteggi' non è online.");
                return;
            }
            else throw e;
        } 
        // Se ha trovato la risorsa "jacopo", ne stampa il contenuto
        if(r.getStatus() == Status.OK.getStatusCode()) {
            System.out.println("GET2: " + r.readEntity(String.class));
        }
        else { 
            System.err.println("GET2: " + r.getStatus() + " " + r.getStatusInfo());
        } 
        
        // Elimina il punteggio di "jacopo" - DELETE
        try { 
            r = punteggi.path("jacopo").request().delete();
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.out.println("DELETE: Il servizio 'punteggi' non è online.");
                return;
            }
            else throw e;
        }
        // Se ha rimosso la risorsa "jacopo", stampa un messaggio di conferma
        if(r.getStatus() == Status.OK.getStatusCode()) {
            System.out.println("DELETE: Risorsa 'jacopo' eliminata");
        }
        else { 
            System.err.println("DELETE: " + r.getStatus() + " " + r.getStatusInfo());
        }
        
        // Legge il punteggio eliminato
        try {
            r = punteggi.path("jacopo").request().get();
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.out.println("GET3: Il servizio 'punteggi' non è online.");
                return;
            }
            else throw e;
        }
        // Se ha trovato la risorsa "jacopo", ne stampa il contenuto
        if(r.getStatus() == Status.OK.getStatusCode()) {
            System.out.println("GET3: " + r.readEntity(String.class));
        }
        else { 
            System.err.println("GET3: " + r.getStatus() + " " + r.getStatusInfo());
        }
    }
}

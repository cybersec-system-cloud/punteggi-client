package punteggi.client.example;

import java.net.ConnectException;
import java.util.Scanner;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Interactive {

    public static void main(String[] args) {
        // Collegamento al servizio punteggi
        Client cli = ClientBuilder.newClient();
        WebTarget punteggi = cli.target("http://localhost:56476/punteggi");
        
        Scanner sc = new Scanner(System.in);
        String scelta = "vuota";
        while(!scelta.equalsIgnoreCase("EXIT")) {
            System.out.println();
            System.out.println("Cosa vuoi fare? (POST/GET/PUT/DELETE/EXIT)");
            scelta = sc.nextLine();
            
            // CASO 1 : POST 
            if(scelta.equalsIgnoreCase("POST")) {
                System.out.print("Inserisci il nome del giocatore da aggiungere: ");
                String g = sc.nextLine();
                System.out.print("Inserisci il relativo punteggio: ");
                int p = sc.nextInt();
                sc.nextLine();
                post(punteggi,g,p);
            }
            // CASO 2 : GET 
            else if(scelta.equalsIgnoreCase("GET")) {
                System.out.print("Inserisci il nome del giocatore da reperire: ");
                String g = sc.nextLine();
                get(punteggi,g);
            } 
            // CASO 3 : PUT
            else if(scelta.equalsIgnoreCase("PUT")) {
                System.out.print("Inserisci il nome del giocatore da aggiornare: ");
                String g = sc.nextLine();
                System.out.print("Inserisci il nuovo punteggio: ");
                int p = sc.nextInt();
                sc.nextLine();
                put(punteggi,g,p);
            } 
            // CASO 4 : DELETE
            else if(scelta.equalsIgnoreCase("DELETE")) {
                System.out.print("Inserisci il nome del giocatore da eliminare: ");
                String giocatore = sc.nextLine();
                delete(punteggi,giocatore);
            } 
            // CASO 5 : EXIT
            else if(scelta.equalsIgnoreCase("EXIT")) {
                System.out.println("Ciao!");
            } 
            // ALTRI CASI : SCONOSCIUTI
            else {
                System.err.println("Azione non possibile. Riprova!");
            }
        }
    }
    
    private static void post(WebTarget punteggi, String giocatore, int punteggio) {
        Response r;
        
        // Esegue la POST gestendo eventuali errori di connessione
        try {
            r = punteggi.queryParam("giocatore", giocatore)
                    .queryParam("punteggio", punteggio)
                    .request()
                    .post(Entity.entity("", MediaType.TEXT_PLAIN));
        } catch (ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.err.println("ERRORE: Impossibile connettersi al servizio");
                return;
            } else throw e;
        }
        // Se la risorsa è stata creata, stampa la sua URI
        if(r.getStatus() == Response.Status.CREATED.getStatusCode()) {
            System.out.println("Punteggio aggiunto " + r.getHeaders().get("location"));
        }
        else {
            System.err.println("ERRORE: POST non riuscita (" + r.getStatusInfo() + ")");
        }
    }
    
    private static void get(WebTarget punteggi, String giocatore) {
        Response r;
        // Esegue la richiesta per reperire il "giocatore", gestendo eventuali errori di connessione
        try{ 
            r = punteggi.path(giocatore).request().get();
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.out.println("ERRORE: Impossibile connettersi al servizio.");
                return;
            }
            else throw e;
        } 
        // Se ha trovato la risorsa "jacopo", ne stampa il contenuto
        if(r.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println(r.readEntity(String.class));
        }
        else { 
            System.err.println("ERRORE: Impossibile recuperare il giocatore richiesto (" + r.getStatusInfo() + ")");
        }
    }
    
    private static void put(WebTarget punteggi, String giocatore, int punteggio) {
        Response r;
        // Esegue la richiesta per aggiornare il "giocatore", gestendo eventuali errori di connessione
        try{
            r = punteggi.path(giocatore)
                        .queryParam("punteggio", punteggio)
                        .request()
                        .put(Entity.entity("", MediaType.TEXT_PLAIN));
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.out.println("ERRORE: Impossibile connettersi al servizio.");
                return;
            }
            else throw e;
        } 
        // Se aggiornato, stampa un messaggio di conferma
        if(r.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println("Il punteggio di '" + giocatore + "' è stato aggiornato." );
        }
        else {
            System.err.println("ERRORE: Impossibile aggiornare il punteggio (" + r.getStatusInfo() + ")");
        }
    }
    
    private static void delete(WebTarget punteggi, String giocatore) {
        Response r;
        try { 
            r = punteggi.path(giocatore).request().delete();
        } catch(ProcessingException e) {
            if(e.getCause() instanceof ConnectException) {
                System.out.println("ERRORE: Impossibile connettersi al servizio.");
                return;
            }
            else throw e;
        }
        // Se ha rimosso la risorsa "jacopo", stampa un messaggio di conferma
        if(r.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println("Il punteggio di '" + giocatore + "' è stato eliminato");
        }
        else { 
            System.err.println("ERRORE: Impossibile eliminare il punteggio (" + r.getStatusInfo() + ")");
        }
    }
}

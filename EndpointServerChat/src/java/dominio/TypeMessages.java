package dominio;

public class TypeMessages {

    //Chat
    public static final String INVIO_SESSIONE = "0001";
    public static final String MESSAGGI_ORDINARI = "0002";
    public static final String PRONTO_PER_REPLICA_MESSAGGI = "0003";
    public static final String MESSAGGI_DI_SERVIZIO = "0004";
    //Tris: gestione lato server
    public static final String RICHIESTA_JOIN = "join";
    public static final String RICHIESTA_MOSSA = "mossa";
    public static final String RICHIESTA_RESET = "reset";
    //Tris: invio messaggi client
    public static final String INVIO_STATO = "stato";
    public static final String INVIO_ERRORE = "errore";
    public static final String INVIO_MESSAGGIO_INFORMAZIONE = "info";
    public static final String INVIO_RUOLO = "ruolo";
    public static final String INVIO_SESSIONE_ANDROID = "session";
    public static final String INVIO_TABELLA_WIN = "win";
    
    public TypeMessages() {

    }

}

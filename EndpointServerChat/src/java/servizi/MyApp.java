package servizi;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@ApplicationPath("api")
public class MyApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

        resources.add(MessageFacadeREST.class);
        resources.add(UtenteFacadeREST.class);
        resources.add(DownloadApk.ApkDownloader.class);

        return resources;
    }
}

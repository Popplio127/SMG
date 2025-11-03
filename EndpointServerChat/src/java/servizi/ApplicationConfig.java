package servizi;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("api") 
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(servizi.UserFacadeREST.class);
        resources.add(tris.GameTris.class);
        resources.add(chat.ControlloreWebSocketChat.class);
        resources.add(tris.ControlloreWebSocketTris.class);
    }
}

package DownloadApk;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletContext;

/**
 *
 * @author I_Particolari
 */
@Path("{nomeApp}")
public class ApkDownloader {

    private static final String APK_NAME = "AppParticolare.apk";

    @GET
    @Produces("application/vnd.android.package-archive")
    public Response downloadApk(@Context ServletContext context) {

        InputStream is = context.getResourceAsStream("/apps/" + APK_NAME);

        if (is == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("APK non trovato nella cartella /apps")
                    .build();
        }

        StreamingOutput fileStream = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                is.close();
            }
        };

        return Response.ok(fileStream)
                .header("Content-Disposition", "attachment; filename=\"" + APK_NAME + "\"")
                .build();
    }
}


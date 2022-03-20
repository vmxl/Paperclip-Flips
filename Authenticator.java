// 
// Decompiled by Procyon v0.5.36
// 

package me.dizzy.dizzyflipper;

import java.util.Locale;
import com.mojang.authlib.GameProfile;
import org.apache.commons.codec.binary.Base64;
import com.google.gson.JsonObject;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.util.Session;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import com.google.gson.JsonElement;
import net.minecraftforge.fml.common.ProgressManager;

public class Authenticator
{
    private static final String BASE_URL = "https://nec.robothanzo.dev/auth";
    public static String myUUID;
    private final ProgressManager.ProgressBar progressBar;
    private String token;
    
    public Authenticator(final ProgressManager.ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
    
    public static JsonElement getAuthenticatedJson(final String jsonUrl) throws IOException {
        if (Main.authenticator.getToken() == null) {
            Reference.logger.warn("Authenticator token is null, trying to get one");
            try {
                Main.authenticator.authenticate(false);
            }
            catch (Exception e) {
                Reference.logger.error(e.getMessage(), (Throwable)e);
                return null;
            }
            if (Main.authenticator.getToken() == null) {
                return null;
            }
        }
        HttpsURLConnection connection;
        try {
            connection = (HttpsURLConnection)new URL(jsonUrl).openConnection();
        }
        catch (IOException e2) {
            Reference.logger.error(e2.getMessage(), (Throwable)e2);
            return null;
        }
        connection.setRequestProperty("User-Agent", "DizzyFlipper/1.0");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", Main.authenticator.getToken());
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        final int code = connection.getResponseCode();
        final String payload = String.join("\n", IOUtils.readLines((connection.getErrorStream() == null) ? connection.getInputStream() : connection.getErrorStream()));
        if (code < 400) {
            return new JsonParser().parse(payload);
        }
        Reference.logger.error(jsonUrl + " :: Received " + connection.getResponseCode() + " along with\n" + payload);
        if (code == 401) {
            try {
                Main.authenticator.authenticate(true);
            }
            catch (AuthenticationException e3) {
                Main.authenticator.token = null;
                return null;
            }
            return getAuthenticatedJson(jsonUrl);
        }
        return null;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public void authenticate(final boolean withProgress) throws IOException, AuthenticationException, NullPointerException {
        final Session session = Minecraft.func_71410_x().func_110432_I();
        final String sessionToken = session.func_148254_d();
        if (withProgress) {
            this.progressBar.step("Authenticating (1/2)");
        }
        final String tempToken = Objects.requireNonNull(this.requestAuth(session.func_148256_e()));
        final MinecraftSessionService yggdrasilMinecraftSessionService = Minecraft.func_71410_x().func_152347_ac();
        final JsonObject d = this.getJwtPayload(tempToken);
        yggdrasilMinecraftSessionService.joinServer(session.func_148256_e(), sessionToken, d.get("server_id").getAsString());
        if (withProgress) {
            this.progressBar.step("Authenticating (2/2)");
        }
        this.token = Objects.requireNonNull(this.verifyAuth(tempToken));
    }
    
    public JsonObject getJwtPayload(final String jwt) {
        final String midPart = jwt.split("\\.")[1].replace("+", "-").replace("/", "_");
        final String base64Decode = new String(Base64.decodeBase64(midPart));
        return (JsonObject)new JsonParser().parse(base64Decode);
    }
    
    private String requestAuth(final GameProfile profile) throws IOException {
        Authenticator.myUUID = profile.getId().toString().replaceAll("-", "").toLowerCase(Locale.ROOT);
        final HttpsURLConnection connection = (HttpsURLConnection)new URL("https://nec.robothanzo.dev/auth/request").openConnection();
        connection.setRequestProperty("User-Agent", "DizzyFlipper Authentication Service/1.0");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.getOutputStream().write(("{\"uuid\":\"" + Authenticator.myUUID + "\",\"username\":\"" + profile.getName() + "\"}").getBytes());
        final int code = connection.getResponseCode();
        final String payload = String.join("\n", IOUtils.readLines((connection.getErrorStream() == null) ? connection.getInputStream() : connection.getErrorStream()));
        if (code >= 400) {
            Reference.logger.error("https://nec.robothanzo.dev/auth/request :: Received " + connection.getResponseCode() + " along with\n" + payload);
            return null;
        }
        final JsonObject json = (JsonObject)new JsonParser().parse(payload);
        if (!json.get("success").getAsBoolean()) {
            return null;
        }
        return json.get("jwt").getAsString();
    }
    
    private String verifyAuth(final String tempToken) throws IOException {
        final HttpsURLConnection urlConnection = (HttpsURLConnection)new URL("https://nec.robothanzo.dev/auth/verify").openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("User-Agent", "DizzyFlipper Authentication Service/1.0");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.getOutputStream().write(("{\"jwt\":\"" + tempToken + "\"}").getBytes());
        final String payload = String.join("\n", IOUtils.readLines((urlConnection.getErrorStream() == null) ? urlConnection.getInputStream() : urlConnection.getErrorStream()));
        final int code = urlConnection.getResponseCode();
        if (code >= 400) {
            Reference.logger.error("https://nec.robothanzo.dev/auth/verify :: Received " + urlConnection.getResponseCode() + " along with\n" + payload);
            return null;
        }
        final JsonObject json = (JsonObject)new JsonParser().parse(payload);
        if (!json.get("success").getAsBoolean()) {
            return null;
        }
        return json.get("token").getAsString();
    }
    
    public void logout() throws IOException {
        final HttpsURLConnection urlConnection = (HttpsURLConnection)new URL("https://nec.robothanzo.dev/auth/logout").openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("User-Agent", "DizzyFlipper Authentication Service/1.0");
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Authorization", this.token);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        final int code = urlConnection.getResponseCode();
        final String payload = String.join("\n", IOUtils.readLines((urlConnection.getErrorStream() == null) ? urlConnection.getInputStream() : urlConnection.getErrorStream()));
        if (code >= 400) {
            Reference.logger.error("https://nec.robothanzo.dev/auth/logout :: Received " + urlConnection.getResponseCode() + " along with\n" + payload);
        }
    }
}

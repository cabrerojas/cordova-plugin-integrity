package cl.oneapp.integrityplugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class IntegrityPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("getAPKHash".equals(action)) {
            getAPKHash(callbackContext);
            return true;
        }
        return false;
    }

    private void getAPKHash(CallbackContext callbackContext) {
        try {
            // Obtiene la ruta del APK instalado
            Context context = cordova.getActivity().getApplicationContext();
            ApplicationInfo appInfo = context.getApplicationInfo();
            String apkPath = appInfo.sourceDir;

            // Calcula el hash SHA-256 del archivo APK
            String computedHash = computeSHA256(apkPath);
            // Retorna el hash calculado
            callbackContext.success(computedHash);
        } catch (Exception e) {
            callbackContext.error("Error al calcular el hash: " + e.getMessage());
        }
    }

    private String computeSHA256(String filePath) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        File file = new File(filePath);
        InputStream is = new FileInputStream(file);
        byte[] buffer = new byte[8192];
        int read;
        while ((read = is.read(buffer)) > 0) {
            digest.update(buffer, 0, read);
        }
        is.close();
        byte[] hashBytes = digest.digest();

        // Convierte los bytes a cadena hexadecimal
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

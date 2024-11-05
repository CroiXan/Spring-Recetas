package com.duoc.recetas.security;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;

public class SeguridadUtil {

    private static final Encoder encoder = ESAPI.encoder();

    public static String sanitizarParaHTML(String input) {
        return encoder.encodeForHTML(input);
    }

    public static String sanitizarParaJavaScript(String input) {
        return encoder.encodeForJavaScript(input);
    }

}

package com.king.app.updater.util;

import android.annotation.SuppressLint;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * SSLSocketFactory Utility
 *
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public final class SSLSocketFactoryUtils {

    private SSLSocketFactoryUtils() {
        throw new AssertionError();
    }

    /**
     * Create an SSLSocketFactory
     *
     * @return {@link SSLSocketFactory}
     */
    public static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new TrustAllX509TrustManager(true, null)}, null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Trust all X509TrustManagers
     *
     * @return {@link X509TrustManager}
     */
    @SuppressLint("CustomX509TrustManager")
    public static class TrustAllX509TrustManager implements X509TrustManager {
        private X509TrustManager standardTrustManager;
        private boolean isTrustAll;

        public TrustAllX509TrustManager(Boolean trustAll, KeyStore keystore) {
            this.isTrustAll = trustAll;
            try {
                TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                factory.init(keystore);
                TrustManager[] trustManagers = factory.getTrustManagers();
                if (trustManagers.length == 0 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
                }
                this.standardTrustManager = (X509TrustManager) trustManagers[0];
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            if (!isTrustAll) {
                standardTrustManager.checkClientTrusted(chain, authType);
            }
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            if (!isTrustAll) {
                if (chain != null && chain.length == 1) {
                    chain[0].checkValidity();
                } else {
                    standardTrustManager.checkServerTrusted(chain, authType);
                }
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return standardTrustManager.getAcceptedIssuers();
        }
    }

    /**
     * Create an X509TrustManager that trusts all certificates
     *
     * @return {@link X509TrustManager}
     */
    public static X509TrustManager createTrustAllX509TrustManager() {
        return new TrustAllX509TrustManager(true, null);
    }

    /**
     * Create a HostnameVerifier that ignores verification and trusts all host addresses
     *
     * @return {@link HostnameVerifier}
     */
    public static HostnameVerifier createAllowAllHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return hostname != null;
            }
        };
    }

}

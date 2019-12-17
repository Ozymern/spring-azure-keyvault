package com.ozymern.spring.azurekeyvault.keyvault;

import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.KeyOperationResult;
import com.microsoft.azure.keyvault.webkey.JsonWebKeyEncryptionAlgorithm;
import com.microsoft.rest.ServiceCallback;
import com.microsoft.rest.ServiceFuture;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Data
@Component
public class AzureKeyVaultUsingJavaClient {

    @Value("${keyvault.client-id}")
    private String clientId;

    @Value("${keyvault.client-key}")
    private String clientKey;

    @Value("${keyvault.uri}")
    private String keyIdentifier;


    private final Logger LOGGER = LoggerFactory.getLogger(AzureKeyVaultUsingJavaClient.class);

    private KeyVaultClient kvClient;

    public void init() {
        kvClient = new KeyVaultClient(
            new ClientSecretKeyVaultCredential(clientId, clientKey));
    }

    public void close() {
        Executors.newFixedThreadPool(1).shutdown();
    }


    public String encryptAsync(String text) {

        ServiceFuture<KeyOperationResult> result = kvClient.encryptAsync(keyIdentifier, JsonWebKeyEncryptionAlgorithm.RSA_OAEP, text.getBytes(StandardCharsets.UTF_8), new ServiceCallback<KeyOperationResult>() {

            @Override
            public void failure(Throwable throwable) {


                LOGGER.error(throwable.getMessage());

            }

            @Override
            public void success(KeyOperationResult keyOperationResult) {

            }
        });

        try {
            close();
            return Base64.getEncoder().encodeToString(result.get().result());
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());

            return null;
        }


    }

    public String decryptAsync(String text) {

        ServiceFuture<KeyOperationResult> result = kvClient.decryptAsync(keyIdentifier, JsonWebKeyEncryptionAlgorithm.RSA_OAEP, Base64.getDecoder().decode(text.getBytes(StandardCharsets.UTF_8)), new ServiceCallback<KeyOperationResult>() {

            @Override
            public void failure(Throwable throwable) {


                LOGGER.error(throwable.getMessage());

            }

            @Override
            public void success(KeyOperationResult keyOperationResult) {

            }
        });

        try {
            close();
            return new String(result.get().result());

        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());

            return null;
        }

    }

    public String encrypt(String text) {


        KeyOperationResult result = kvClient.encrypt(keyIdentifier, JsonWebKeyEncryptionAlgorithm.RSA_OAEP_256, text.getBytes(StandardCharsets.UTF_8));

        return (result != null) ? Base64.getEncoder().encodeToString(result.result()) : null;

    }

    public String decrypt(String text) {

        KeyOperationResult result = kvClient.decrypt(keyIdentifier, JsonWebKeyEncryptionAlgorithm.RSA_OAEP_256, Base64.getDecoder().decode(text.getBytes(StandardCharsets.UTF_8)));


        return (result != null) ? new String(result.result()) : null;


    }


}

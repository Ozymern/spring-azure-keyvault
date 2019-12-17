package com.ozymern.spring.azurekeyvault;

import com.ozymern.spring.azurekeyvault.keyvault.AzureKeyVaultUsingJavaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAzureKeyvaultApplication  implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringAzureKeyvaultApplication.class, args);
    }


    @Autowired
    private AzureKeyVaultUsingJavaClient azureKeyVaultUsingJavaClient;


    @Override
    public void run(String... args)  {

        azureKeyVaultUsingJavaClient.init();

        System.out.println(azureKeyVaultUsingJavaClient.encrypt("ozymern"));


        System.out.println(azureKeyVaultUsingJavaClient.decrypt(azureKeyVaultUsingJavaClient.encrypt("ozymern")));
        azureKeyVaultUsingJavaClient.close();
    }


}

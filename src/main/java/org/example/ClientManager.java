package org.example;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClientManager {

    private final Map<String, Client> clients = new HashMap<>();

    public void registerClient(Client client) {
        clients.put(client.getClientId(), client);
    }


    public Client findClient(String clientId) {
        return clients.get(clientId);
    }

    public void deleteClient(String clientId) {
        clients.remove(clientId);
    }

    public Collection<Client> getAllClients() {
        return clients.values();
    }
}

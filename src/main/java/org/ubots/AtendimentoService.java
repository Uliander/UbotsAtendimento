package org.ubots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AtendimentoService {

    private final ClienteRepository clienteRepository;

    // Armazena os clientes atendidos atualmente por cada atendente
    private final Map<String, List<Cliente>> atendentesClientes = new HashMap<>();

    // Armazena os clientes na fila de espera para cada atendente
    private final Map<String, Queue<Cliente>> filaDeEspera = new HashMap<>();

    @Autowired
    public AtendimentoService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Método para atender um cliente
    public String atenderCliente(String atendente, Cliente cliente) throws Exception {
        List<Cliente> clientesAtendidos = atendentesClientes.getOrDefault(atendente, new ArrayList<>());

        // Verifica se o atendente já está com 3 clientes
        if (clientesAtendidos.size() >= 3) {
            // Se sim, coloca o cliente na fila
            filaDeEspera.computeIfAbsent(atendente, k -> new LinkedList<>()).add(cliente);
            return "Atendente já está ocupado. Cliente " + cliente.getNome() + " foi adicionado à fila de espera.";
        }

        // Caso contrário, o cliente é atendido imediatamente
        clientesAtendidos.add(cliente);
        atendentesClientes.put(atendente, clientesAtendidos);
        clienteRepository.save(cliente);
        return "Cliente " + cliente.getNome() + " sendo atendido por atendente " + atendente;
    }

    // Método para listar clientes atendidos atualmente por um atendente
    public List<Cliente> listarClientesAtendidos(String atendente) {
        return atendentesClientes.getOrDefault(atendente, new ArrayList<>());
    }

    // Finalizar atendimento e atender o próximo da fila, se houver
    public void finalizarAtendimento(String atendente, Long clienteId) {
        List<Cliente> clientesAtendidos = atendentesClientes.get(atendente);

        if (clientesAtendidos != null) {
            // Remove o cliente da lista de atendimentos atuais
            clientesAtendidos.removeIf(c -> c.getId().equals(clienteId));
            atendentesClientes.put(atendente, clientesAtendidos);

            // Verifica se há clientes na fila de espera e inicia o atendimento
            Queue<Cliente> fila = filaDeEspera.getOrDefault(atendente, new LinkedList<>());
            if (!fila.isEmpty()) {
                Cliente proximoCliente = fila.poll(); // Remove o próximo cliente da fila
                clientesAtendidos.add(proximoCliente);
                clienteRepository.save(proximoCliente);
                atendentesClientes.put(atendente, clientesAtendidos);

                System.out.println("Cliente " + proximoCliente.getNome() + " foi movido da fila para o atendimento.");
            }
        }
    }

    // Método para listar clientes na fila de espera
    public List<Cliente> listarClientesNaFila(String atendente) {
        Queue<Cliente> fila = filaDeEspera.getOrDefault(atendente, new LinkedList<>());
        return new ArrayList<>(fila);
    }
}

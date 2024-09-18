package org.ubots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atendimento")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    @Autowired
    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @PostMapping("/{atendente}/atender")
    public ResponseEntity<String> atenderCliente(@PathVariable String atendente, @RequestBody Cliente cliente) {
        try {
            String response = atendimentoService.atenderCliente(atendente, cliente);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{atendente}/clientes")
    public ResponseEntity<List<Cliente>> listarClientesAtendidos(@PathVariable String atendente) {
        return ResponseEntity.ok(atendimentoService.listarClientesAtendidos(atendente));
    }

    @GetMapping("/{atendente}/fila")
    public ResponseEntity<List<Cliente>> listarClientesNaFila(@PathVariable String atendente) {
        return ResponseEntity.ok(atendimentoService.listarClientesNaFila(atendente));
    }

    @PostMapping("/{atendente}/finalizar/{clienteId}")
    public ResponseEntity<String> finalizarAtendimento(@PathVariable String atendente, @PathVariable Long clienteId) {
        atendimentoService.finalizarAtendimento(atendente, clienteId);
        return ResponseEntity.ok("Atendimento finalizado para cliente " + clienteId);
    }
}
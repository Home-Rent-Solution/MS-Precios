package controller;

import model.Precio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.PreciosService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/precios")
public class PreciosController {
    @Autowired
    private PreciosService service;

    @GetMapping
    public List<Precio> listar() {
        return service.listar();
    }

    @PostMapping
    public Precio guardar(@RequestBody Precio precio) {
        return service.guardar(precio);
    }
}

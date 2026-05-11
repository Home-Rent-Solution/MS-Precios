package com.homerentsolution.msprecios.controller;

import com.homerentsolution.msprecios.model.Precio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.homerentsolution.msprecios.service.PrecioService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/precios")

public class PrecioController {
    @Autowired
    private PrecioService service;

    //obtener la lista
    @GetMapping
    public List<Precio> listar() {

        return service.listar();
    }

    //post para guardar
    @PostMapping
    public Precio guardar(@RequestBody Precio precio) {

        return service.guardar(precio);
    }

    //buscar por Id
    @GetMapping("/{id}")
    public Precio buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }



    //eliminar
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }    //actualizar por id
    @PutMapping("/{id}")
    public Precio actualizar(@PathVariable Long id,
                             @RequestBody Precio precio) {

        return service.actualizar(id, precio);
    }
}

package service;

import model.Precio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.PrecioRepository;

import java.util.List;

@Service
public class PreciosService {
    @Autowired
    private PrecioRepository repository;

    public List<Precio> listar() {
        return repository.findAll();
    }

    public Precio guardar(Precio precio) {
        return repository.save(precio);
    }
}

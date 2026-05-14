package com.edustay.backend.config;

import com.edustay.backend.models.Regla;
import com.edustay.backend.models.Servicio;
import com.edustay.backend.repositories.ReglaRepository;
import com.edustay.backend.repositories.ServicioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ServicioRepository servicioRepository;
    private final ReglaRepository reglaRepository;

    public DataSeeder(ServicioRepository servicioRepository, ReglaRepository reglaRepository) {
        this.servicioRepository = servicioRepository;
        this.reglaRepository = reglaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (servicioRepository.count() == 0) {
            var servicios = Arrays.asList(
                    "WiFi",
                    "Luz",
                    "Agua",
                    "Lavandería",
                    "Limpieza",
                    "Cocina compartida",
                    "Calefacción",
                    "Aire acondicionado",
                    "Estacionamiento",
                    "TV por cable"
            );
            servicios.forEach(nombre -> {
                if (!servicioRepository.existsByNombre(nombre)) {
                    Servicio s = new Servicio();
                    s.setNombre(nombre);
                    servicioRepository.save(s);
                }
            });
        }

        if (reglaRepository.count() == 0) {
            var reglas = Arrays.asList(
                    "No mascotas",
                    "No fumar",
                    "Solo estudiantes",
                    "No fiestas ruidosas",
                    "Respetar horarios de convivencia"
            );
            reglas.forEach(desc -> {
                Regla r = new Regla();
                r.setDescripcion(desc);
                reglaRepository.save(r);
            });
        }
    }
}

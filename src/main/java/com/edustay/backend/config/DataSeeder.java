package com.edustay.backend.config;

import com.edustay.backend.models.Regla;
import com.edustay.backend.models.Servicio;
import com.edustay.backend.models.Usuario;
import com.edustay.backend.models.PerfilEstudiante;
import com.edustay.backend.models.Habitacion;
import com.edustay.backend.models.enums.UserRole;
import com.edustay.backend.models.enums.VerificationStatus;
import com.edustay.backend.models.enums.RoomStatus;
import com.edustay.backend.repositories.ReglaRepository;
import com.edustay.backend.repositories.ServicioRepository;
import com.edustay.backend.repositories.UsuarioRepository;
import com.edustay.backend.repositories.PerfilEstudianteRepository;
import com.edustay.backend.repositories.HabitacionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ServicioRepository servicioRepository;
    private final ReglaRepository reglaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilEstudianteRepository perfilEstudianteRepository;
    private final HabitacionRepository habitacionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(ServicioRepository servicioRepository, 
                      ReglaRepository reglaRepository,
                      UsuarioRepository usuarioRepository,
                      PerfilEstudianteRepository perfilEstudianteRepository,
                      HabitacionRepository habitacionRepository,
                      PasswordEncoder passwordEncoder) {
        this.servicioRepository = servicioRepository;
        this.reglaRepository = reglaRepository;
        this.usuarioRepository = usuarioRepository;
        this.perfilEstudianteRepository = perfilEstudianteRepository;
        this.habitacionRepository = habitacionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Cargar Servicios
        cargarServicios();
        
        // Cargar Reglas
        cargarReglas();
        
        // Cargar Usuarios de prueba
        cargarUsuarios();
        
        // Cargar Habitaciones de ejemplo
        cargarHabitaciones();
    }

    private void cargarServicios() {
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
    }

    private void cargarReglas() {
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

    private void cargarUsuarios() {
        long count = usuarioRepository.count();
        System.out.println("📊 Verificando usuarios existentes: " + count);
        
        if (count == 0) {
            System.out.println("🔄 Creando usuarios de prueba...");
            // Usuario ADMIN
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellido("EduStay");
            admin.setEmail("admin@edustay.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setTelefono("999888777");
            admin.setDni("12345678");
            admin.setRol(UserRole.ADMIN);
            admin.setEmailVerificado(true);
            admin.setIdentidadVerificada(VerificationStatus.VERIFICADO);
            usuarioRepository.save(admin);
            System.out.println("✓ Usuario ADMIN creado: admin@edustay.com / admin123");

            // Usuario ESTUDIANTE
            Usuario estudiante = new Usuario();
            estudiante.setNombre("Juan");
            estudiante.setApellido("Pérez");
            estudiante.setEmail("estudiante@edustay.com");
            estudiante.setPassword(passwordEncoder.encode("estudiante123"));
            estudiante.setTelefono("987654321");
            estudiante.setDni("87654321");
            estudiante.setRol(UserRole.ESTUDIANTE);
            estudiante.setEmailVerificado(true);
            estudiante.setIdentidadVerificada(VerificationStatus.VERIFICADO);
            Usuario estudianteSaved = usuarioRepository.save(estudiante);
            
            // Crear perfil estudiante
            PerfilEstudiante perfil = new PerfilEstudiante();
            perfil.setUsuario(estudianteSaved);
            perfil.setCarrera("Ingeniería Informática");
            perfil.setCiclo(4);
            perfil.setUniversidad("UTP Piura");
            perfil.setPreferenciasConvivencia("Prefiero compartir con compañeros tranquilos y responsables");
            perfilEstudianteRepository.save(perfil);
            System.out.println("✓ Usuario ESTUDIANTE creado: estudiante@edustay.com / estudiante123");

            // Usuario ARRENDADOR
            Usuario arrendador = new Usuario();
            arrendador.setNombre("Carlos");
            arrendador.setApellido("Rodríguez");
            arrendador.setEmail("arrendador@edustay.com");
            arrendador.setPassword(passwordEncoder.encode("arrendador123"));
            arrendador.setTelefono("998877665");
            arrendador.setDni("11223344");
            arrendador.setRol(UserRole.ARRENDADOR);
            arrendador.setEmailVerificado(true);
            arrendador.setIdentidadVerificada(VerificationStatus.VERIFICADO);
            usuarioRepository.save(arrendador);
            System.out.println("✓ Usuario ARRENDADOR creado: arrendador@edustay.com / arrendador123");
            
            System.out.println("✅ Seeder de usuarios completado exitosamente");
        } else {
            System.out.println("⏭️  Saltando seeder de usuarios: ya existen " + count + " usuario(s) en la BD");
        }
    }

    private void cargarHabitaciones() {
        if (habitacionRepository.count() == 0) {
            System.out.println("🔄 Creando habitaciones de ejemplo...");
            
            // Recuperar el arrendador
            Usuario arrendador = usuarioRepository.findByEmail("arrendador@edustay.com").orElse(null);
            if (arrendador == null) {
                System.out.println("⚠️  No se encontró arrendador, saltando creación de habitaciones");
                return;
            }
            
            // Obtener servicios y reglas
            var todosLosServicios = servicioRepository.findAll();
            var todasLasReglas = reglaRepository.findAll();
            
            // Habitación 1: Moderna y céntrica
            Habitacion hab1 = new Habitacion();
            hab1.setArrendador(arrendador);
            hab1.setTitulo("Habitación Moderna en el Centro");
            hab1.setDescripcion("Habitación moderna y acogedora con acceso a cocina compartida, WiFi de alta velocidad y ambiente tranquilo. Perfecta para estudiantes universitarios.");
            hab1.setPrecio(450.0);
            hab1.setDireccion("Jr. Piura 245, Piura");
            hab1.setDistanciaUtpMinutos(8);
            hab1.setLatitud(-5.1947);
            hab1.setLongitud(-80.6330);
            hab1.setEstado(RoomStatus.DISPONIBLE);
            hab1.setServicios(new java.util.HashSet<>(todosLosServicios.subList(0, Math.min(4, todosLosServicios.size()))));
            hab1.setReglas(new java.util.HashSet<>(todasLasReglas.subList(0, Math.min(3, todasLasReglas.size()))));
            habitacionRepository.save(hab1);
            System.out.println("✓ Habitación 1 creada: Moderna en el Centro - S/. 450");
            
            // Habitación 2: Cerca de UTP
            Habitacion hab2 = new Habitacion();
            hab2.setArrendador(arrendador);
            hab2.setTitulo("Cuarto a 5 minutos de UTP");
            hab2.setDescripcion("Ubicación inmejorable a solo 5 minutos de la Universidad Tecnológica del Perú. Habitación cómoda con baño compartido, agua caliente 24/7 y vecindario seguro.");
            hab2.setPrecio(380.0);
            hab2.setDireccion("Av. Universidad 890, Piura");
            hab2.setDistanciaUtpMinutos(5);
            hab2.setLatitud(-5.1850);
            hab2.setLongitud(-80.6450);
            hab2.setEstado(RoomStatus.DISPONIBLE);
            hab2.setServicios(new java.util.HashSet<>(todosLosServicios.subList(0, Math.min(5, todosLosServicios.size()))));
            hab2.setReglas(new java.util.HashSet<>(todasLasReglas));
            habitacionRepository.save(hab2);
            System.out.println("✓ Habitación 2 creada: Cerca de UTP - S/. 380");
            
            // Habitación 3: Lujo con servicios
            Habitacion hab3 = new Habitacion();
            hab3.setArrendador(arrendador);
            hab3.setTitulo("Suite Premium con Todos los Servicios");
            hab3.setDescripcion("Habitación de lujo completamente amueblada con aire acondicionado, TV por cable, WiFi ultra rápido. Incluye servicio de limpieza diaria y estacionamiento privado.");
            hab3.setPrecio(650.0);
            hab3.setDireccion("Calle Comercio 1200, Piura");
            hab3.setDistanciaUtpMinutos(12);
            hab3.setLatitud(-5.2050);
            hab3.setLongitud(-80.6200);
            hab3.setEstado(RoomStatus.DISPONIBLE);
            hab3.setServicios(new java.util.HashSet<>(todosLosServicios));
            hab3.setReglas(new java.util.HashSet<>(todasLasReglas.subList(0, Math.min(2, todasLasReglas.size()))));
            habitacionRepository.save(hab3);
            System.out.println("✓ Habitación 3 creada: Suite Premium - S/. 650");
            
            // Habitación 4: Económica
            Habitacion hab4 = new Habitacion();
            hab4.setArrendador(arrendador);
            hab4.setTitulo("Cuarto Económico para Estudiantes");
            hab4.setDescripcion("Opción económica para estudiantes con presupuesto limitado. Habitación básica pero limpia y segura. Acceso a cocina compartida y áreas comunes.");
            hab4.setPrecio(300.0);
            hab4.setDireccion("Jr. Ayacucho 567, Piura");
            hab4.setDistanciaUtpMinutos(15);
            hab4.setLatitud(-5.1750);
            hab4.setLongitud(-80.6500);
            hab4.setEstado(RoomStatus.DISPONIBLE);
            hab4.setServicios(new java.util.HashSet<>(todosLosServicios.subList(0, Math.min(3, todosLosServicios.size()))));
            hab4.setReglas(new java.util.HashSet<>(todasLasReglas));
            habitacionRepository.save(hab4);
            System.out.println("✓ Habitación 4 creada: Económica - S/. 300");
            
            System.out.println("✅ Seeder de habitaciones completado exitosamente");
        } else {
            System.out.println("⏭️  Saltando seeder de habitaciones: ya existen " + habitacionRepository.count() + " habitación(es) en la BD");
        }
    }
}

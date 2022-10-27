package org.yair.springcloud.msvc.usuarios.facade;


import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.yair.springcloud.msvc.usuarios.models.entity.Usuario;
import org.yair.springcloud.msvc.usuarios.services.UsuarioService;

import java.util.*;

@Component
public class UsuarioFacade {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private Environment env;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Map<String, Object> listar() {
        Map<String, Object> body = new HashMap<>();
        body.put("usuarios", usuarioService.listar());
        body.put("pod_info", env.getProperty("MY_POD_NAME") + " : " + env.getProperty("MY_POD_IP"));
        body.put("texto", env.getProperty("config.texto") );
        return body;
    }

    public ResponseEntity<?> detalle(Long id) {
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);

        return usuarioOptional.isPresent()
                ? ResponseEntity.ok(usuarioOptional.get())
                : ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> crear(Usuario usuario) {

        if (this.usuarioService.existePorEmail(usuario.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(Collections.singletonMap("mensaje", "El correo ya existe"));
        }

        usuario.setPassword( passwordEncoder.encode(usuario.getPassword()) );

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }

    public ResponseEntity<?> editar(Usuario usuario, Long id) {

        Optional<Usuario> o = usuarioService.porId(id);

        if (o.isPresent()) {

            Usuario usuarioDb = o.get();

            if (!usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail()) && this.usuarioService.porEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(Collections.singletonMap("mensaje", "El correo electronico ya existe"));
            }

            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword( passwordEncoder.encode(usuario.getPassword()) );
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuarioDb));
        }

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> eliminar(Long id) {
        Optional<Usuario> o = usuarioService.porId(id);

        if (o.isPresent()) {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> obtenerAlumnosPorCurso(List<Long> ids) {

        return ResponseEntity.ok(usuarioService.listarPorIds(ids));

    }
}

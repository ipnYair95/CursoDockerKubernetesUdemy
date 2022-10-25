package org.yair.springcloud.msvc.usuarios.facade;


import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.yair.springcloud.msvc.usuarios.models.entity.Usuario;
import org.yair.springcloud.msvc.usuarios.services.UsuarioService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UsuarioFacade {

    @Autowired
    private UsuarioService usuarioService;

    public Map<String,List<Usuario>> listar() {
        return  Collections.singletonMap("users", usuarioService.listar() );
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



        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }

    public ResponseEntity<?> editar(Usuario usuario, Long id) {

        Optional<Usuario> o = usuarioService.porId(id);

        if (o.isPresent()) {

            Usuario usuarioDb = o.get();

            if ( !usuario.getEmail().equalsIgnoreCase( usuarioDb.getEmail() )  && this.usuarioService.porEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(Collections.singletonMap("mensaje", "El correo electronico ya existe"));
            }

            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
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

        return ResponseEntity.ok( usuarioService.listarPorIds(ids) );

    }
}

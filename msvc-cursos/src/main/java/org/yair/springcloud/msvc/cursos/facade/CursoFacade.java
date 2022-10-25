package org.yair.springcloud.msvc.cursos.facade;

import feign.FeignException;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.yair.springcloud.msvc.cursos.models.Usuario;
import org.yair.springcloud.msvc.cursos.models.entity.Curso;
import org.yair.springcloud.msvc.cursos.services.CursoService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class CursoFacade {

    @Autowired
    private CursoService service;

    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(this.service.listar());
    }

    public ResponseEntity<?> detalle(Long id) {

        //Optional<Curso> o = this.service.porId(id);
        Optional<Curso> o = this.service.porIdConUsuarios(id);

        return o.isPresent()
                ? ResponseEntity.ok(o.get())
                : ResponseEntity.notFound().build();

    }

    public ResponseEntity<?> crear(Curso curso) {
        Curso cursoDb = this.service.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    public ResponseEntity<?> editar(Curso curso, Long id) {

        Optional<Curso> o = this.service.porId(id);

        if (o.isPresent()) {
            Curso cursoDb = o.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(this.service.guardar(cursoDb));
        }

        return ResponseEntity.notFound().build();

    }

    public ResponseEntity<?> eliminar(Long id) {
        Optional<Curso> o = this.service.porId(id);

        if (o.isPresent()) {
            this.service.eliminar(o.get().getId());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();

    }

    public ResponseEntity<?> asignarUsuario(Usuario usuario, Long cursoId) {

        Optional<Usuario> o;

        try {
            o = this.service.asginarUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario o error en la comunicación: " + e.getMessage()));
        }

        return o.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(o.get())
                : ResponseEntity.notFound().build();

    }

    public ResponseEntity<?> crearUsuario(Usuario usuario, Long cursoId) {

        Optional<Usuario> o;

        try {
            o = this.service.crearUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No se pudo crear el usuario: " + e.getMessage()));
        }

        return o.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(o.get())
                : ResponseEntity.notFound().build();

    }

    public ResponseEntity<?> eliminarUsuario(Usuario usuario, Long cursoId) {

        Optional<Usuario> o;

        try {
            o = this.service.eliminarUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No se pudo eliminar el usuario: " + e.getMessage()));
        }

        return o.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(o.get())
                : ResponseEntity.notFound().build();

    }

    public ResponseEntity<?> eliminarCursoUsuarioPorId(Long id) {
        service.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }
}

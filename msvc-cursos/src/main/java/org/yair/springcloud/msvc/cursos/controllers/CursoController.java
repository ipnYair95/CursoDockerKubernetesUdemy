package org.yair.springcloud.msvc.cursos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.yair.springcloud.msvc.cursos.models.Usuario;
import org.yair.springcloud.msvc.cursos.models.entity.Curso;
import org.yair.springcloud.msvc.cursos.facade.CursoFacade;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CursoController {

    @Autowired
    CursoFacade cursoFacade;

    @GetMapping
    public ResponseEntity<List<Curso>> listar(){
        return this.cursoFacade.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        return this.cursoFacade.detalle(id);
    }

    @PostMapping
    public ResponseEntity<?> crear( @Valid @RequestBody Curso curso, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return validar(bindingResult);
        }

        return this.cursoFacade.crear(curso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar( @Valid  @RequestBody Curso curso, BindingResult bindingResult, @PathVariable Long id ){

        if (bindingResult.hasErrors()) {
            return validar(bindingResult);
        }

        return this.cursoFacade.editar(curso, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        return this.cursoFacade.eliminar(id);
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        return this.cursoFacade.asignarUsuario(usuario, cursoId);
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        return this.cursoFacade.crearUsuario(usuario, cursoId);
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        return this.cursoFacade.eliminarUsuario(usuario, cursoId);
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Long id){
        return this.cursoFacade.eliminarCursoUsuarioPorId(id);
    }


    private ResponseEntity<?> validar(BindingResult bindingResult) {
        Map<String, String> errores = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }


}

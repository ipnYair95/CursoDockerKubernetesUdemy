package org.yair.springcloud.msvc.usuarios.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.yair.springcloud.msvc.usuarios.facade.UsuarioFacade;
import org.yair.springcloud.msvc.usuarios.models.entity.Usuario;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioFacade usuarioFacade;

    @GetMapping
    public Map<String,List<Usuario>>  listar() {
        return usuarioFacade.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        return usuarioFacade.detalle(id);
    }

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return validar(bindingResult);
        }

        return usuarioFacade.crear(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult bindingResult, @PathVariable Long id) {

        if (bindingResult.hasErrors()) {
            return validar(bindingResult);
        }

        return usuarioFacade.editar(usuario, id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return usuarioFacade.eliminar(id);
    }

    @GetMapping("/usuarios-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids ){
        return usuarioFacade.obtenerAlumnosPorCurso(ids);
    }


    private ResponseEntity<?> validar(BindingResult bindingResult) {
        Map<String, String> errores = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }


}

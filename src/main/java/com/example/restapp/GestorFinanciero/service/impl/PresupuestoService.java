package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.dto.PresupuestoDTO;
import com.example.restapp.GestorFinanciero.dto.PresupuestoResumenDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;
import com.example.restapp.GestorFinanciero.models.EstadoPresupuesto;
import com.example.restapp.GestorFinanciero.models.Presupuesto;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.repo.EstadoPresupuestoRepo;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.PresupuestoRepo;
import com.example.restapp.GestorFinanciero.repo.TransaccionRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import com.example.restapp.GestorFinanciero.service.IPresupuestoService;
import com.example.restapp.GestorFinanciero.service.IUsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PresupuestoService extends GenericService<Presupuesto, Integer> implements IPresupuestoService {
    private final PresupuestoRepo repo;
    private final UsuarioRepo usuarioRepo;
    private final EstadoPresupuestoRepo estadoPresupuestoRepo;
    private final TransaccionRepo transaccionRepo;
    private final IUsuarioService usuarioService;
    @Override
    protected IGenericRepo<Presupuesto, Integer> getRepo(){
        return repo;
    }
    @Override
    public Presupuesto crearPresupuestoDto(PresupuestoDTO dto, Integer idUsuario) {

        if (dto.getMontoEstablecido() == null || dto.getMontoEstablecido() <= 0) {
            throw new IllegalArgumentException("El monto establecido debe ser mayor que cero");
        }

        if (dto.getPeriodo() == null || dto.getPeriodo().isBlank()) {
            throw new IllegalArgumentException("El periodo es obligatorio");
        }

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));

        EstadoPresupuesto estado = estadoPresupuestoRepo
                .findByNombreEstadoPresupuesto(dto.getNombreEstadoPresupuesto())
                .orElseThrow(() -> new ModelNotFoundException("Estado de presupuesto no encontrado"));

        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setMontoEstablecido(dto.getMontoEstablecido());
        presupuesto.setMontoMinimo(dto.getMontoMinimo());
        presupuesto.setMontoMaximo(dto.getMontoMaximo());
        presupuesto.setPeriodo(dto.getPeriodo());

        presupuesto.setFechaInicial(dto.getFechaInicial());
        presupuesto.setFechaFinal(dto.getFechaFinal());

        presupuesto.setEstadoPresupuesto(estado);
        presupuesto.setUsuarioPresupuesto(usuario);
        

        presupuesto.setMeta(null);
        repo.save(presupuesto);
        return presupuesto;
    }
    @Override
    public void evaluarEstadoPresupuesto(Presupuesto p) {

        float limite = p.getMontoEstablecido();
        float actual = p.getMontoActual();

        EstadoPresupuesto estado;

        if (actual > limite) {
            estado = estadoPresupuestoRepo.findByNombreEstadoPresupuesto("Excedido")  .orElseThrow(() -> new ModelNotFoundException("Estado no encontrado"));
        }
        else if (actual >= limite * 0.8) {
            estado = estadoPresupuestoRepo.findByNombreEstadoPresupuesto("En Riesgo") .orElseThrow(() -> new ModelNotFoundException("Estado no encontrado"));
        }
        else {
            estado = estadoPresupuestoRepo.findByNombreEstadoPresupuesto("Activo") .orElseThrow(() -> new ModelNotFoundException("Estado no encontrado"));
        }

        // si el periodo terminó, evaluar completado
        if (LocalDate.now().isAfter(p.getFechaFinal())) {
            if (actual <= limite) {
                estado = estadoPresupuestoRepo.findByNombreEstadoPresupuesto("Completado") .orElseThrow(() -> new ModelNotFoundException("Estado no encontrado"));
            }
        }

        p.setEstadoPresupuesto(estado);
        repo.save(p);
    }



    @Override
    @Transactional
    public boolean verificarPresupuestosCompletados(Integer idUsuario) {

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));

        Long completados = repo.contarPresupuestosCompletados(idUsuario);

        System.out.println("Presupuestos completados: " + completados);

        if (completados >= 3) {
            usuarioService.asignarTrofeo(usuario, 9);

            return true;
        }

        return false;
    }

    @Override
    public List<PresupuestoResumenDTO> listarPresupuestosPorUsuario(Integer idUsuario) {

        List<Presupuesto> lista = repo.findByUsuarioPresupuesto_Id(idUsuario);

        return lista.stream().map(p -> {
            PresupuestoResumenDTO dto = new PresupuestoResumenDTO();
            dto.setIdPresupuesto(p.getIdPresupuesto());
            dto.setMontoEstablecido(p.getMontoEstablecido());
            dto.setMontoActual(p.getMontoActual());
            dto.setMontoMinimo(p.getMontoMinimo());
            dto.setMontoMaximo(p.getMontoMaximo());
            dto.setPeriodo(p.getPeriodo());
            dto.setFechaInicial(p.getFechaInicial());
            dto.setFechaFinal(p.getFechaFinal());
            dto.setEstado(p.getEstadoPresupuesto().getNombreEstadoPresupuesto());
            return dto;
        }).toList();
    }





}

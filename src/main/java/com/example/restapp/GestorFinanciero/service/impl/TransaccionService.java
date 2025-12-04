package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.models.MetaTransaccion;
import com.example.restapp.GestorFinanciero.models.Presupuesto;
import com.example.restapp.GestorFinanciero.models.TipoTransaccion;
import com.example.restapp.GestorFinanciero.models.Transaccion;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.MetaRepo;
import com.example.restapp.GestorFinanciero.repo.PresupuestoRepo;
import com.example.restapp.GestorFinanciero.repo.TipoTransaccionRepo;
import com.example.restapp.GestorFinanciero.repo.TransaccionRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import com.example.restapp.GestorFinanciero.service.IMetaService;
import com.example.restapp.GestorFinanciero.service.IPresupuestoService;
import com.example.restapp.GestorFinanciero.service.ITransaccionService;
import com.example.restapp.GestorFinanciero.service.IUsuarioService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.example.restapp.GestorFinanciero.models.Meta;
import org.springframework.stereotype.Service;

import com.example.restapp.GestorFinanciero.dto.EditarTransaccionDTO;
import com.example.restapp.GestorFinanciero.dto.TransaccionDTO;
import com.example.restapp.GestorFinanciero.exception.ModelNotFoundException;

@Service
@RequiredArgsConstructor
public class TransaccionService extends GenericService<Transaccion, Integer> implements ITransaccionService {
    private final TransaccionRepo repo;
    private final UsuarioRepo usuarioRepo;
    private final TipoTransaccionRepo tipoTransaccionRepo;
    private final MetaRepo metaRepo;
    private final PresupuestoRepo presupuestoRepo;

    private final IMetaService metaService;
    private final IUsuarioService usuarioService;
    private final IPresupuestoService presupuestoService;

    @Override
    protected IGenericRepo<Transaccion, Integer> getRepo() {
        return repo;
    }

    @Override
    public Transaccion CrearTransaccionDTO(TransaccionDTO dto) {

        if (dto.getMonto() == null || dto.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }

        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ModelNotFoundException("Usuario no encontrado"));

        Transaccion transaccion = new Transaccion();
        transaccion.setMonto(dto.getMonto());
        transaccion.setDescripcion(dto.getDescripcion());
        transaccion.setUsuarioTransacciones(usuario);
        transaccion.setFechaTransaccion(LocalDate.now());

        TipoTransaccion tipoTransaccion = tipoTransaccionRepo.findById(dto.getTipoTransaccionId())
                .orElseThrow(() -> new ModelNotFoundException("Tipo de transacción no encontrado"));
        transaccion.setTipoTransaccion(tipoTransaccion);

        Integer tipo = dto.getTipoTransaccionId();

        Meta meta = null;

        if (dto.getIdMeta() != null) {

            meta = metaRepo.findById(dto.getIdMeta())
                    .orElseThrow(() -> new ModelNotFoundException("Meta no encontrada"));

            if (!meta.getUsuarioMetas().getId().equals(dto.getIdUsuario())) {
                throw new IllegalArgumentException("La meta no pertenece al usuario");
            }

            if (tipo == 2) { 
                meta.setMontoActual(meta.getMontoActual() + dto.getMonto());
            } else if (tipo == 1) { 
                if (meta.getMontoActual() - dto.getMonto() < 0) {
                    throw new IllegalArgumentException("No puedes gastar más de lo ahorrado en la meta");
                }
                meta.setMontoActual(meta.getMontoActual() - dto.getMonto());
            }

            MetaTransaccion metaTransaccion = new MetaTransaccion();
            metaTransaccion.setMeta(meta);
            metaTransaccion.setTransaccion(transaccion);

            transaccion.getMetaTransaccion().add(metaTransaccion);
        }


        Presupuesto presupuesto = null;

        if (dto.getPresupuestoId() != null) {

            presupuesto = presupuestoRepo.findById(dto.getPresupuestoId())
                    .orElseThrow(() -> new ModelNotFoundException("Presupuesto no encontrado"));

            if (!presupuesto.getUsuarioPresupuesto().getId().equals(dto.getIdUsuario())) {
                throw new IllegalArgumentException("El presupuesto no pertenece al usuario");
            }

            transaccion.setPresupuesto(presupuesto);
            presupuesto.getTransacciones().add(transaccion);

            if (tipo == 1) { 
                presupuesto.setMontoActual(
                        presupuesto.getMontoActual() + dto.getMonto()
                );
            }
        }


        repo.save(transaccion);

        if (meta != null) {
            metaService.validarCumplimientoDeMeta(meta);
        }

        if (presupuesto != null) {
            presupuestoService.evaluarEstadoPresupuesto(presupuesto);
            presupuestoService.verificarPresupuestosCompletados(usuario.getId());
        }

        cantidadDeTransacciones(usuario);

        return transaccion;
    }



    @Override
    public Transaccion updateTransaccion(Integer idTransaccion, Integer idUsuario, EditarTransaccionDTO nuevosDatos)  {

        Transaccion tupdate = repo.findById(idTransaccion)
                .orElseThrow(() -> new ModelNotFoundException("Transacción no encontrada"));

        if (!tupdate.getUsuarioTransacciones().getId().equals(idUsuario)) {
            throw new IllegalArgumentException("Usuario no autorizado para actualizar esta transacción");
        }

        tupdate.setDescripcion(nuevosDatos.getDescripcion());
        tupdate.setMonto(nuevosDatos.getMonto());


        return repo.save(tupdate);
    }

   @Override
    public Integer cantidadDeTransacciones(Usuario user) {

        int cantidad = user.getTransacciones().size();

        if (cantidad >= 20 && ! usuarioService.usuarioTieneTrofeo(user, 8)) {
            usuarioService.asignarTrofeo(user, 8);
            usuarioService.asignarNiveles(user.getId());
        }

        return cantidad;
    }

    @Override
    public void eliminarTransaccion(Integer idTransaccion, Integer idUsuario) {

        Transaccion transaccion = repo.findById(idTransaccion)
                .orElseThrow(() -> new ModelNotFoundException("Transacción no encontrada"));

        if (!transaccion.getUsuarioTransacciones().getId().equals(idUsuario)) {
            throw new IllegalArgumentException("No puedes eliminar esta transacción");
        }

        Float monto = transaccion.getMonto();


        if (!transaccion.getMetaTransaccion().isEmpty()) {

            MetaTransaccion relacion = transaccion.getMetaTransaccion()
                    .stream()
                    .findFirst()
                    .get();

            Meta meta = relacion.getMeta();

            // Restar el monto a la meta
            meta.setMontoActual(meta.getMontoActual() - monto);

            metaRepo.save(meta);
        }


        if (transaccion.getPresupuesto() != null) {

            Presupuesto presupuesto = transaccion.getPresupuesto();

            if (transaccion.getTipoTransaccion().getIdTipoTransaccion() == 1) {
                presupuesto.setMontoActual(
                        presupuesto.getMontoActual() - monto
                );
            }

            presupuestoRepo.save(presupuesto);

            presupuestoService.evaluarEstadoPresupuesto(presupuesto);
        }

        repo.delete(transaccion);
    }

    @Override
    public List<Transaccion> listarTransaccionesPorUsuario(Integer idUsuario) {
        return repo.findByUsuarioTransacciones_Id(idUsuario);
    }

    @Override
    public List<Transaccion> listarTransaccionesPorPresupuesto(Integer idPresupuesto, Integer idUsuario) {

        Presupuesto presupuesto = presupuestoRepo.findById(idPresupuesto)
                .orElseThrow(() -> new ModelNotFoundException("Presupuesto no encontrado"));

        if (!presupuesto.getUsuarioPresupuesto().getId().equals(idUsuario)) {
            throw new IllegalArgumentException("No puedes ver transacciones de un presupuesto ajeno");
        }

        return repo.findByPresupuesto_IdPresupuesto(idPresupuesto);
    }



}

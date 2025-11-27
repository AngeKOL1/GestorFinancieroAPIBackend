package com.example.restapp.GestorFinanciero.service.impl;

import com.example.restapp.GestorFinanciero.models.MetaTransaccion;
import com.example.restapp.GestorFinanciero.models.TipoTransaccion;
import com.example.restapp.GestorFinanciero.models.Transaccion;
import com.example.restapp.GestorFinanciero.models.Usuario;
import com.example.restapp.GestorFinanciero.repo.IGenericRepo;
import com.example.restapp.GestorFinanciero.repo.MetaRepo;
import com.example.restapp.GestorFinanciero.repo.TipoTransaccionRepo;
import com.example.restapp.GestorFinanciero.repo.TransaccionRepo;
import com.example.restapp.GestorFinanciero.repo.UsuarioRepo;
import com.example.restapp.GestorFinanciero.service.IMetaService;
import com.example.restapp.GestorFinanciero.service.ITransaccionService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;

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

    private final IMetaService metaService;

    @Override
    protected IGenericRepo<Transaccion, Integer> getRepo() {
        return repo;
    }
    //Faltan validaciones de negocio
    @Override
    public Transaccion CrearTransaccionDTO(TransaccionDTO dto) {
        Meta meta= new Meta();
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

        transaccion.setMetaTransaccion(new HashSet<>());

        if (dto.getIdMeta() != null) {
            meta = metaRepo.findById(dto.getIdMeta())
                .orElseThrow(() -> new ModelNotFoundException("Meta no encontrada"));

            if (!meta.getUsuarioMetas().getId().equals(dto.getIdUsuario())) {
                throw new ModelNotFoundException("Usuario no autorizado para usar esta meta");
            }
            meta.setMontoActual(meta.getMontoActual()+dto.getMonto());
            MetaTransaccion metaTransaccion = new MetaTransaccion();
            metaTransaccion.setMeta(meta);
            metaTransaccion.setTransaccion(transaccion);
            transaccion.getMetaTransaccion().add(metaTransaccion);
        }


        if (dto.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
        //Validar si ya se cumplió el monto antes de la fecha estimada
        metaService.validarCumplimientoDeMeta(meta);
        return repo.save(transaccion);
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

}

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
import com.example.restapp.GestorFinanciero.service.ITransaccionService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;

import com.example.restapp.GestorFinanciero.models.Meta;
import org.springframework.stereotype.Service;

import com.example.restapp.GestorFinanciero.DTO.EditarTransaccionDTO;
import com.example.restapp.GestorFinanciero.DTO.TransaccionDTO;

@Service
@RequiredArgsConstructor
public class TransaccionService extends GenericService<Transaccion, Integer> implements ITransaccionService {
    private final TransaccionRepo repo;
    private final UsuarioRepo usuarioRepo;
    private final TipoTransaccionRepo tipoTransaccionRepo;
    private final MetaRepo metaRepo;

    @Override
    protected IGenericRepo<Transaccion, Integer> getRepo() {
        return repo;
    }
    //Faltan validaciones de negocio
    @Override
    public Transaccion CrearTransaccionDTO(TransaccionDTO dto) throws Exception {
        Usuario usuario = usuarioRepo.findById(dto.getIdUsuario())
            .orElseThrow(() -> new Exception("Usuario no encontrado"));

        Transaccion transaccion = new Transaccion();
        transaccion.setMonto(dto.getMonto());
        transaccion.setDescripcion(dto.getDescripcion());
        transaccion.setUsuarioTransacciones(usuario);
        transaccion.setFechaTransaccion(LocalDate.now());

        TipoTransaccion tipoTransaccion = tipoTransaccionRepo.findById(dto.getTipoTransaccionId())
            .orElseThrow(() -> new Exception("Tipo de transacción no encontrado"));
        transaccion.setTipoTransaccion(tipoTransaccion);

        transaccion.setMetaTransaccion(new HashSet<>());

        if (dto.getIdMeta() != null) {
            Meta meta = metaRepo.findById(dto.getIdMeta())
                .orElseThrow(() -> new Exception("Meta no encontrada"));

            if (!meta.getUsuarioMetas().getId().equals(dto.getIdUsuario())) {
                throw new Exception("Usuario no autorizado para usar esta meta");
            }

            MetaTransaccion metaTransaccion = new MetaTransaccion();
            metaTransaccion.setMeta(meta);
            metaTransaccion.setTransaccion(transaccion);
            transaccion.getMetaTransaccion().add(metaTransaccion);
        }

        if (dto.getMonto() <= 0) {
            throw new Exception("El monto debe ser mayor que cero");
        }

        return repo.save(transaccion);
    }

    @Override
    public Transaccion updateTransaccion(Integer idTransaccion, Integer idUsuario, EditarTransaccionDTO nuevosDatos) throws Exception {

        Transaccion tupdate = repo.findById(idTransaccion)
                .orElseThrow(() -> new Exception("Transacción no encontrada"));

        if (!tupdate.getUsuarioTransacciones().getId().equals(idUsuario)) {
            throw new Exception("Usuario no autorizado para actualizar esta transacción");
        }

        tupdate.setDescripcion(nuevosDatos.getDescripcion());
        tupdate.setMonto(nuevosDatos.getMonto());


        return repo.save(tupdate);
    }

}

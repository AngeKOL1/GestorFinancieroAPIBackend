package com.example.restapp.GestorFinanciero.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.example.restapp.GestorFinanciero.models.Presupuesto;

public interface PresupuestoRepo extends IGenericRepo<Presupuesto, Integer>{
    @Query("SELECT COUNT(p) FROM Presupuesto p " +
       "WHERE p.usuarioPresupuesto.id = :idUsuario " +
       "AND p.estadoPresupuesto.nombreEstadoPresupuesto = 'Completado'")
    Long contarPresupuestosCompletados(Integer idUsuario);

    List<Presupuesto> findByUsuarioPresupuesto_Id(Integer idUsuario);

}

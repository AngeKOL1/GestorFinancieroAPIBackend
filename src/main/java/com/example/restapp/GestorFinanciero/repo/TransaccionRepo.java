package com.example.restapp.GestorFinanciero.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restapp.GestorFinanciero.models.Transaccion;

public interface TransaccionRepo extends IGenericRepo<Transaccion, Integer> {
    @Query("SELECT COALESCE(SUM(t.Monto), 0) FROM Transaccion t " +
       "WHERE t.usuarioTransacciones.id = :idUsuario " +
       "AND t.fechaTransaccion BETWEEN :inicio AND :fin")
        Double sumByUsuarioYPeriodo(@Param("idUsuario") Integer idUsuario,
                                    @Param("inicio") LocalDate inicio,
                                    @Param("fin") LocalDate fin);

    @Query("SELECT COALESCE(SUM(t.Monto), 0) FROM Transaccion t " +
       "WHERE t.presupuesto.idPresupuesto = :idPresupuesto")
    Double sumByPresupuesto(@Param("idPresupuesto") Integer idPresupuesto);

    List<Transaccion> findByUsuarioTransacciones_Id(Integer idUsuario);

    List<Transaccion> findByPresupuesto_IdPresupuesto(Integer idPresupuesto);





}

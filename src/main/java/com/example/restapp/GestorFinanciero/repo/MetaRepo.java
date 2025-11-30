package com.example.restapp.GestorFinanciero.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.restapp.GestorFinanciero.models.Meta;
import com.example.restapp.GestorFinanciero.models.Transaccion;


public interface MetaRepo extends IGenericRepo<Meta,Integer>{
    List<Meta> findByUsuarioMetas_Id(Integer idUsuario);
     @Query("""
        select count(
            distinct 
            case 
                when m.misCategoriaMeta is not null 
                    then concat('C', m.misCategoriaMeta.idMisCategoriasMetas)
                else concat('D', m.categoriaMetas.idCategoriaMeta)
            end
        )
        from Meta m
        where m.usuarioMetas.id = :idUsuario
    """)
    long contarCategoriasDistintasPorUsuario(@Param("idUsuario") Integer idUsuario);

    @Query("SELECT t FROM Transaccion t JOIN t.metaTransaccion mt WHERE mt.meta.idMeta = :idMeta")
     List<Transaccion> findByMetaId(Integer idMeta);
}

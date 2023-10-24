/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instrumentos.logic;

import instrumentos.data.CalibracionesDao;
import instrumentos.data.TipoInstrumentoDao;
import instrumentos.data.InstrumentoDao;
import instrumentos.data.MedicionesDao;
import java.util.List;

public class Service {
    private static Service theInstance;
    public static Service instance(){
        if (theInstance==null){
            theInstance=new Service();
        }
        return theInstance;
    }

    private TipoInstrumentoDao tipoInstrumentoDao;
    private InstrumentoDao instrumentoDao;
    private CalibracionesDao calibracionesDao;
    private MedicionesDao medicionesDao;

    public Service() {
        try{
            tipoInstrumentoDao = new TipoInstrumentoDao();
            instrumentoDao = new InstrumentoDao();
            calibracionesDao = new CalibracionesDao();
            medicionesDao = new MedicionesDao();
        }
        catch(Exception e){
        }
    }

    //================= TIPOS DE INSTRUMENTO ============
    public void create(TipoInstrumento e)throws Exception{
        tipoInstrumentoDao.create(e);
    }

    public TipoInstrumento read(TipoInstrumento e) throws Exception{
        return tipoInstrumentoDao.read(e.getCodigo());
    }

    public void update(TipoInstrumento e)throws Exception{
        tipoInstrumentoDao.update(e);
    }

    public void delete(TipoInstrumento e)throws Exception{
        tipoInstrumentoDao.delete(e);
    }

    public List<TipoInstrumento> search(TipoInstrumento e) throws Exception {
        return tipoInstrumentoDao.search(e);
    }

    // ------------ INSTRUMENTOS -------------

    public void create(Instrumento e) throws Exception{
        instrumentoDao.create(e);
    }

    public Instrumento read(Instrumento e) throws Exception{
        return instrumentoDao.read(e.getSerie());
    }

    public void update(Instrumento e)throws Exception{
        instrumentoDao.update(e);
    }

    public void delete(Instrumento e)throws Exception{
        instrumentoDao.delete(e);
    }

    public List<Instrumento> search(Instrumento v) throws Exception {
        return instrumentoDao.search(v);
    }

    //================= Calibraciones ============

    public void create(Calibraciones e) throws Exception{
        calibracionesDao.create(e);
    }

    public Calibraciones read(Calibraciones e) throws Exception{
        return calibracionesDao.read(e.getNumero());
    }

    public void update(Calibraciones e)throws Exception{
        calibracionesDao.update(e);
    }

    public void delete(Calibraciones e)throws Exception{
        calibracionesDao.delete(e);
    }

    public List<Calibraciones> search(Calibraciones v) throws Exception {
        return calibracionesDao.search(v);
    }
}
